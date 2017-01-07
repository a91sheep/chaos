/**
 * 
 */
package com.store59.print.service.gala259user;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.DateUtil;
import com.store59.print.common.filter.Print259UserFilter;
import com.store59.print.common.model.gala259user.Print259Record;
import com.store59.print.common.model.gala259user.Print259Repay;
import com.store59.print.common.model.gala259user.Print259User;
import com.store59.print.common.model.gala259user.PrintShopTime;
import com.store59.print.common.remoting.Print259UserService;
import com.store59.print.dao.Print259RecordDao;
import com.store59.print.dao.Print259RepayDao;
import com.store59.print.dao.Print259UserDao;
import com.store59.print.dao.PrintShopTimeDao;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月20日 下午2:27:08
 * @since 1.0
 */

@RemoteService(serviceInterface = Print259UserService.class, serviceType = ServiceType.HESSIAN, exportPath = "/print259user")
public class Print259UserServiceImpl implements Print259UserService {

	private static Logger logger = LoggerFactory.getLogger(Print259UserService.class);

	@Autowired
	private Print259UserDao print259UserDao;

	@Autowired
	private Print259RepayDao print259RepayDao;

	@Autowired
	private Print259RecordDao print259RecordDao;

	@Autowired
	private PrintShopTimeDao printShopTimeDao;

	@Override
	public Result<List<Print259User>> findByFilter(Print259UserFilter filter) {
		if (filter.getDorm_id() == null || filter.getStatus() == null) {
			throw new ServiceException(4, "dormId or status must exist");
		}

		return ResultHelper.genResultWithSuccess(print259UserDao.find259UserByFilter(filter));
	}

	@Override
	public Result<Integer> findCountByFilter(Print259UserFilter filter) {
		if (filter.getDorm_id() == null || filter.getStatus() == null) {
			throw new ServiceException(4, "dormId or status must exist");
		}

		return ResultHelper.genResultWithSuccess(print259UserDao.findCountByFilter(filter));
	}

	@Override
	public Result<List<Print259Repay>> findRepayByDormId(Integer dormId) {
		if (dormId == null) {
			throw new ServiceException(4, "dormId must exist");
		}

		return ResultHelper.genResultWithSuccess(print259RepayDao.findRepayByDormId(dormId));
	}

	@Override
	public Result<Print259Record> findDormFinish259Time(Integer dormId) {
		if (dormId == null) {
			throw new ServiceException(4, "dormId must exist");
		}
		return ResultHelper.genResultWithSuccess(print259RecordDao.findDormFinish259Time(dormId));
	}

	@Override
	public Result<List<PrintShopTime>> findPrintShopTimeByDormId(Integer dormId, Long time) {
		if (dormId == null || time == null) {
			throw new ServiceException(4, "dormId and time must exist");
		}
		return ResultHelper.genResultWithSuccess(printShopTimeDao.findPrintShopTimeByDormId(dormId, time));
	}

	@Override
	public Result<Boolean> findCountPrintShopTimeByDormId(Integer dormId, Long time) {
		Integer totalCount = 0;
		if (dormId == null || time == null) {
			throw new ServiceException(4, "dormId and time must exist");
		}

		// 判断该月营业时间，2，7，8月不考核，1，6，9月每天营业5小时10天， 其他都要每天营业5小时20天
		long timestamp = time * 1000L;
		int month = DateUtil.getMonth(new Date(timestamp));
		if (month == 2 || month == 7 || month == 8) {
			return ResultHelper.genResultWithSuccess(true);

		} else if (month == 1 || month == 6 || month == 9) {
			totalCount = printShopTimeDao.findCountPrintShopTimeByDormId(dormId, time);

			if (totalCount < 10) {
				return ResultHelper.genResultWithSuccess(false);
			}
		} else {
			totalCount = printShopTimeDao.findCountPrintShopTimeByDormId(dormId, time);
			if (totalCount < 20) {
				return ResultHelper.genResultWithSuccess(false);
			}
		}

		return ResultHelper.genResultWithSuccess(true);
	}

	@Override
	public Result<Boolean> update259RecordValidValue(Integer dormId, Byte status) {
		if (dormId == null || status == null) {
			throw new ServiceException(4, "dormId and status must exist");
		}

		return ResultHelper.genResultWithSuccess(print259RecordDao.update259RecordValidValue(dormId, status) == 1);
	}

	@Override
	public Result<Boolean> updateBatchRepayList(List<Print259Repay> repaylist) {
		if (repaylist == null || repaylist.size() == 0) {
			return ResultHelper.genResultWithSuccess(true);
		}
		return ResultHelper.genResultWithSuccess(print259RepayDao.updateBatchRepayList(repaylist) == repaylist.size());
	}

}
