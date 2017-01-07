package com.store59.printapi.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.store59.dto.common.enums.BizTypeEnum;
import com.store59.dto.common.enums.PayTypeEnum;
import com.store59.dto.common.order.OrderPayStatusEnum;
import com.store59.dto.common.order.OrderStatusEnum;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.order.common.service.dto.OrderContext;
import com.store59.order.common.service.dto.OrderCreateResultInfo;
import com.store59.order.common.service.dto.OrderDTO;
import com.store59.order.common.service.dto.OrderItemDTO;
import com.store59.order.common.service.dto.OrderOperateDTO;
import com.store59.order.common.service.dto.OrderOperateQuery;
import com.store59.order.common.service.dto.OrderOperateType;
import com.store59.order.common.service.dto.OrderPayDTO;
import com.store59.order.common.service.dto.OrderQuery;
import com.store59.order.common.service.dto.OrderQueryWith;
import com.store59.order.common.service.dto.OrderUpdate;
import com.store59.order.common.service.enums.OrderOperateTypeEnum;
import com.store59.order.common.service.enums.OrderPaySourceEnum;
import com.store59.order.common.service.enums.OrderPayerTypeEnum;
import com.store59.order.common.service.enums.OrderSourceEnum;
import com.store59.order.common.service.facade.BuyerOrderQueryFacade;
import com.store59.order.common.service.facade.OrderCreateFacade;
import com.store59.order.common.service.facade.OrderUpdateFacade;
import com.store59.print.common.data.ColumnConst;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.ExtensionColumnConstant;
import com.store59.printapi.common.utils.DateUtil;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.model.result.order.OrderCenterDTO;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/11
 * @since 1.0
 */
@Service
public class OrderCenterService {

    @Autowired
    private OrderUpdateFacade     orderUpdateFacade;
    @Autowired
    private OrderCreateFacade     orderCreateFacade;
    @Autowired
    private BuyerOrderQueryFacade buyerOrderQueryFacade;

    @Value("${aliyun.oss.print.domainName}")
    private String domainName;

