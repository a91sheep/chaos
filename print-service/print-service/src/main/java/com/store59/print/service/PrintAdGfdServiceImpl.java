/**
 * 
 */
package com.store59.print.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.StringUtil;
import com.store59.print.common.filter.FreePrintOrderFilter;
import com.store59.print.common.model.PrintAdGfdOrder;
import com.store59.print.common.remoting.PrintAdGfdService;
import com.store59.print.dao.PrintAdGfdDao;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年8月23日 上午10:26:00
 * @since 1.0
 */
@RemoteService(serviceInterface = PrintAdGfdService.class, serviceType = ServiceType.HESSIAN, exportPath = "/printadgfdorder")
public class PrintAdGfdServiceImpl implements PrintAdGfdService {

	@Autowired
	private PrintAdGfdDao printAdGfdDao;

	@Override
	public Result<PrintAdGfdOrder> insert(PrintAdGfdOrder adGfdOrder) {
		if (adGfdOrder == null) {
			throw new ServiceException(4, "adGfdOrder cannot null");
		}
		if (printAdGfdDao.insert(adGfdOrder) == 1) {
			return ResultHelper.genResultWithSuccess(adGfdOrder);
		}
		return ResultHelper.genResultWithSuccess(null);
	}

	@Override
	public Result<Boolean> update(PrintAdGfdOrder adGfdOrder) {
		if (adGfdOrder == null) {
			throw new ServiceException(4, "adGfdOrder cannot null");
		}
		return ResultHelper.genResultWithSuccess(printAdGfdDao.update(adGfdOrder) == 1 ? true : false);
	}

	@Override
	public Result<PrintAdGfdOrder> findByOrderId(String order_id) {
		if (StringUtil.isEmpty(order_id)) {
			throw new ServiceException(4, "order_id cannot null or empty");
		}

		return ResultHelper.genResultWithSuccess(printAdGfdDao.findByOrderId(order_id));
	}

	@Override
	public Result<Integer> calFreePrintCount(FreePrintOrderFilter filter) {
		if (filter == null) {
			throw new ServiceException(4, "filter cannot null");
		}
		return ResultHelper.genResultWithSuccess(printAdGfdDao.calFreePrintCount(filter));
	}

	@Override
	public Result<List<PrintAdGfdOrder>> findFreeOrderByFilter(FreePrintOrderFilter filter) {
		if (filter == null) {
			throw new ServiceException(4, "filter cannot null");
		}

		return ResultHelper.genResultWithSuccess(printAdGfdDao.findFreeOrderByFilter(filter));
	}

}
