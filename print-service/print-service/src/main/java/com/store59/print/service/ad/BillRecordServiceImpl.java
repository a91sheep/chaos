package com.store59.print.service.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.print.common.filter.BillRecordFilter;
import com.store59.print.common.model.ad.BillRecord;
import com.store59.print.common.remoting.BillRecordService;
import com.store59.print.service.ad.business.BillRecordBusiness;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

/**
 * Created on 2016-08-17.
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = BillRecordService.class, exportPath = "/billrecord")
public class BillRecordServiceImpl implements BillRecordService {
	@Autowired
	private BillRecordBusiness billRecordBusiness;


	public Result<List<BillRecord>> findBillRecordList(BillRecordFilter filter) {
		return ResultHelper.genResultWithSuccess(billRecordBusiness.findBillRecordList(filter));
	}

	public Result<BillRecord> addBillRecord(BillRecord record) {
		return ResultHelper.genResultWithSuccess(billRecordBusiness.addBillRecord(record));
	}


}