    private final String itemId = "print666";
    Logger logger=LoggerFactory.getLogger(OrderCenterService.class);
    /**
     * 创建订单
     *
     * @param orderCenterDTO
     * @return
     */
    public OrderCenterDTO createOrder(OrderCenterDTO orderCenterDTO) {
        Result<OrderCreateResultInfo> result = orderCreateFacade.createOrder(converToOrderDTO(orderCenterDTO),
                new OrderContext());
        if (result == null || result.getStatus() != 0 || result.getData() == null) {
            if (!StringUtil.isBlank(result.getMsg())) {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, result.getMsg());
            } else {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, "订单创建失败!");
            }
        }
        return converToOrderCenterDTO(result.getData().getOrderDTO());
    }

    /**
     * 获取订单信息
     *
     * @param orderId
     * @param withOrderItems 是否查询订单商品信息
     * @param withOrderPays  是否查询订单支付明细
     * @return
     */
    public OrderCenterDTO getOrderByOrderId(String orderId, boolean withOrderItems, boolean withOrderPays) {
        OrderQueryWith orderQueryWith = new OrderQueryWith();
        orderQueryWith.setWithOrderItems(withOrderItems);
        orderQueryWith.setWithOrderPays(withOrderPays);
        Result<OrderDTO> result = buyerOrderQueryFacade.queryOrder(orderId, orderQueryWith);

        if (result == null || result.getStatus() != 0 || result.getData() == null) {
            if (!StringUtil.isBlank(result.getMsg())) {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, result.getMsg());
            } else {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, "获取订单信息失败!");
            }
        }
        return converToOrderCenterDTO(result.getData());
    }

    /**
     * 获取订单列表
     *
     * @param query
     * @param page
     * @param pageSize
     * @return
     */
    public List<OrderCenterDTO> getOrderList(OrderQuery query, int page, int pageSize) {
        List<BizTypeEnum> typeList = new ArrayList<>();
        typeList.add(BizTypeEnum.PRINT_DOC);
        typeList.add(BizTypeEnum.PRINT_PHOTO);
        query.setTypeList(typeList);
        Result<List<OrderDTO>> result = buyerOrderQueryFacade.queryOrdersPaging(query, page, pageSize);

        if (result == null || result.getStatus() != 0 || result.getData() == null) {
            if (!StringUtil.isBlank(result.getMsg())) {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, result.getMsg());
            } else {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, "获取订单信息失败!");
            }
        }
        List<OrderCenterDTO> orderCenterDTOList = new ArrayList<>();
        for (OrderDTO orderDTO : result.getData()) {
            OrderCenterDTO orderCenterDTO = converToOrderCenterDTO(orderDTO);
            orderCenterDTOList.add(orderCenterDTO);
        }
        return orderCenterDTOList;
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    public boolean cancelOrder(String orderId) {
        OrderUpdate orderUpdate = new OrderUpdate();
        orderUpdate.setId(orderId);
        orderUpdate.setStatus(OrderStatusEnum.CANCELED);
        orderUpdate.setSubStatus(PrintConst.ORDER_STATUS_CANCELED);
        OrderOperateType operateType = new OrderOperateType();
        operateType.setType(OrderOperateTypeEnum.BUYER_CANCEL);
        Result<?> result = orderUpdateFacade.updateOrder(orderUpdate, operateType);

        if (result == null || result.getStatus() != 0) {
            if (!StringUtil.isBlank(result.getMsg())) {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, result.getMsg());
            } else {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, "订单:" + orderId + ",取消失败!");
            }
        }
        return true;
    }

    /**
     * 更新订单
     *
     * @return
     */
    public boolean updateOrder(PrintOrder printOrder) {
        OrderUpdate orderUpdate = new OrderUpdate();
        OrderOperateType operateType = new OrderOperateType();
        orderUpdate.setId(printOrder.getOrderId() + "");
        if (printOrder.getStatus().byteValue() == OrderStatusEnum.CONFIRMED.getCode().byteValue()) { // 1:文档转换完成--确认订单
            orderUpdate.setStatus(OrderStatusEnum.CONFIRMED);
            operateType.setType(OrderOperateTypeEnum.SELLER_CONFIRM);
        } else if (printOrder.getStatus().byteValue() == OrderStatusEnum.DELIVERED.getCode().byteValue()) { // 2:已送达
            orderUpdate.setStatus(OrderStatusEnum.DELIVERED);
            operateType.setType(OrderOperateTypeEnum.BUYER_CANCEL);
        } else if (printOrder.getStatus().byteValue() == OrderStatusEnum.FINISHED.getCode().byteValue()) { // 4:订单完成
            orderUpdate.setStatus(OrderStatusEnum.FINISHED);
            operateType.setType(OrderOperateTypeEnum.BUYER_CANCEL);
        } else if (printOrder.getStatus().byteValue() == OrderStatusEnum.CANCELED.getCode().byteValue()) { // 5:订单取消
            orderUpdate.setStatus(OrderStatusEnum.CANCELED);
            operateType.setType(OrderOperateTypeEnum.BUYER_CANCEL);
        } else if (printOrder.getStatus().byteValue() == OrderStatusEnum.CLOSED.getCode().byteValue()) { // 6:已打印--订单被关闭
            orderUpdate.setStatus(OrderStatusEnum.CLOSED);
            operateType.setType(OrderOperateTypeEnum.BUYER_CANCEL);
        } else {
            return false;
        }
        Result<?> result = orderUpdateFacade.updateOrder(orderUpdate, operateType);

        if (result == null || result.getStatus() != 0) {
            if (!StringUtil.isBlank(result.getMsg())) {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, result.getMsg());
            } else {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, "订单取消失败!");
            }
        }
        return true;
    }

    public OrderCenterDTO converToOrderCenterDTO(OrderDTO orderDTO) {

        PrintOrder printOrder = new PrintOrder();
        printOrder.setOrderId(orderDTO.getId());
        if (orderDTO.getStatus() != null) {
            printOrder.setStatus(orderDTO.getStatus().getCode());
        } else {
            printOrder.setStatus(orderDTO.getSubStatus());
        }

        printOrder.setDeliveryTime(orderDTO.getBuyerExpectTime());
        if (orderDTO.getType().getCode().byteValue() == BizTypeEnum.PRINT_DOC.getCode().byteValue()) {
            printOrder.setDocType(PrintConst.PRINT_ORDER_TYPE_DOC);
        }
        if (orderDTO.getType().getCode().byteValue() == BizTypeEnum.PRINT_PHOTO.getCode().byteValue()) {
            printOrder.setDocType(PrintConst.PRINT_ORDER_TYPE_PHOTO);
        }
        printOrder.setSource(orderDTO.getSource().getCode());
        printOrder.setPhone(orderDTO.getBuyerPhone());
        printOrder.setAddress(orderDTO.getBuyerAddress());
        printOrder.setRemark(orderDTO.getBuyerRemark());
        // 数据库中存储的金额单位是分
        printOrder.setDeliveryAmount(
                BigDecimal.valueOf(orderDTO.getDeliveryFee() / 100.0).setScale(2, RoundingMode.HALF_UP).doubleValue());
        printOrder.setUid(Long.valueOf(orderDTO.getBuyerId()));
        printOrder.setUname(orderDTO.getBuyerName());
        printOrder.setBuyerContact(orderDTO.getBuyerPhone());
        printOrder.setCreateTime(orderDTO.getCreateTime().getTime() / 1000);
        // ----扩展字段
        Map<String, String> extension = orderDTO.getExtension();
        printOrder.setDeliveryType(Byte.valueOf(extension.get(ColumnConst.DELIVERY_TYPE)));
        printOrder.setAdPageNum(Integer.valueOf(extension.get(ColumnConst.AD_PAGE_NUM)));
        printOrder.setAdUnitPrice(Double.valueOf(extension.get(ColumnConst.AD_UNIT_PRICE)));
        printOrder.setTotalAmount(Double.valueOf(extension.get(ColumnConst.DOC_AMOUNT))); // 此字段
        // =
        // 未纯文档价格+福利纸减免后的价格
        printOrder.setDormId(Integer.valueOf(extension.get(ColumnConst.DORM_ID)));
        printOrder.setAutoConfirmHours(Integer.valueOf(extension.get(ColumnConst.AUTO_CONFIRM_HOURS)));

        // -----支付表
        List<OrderPayDTO> payList = orderDTO.getPayList();

        if (payList != null && payList.size() > 0) {
            for (OrderPayDTO orderPayDTO : payList) {
                if (orderPayDTO.getType().getCode().byteValue() == PayTypeEnum.COUPON.getCode().byteValue()) {
                    // 优惠减免
                    printOrder.setCouponCode(orderPayDTO.getOutId());
                    printOrder.setCouponDiscount(BigDecimal.valueOf(orderPayDTO.getAmount() / 100.0)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    // 支付方式
                    printOrder.setPayType(orderPayDTO.getType().getCode());
                    // TODO 此处时间的转化需要验证
                    printOrder.setPayTime(orderPayDTO.getCreateTime().getTime() / 1000);
                    printOrder.setTradeNo(orderPayDTO.getOutId());
                }
            }
        }

        // -----操作表
        OrderOperateQuery orderOperateQuery = new OrderOperateQuery();
        orderOperateQuery.setOrderId(orderDTO.getId());
        Result<List<OrderOperateDTO>> result = buyerOrderQueryFacade.queryOrderOperates(orderOperateQuery);
        if (result == null || result.getStatus() != 0 || result.getData() == null) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    "获取订单操作记录失败,订单号:" + orderDTO.getId());
        }
        List<OrderOperateDTO> operateDTOList = result.getData();
        if (operateDTOList.size() > 0) {
            for (OrderOperateDTO operateDTO : operateDTOList) {
                if (operateDTO.getOrderSubStatus() != null
                        && operateDTO.getOrderSubStatus().byteValue() == OrderStatusEnum.CLOSED.getCode().byteValue()) {
                    // (对应打印的状态为已打印)
                    printOrder.setPrintTime(operateDTO.getCreateTime().getTime() / 1000);
                    if (orderDTO.getStatus() != null) {
                        if (orderDTO.getStatus().getCode() != 2 && orderDTO.getStatus().getCode() != 4) {
                            printOrder.setStatus(orderDTO.getSubStatus());
                        } else {
                            printOrder.setStatus(orderDTO.getStatus().getCode());
                        }
                    } else {
                        printOrder.setStatus(orderDTO.getSubStatus());
                    }

                } else if (operateDTO.getOrderStatus() != null && operateDTO.getOrderStatus().getCode()
                        .byteValue() == OrderStatusEnum.INIT.getCode().byteValue()) {
                    // 新订单
//					printOrder.setCreateTime(operateDTO.getCreateTime().getTime() / 1000);
                } else if (operateDTO.getOrderStatus() != null && operateDTO.getOrderStatus().getCode()
                        .byteValue() == OrderStatusEnum.FINISHED.getCode().byteValue()) {
                    // 订单已完成
                    printOrder.setFinishTime(operateDTO.getCreateTime().getTime() / 1000);
                } else if (operateDTO.getOrderStatus() != null && operateDTO.getOrderStatus().getCode()
                        .byteValue() == OrderStatusEnum.CANCELED.getCode().byteValue()) {
                    // 订单取消
                    if (operateDTO.getOperateType().equals(OrderOperateTypeEnum.SELLER_CANCEL.getCode())) {
//                        printOrder.setCancelReason(CommonConstant.CANCEL_REASON[Integer.parseInt(operateDTO.getRemark())]);
                    	try{
                    		printOrder.setCancelReason(CommonConstant.CANCEL_REASON[Integer.parseInt(operateDTO.getRemark())]);
                    	}catch(Exception e){
                    		printOrder.setCancelReason(operateDTO.getRemark());
                    	}
                    } else {
                        printOrder.setCancelReason(operateDTO.getRemark());
                    }
                }
            }
        }

        /**
         * 订单商品详情
         */
        if (orderDTO.getItemList() != null && orderDTO.getItemList().size() > 0) {
            List<PrintOrderDetail> orderDetailList = new ArrayList<>();
            for (OrderItemDTO itemDTO : orderDTO.getItemList()) {
                orderDetailList.add(converOrderItemToPrintDetail(itemDTO));
            }
            printOrder.setDetails(orderDetailList);
        }
        OrderCenterDTO orderCenterDTO = new OrderCenterDTO();
        orderCenterDTO.setPrintOrder(printOrder);
        Map<String, String> order_ret_extension = new HashMap<>();
        if (orderDTO.getRefundStatus() != null) {
            order_ret_extension.put(ExtensionColumnConstant.REFUNDSTATUS, orderDTO.getRefundStatus().getCode() + "");
        }
        orderCenterDTO.setExtension(order_ret_extension);
        orderCenterDTO.setSellerId(orderDTO.getSellerId());
        return orderCenterDTO;
    }

    public OrderDTO converToOrderDTO(OrderCenterDTO orderCenterDTO) {
        OrderDTO orderDTO = new OrderDTO();

        PrintOrder printOrder = orderCenterDTO.getPrintOrder();
        if (printOrder.getOrderId() != null) {
            orderDTO.setId(printOrder.getOrderId());
        }

        if (printOrder.getDocType().byteValue() == PrintConst.PRINT_ORDER_TYPE_DOC) {
            orderDTO.setType(BizTypeEnum.PRINT_DOC);
        } else if (printOrder.getDocType().byteValue() == PrintConst.PRINT_ORDER_TYPE_PHOTO) {
            orderDTO.setType(BizTypeEnum.PRINT_PHOTO);
        }

        if (printOrder.getStatus().byteValue() == OrderStatusEnum.INIT.getCode().byteValue()) { // 0:文档转换中--新订单
            orderDTO.setStatus(OrderStatusEnum.INIT);
        }
        if (printOrder.getStatus().byteValue() == OrderStatusEnum.CONFIRMED.getCode().byteValue()) { // 1:文档转换完成--确认订单
            orderDTO.setStatus(OrderStatusEnum.CONFIRMED);
        }
        if (printOrder.getStatus().byteValue() == OrderStatusEnum.DELIVERED.getCode().byteValue()) { // 2:已送达
            orderDTO.setStatus(OrderStatusEnum.DELIVERED);
        }
        if (printOrder.getStatus().byteValue() == OrderStatusEnum.FINISHED.getCode().byteValue()) { // 4:订单完成
            orderDTO.setStatus(OrderStatusEnum.FINISHED);
        }
        if (printOrder.getStatus().byteValue() == OrderStatusEnum.CANCELED.getCode().byteValue()) { // 5:订单取消
            orderDTO.setStatus(OrderStatusEnum.CANCELED);
        }
        if (printOrder.getStatus().byteValue() == OrderStatusEnum.CLOSED.getCode().byteValue()) { // 6:已打印
            orderDTO.setSubStatus(OrderStatusEnum.CLOSED.getCode()); // 已打印记录在子状态
        }

        if (printOrder.getPayType()!=null && printOrder.getPayTime() != null) {
            orderDTO.setPayStatus(OrderPayStatusEnum.FINISHED); // 已支付
        } else {
            orderDTO.setPayStatus(OrderPayStatusEnum.WAITING); // 待支付
        }
        // orderDTO.setRefundStatus();
        if (PrintConst.ORDER_SOURCE_PC == printOrder.getSource().byteValue()) {
            orderDTO.setSource(OrderSourceEnum.PC);
        }
        if (PrintConst.ORDER_SOURCE_WEIXIN == printOrder.getSource().byteValue()) { // 微信
            orderDTO.setSource(OrderSourceEnum.WAP); // 手机网页
        }
        if (PrintConst.ORDER_SOURCE_IOS == printOrder.getSource().byteValue()) {
            orderDTO.setSource(OrderSourceEnum.IOS);
        }
        if (PrintConst.ORDER_SOURCE_ANDROID == printOrder.getSource().byteValue()) {
            orderDTO.setSource(OrderSourceEnum.ANDROID);
        }

        try {
            orderDTO.setCreateTime(DateUtil.timestampToDate(Integer.valueOf(printOrder.getCreateTime() + "")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 配送费
        BigDecimal deliveryFee = new BigDecimal(0);
        if (printOrder.getDeliveryAmount() != null) {
            deliveryFee = new BigDecimal(printOrder.getDeliveryAmount() * 100);
            orderDTO.setDeliveryFee(deliveryFee.setScale(0, RoundingMode.HALF_UP).intValue());
        }
        // 商品价格
        BigDecimal orderAmount = new BigDecimal(0);
        if (printOrder.getTotalAmount() != null) {
            orderAmount = new BigDecimal(printOrder.getTotalAmount() * 100);
            orderDTO.setOrderAmount(orderAmount.setScale(0, RoundingMode.HALF_UP).intValue());
        }
        // 优惠券减免金额
        BigDecimal couponAmount = new BigDecimal(0);
        if (printOrder.getCouponCode() != null) {
            couponAmount = new BigDecimal(printOrder.getCouponDiscount() * 100);
        }
        // 计算最终支付金额
        BigDecimal payAmount = orderAmount.add(deliveryFee).subtract(couponAmount);
        orderDTO.setPayAmount(payAmount.setScale(0, RoundingMode.HALF_UP).intValue()==0?1:payAmount.setScale(0, RoundingMode.HALF_UP).intValue());
        orderDTO.setSellerId(orderCenterDTO.getSellerId());
        orderDTO.setSellerName(orderCenterDTO.getSellerName());
        orderDTO.setSellerPhone(orderCenterDTO.getSellerPhone());
        orderDTO.setSellerAddress(orderCenterDTO.getSellerAddress());
        orderDTO.setSellerSiteId(orderCenterDTO.getSellerSiteId());
        orderDTO.setSellerDormentryId(orderCenterDTO.getSellerDormentryId());
        orderDTO.setSellerShopId(orderCenterDTO.getSellerShopId());
        orderDTO.setBuyerId(printOrder.getUid() == null ? "" : printOrder.getUid() + "");
        orderDTO.setBuyerName(printOrder.getUname());
        orderDTO.setBuyerPhone(printOrder.getPhone());
        orderDTO.setBuyerAddress(printOrder.getAddress());
        orderDTO.setBuyerExpectTime(printOrder.getDeliveryTime());
        orderDTO.setBuyerRemark(printOrder.getRemark());
        orderDTO.setDeviceId(printOrder.getDeviceId());
        Integer curr_time = DateUtil.getCurrentTimeSeconds();
        try {
            orderDTO.setUpdateTime(DateUtil.timestampToDate(curr_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**
         * 订单商品信息
         */
        List<OrderItemDTO> itemList = new ArrayList<>();
        if (printOrder.getDetails() != null && printOrder.getDetails().size() > 0) {
            for (PrintOrderDetail detail : printOrder.getDetails()) {
                // 以下记录在扩展字段
                itemList.add(converPrintDetailToOrderItem(detail));
            }
            orderDTO.setItemList(itemList);
        }

        // 订单扩展字段
        Map<String, String> orderDTO_ExtMap = new HashMap<>();
        orderDTO_ExtMap.put(ColumnConst.DELIVERY_TYPE,
                printOrder.getDeliveryType() == null ? "" : printOrder.getDeliveryType() + "");
        orderDTO_ExtMap.put(ColumnConst.AD_PAGE_NUM,
                printOrder.getAdPageNum() == null ? "" : printOrder.getAdPageNum() + "");
        orderDTO_ExtMap.put(ColumnConst.AD_UNIT_PRICE,
                printOrder.getAdUnitPrice() == null ? "" : printOrder.getAdUnitPrice() + "");
        orderDTO_ExtMap.put(ColumnConst.DOC_AMOUNT,
                printOrder.getTotalAmount() == null ? "" : printOrder.getTotalAmount() + "");
        orderDTO_ExtMap.put(ColumnConst.DORM_ID, printOrder.getDormId() == null ? "" : printOrder.getDormId() + "");
        orderDTO_ExtMap.put(ColumnConst.AUTO_CONFIRM_HOURS,
                printOrder.getAutoConfirmHours() == null ? "12" : printOrder.getAutoConfirmHours() + "");
        orderDTO.setExtension(orderDTO_ExtMap);

        /**
         * 创建订单还未支付,不需要订单支付明细
         */
        if (!StringUtil.isBlank(printOrder.getCouponCode())) {
            OrderPayDTO orderPayDTO = new OrderPayDTO();
            orderPayDTO.setOrderId(printOrder.getOrderId());
            orderPayDTO.setType(PayTypeEnum.COUPON);
            if (PrintConst.ORDER_SOURCE_PC == printOrder.getSource().byteValue()) {
                orderPayDTO.setSource(OrderPaySourceEnum.PC);
            }
            if (PrintConst.ORDER_SOURCE_WEIXIN == printOrder.getSource().byteValue()) { // 微信
                orderPayDTO.setSource(OrderPaySourceEnum.WAP); // 手机网页
            }
            if (PrintConst.ORDER_SOURCE_IOS == printOrder.getSource().byteValue()) {
                orderPayDTO.setSource(OrderPaySourceEnum.IOS);
            }
            if (PrintConst.ORDER_SOURCE_ANDROID == printOrder.getSource().byteValue()) {
                orderPayDTO.setSource(OrderPaySourceEnum.ANDROID);
            }
            orderPayDTO.setPayStatus(OrderPayStatusEnum.FINISHED);
            BigDecimal paycouponAmount = new BigDecimal(printOrder.getCouponDiscount() * 100);
            orderPayDTO.setAmount(paycouponAmount.setScale(0, RoundingMode.HALF_UP).intValue());
            orderPayDTO.setPayerId(printOrder.getUid() == null ? "" : printOrder.getUid() + "");
            orderPayDTO.setPayerType(OrderPayerTypeEnum.BUYER);
            orderPayDTO.setOutId(printOrder.getCouponCode());

            List<OrderPayDTO> list = new ArrayList<>();
            list.add(orderPayDTO);
            orderDTO.setPayList(list);
        }

        return orderDTO;
    }

    public OrderItemDTO converPrintDetailToOrderItem(PrintOrderDetail detail) {
        OrderItemDTO itemDTO = new OrderItemDTO();
        if (detail.getOrderDetailId() != null) {
            itemDTO.setId(detail.getOrderDetailId());
        }
        if (detail.getOrderId() != null) {
            itemDTO.setOrderId(detail.getOrderId());
        }
        itemDTO.setItemId(itemId);
        if (!StringUtil.isBlank(detail.getFileName())) {
            itemDTO.setName(detail.getFileName());
        }
        if (detail.getNum() != null) {
            itemDTO.setQuantity(detail.getNum().shortValue());
        }
        if (detail.getAmount() != null) {
            Float amount = detail.getAmount() * 100;
            itemDTO.setAmount(amount.intValue());
            itemDTO.setPrice(itemDTO.getAmount() / itemDTO.getQuantity());
        }

        Map<String, String> itemMap = new HashMap<>();
        if (detail.getStatus() != null) {
            itemMap.put(ColumnConst.STATUS, detail.getStatus() + "");
        }
        if (detail.getIsFirst() != null) {
            itemMap.put(ColumnConst.IS_FIRST, detail.getIsFirst() + "");
        }
        if (detail.getSourceMD5() != null) {
            itemMap.put(ColumnConst.SOURCE_MD5, detail.getSourceUrl().substring(domainName.length() + 1));
        }
        if (detail.getPdfMD5() != null) {
            itemMap.put(ColumnConst.PDF_MD5, detail.getUrl().substring(domainName.length() + 1));
        }
        if (detail.getPdfSize() != null) {
            itemMap.put(ColumnConst.PDF_SIZE, detail.getPdfSize() + "");
        }
        if (detail.getPrintType() != null) {
            itemMap.put(ColumnConst.PRINT_TYPE, detail.getPrintType() + "");
        }
        if (detail.getReprintType() != null) {
            itemMap.put(ColumnConst.REPRINT_TYPE, detail.getReprintType() + "");
        }
        if (detail.getPageNum() != null) {
            itemMap.put(ColumnConst.PAGE_NUM, detail.getPageNum() + "");
        }
        if (detail.getCheckStatus() != null) {
            itemMap.put(ColumnConst.CHECK_STATUS, detail.getCheckStatus() + "");
        } else {
            itemMap.put(ColumnConst.CHECK_STATUS, "0"); // 0:未检测
        }
        if (detail.getCheckMsg() != null) {
            itemMap.put(ColumnConst.CHECK_MSG, detail.getCheckMsg());
        }
        if (detail.getCheckTime() != null) {
            itemMap.put(ColumnConst.CHECK_TIME, detail.getCheckTime() + "");
        }
        if (detail.getIsProfit() != null) {
            itemMap.put(ColumnConst.IS_PROFIT, detail.getIsProfit() + "");
        } else {
            itemMap.put(ColumnConst.IS_PROFIT, "0"); // 默认为0,未返利
        }
        itemDTO.setExtension(itemMap);
        return itemDTO;
    }

    public PrintOrderDetail converOrderItemToPrintDetail(OrderItemDTO itemDTO) {
        PrintOrderDetail printOrderDetail = new PrintOrderDetail();
        if (itemDTO.getId() != null) {
            printOrderDetail.setOrderDetailId(itemDTO.getId());
        }
        if (itemDTO.getOrderId() != null) {
            printOrderDetail.setOrderId(itemDTO.getOrderId());
        }
        printOrderDetail.setFileName(itemDTO.getName());
        printOrderDetail.setNum(Integer.valueOf(itemDTO.getQuantity()));
        printOrderDetail.setAmount(
                BigDecimal.valueOf(itemDTO.getAmount() / 100.0).setScale(2, RoundingMode.HALF_UP).floatValue());

        // 以下记录在扩展字段
        Map<String, String> item_extension = itemDTO.getExtension();
//        if(item_extension.get(ColumnConst.STATUS)==null){
//        	logger.info("订单详情状态为空："+JsonUtil.getJsonFromObject(itemDTO));
//        }
//        logger.info("订单详情日志版本："+JsonUtil.getJsonFromObject(item_extension));
        printOrderDetail.setStatus(Byte.valueOf(item_extension.get(ColumnConst.STATUS)==null?"1":item_extension.get(ColumnConst.STATUS)));
        printOrderDetail.setIsFirst(Byte.valueOf(item_extension.get(ColumnConst.IS_FIRST)));
        printOrderDetail.setSourceMD5(item_extension.get(ColumnConst.SOURCE_MD5)==null?item_extension.get(ColumnConst.PDF_MD5):item_extension.get(ColumnConst.SOURCE_MD5));
        printOrderDetail.setSourceUrl(domainName + "/" + item_extension.get(ColumnConst.SOURCE_MD5));
        printOrderDetail.setPdfMD5(item_extension.get(ColumnConst.PDF_MD5));
        printOrderDetail.setUrl(domainName + "/" + item_extension.get(ColumnConst.PDF_MD5));
        printOrderDetail.setPrintType(Byte.valueOf(item_extension.get(ColumnConst.PRINT_TYPE)));
        printOrderDetail.setReprintType(Byte.valueOf(item_extension.get(ColumnConst.REPRINT_TYPE)));
        printOrderDetail.setPageNum(Integer.valueOf(item_extension.get(ColumnConst.PAGE_NUM)));
        printOrderDetail.setCheckStatus(Byte.valueOf(item_extension.get(ColumnConst.CHECK_STATUS)));
        printOrderDetail.setCheckMsg(item_extension.get(ColumnConst.CHECK_MSG));
        if (!StringUtil.isBlank(item_extension.get(ColumnConst.CHECK_TIME))) {
            printOrderDetail.setCheckTime(Long.valueOf(item_extension.get(ColumnConst.CHECK_TIME)));
        }
        if (!StringUtil.isBlank(item_extension.get(ColumnConst.IS_PROFIT))) {
            printOrderDetail.setIsProfit(Byte.valueOf(item_extension.get(ColumnConst.IS_PROFIT)));
        }
        if (!StringUtil.isBlank(item_extension.get(ColumnConst.PDF_SIZE))) {
            printOrderDetail.setPdfSize(Long.valueOf(item_extension.get(ColumnConst.PDF_SIZE)));
        }
        return printOrderDetail;
    }
}
