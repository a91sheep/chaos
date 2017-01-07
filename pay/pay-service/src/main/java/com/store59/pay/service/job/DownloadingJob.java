package com.store59.pay.service.job;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.mapper.BatchWithrawInfoMapper;
import com.store59.pay.mapper.PayDetailMapper;
import com.store59.pay.model.BatchWithrawInfo;
import com.store59.pay.model.PayDetail;
import com.store59.pay.service.config.WangshangConfig;
import com.store59.pay.service.enums.BatchWithdrawStateEnum;
import com.store59.pay.service.util.ExcelUtil;
import com.store59.pay.service.util.SFTPUtil;

/**
 * 下载代发结果文件
 * @author beangou
 *
 */
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class DownloadingJob implements Runnable{
	
	private WangshangConfig wangshangConfig;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadingJob.class);
	
	private String batchNo;
	
	private String batchTransNo;
	
	private BatchWithrawInfoMapper masterBatchWithrawInfoMapper;
	
	private PayDetailMapper masterPayDetailMapper;
	
    private JavaMailSender mailSender;
	
	public DownloadingJob(String batchNo, String batchTransNo, BatchWithrawInfoMapper masterBatchWithrawInfoMapper, PayDetailMapper masterPayDetailMapper, WangshangConfig wangshangConfig, JavaMailSender mailSender) {
		this.batchNo = batchNo;
		this.batchTransNo = batchTransNo;
		this.masterBatchWithrawInfoMapper = masterBatchWithrawInfoMapper;
		this.masterPayDetailMapper = masterPayDetailMapper;
		this.wangshangConfig = wangshangConfig;
		this.mailSender = mailSender;
	}

	@Override
	public void run() {
		while(true) {
			try {
				LOGGER.info("等待20分钟...");
				// 如果上传失败，20min后再次下载
				Thread.sleep(20 * 60 * 1000);
				BatchWithrawInfo info = masterBatchWithrawInfoMapper.selectByTradeNo(batchNo);
				if(BatchWithdrawStateEnum.NOTICING_ERP_RESULT_FAILURE.getCode() == info.getState() 
					|| BatchWithdrawStateEnum.NOTICING_ERP_RESULT_SUCCESS.getCode() == info.getState()) {
					// 如果已经下载成功（被定时任务执行）
					LOGGER.info("batchNo={}已经被定时任务下载，所以无需下载，线程结束", batchNo);
					break;
				}
				LOGGER.info("开始下载文件:{}....", batchTransNo);
				InputStream is = new SFTPUtil(wangshangConfig).downloadResultFile(batchTransNo, null);
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
					break;
				}else {
					info.setState(BatchWithdrawStateEnum.DOWNLOAD_RESULT_FILE_FAILURE.getCode());
					masterBatchWithrawInfoMapper.updateByTradeNoSelective(info);
					LOGGER.error("下载结果文件失败，再次重试, batchTransNo={}, batchNo={}", batchTransNo, batchNo);
				}
			} catch (InterruptedException e) {
				LOGGER.error("{}下载失败，睡眠发生异常，{}", batchTransNo, e);
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("下载结果文件失败，再次重试, batchTransNo={}, batchNo={}, {}", batchTransNo, batchNo, e);
			}
		}
	}

}
