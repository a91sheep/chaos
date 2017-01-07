package com.yunyin.print.service.controller;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.StringUtil;
import com.yunyin.print.common.model.PrintOrderDetail;
import com.yunyin.print.service.constant.Constant;
import com.yunyin.print.service.dao.PrintOrderDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/14
 * @since 1.0
 */
@RestController
@RequestMapping("/orderDetail/*")
public class OrderdetailController {
    @Autowired
    PrintOrderDetailDao detailDao;

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public Result<List<PrintOrderDetail>> findByOrderId(String orderId) {
        if (StringUtil.isEmpty(orderId)) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "orderId cannot be null or enpty");
        }
        return ResultHelper.genResultWithSuccess(detailDao.findByOrderId(orderId));
    }

    @RequestMapping(value = "/recordList", method = RequestMethod.GET)
    public Result<List<PrintOrderDetail>> findByOrderId(List<String> orderIds) {
        if (orderIds == null || orderIds.size() == 0) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "orderIds cannot be null or enpty");
        }
        return ResultHelper.genResultWithSuccess(detailDao.findByOrderIds(orderIds));
    }

    @RequestMapping(value = "/addBatch", method = RequestMethod.POST)
    public Result<Boolean> addBatch(String orderId, List<PrintOrderDetail> details) {
        if (StringUtil.isEmpty(orderId)) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "orderId cannot be null or enpty");
        }
        return ResultHelper.genResultWithSuccess(detailDao.addBatch(orderId, details) == details.size() ? true : false);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<Boolean> update(PrintOrderDetail detail) {
        if (detail == null) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "PrintOrderDetail cannot be null or enpty");
        }
        return ResultHelper.genResultWithSuccess(detailDao.updateStatus(detail) == 1 ? true : false);
    }

}
