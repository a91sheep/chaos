package com.store59.print.common.remoting;

import java.util.List;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.filter.BillRecordFilter;
import com.store59.print.common.model.ad.BillRecord;

/**
 * BillRecord 接口
 * 
 * Created on 2016-08-17.
 */
public interface BillRecordService {

	/**
	 * 查询BillRecord信息列表
	 *
	 * @param filter
	 * @return
	 */
	Result<List<BillRecord>> findBillRecordList(BillRecordFilter filter);

	/**
	 * 添加BillRecord信息
	 *
	 * @param record
	 * @return
	 */
	Result<BillRecord> addBillRecord(BillRecord record);
}
