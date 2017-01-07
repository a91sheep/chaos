/**
 *
 */
package com.store59.print.service;

import com.store59.base.common.api.OrderPayAbnormalRecordApi;
import com.store59.base.common.model.OrderPayAbnormalRecord;
import com.store59.creditcard.common.enums.CancelTypeEnum;
import com.store59.creditcard.common.enums.TradeSourceTypeEnum;
import com.store59.creditcard.common.enums.TradeTypeEnum;
import com.store59.creditcard.common.remoting.CreditCardPayApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.data.ConstDorm;
import com.store59.dorm.common.filter.DormShopFilter;
import com.store59.dorm.common.model.DormShop;
import com.store59.dorm.common.model.DormShopDelivery;
import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.enums.PrintOrderStatusEnum;
import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.print.common.remoting.PrintOrderService;
import com.store59.print.dao.PrintOrderDao;
import com.store59.print.dao.PrintOrderDetailDao;
import com.store59.print.tools.OrderIdTool;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.utils.ArrayList;
import java.utils.Arrays;
import java.utils.List;
import java.utils.stream.Collectors;

/**
 * 打印店订单order,数据实现层
 *
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2016年1月7日
 * @since 1.1
 */
@RemoteService(serviceInterface = PrintOrderService.class, serviceType = ServiceType.HESSIAN, exportPath = "/printorder")
public class PrintOrderServiceImpl implements PrintOrderService {

    private static Logger logger = LoggerFactory.getLogger(PrintOrderService.class);

    @Autowired
    private PrintOrderDao       printOrderDao;
    @Autowired
    private PrintOrderDetailDao printOrderDetailDao;

    @Autowired
    private OrderPayAbnormalRecordApi orderPayAbnormalRecordApi;

    @Autowired
    private DormShopApi dormShopApi;

    @Autowired
    private CreditCardPayApi creditCardPayApi;

    @Override
    public Result<Boolean> updatePrintOrderDetail(PrintOrderDetail detail) {
        return ResultHelper.genResultWithSuccess(printOrderDetailDao.updateStatus(detail) == 1);
    }

    @Override
    public Result<PrintOrderDetail> insertPrintOrderDetail(Long orderId, PrintOrderDetail printOrderDetail) {
        List<PrintOrderDetail> details = new ArrayList<>();
        details.add(printOrderDetail);
        if (printOrderDetailDao.addBatch(orderId, details) == details.size()) {
            return ResultHelper.genResultWithSuccess(printOrderDetail);
        }
        return ResultHelper.genResultWithSuccess(null);
    }

    @Override
    public Result<PrintOrder> findByOrderId(Long orderId, Boolean isDetail) {
        PrintOrder printOrder = printOrderDao.findByOrderId(orderId);
        if (printOrder != null && isDetail) {
            printOrder.setDetails(printOrderDetailDao.findByOrderId(orderId));
        }
        return ResultHelper.genResultWithSuccess(printOrder);
    }

    @Override
    public Result<List<PrintOrder>> findByFilter(PrintOrderFilter filter, Boolean isDetail) {
        if (filter.getDormId() == null && filter.getUid() == null) {
            throw new ServiceException(4, "dormId or uid must exists one");
        }

        List<PrintOrder> printOrders = new ArrayList<>();
        List<Integer> statusList = filter.getStatusList();
        if (statusList != null && statusList.size() > 0) {
            if (statusList.size() == 1) {
                int status = statusList.get(0);
                if (status == (int) PrintConst.ORDER_STATUS_CONFIRMED) {// 前端请求可打印
                    filter.setStatusList(
                            Arrays.asList((int) PrintConst.ORDER_STATUS_INIT, (int) PrintConst.ORDER_STATUS_CONFIRMED));
                    filter.setPayTime(0L);
                    filter.setPayType(PrintConst.ORDER_STATUS_CONFIRMED);
                    filter.setTradeNo("1");

                } else if (status == (int) PrintConst.ORDER_STATUS_INIT) {// 前端请求未支付
                    filter.setStatusList(
                            Arrays.asList((int) PrintConst.ORDER_STATUS_INIT, (int) PrintConst.ORDER_STATUS_CONFIRMED));
                    filter.setPayTime(null);
                    filter.setPayType(null);
                    filter.setTradeNo(null);
                }
                printOrders = printOrderDao.findByFilter(filter);

            } else {
                for (int status : statusList) {
                    PrintOrderFilter tmpFilter = filter;
                    tmpFilter.setStatusList(null);

                    if (status == (int) PrintConst.ORDER_STATUS_CONFIRMED) {// 前端请求可打印
                        tmpFilter.setStatusList(Arrays.asList((int) PrintConst.ORDER_STATUS_INIT,
                                (int) PrintConst.ORDER_STATUS_CONFIRMED));
                        tmpFilter.setPayTime(0L);
                        tmpFilter.setPayType(PrintConst.ORDER_STATUS_CONFIRMED);
                        tmpFilter.setTradeNo("1");

                    } else if (status == (int) PrintConst.ORDER_STATUS_INIT) {// 前端请求未支付
                        tmpFilter.setStatusList(Arrays.asList((int) PrintConst.ORDER_STATUS_INIT,
                                (int) PrintConst.ORDER_STATUS_CONFIRMED));
                        tmpFilter.setPayTime(null);
                        tmpFilter.setPayType(null);
                        tmpFilter.setTradeNo(null);

                    } else {
                        tmpFilter.setStatusList(Arrays.asList(status));
                    }

                    printOrders.addAll(printOrderDao.findByFilter(tmpFilter));
                }

            }
        } else {
            printOrders = printOrderDao.findByFilter(filter);
        }

        if (!CollectionUtils.isEmpty(printOrders) && isDetail) {
            List<Long> orderIds = printOrders.stream().map(PrintOrder::getOrderId).collect(Collectors.toList());

            List<PrintOrderDetail> details = printOrderDetailDao.findByOrderIds(orderIds);
            for (PrintOrder order : printOrders) {
                order.setDetails(isMatch(details, order.getOrderId()));
            }

        }
        return ResultHelper.genResultWithSuccess(printOrders);
    }

