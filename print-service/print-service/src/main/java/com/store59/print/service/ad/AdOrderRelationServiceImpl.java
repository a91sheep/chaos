package com.store59.print.service.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.print.common.filter.AdOrderRelationFilter;
import com.store59.print.common.filter.LineCharFilter;
import com.store59.print.common.model.ad.AdOrderRelation;
import com.store59.print.common.model.ad.AdStatistics;
import com.store59.print.common.model.ad.LineChar;
import com.store59.print.common.remoting.AdOrderRelationService;
import com.store59.print.service.ad.business.AdOrderRelationBusiness;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

/**
 * Created on 2016-08-17.
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = AdOrderRelationService.class, exportPath = "/adorderrelation")
public class AdOrderRelationServiceImpl implements AdOrderRelationService {
	@Autowired
	private AdOrderRelationBusiness adOrderRelationBusiness;

	public Result<List<AdOrderRelation>> findAdOrderRelationList(AdOrderRelationFilter filter) {
		return ResultHelper.genResultWithSuccess(adOrderRelationBusiness.findAdOrderRelationList(filter));
	}

	public Result<AdOrderRelation> addAdOrderRelation(AdOrderRelation record) {
		return ResultHelper.genResultWithSuccess(adOrderRelationBusiness.addAdOrderRelation(record));
	}

	@Override
	public Result<AdStatistics> statistics(String orderId, Long uid) {
		return ResultHelper.genResultWithSuccess(adOrderRelationBusiness.statistics(orderId, uid));
	}

	@Override
	public Result<List<LineChar>> lineChar(LineCharFilter filter) {
		return ResultHelper.genResultWithSuccess(adOrderRelationBusiness.lineChar(filter));
	}
	
	public Result<Integer> addAdOrderRelationList(List<AdOrderRelation> list){
		return ResultHelper.genResultWithSuccess(adOrderRelationBusiness.addAdOrderRelationList(list));
	}
	public Result<Integer> getTotalAmounts(AdOrderRelationFilter filter){
		return ResultHelper.genResultWithSuccess(adOrderRelationBusiness.getTotalAmount(filter));
	}
}
