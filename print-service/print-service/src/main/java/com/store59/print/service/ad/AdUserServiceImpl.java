package com.store59.print.service.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.print.common.filter.AdUserFilter;
import com.store59.print.common.model.ad.AdUser;
import com.store59.print.common.remoting.AdUserService;
import com.store59.print.service.ad.business.AdUserBusiness;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

/**
 * Created on 2016-08-17.
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = AdUserService.class, exportPath = "/aduser")
public class AdUserServiceImpl implements AdUserService {
	@Autowired
	private AdUserBusiness adUserBusiness;

	public Result<List<AdUser>> findAdUserList(AdUserFilter filter) {
		return ResultHelper.genResultWithSuccess(adUserBusiness.findAdUserList(filter));
	}

	public Result<AdUser> addAdUser(AdUser record) {
		return ResultHelper.genResultWithSuccess(adUserBusiness.addAdUser(record));
	}

	public Result<AdUser> updateAdUser(AdUser record) {
		if (record.getUid() == null || record.getUid().equals("0")) {
			ResultHelper.genResult(-1, "uid不能为空");
		}
		return ResultHelper.genResultWithSuccess(adUserBusiness.updateAdUser(record));
	}

	@Override
	public Result<Integer> getTotalAmounts(AdUserFilter filter) {
		return ResultHelper.genResultWithSuccess(adUserBusiness.getTotalAmount(filter));
	}

	public Result<AdUser> findAdUser(Long uid){
		return ResultHelper.genResultWithSuccess(adUserBusiness.findAdUser(uid));
	}
}