    @Override
    public Result<Integer> findCountByFilter(PrintOrderFilter filter) {
        if (filter.getDormId() == null && filter.getUid() == null) {
            throw new ServiceException(4, "dormId or uid must exists one");
        }

        return ResultHelper.genResultWithSuccess(printOrderDao.findCountByFilter(filter));
    }

    @Override
    public Result<Double> findSumAmountByFilter(PrintOrderFilter filter) {
        if (filter.getDormId() == null && filter.getUid() == null) {
            throw new ServiceException(4, "dormId or uid must exists one");
        }

        return ResultHelper.genResultWithSuccess(printOrderDao.findSumAmountByFilter(filter));
    }

    @Override
    @Transactional
    public Result<PrintOrder> insert(PrintOrder printOrder) {
        printOrder.setOrderId(OrderIdTool.getPrintOderId());
        logger.info("insert p_order: {}", JsonUtil.getJsonFromObject(printOrder));

        // 获取订单的配送信息，生成auto_confirm_hours
        if (null != printOrder.getDeliveryType()
                && printOrder.getDeliveryType().compareTo(PrintConst.ORDER_DELIVERY_TAKE) == 0) {
            DormShopFilter filter = new DormShopFilter();
            filter.setDormId(printOrder.getDormId());
            filter.setIsDelivery(true);
            filter.setType(ConstDorm.DORM_SHOP_TYPE_2);
            Result<List<DormShop>> result = dormShopApi.findByFilter(filter);
            if (null != result && result.getStatus() == 0) {
                List<DormShop> dormShops = result.getData();
                if (CollectionUtils.isNotEmpty(dormShops)) {
                    DormShop dormShop = dormShops.get(0);
                    List<DormShopDelivery> shopDeliveries = dormShop.getDormShopDeliveries();
                    for (DormShopDelivery delivery : shopDeliveries) {
                        if (delivery.getMethod().compareTo(ConstDorm.DORM_SHOP_DELIVERY_METHOD_2) == 0
                                && delivery.getStatus().compareTo(ConstDorm.DORM_SHOP_DELIVERY_STATUS_1) == 0
                                && delivery.getAutoConfirmSwitch().compareTo(1) == 0) {
                            printOrder.setAutoConfirmHours(delivery.getAutoConfirm());
                        }
                    }
                }
            }
        }

        // 订单基本信息插入成功后
        if (printOrderDao.insert(printOrder) == 1) {
            // 插入订单详情
            logger.info("insert p_order_detail: {}", JsonUtil.getJsonFromObject(printOrder.getDetails()));
            List<PrintOrderDetail> details = printOrder.getDetails();

            if (printOrderDetailDao.addBatch(printOrder.getOrderId(), details) == details.size()) {
                return ResultHelper.genResultWithSuccess(printOrder);
            }
        }

        return ResultHelper.genResultWithSuccess(null);
    }

