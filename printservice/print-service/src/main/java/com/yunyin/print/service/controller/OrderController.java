package com.yunyin.print.service.controller;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.StringUtil;
import com.yunyin.print.common.enums.OrderStatusEnum;
import com.yunyin.print.common.filter.PrintOrderFilter;
import com.yunyin.print.common.model.PrintOrder;
import com.yunyin.print.common.model.PrintOrderDetail;
import com.yunyin.print.service.constant.Constant;
import com.yunyin.print.service.dao.PrintOrderDao;
import com.yunyin.print.service.dao.PrintOrderDetailDao;
import com.yunyin.print.service.utils.DateUtil;
import com.yunyin.print.service.utils.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/14
 * @since 1.0
 */
@RestController
@RequestMapping("/order/*")
public class OrderController {
    @Autowired
    private PrintOrderDao       orderDao;
    @Autowired
    private PrintOrderDetailDao detailDao;

    private String DEFAULT_CODE_ORIGINAL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";

    private Logger log = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public Result<PrintOrder> getOrderByOrderId(String orderId, Boolean withDetail) {
        if (StringUtil.isEmpty(orderId)) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "orderId cannot be null or enpty");
        }
        PrintOrder order = orderDao.findByOrderId(orderId);
        if (order != null && withDetail) {
            order.setDetails(detailDao.findByOrderId(orderId));
        }
        return ResultHelper.genResultWithSuccess(order);
    }


    @RequestMapping(value = "/printCode", method = RequestMethod.GET)
    public Result<PrintOrder> getOrderByPrintCode(String printCode, Boolean withDetail) {
        if (StringUtil.isEmpty(printCode)) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "printCode cannot be null or enpty");
        }
        PrintOrder order = orderDao.findByPrintCode(printCode);
        if (order != null && withDetail) {
            order.setDetails(detailDao.findByOrderId(order.getOrderId()));
        }
        return ResultHelper.genResultWithSuccess(order);
    }

    @RequestMapping(value = "/findByFilter", method = RequestMethod.GET)
    public Result<List<PrintOrder>> getOrderByFilter(PrintOrderFilter filter, Boolean withDetail) {
        if (filter.getSiteId() == null && filter.getUid() == null) {
            //限制查询结果集大小
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "siteId or uid must exists one");
        }
        if (filter == null) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "PrintOrderFilter cannot be null");
        }
        List<PrintOrder> orders = orderDao.findByFilter(filter);

        if (!CollectionUtils.isEmpty(orders) && withDetail) {
            List<String> orderIds = orders.stream().map(PrintOrder::getOrderId).collect(Collectors.toList());

            List<PrintOrderDetail> details = detailDao.findByOrderIds(orderIds);

            for (PrintOrder order : orders) {
                order.setDetails(isMatch(details, order.getOrderId()));
            }
        }
        return ResultHelper.genResultWithSuccess(orders);
    }

    //TODO 根据print_code去获取订单

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @Transactional
    public Result<PrintOrder> insert(PrintOrder printOrder) {
        if (printOrder == null) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "PrintOrder cannot be null or enpty");
        }
        try {
            printOrder.setOrderId(IdGenerator.generateId(printOrder.getUid() + "", DateUtil.timestampToDate(printOrder.getCreateTime().intValue())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //生成6位打印码
        String printCode = getPrintCode();
        log.info("订单号:" + printOrder.getOrderId() + "－－－－打印码:" + printCode);
        printOrder.setPrintCode(printCode);

        //订单信息插入成功后，插入订单详情信息
        if (orderDao.insert(printOrder) == 1) {
            List<PrintOrderDetail> details = printOrder.getDetails();
            if (detailDao.addBatch(printOrder.getOrderId(), details) == details.size()) {  //插入成功
                return ResultHelper.genResultWithSuccess(printOrder);
            }
        }
        return ResultHelper.genResultWithSuccess(null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<Boolean> update(PrintOrder printOrder) {
        if (printOrder == null || printOrder.getOrderId() == null) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "PrintOrder cannot be null or enpty");
        }
        PrintOrder dbPrintOrder = orderDao.findMasterByOrderId(printOrder.getOrderId());

        byte status = printOrder.getOrderStatus().byteValue();

        //订单状态校验
        if (status != OrderStatusEnum.INIT.getCode().byteValue() ||
                status != OrderStatusEnum.CONFIRMED.getCode().byteValue() ||
                status != OrderStatusEnum.FINISHED.getCode().byteValue() ||
                status != OrderStatusEnum.CANCELED.getCode().byteValue()) {
            throw new ServiceException(Constant.EXCEPTION_CODE_ORDER_STATUS_ILLEGAL, "订单状态非法");
        }

        //取消订单
        byte dbStatus = dbPrintOrder.getOrderStatus().byteValue();
        if (status == OrderStatusEnum.CANCELED.getCode().byteValue()) {
            if (dbStatus == OrderStatusEnum.FINISHED.getCode()) {
                throw new ServiceException(Constant.EXCEPTION_CODE_ORDER_CANCEL_ILLEGAL, "当前订单已完成,不能取消");
            }

            //如果已经支付，需要增加退款操作
            if (dbPrintOrder.getPayTime() != null && dbPrintOrder.getPayTime() > 0) {
                //TODO 退款操作！！！
            }
        }

        return ResultHelper.genResultWithSuccess(orderDao.update(printOrder) == 1 ? true : false);
    }


    /**
     * 数据摘选
     *
     * @param details 数据集
     * @param orderId 订单id
     * @return
     */
    public List<PrintOrderDetail> isMatch(List<PrintOrderDetail> details, String orderId) {
        if (CollectionUtils.isEmpty(details) || null == orderId)
            return null;

        return details.stream().filter(detail -> orderId.compareTo(detail.getOrderId()) == 0)
                .collect(Collectors.toList());
    }

    public String getPrintCode() {
        String printCode = "";
        Random random = new Random(64);
        while (true) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                builder.append(DEFAULT_CODE_ORIGINAL.charAt(random.nextInt()));
            }
            printCode = builder.toString();
            //校验当前打印码是否已经存在
            PrintOrder printOrder = orderDao.findByPrintCode(printCode);
            if (printOrder == null) {
                //打印码唯一，退出循环
                break;
            }
        }
        return printCode;
    }
}
