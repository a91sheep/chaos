package com.store59.print.service.ad;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.print.common.filter.AdOrderFilter;
import com.store59.print.common.model.ad.AdFreeOrder;
import com.store59.print.common.model.ad.AdOrder;
import com.store59.print.common.remoting.AdOrderService;
import com.store59.print.service.ad.business.AdOrderBusiness;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

/**
 * Created on 2016-08-17.
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = AdOrderService.class, exportPath = "/adorder")
public class AdOrderServiceImpl implements AdOrderService {
	@Autowired
	private AdOrderBusiness adOrderBusiness;

	public Result<AdOrder> findAdOrder(String id, Long uid) {
		return ResultHelper.genResultWithSuccess(adOrderBusiness.findAdOrder(id, uid));
	}

	public Result<List<AdOrder>> findAdOrderList(AdOrderFilter filter) {
		return ResultHelper.genResultWithSuccess(adOrderBusiness.findAdOrderList(filter));
	}

	public Result<AdOrder> addAdOrder(AdOrder record) {
		return ResultHelper.genResultWithSuccess(adOrderBusiness.addAdOrder(record));
	}

	public Result<Boolean> updateAdOrder(AdOrder record) {
		if (record.getStatus() == 4) {
			// TODO 订单状态判断
			AdOrder rs = adOrderBusiness.findAdOrder(record.getOrderId(), record.getUid());
			if (rs.getStatus() == 1) {

			} else {
				return ResultHelper.genResult(-1, "非待审核订单用户不能取消");
			}
		}
		return ResultHelper.genResultWithSuccess(adOrderBusiness.updateAdOrder(record));
	}

	public Result<AdFreeOrder> findFreeOrder(AdOrderFilter filter) {
		AdFreeOrder free = new AdFreeOrder();
		filter.setOffset(0);
		Integer[] status = { 3 };
		List<Integer> statusList = Arrays.asList(status);
		filter.setStatusList(statusList);
		filter.setSort(2);
		List<AdOrder> footerResult = adOrderBusiness.freeAd(filter);
		free.setFooterOrder(footerResult);
		// 获取免费打印订单页
		filter.setLimit(1);
		filter.setSort(1);
		List<AdOrder> homeResult = adOrderBusiness.freeAd(filter);
		if (homeResult.size() == 0) {
			free.setHomeOrder(null);
		} else {
			free.setHomeOrder(homeResult.get(0));
		}
		return ResultHelper.genResultWithSuccess(free);
	}
	public Result<Integer> getTotalAmounts(AdOrderFilter filter){
		return ResultHelper.genResultWithSuccess(adOrderBusiness.getTotalAmount(filter));
	}
	public Result<List<AdOrder>> findOrderUsedList(AdOrderFilter filter) {
		return ResultHelper.genResultWithSuccess(adOrderBusiness.findOrderUsedList(filter));
	}
}