    @Override
    public Result<Boolean> update(PrintOrder printOrder) {
        if (printOrder.getOrderId() == null) {
            throw new ServiceException(4, "orderId cannot be null");
        }

        // 以下为数据校验
        PrintOrder basePrintOrder = printOrderDao.findMasterByOrderId(printOrder.getOrderId());

        Byte baseStatus = basePrintOrder.getStatus();
        if (baseStatus == PrintOrderStatusEnum.FINISHED.getCode()
                || baseStatus == PrintOrderStatusEnum.CANCELED.getCode()) {
            throw new ServiceException(3900, "订单处于完结状态，不能修改");
        }

        Byte status = printOrder.getStatus();
        if (status != null) {
            if (status != PrintOrderStatusEnum.CONFIRMED.getCode() && status != PrintOrderStatusEnum.PRINTED.getCode()
                    && status != PrintOrderStatusEnum.SENDING.getCode() && status != PrintOrderStatusEnum.INIT.getCode()
                    && status != PrintOrderStatusEnum.CANCELED.getCode()
                    && status != PrintOrderStatusEnum.FINISHED.getCode()) {
                throw new ServiceException(3901, "订单状态非法，不能修改");
            }

            if (status.byteValue() == PrintOrderStatusEnum.CANCELED.getCode().byteValue()) {
                if (baseStatus == PrintOrderStatusEnum.SENDING.getCode()
                        || baseStatus == PrintOrderStatusEnum.PRINTED.getCode()) {
                    throw new ServiceException(3900, "当前订单已打印,不能取消");
                }

                // 取消订单时，需要对在线支付的订单，添加异常订单处理记录
                if (basePrintOrder.getPayTime() != null && basePrintOrder.getPayTime() > 0) {
                    if (basePrintOrder.getPayType().byteValue() == PrintConst.ORDER_PAY_CREDITCARD && printOrder.getCancelReason() == null) {
                        // 信用钱包
                        Result<Boolean> result = creditCardPayApi.cancel(basePrintOrder.getUid(),
                                basePrintOrder.getOrderId() + "", TradeTypeEnum.DAILY, TradeSourceTypeEnum.PRINT,
                                CancelTypeEnum.USER_CANCEL, null);
                        if (result == null || result.getStatus() != 0 || null == result.getData()) {
                            throw new ServiceException(-1, "信用钱包退款失败");
                        }
                    } else {
                        OrderPayAbnormalRecord record = new OrderPayAbnormalRecord();
                        record.setOrderType((byte) 4);
                        record.setOrderSn(String.valueOf(basePrintOrder.getOrderId()));
                        BigDecimal documentAmount = new BigDecimal(basePrintOrder.getTotalAmount());
                        BigDecimal deliveryAmount = new BigDecimal(basePrintOrder.getDeliveryAmount());
                        Double couponDiscount = basePrintOrder.getCouponDiscount() == null ? 0.00
                                : basePrintOrder.getCouponDiscount();
                        BigDecimal oldCouponAmount = new BigDecimal(-couponDiscount); // 优惠券的钱需要减去
                        Double orderAmount = documentAmount.add(deliveryAmount).add(oldCouponAmount)
                                .setScale(2, RoundingMode.HALF_UP).doubleValue();
                        if (orderAmount.doubleValue() > 0.00) {
                            //非0元支付
                            record.setOrderAmount(orderAmount);
                            record.setUid(basePrintOrder.getUid());
                            record.setPhone(basePrintOrder.getPhone());
                            record.setPayType(basePrintOrder.getPayType());
                            record.setPayTradeNo(basePrintOrder.getTradeNo());
                            record.setStatus((byte) 0);
                            record.setPayTime(basePrintOrder.getPayTime().intValue());
                            Result<OrderPayAbnormalRecord> result = orderPayAbnormalRecordApi
                                    .addOrderPayAbnormalRecord(record);
                            if (result == null || result.getStatus() != 0 || null == result.getData()) {
                                throw new ServiceException(-1, "添加异常订单记录失败");
                            }
                        } else if (orderAmount.doubleValue() == 0) {
                            //0元支付,退款1分钱
                            record.setOrderAmount(0.01);
                            record.setUid(basePrintOrder.getUid());
                            record.setPhone(basePrintOrder.getPhone());
                            record.setPayType(basePrintOrder.getPayType());
                            record.setPayTradeNo(basePrintOrder.getTradeNo());
                            record.setStatus((byte) 0);
                            record.setPayTime(basePrintOrder.getPayTime().intValue());
                            Result<OrderPayAbnormalRecord> result = orderPayAbnormalRecordApi
                                    .addOrderPayAbnormalRecord(record);
                            if (result == null || result.getStatus() != 0 || null == result.getData()) {
                                throw new ServiceException(-1, "添加异常订单记录失败");
                            }
                        }
                    }
                }
            }
        }

        return ResultHelper.genResultWithSuccess(printOrderDao.update(printOrder) == 1 ? true : false);
    }

    @Override
    public Result<Boolean> updateProfit(int detailId, byte isProfit) {
        PrintOrderDetail detail = new PrintOrderDetail();
        detail.setOrderDetailId(detailId);
        detail.setIsProfit(isProfit);
        return ResultHelper.genResultWithSuccess(printOrderDetailDao.updateProfit(detail) == 1);
    }

    /**
     * 数据摘选
     *
     * @param details 数据集
     * @param orderId 订单id
     * @return
     */
    public List<PrintOrderDetail> isMatch(List<PrintOrderDetail> details, Long orderId) {
        if (CollectionUtils.isEmpty(details) || null == orderId)
            return null;

        return details.stream().filter(detail -> orderId.compareTo(detail.getOrderId()) == 0)
                .collect(Collectors.toList());
    }
}
