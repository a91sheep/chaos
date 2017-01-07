package com.store59.pay.service.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.mapper.BatchWithrawInfoMapper;
import com.store59.pay.model.BatchWithrawInfo;
import com.store59.pay.service.config.WangshangConfig;
import com.store59.pay.service.enums.BatchWithdrawStateEnum;
import com.store59.pay.service.util.HttpClient;

/**
 * 通知ERP批量代发结果
 * @author beangou
 *
 */
public class NoticingErpUploadingJob implements Runnable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NoticingErpUploadingJob.class);
	
	private String batchNo;
	
	private BatchWithrawInfoMapper masterBatchWithrawInfoMapper;
	
	private WangshangConfig wangshangConfig;
	
	public static final String ERP_SALT = "dc6bb89e1222f5c4ec42314354fe9ca9ab7252c9";
	
	public NoticingErpUploadingJob(String batchNo, BatchWithrawInfoMapper masterBatchWithrawInfoMapper, WangshangConfig wangshangConfig) {
		this.batchNo = batchNo;
		this.masterBatchWithrawInfoMapper = masterBatchWithrawInfoMapper;
		this.wangshangConfig = wangshangConfig;
	}

	@Override
	public void run() {
		while(true) {
			BatchWithrawInfo info = masterBatchWithrawInfoMapper.selectByTradeNo(batchNo);
			if(info.getState() != BatchWithdrawStateEnum.UPLOADED.getCode()) {
				LOGGER.info("批次号{}已经成功通知到ERP上传成功，线程结束", batchNo);
				break;
			}
			// 通知ERP结果
			LOGGER.info("开始通知ERP上传成功, batchNo={}", batchNo);
			// 通知成功，即 break
			try {
				Map<String, String> paramMap = new HashMap<>();
				paramMap.put("batchNo", batchNo);
				String sign = DigestUtils.md5Hex(batchNo+ERP_SALT);
				paramMap.put("sign", sign);
				Result<Map<String, String>> result = HttpClient.post(wangshangConfig.getErpReqUrl().trim()+"/withdraw/callback/doc_uploaded", paramMap);
				LOGGER.info("通知ERP上传成功的返回结果=>{}", JsonUtil.getJsonFromObject(result));
				BatchWithrawInfo record = new BatchWithrawInfo();
				if(result.getStatus() == 0) {
					record.setState(BatchWithdrawStateEnum.UPLOADED.getCode());
					masterBatchWithrawInfoMapper.updateByPrimaryKey(record);
					break;
				}
				// 每5分钟通知一次
				Thread.sleep(5*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
