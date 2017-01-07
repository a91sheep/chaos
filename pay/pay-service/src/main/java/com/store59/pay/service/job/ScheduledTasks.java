/**
 * 
 */
package com.store59.pay.service.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.mapper.BatchWithrawInfoMapper;
import com.store59.pay.mapper.PayDetailMapper;
import com.store59.pay.model.BatchWithrawInfo;
import com.store59.pay.model.PayDetail;
import com.store59.pay.service.config.WangshangConfig;
import com.store59.pay.service.enums.BatchWithdrawStateEnum;
import com.store59.pay.service.util.ExcelUtil;
import com.store59.pay.service.util.HttpClient;
import com.store59.pay.service.util.SFTPUtil;

/**
 * 
 * @author <a href="mailto:liutb@59store.com">小彬</a>
 * @version 1.1 2016年4月27日
 * @since 1.1
 */
@Component
@Configurable
@EnableScheduling
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class ScheduledTasks {
	
    @Autowired
    JavaMailSender mailSender;
    
	@Autowired
    private BatchWithrawInfoMapper masterBatchWithrawInfoMapper;
    
    @Autowired
    private PayDetailMapper masterPayDetailMapper;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    
    private static String PATH = ScheduledTasks.class.getClassLoader().getResource("").getPath() + "files/upload/";
    
    @Autowired
	private WangshangConfig wangshangConfig;
    
    private String[] to = {"liutb@59store.com"};
    
    // 上传名册文件，每1小时检查一次
    @Scheduled(cron = "0 0 0/1 * * * ")
    public void uploading() {
    	// 找出需要上传的文件记录
    	Integer[] states = {BatchWithdrawStateEnum.UPLOADING.getCode()};
    	List<BatchWithrawInfo> list = masterBatchWithrawInfoMapper.selectByState(states);
    	LOGGER.info("uploading schedule...");
    	LOGGER.info(JsonUtil.getJsonFromObject(list));
    	if(list.isEmpty()) {
    		return;
    	}
		for (BatchWithrawInfo batchWithrawInfo : list) {
			String tradeNo = "";
			try {
				tradeNo = batchWithrawInfo.getTradeNo();
				LOGGER.info("开始上传文件:{}....", tradeNo);
				new SFTPUtil(wangshangConfig).upLoadFile(new FileInputStream(PATH+tradeNo+"_encrypted.xls"), tradeNo);
				batchWithrawInfo.setState(BatchWithdrawStateEnum.UPLOADED.getCode());
				// 更新数据库中的状态
				int updateNum = masterBatchWithrawInfoMapper.updateByTradeNoSelective(batchWithrawInfo);
				if(updateNum == 1) {
					LOGGER.info("trade_no={}更新BatchWithrawInfo状态成功", tradeNo);
				}else {
					LOGGER.error("trade_no={}更新BatchWithrawInfo状态失败", tradeNo);
				}
				LOGGER.info("{}上传成功", PATH+tradeNo+"_encrypted.xls");
				// 通知ERP名册文件上传成功
				new Thread(new NoticingErpUploadingJob(tradeNo, masterBatchWithrawInfoMapper, wangshangConfig)).start();
			} catch (Exception e) {
				sendMail("批次号为"+tradeNo+"的文件上传失败", "批次号为"+tradeNo+"的文件上传失败", to);
				LOGGER.error("{}上传失败，{}", PATH+batchWithrawInfo.getTradeNo()+"_encrypted.xls", e);
				e.printStackTrace();
			}
		} 
    }
    
    // 下载结果文件,每1小时检查一次
    @Scheduled(cron = "0 0 0/1 * * * ")
    public void downloading() {
    	// 找出需要下载的文件记录
    	Integer[] states = {BatchWithdrawStateEnum.DOWNLOAD_RESULT_FILE_FAILURE.getCode(), BatchWithdrawStateEnum.CONFIRMED_SUCCESS.getCode()};
    	List<BatchWithrawInfo> list = masterBatchWithrawInfoMapper.selectByState(states);
    	LOGGER.info(JsonUtil.getJsonFromObject(list));
    	LOGGER.info("downloading schedule...");
    	if(list.isEmpty()) {
    		return;
    	}
    	
    	for (BatchWithrawInfo batchWithrawInfo : list) {
        	String batchTransNo = batchWithrawInfo.getBatchTransNo();
        	String batchNo = batchWithrawInfo.getTradeNo();
	    	try {
				LOGGER.info("开始下载文件:{}....", batchTransNo);
				InputStream is = new SFTPUtil(wangshangConfig).downloadResultFile(batchTransNo, null);
				if(is == null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					Date yesterday = new Date(new Date().getTime()-24*60*60*1000);
		        	String dateStr = sdf.format(yesterday);
		        	// 有可能是昨天的请求，所以需要到昨天目录查找
					is = new SFTPUtil(wangshangConfig).downloadResultFile(batchTransNo, dateStr);
				}
				BatchWithrawInfo info = new BatchWithrawInfo();
				info.setTradeNo(batchNo);
				if(is != null) {
					info.setState(BatchWithdrawStateEnum.DOWNLOAD_RESULT_FILE_SUCCESS.getCode());
					// 更新batchWithdraw的状态，即下载成功
					int updateBatchWithdrawResult = masterBatchWithrawInfoMapper.updateByTradeNoSelective(info);
					LOGGER.info("updateBatchWithdrawResult完成更新数据{}条", updateBatchWithdrawResult);
					List<PayDetail> payDetailListResult = ExcelUtil.readExcel(is, batchTransNo, batchNo);
					// 批量更新数据库中的payDetail转账结果
					int updateResult = masterPayDetailMapper.batchUpdateByCheckingId(payDetailListResult);
					LOGGER.info("batchTransNo完成更新数据{}条", updateResult);
					LOGGER.info("完成下载文件:{}", batchTransNo);
					// 下载结果文件成功，开始通知ERP
					new Thread(new NoticingErpResultJob(batchNo, masterPayDetailMapper, masterBatchWithrawInfoMapper, wangshangConfig, mailSender)).start();
				}else {
					sendMail("批次号为"+batchNo+"的文件下载失败", "批次号为"+batchNo+"的文件下载失败", to);
					LOGGER.error("下载结果文件失败，再次重试, batchTransNo={}, batchNo={}", batchTransNo, batchNo);
				}
			} catch (Exception e) {
				e.printStackTrace();
				sendMail("批次号为"+batchNo+"的文件下载失败", "批次号为"+batchNo+"的文件下载失败，失败信息："+e.getMessage(), to);
				LOGGER.error("下载结果文件失败，再次重试, batchTransNo={}, batchNo={}, {}", batchTransNo, batchNo, e);
			}
		}
    }
    
    // 通知ERP结果,每1小时检查一次
    @Scheduled(cron = "0 0 0/1 * * * ")
    public void noticingErpResult() {
    	// 找出通知ERP失败的批次
    	LOGGER.info("noticingErp result schedule...");
    	// 找出通知结果失败的数据
    	Integer[] states = {BatchWithdrawStateEnum.NOTICING_ERP_RESULT_FAILURE.getCode()};
    	List<BatchWithrawInfo> batchWithrawInfolist = masterBatchWithrawInfoMapper.selectByState(states);
    	if(batchWithrawInfolist.isEmpty()) {
    		return;
    	}
    	for (BatchWithrawInfo batchWithrawInfo : batchWithrawInfolist) {
    		String batchNo = batchWithrawInfo.getTradeNo();
        	// 通知ERP结果
    		LOGGER.info("开始通知ERP批量代发结果, batchNo={}", batchNo);
    		// 通知成功，即 break
    		List<PayDetail> list = masterPayDetailMapper.selectByTradeNo(batchNo);
    		if(list.isEmpty()) {
    			LOGGER.info("批次号{}没有提现数据", batchNo);
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
    				// （增加流水号、姓名、金额）
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
				if(failSb.length() > 0) {
					String[] resutlTo = {"liutb@59store.com", "hucx@59store.com", "suhy@59store.com"};
					sendMail("批量代发异常流水记录(批次号："+batchNo+")", failSb.toString(), resutlTo);
				}
			}else {
				sendMail("批次号为"+batchNo+"通知ERP结果失败", "批次号为"+batchNo+"通知ERP结果失败", to);
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
    	LOGGER.info("开始发送邮件");
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
        LOGGER.info("开始发送邮件");
    }
    
}
