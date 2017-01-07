/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.wangshang;

import java.util.Map;

import com.store59.pay.model.BatchWithrawInfo;

/**
 * 签名服务接口.
 *
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public interface WithdrawService {
	public int insert(BatchWithrawInfo record);
	
	public Map<String, Object> batchWithdraw(String bizNo, String[] withdrawInfo) throws Exception;

	Map<String, Object> withdrawConfirm(String batchNo, String bizNo, String smsCode, String totalCount, String totalAmount, String currencyCode, String remark) throws Exception;

	public Map<String, Object> smscode(String bizNo, String bizName, String certNo) throws Exception;

	boolean downloadResultFile(String batchTransNo, String batchNo) throws Exception;

	BatchWithrawInfo selectByTradeNo(String batchNo);
}
