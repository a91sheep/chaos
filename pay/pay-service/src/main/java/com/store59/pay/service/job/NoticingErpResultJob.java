package com.store59.pay.service.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.mapper.BatchWithrawInfoMapper;
import com.store59.pay.mapper.PayDetailMapper;
import com.store59.pay.model.BatchWithrawInfo;
import com.store59.pay.model.PayDetail;
import com.store59.pay.service.config.WangshangConfig;
import com.store59.pay.service.enums.BatchWithdrawStateEnum;
import com.store59.pay.service.util.HttpClient;

/**
 * 通知ERP批量代发结果
 * @author beangou
 *
 */
public class NoticingErpResultJob implements Runnable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NoticingErpResultJob.class);
	
	private String batchNo;
	
	private PayDetailMapper masterPayDetailMapper;
	
	private BatchWithrawInfoMapper masterBatchWithrawInfoMapper;
	
	private WangshangConfig wangshangConfig;
	
    private JavaMailSender mailSender;
	
	public NoticingErpResultJob(String batchNo, PayDetailMapper masterPayDetailMapper, BatchWithrawInfoMapper masterBatchWithrawInfoMapper, WangshangConfig wangshangConfig, JavaMailSender mailSender) {
		this.batchNo = batchNo;
		this.masterPayDetailMapper = masterPayDetailMapper;
		this.masterBatchWithrawInfoMapper = masterBatchWithrawInfoMapper;
		this.wangshangConfig = wangshangConfig;
		this.mailSender = mailSender;
	}

	@Override
	public void run() {
		while(true) {
			try {
				// 防止数据库未及时更新，先sleep一分钟
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			BatchWithrawInfo info = masterBatchWithrawInfoMapper.selectByTradeNo(batchNo);
			if(info.getState() == BatchWithdrawStateEnum.NOTICING_ERP_RESULT_SUCCESS.getCode()) {
				LOGGER.info("批次号{}已经通知ERP结果，并返回成功，线程结束", batchNo);
				break;
			}
			// 通知ERP结果
			LOGGER.info("开始通知ERP批量代发结果, batchNo={}", batchNo);
			// 通知成功，即 break
			List<PayDetail> list = masterPayDetailMapper.selectByTradeNo(batchNo);
			LOGGER.info("list=>" + JsonUtil.getJsonFromObject(list));
			if(list.isEmpty()) {
				return;
			}
			Map<String, String> paramMap = new HashMap<>();
			int len = list.size();
			paramMap.put("batchNo", batchNo);
			StringBuffer failSb = new StringBuffer();
			for (int i = 0; i < len; i++) {
				PayDetail detail = list.get(i);
				int tradeStatus = detail.getTradeStatus();
				String tradeStatusStr = "";
				if(tradeStatus == 1) {
					tradeStatusStr = "SUCCESS";
				}else if(tradeStatus == 2) {
					tradeStatusStr = "FAIL";
					failSb.append("流水号：").append(detail.getCheckingId()).append(",姓名：").append(detail.getReceivorName()).append(",金额：").append(detail.getAmount()).append(",状态：转账失败").append("\n");
				}else {
					// 未处理的提现
					tradeStatusStr = "UNTREATED";
					failSb.append("流水号：").append(detail.getCheckingId()).append(",姓名：").append(detail.getReceivorName()).append(",金额：").append(detail.getAmount()).append(",状态：转账未处理").append("\n");
				}
				paramMap.put("tradeResult["+i+"][id]", detail.getCheckingId());
				paramMap.put("tradeResult["+i+"][status]", tradeStatusStr);
				paramMap.put("tradeResult["+i+"][remark]", detail.getTradeDesc());
			}
			String sign = DigestUtils.md5Hex(batchNo+len+NoticingErpUploadingJob.ERP_SALT);
			paramMap.put("sign", sign);
			LOGGER.info("通知ERP结果参数=>{}", JsonUtil.getJsonFromObject(paramMap));
			Result<Map<String, String>> result = HttpClient.post(wangshangConfig.getErpReqUrl().trim()+"/withdraw/callback/trade_result", paramMap);
			LOGGER.info("通知ERP结果返回数据=>{}", JsonUtil.getJsonFromObject(result));
			BatchWithrawInfo record = new BatchWithrawInfo();
			record.setTradeNo(batchNo);
			if(result.getStatus() == 0) {
				// 更新状态
				record.setState(BatchWithdrawStateEnum.NOTICING_ERP_RESULT_SUCCESS.getCode());
				masterBatchWithrawInfoMapper.updateByTradeNoSelective(record);
				// （增加流水号、姓名、金额）
				if(failSb.length() > 0) {
					String[] resutlTo = {"liutb@59store.com", "hucx@59store.com", "suhy@59store.com"};
					sendMail("批量代发异常流水记录(批次号："+batchNo+")", failSb.toString(), resutlTo);
				}
				break;
			}else {
				// 更新状态
				record.setState(BatchWithdrawStateEnum.NOTICING_ERP_RESULT_FAILURE.getCode());
				masterBatchWithrawInfoMapper.updateByTradeNoSelective(record);
				String[] resutlTo = {"liutb@59store.com"};
				sendMail("批量代发异常流水记录(批次号："+batchNo+")", failSb.toString(), resutlTo);
			}
			try {
				// 每10分钟通知一次
				Thread.sleep(5*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	 /**
     * 发送邮件
     * @param subject
     * @param content
     * @param to
     */
    public void sendMail(String subject, String content, String[] to) {
    	final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		File tempFile = null;
        FileOutputStream fos = null;
		try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setFrom("notice@59store.com");
            message.setTo(to);
            message.setSubject("ERP后台提现邮件:"+subject);
            message.setText(subject);
            tempFile = File.createTempFile(subject+"temp", ".txt");
            fos = new FileOutputStream(tempFile);
            fos.write(content.getBytes());
            message.addAttachment(subject+".txt", tempFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempFile.deleteOnExit();
        }
        this.mailSender.send(mimeMessage);
    }

}
