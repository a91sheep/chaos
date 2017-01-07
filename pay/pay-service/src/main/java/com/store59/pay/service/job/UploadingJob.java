package com.store59.pay.service.job;

import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.store59.pay.mapper.BatchWithrawInfoMapper;
import com.store59.pay.model.BatchWithrawInfo;
import com.store59.pay.service.config.WangshangConfig;
import com.store59.pay.service.enums.BatchWithdrawStateEnum;
import com.store59.pay.service.util.SFTPUtil;

public class UploadingJob implements Runnable{
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadingJob.class);
	
	// 加密后的名册文件
	private String encryptFileName;
	// 批次号
	private String batchNo;
	
    private BatchWithrawInfoMapper masterBatchWithrawInfoMapper;
    
    private WangshangConfig wangshangConfig;
	
	public UploadingJob(String encryptFileName, String batchNo, BatchWithrawInfoMapper masterBatchWithrawInfoMapper, WangshangConfig wangshangConfig) {
		this.encryptFileName = encryptFileName;
		this.batchNo = batchNo;
		this.masterBatchWithrawInfoMapper = masterBatchWithrawInfoMapper;
		this.wangshangConfig = wangshangConfig;
	}
 	
	@Override
	public void run() {
		SFTPUtil sftpUtil = new SFTPUtil(wangshangConfig);
		while(true) {
			try {
				BatchWithrawInfo info = masterBatchWithrawInfoMapper.selectByTradeNo(batchNo);
				if(info.getState() > 1) {
					// 如果scheduledTask已经成功将文件上传，就不用执行了
					LOGGER.info("批次号{}已经上传成功，线程结束", batchNo);
					break;
				}
				// 每一批的提现状态（0：上传文件中；1：成功上传文件；2：成功获取结果文件；3：获取结果文件失败；4：上传文件失败）
				LOGGER.info("开始上传文件:{}....", encryptFileName);
				sftpUtil.upLoadFile(new FileInputStream(encryptFileName), batchNo);
				info.setTradeNo(batchNo);
				info.setState(BatchWithdrawStateEnum.UPLOADED.getCode());
				// 更新数据库中的状态
				int updateNum = masterBatchWithrawInfoMapper.updateByTradeNoSelective(info);
				if(updateNum == 1) {
					LOGGER.info("trade_no={}更新BatchWithrawInfo状态成功", batchNo);
				}else {
					LOGGER.error("trade_no={}更新BatchWithrawInfo状态失败", batchNo);
				}
				LOGGER.info("{}上传成功", encryptFileName);
				// 通知ERP名册文件上传成功
				new Thread(new NoticingErpUploadingJob(batchNo, masterBatchWithrawInfoMapper, wangshangConfig)).start();
				break;
			} catch (Exception e) {
				LOGGER.error("{}上传失败，{}", encryptFileName, e);
				e.printStackTrace();
			}
			try {
				// 如果上传失败，5min后再次上传
				Thread.sleep(5 * 60 * 1000);
			} catch (InterruptedException e) {
				LOGGER.error("{}上传失败，睡眠发生异常，{}", encryptFileName, e);
				e.printStackTrace();
			}
		}
	}
	
}
