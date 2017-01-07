package com.store59.printapi.service.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.store59.dto.common.enums.BizTypeEnum;
import com.store59.dto.common.order.OrderRefundStatusEnum;
import com.store59.dto.common.order.OrderStatusEnum;
import com.store59.order.common.service.dto.OrderQuery;
import com.store59.print.common.data.ColumnConst;
import com.store59.printapi.common.constant.ExtensionColumnConstant;
import com.store59.printapi.model.result.order.OrderCenterDTO;
import com.store59.printapi.service.OrderCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.base.common.api.OrderPayAbnormalRecordApi;
import com.store59.base.common.filter.OrderPayAbnormalRecordFilter;
import com.store59.base.common.model.OrderPayAbnormalRecord;
import com.store59.coupon.model.businessObject.Coupon;
import com.store59.coupon.model.param.CouponQueryParam;
import com.store59.coupon.remoting.CouponService;
import com.store59.dorm.common.api.DormApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.api.DormShopDeliveryApi;
import com.store59.dorm.common.api.DormShopPriceApi;
import com.store59.dorm.common.data.ConstDorm;
import com.store59.dorm.common.filter.DormShopDeliveryFilter;
import com.store59.dorm.common.filter.DormShopFilter;
import com.store59.dorm.common.model.Dorm;
import com.store59.dorm.common.model.DormShop;
import com.store59.dorm.common.model.DormShopDelivery;
import com.store59.dorm.common.model.DormShopPrice;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.print.common.remoting.PrintOrderService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.exception.CommonException;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.wechat.TokenManager;
import com.store59.printapi.common.wechat.api.MessageAPI;
import com.store59.printapi.model.param.createOrder.AppCreateOrderDetailParam;
import com.store59.printapi.model.param.order.MyOrderParam;
import com.store59.printapi.model.param.wechat.message.TextMessage;
import com.store59.printapi.model.result.order.AppOrder;
import com.store59.printapi.model.result.order.MyOrderResult;
import com.store59.printapi.model.result.order.MyOrderResultList;
import com.store59.printapi.service.BaseService;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @version 1.0 2016/01/13
 * @since 1.0
 */
@Service
public class MyOrderService extends BaseService {

    private static Logger log = LoggerFactory.getLogger(MyOrderService.class);

    @Autowired
    private PrintOrderService printOrderService;

    @Autowired
    private DormShopApi dormShopService;

    @Autowired
    private DormApi dormApi;

    @Autowired
    private RabbitTemplate      rabbitTemplate;
    @Autowired
    private DormShopDeliveryApi dormShopDeliveryApi;

    private Logger logger = LoggerFactory.getLogger(MyOrderService.class);

    @Autowired
    private CouponService                   couponService;
    @Autowired
    private DormShopPriceApi                dormShopPriceApi;
    @Value("${alipay.notify.url}")
    private String                          paycallbackUrl;
    @Autowired
    private OrderPayAbnormalRecordApi       orderPayAbnormalRecordApi;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    @Value("${cookie.max.age}")
    private int                             cookieMaxAge;
    @Value("${print.wechat.appid}")
    private String                          appid;
    @Value("${print.wechat.orderlist.url}")
    private String                          orderListUrl;
    @Value("${print.wechat.pay.openid.url}")
    private String                          payOpenidUrl;
    @Autowired
    TokenManager tokenManager;
    @Autowired
    private OrderCenterService orderCenterService;

    /**
     * 我的订单列表
     *
     * @param uid
     * @return
     */
    @SuppressWarnings("null")
    public List<MyOrderResult> getMyOrderList(Long uid, MyOrderParam myOrderParam) {
        List<MyOrderResult> list = new ArrayList<>();

        List<OrderStatusEnum> statusList = new ArrayList<>();
        if (myOrderParam.getType() != null && myOrderParam.getType().size() > 0) {
            for (int status : myOrderParam.getType()) {
                if (status == 0) {
                    statusList.add(OrderStatusEnum.INIT);
                }
                if (status == 1) {
                    statusList.add(OrderStatusEnum.CONFIRMED);
                }
                if (status == 2) {
                    statusList.add(OrderStatusEnum.DELIVERED);
                }
                if (status == 4) {
                    statusList.add(OrderStatusEnum.FINISHED);
                }
                if (status == 5) {
                    statusList.add(OrderStatusEnum.CANCELED);
                }
                if (status == 6) {
                    statusList.add(OrderStatusEnum.CLOSED);
                }
            }
        } else {
            statusList.add(OrderStatusEnum.CONFIRMED);
        }

        //接入订单中心
        int page = 1;
        int pageSize = 1000;
        if (myOrderParam.getLimit() != null && myOrderParam.getOffset() != null) {
            pageSize = myOrderParam.getLimit();
            page = myOrderParam.getOffset()/myOrderParam.getLimit() + 1;
        }
        //接入订单中心
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setBuyerId(uid + "");
        orderQuery.setWithOrderPays(true);
        orderQuery.setWithOrderItems(true);
        orderQuery.setStatusList(statusList);

        List<OrderCenterDTO> orderCenterDTOList = orderCenterService.getOrderList(orderQuery, page, pageSize);

        if (orderCenterDTOList == null || orderCenterDTOList.size() == 0) {
            return list;
        }

        Set<Integer> dormIdSet = new HashSet<>();
        for (OrderCenterDTO orderCenterDTO : orderCenterDTOList) {
            PrintOrder printOrder = orderCenterDTO.getPrintOrder();
            // 数据库中的组合状态映射成前端展示的业务逻辑状态
            if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                    && printOrder.getPayTime() == null && printOrder.getPayType() == null) {// 未支付
                printOrder.setStatus((byte) 0);
            }
            if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                    && printOrder.getPayTime() != null && printOrder.getPayType() != null) {
                printOrder.setStatus((byte) 1);// 未打印= 文档转换中or文档转换完成 + 已支付
            }
            // 其他状态值前后端保持一致
            MyOrderResult myOrderResult = new MyOrderResult();
            myOrderResult.setOrderId(printOrder.getOrderId() + "");
            myOrderResult.setOrderStatus(printOrder.getStatus());
            myOrderResult.setAddress(printOrder.getAddress());
            myOrderResult.setPhone(printOrder.getPhone());
            myOrderResult.setCreateTime(printOrder.getCreateTime());
            myOrderResult.setPayType(printOrder.getPayType());
            myOrderResult.setCouponDiscount(printOrder.getCouponDiscount());
            myOrderResult.setAdPageNum(printOrder.getAdPageNum());

            BigDecimal orderAmount = new BigDecimal(printOrder.getTotalAmount());
            BigDecimal deliveryAmount = new BigDecimal(printOrder.getDeliveryAmount());
            Double couponDiscount = printOrder.getCouponDiscount() == null ? 0.00 : printOrder.getCouponDiscount();
            BigDecimal couponAmount = new BigDecimal(-couponDiscount);

            myOrderResult.setPrintAmount(printOrder.getTotalAmount());
            myOrderResult.setDeliveryTime(printOrder.getDeliveryTime());
            myOrderResult.setDeliveryType(printOrder.getDeliveryType());
            myOrderResult.setDeliveryAmount(printOrder.getDeliveryAmount());
            // 返回给前端订单总价=文档价格+配送费-优惠券金额
            double totalAmount = orderAmount.add(deliveryAmount).add(couponAmount).setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            if (totalAmount == 0) {
                totalAmount = 0.01;
            }
            myOrderResult.setTotalAmount(totalAmount);
            myOrderResult.setDormId(printOrder.getDormId());
            myOrderResult.setRemark(printOrder.getRemark());
            List<PrintOrderDetail> myOrderDetail = new ArrayList<>();
            for (PrintOrderDetail detail : printOrder.getDetails()) {
                if (detail.getIsFirst().byteValue() == CommonConstant.ORDER_FIRST_LIST_0) {
                    myOrderDetail.add(detail);
                }
            }
            myOrderResult.setMyOrderDetail(myOrderDetail);
            dormIdSet.add(printOrder.getDormId());
            list.add(myOrderResult);
        }

        Map<Integer, Dorm> dormMap = new HashMap<Integer, Dorm>();
        Map<Integer, DormShop> dormShopMap = new HashMap<Integer, DormShop>();
        Map<Integer, DormShopDelivery> shopDeliveryMap = new HashMap<Integer, DormShopDelivery>();
        if (!CollectionUtils.isEmpty(dormIdSet)) {
            List<Integer> dormSearchList = new ArrayList<>();
            for (Integer dormId : new ArrayList<Integer>(dormIdSet)) {
                if (StringUtil.isBlank(valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + dormId))) {
                    dormSearchList.add(dormId);
                } else {
                    dormMap.put(dormId, JsonUtil.getObjectFromJson(
                            valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + dormId), new TypeReference<Dorm>() {
                            }));
                }
            }

            if (dormSearchList.size() > 0) {
                Result<List<Dorm>> dormListResult = dormApi.getDormListByIdList(dormSearchList);
                if (dormListResult == null || dormListResult.getStatus() != 0 || dormListResult.getData() == null) {
                    throw new BaseException(dormListResult.getStatus(), dormListResult.getMsg());
                }
                if (dormListResult.getData() != null && dormListResult.getData().size() > 0) {
                    // dormMap =
                    // dormListResult.getData().parallelStream().collect(Collectors.toMap(Dorm::getDormId,
                    // (p) -> p));
                    for (Dorm dorm : dormListResult.getData()) {
                        if (dorm.getDormId() != null) {
                            dormMap.put(dorm.getDormId(), dorm);
                            valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + dorm.getDormId(),
                                    JsonUtil.getJsonFromObject(dorm), cookieMaxAge, TimeUnit.SECONDS);
                        }
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(dormIdSet)) {

            Result<List<DormShop>> dormshopRet = new Result<List<DormShop>>();
            List<DormShop> dormShopList = new ArrayList<DormShop>();
            List<Integer> dormshopSearchList = new ArrayList<>();
            for (Integer dormId : new ArrayList<Integer>(dormIdSet)) {
                String dormShopStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM_SHOP + dormId);
                if (StringUtil.isBlank(dormShopStr)) {
                    dormshopSearchList.add(dormId);
                } else {
                    dormShopList.add(JsonUtil.getObjectFromJson(dormShopStr, DormShop.class));
                    dormShopMap.put(dormId, JsonUtil.getObjectFromJson(dormShopStr, DormShop.class));
                }
            }

            if (dormshopSearchList.size() > 0) {
                DormShopFilter dormShopFilter = new DormShopFilter();
                dormShopFilter.setDormIds(dormshopSearchList);
                dormShopFilter.setType((byte) 2); // 打印店
                Result<List<DormShop>> dormshopRetLeft = dormShopService.findByFilter(dormShopFilter);
                if (dormshopRetLeft == null || dormshopRetLeft.getStatus() != 0 || dormshopRetLeft.getData() == null) {
                    throw new BaseException(dormshopRetLeft.getStatus(), dormshopRetLeft.getMsg());
                }
                if (dormshopRetLeft.getData() != null && dormshopRetLeft.getData().size() > 0) {
                    dormShopList.addAll(dormshopRetLeft.getData());
                    for (DormShop dormShop : dormshopRetLeft.getData()) {
                        dormShopMap.put(dormShop.getDormId(), dormShop);
                        valueOpsCache.set(CommonConstant.KEY_REDIS_DORM_SHOP + dormShop.getDormId(),
                                JsonUtil.getJsonFromObject(dormShop), cookieMaxAge, TimeUnit.SECONDS);
                    }
                }
            }
            dormshopRet.setData(dormShopList);
            /**
             * 获取店铺配送信息
             */
            Set<Integer> shopIds = dormshopRet.getData().parallelStream().map(DormShop::getShopId)
                    .collect(Collectors.toSet());
            DormShopDeliveryFilter dsdFilter = new DormShopDeliveryFilter();
            ArrayList<Integer> shopIdsSearch = new ArrayList<Integer>();
            for (Integer shopId : shopIds) {
                String shopInfo = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM_DELIVERY + shopId);
                if (StringUtil.isBlank(shopInfo)) {
                    shopIdsSearch.add(shopId);
                } else {
                    shopDeliveryMap.put(shopId, JsonUtil.getObjectFromJson(shopInfo, DormShopDelivery.class));
                }
            }
            if (shopIdsSearch.size() > 0) {
                dsdFilter.setShopIds(shopIdsSearch);
                dsdFilter.setMethod(CommonConstant.DELIVERY_TYPE_YOURSELF);
                dsdFilter.setStatus(ConstDorm.DORM_SHOP_DELIVERY_STATUS_1);// 启用
                Result<List<DormShopDelivery>> dsdListresult = dormShopDeliveryApi.findByFilter(dsdFilter);
                if (dsdListresult.getStatus() != 0) {
                    throw new BaseException(dsdListresult.getStatus(), dsdListresult.getMsg());
                }
                if (dsdListresult.getData() != null && dsdListresult.getData().size() > 0) {
                    shopDeliveryMap.putAll(dsdListresult.getData().parallelStream()
                            .collect(Collectors.toMap(DormShopDelivery::getShopId, (p) -> p)));
                    for (DormShopDelivery di : dsdListresult.getData()) {
                        valueOpsCache.set(CommonConstant.KEY_REDIS_DORM_DELIVERY + di.getShopId(),
                                JsonUtil.getJsonFromObject(di), cookieMaxAge + 10, TimeUnit.SECONDS);
                    }
                }
            }
        }

        // 拼接打印店地址
        for (MyOrderResult p : list) {
            if (dormMap != null && p.getDormId() != null && dormMap.get(p.getDormId()) != null) {
                p.setDormPhone(dormMap.get(p.getDormId()).getPhone());
            }
            if (dormShopMap != null && p.getDormId() != null && dormShopMap.get(p.getDormId()) != null) {
                p.setShopName(dormShopMap.get(p.getDormId()).getName());
            }
            if (dormShopMap != null && shopDeliveryMap != null && p.getDormId() != null
                    && dormShopMap.get(p.getDormId()) != null) {
                Integer shopId = dormShopMap.get(p.getDormId()).getShopId();
                if (shopDeliveryMap.get(shopId) != null) {
                    p.setDormAddress(shopDeliveryMap.get(shopId).getAddress());
                }
            }
        }
        return list;
    }

    /**
     * for app
     *
     * @param uid
     * @param myOrderParam
     * @return
     */
    @SuppressWarnings("null")
    public List<AppOrder> getOrderByUidAndStatus(Long uid, MyOrderParam myOrderParam) {
        List<AppOrder> list = new ArrayList<>();

//		PrintOrderFilter filter = new PrintOrderFilter();
//		filter.setUid(uid);
//		// app 分页计算方式
        int page = 1;
        int pageSize = 1000;
        if (myOrderParam.getPage() != null && myOrderParam.getNum_per_page() != null) {
            pageSize = myOrderParam.getNum_per_page();
            page = myOrderParam.getPage();
        }
//
//		log.info("**************获取订单的请求的参数:" + JsonUtil.getJsonFromObject(filter));
//		Result<List<PrintOrder>> result = printOrderService.findByFilter(filter, true);
//		log.info("**************获取的订单列表:" + JsonUtil.getJsonFromObject(result));
        //接入订单中心
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setBuyerId(uid + "");
        orderQuery.setWithOrderPays(true);
        orderQuery.setWithOrderItems(true);
        List<OrderCenterDTO> orderCenterDTOList = orderCenterService.getOrderList(orderQuery, page, pageSize);

        if (orderCenterDTOList == null || orderCenterDTOList.size() == 0) {
            return list;
        }

        Set<Integer> dormIdSet = new HashSet<>();
        for (OrderCenterDTO orderCenterDTO : orderCenterDTOList) {
            PrintOrder printOrder = orderCenterDTO.getPrintOrder();
            // 数据库中的组合状态映射成前端展示的业务逻辑状态
            if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                    && printOrder.getPayTime() == null && printOrder.getPayType() == null) {// 未支付
                printOrder.setStatus((byte) 0);
            }
            if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                    && printOrder.getPayTime() != null && printOrder.getPayType() != null) {
                printOrder.setStatus((byte) 1);// 未打印= 文档转换中or文档转换完成 + 已支付
            }
            //此处 REFUNDSTATUS 为空,先给2值,对应无任何状态
            AppOrder appOrder = getAppOrder(printOrder,
                    orderCenterDTO.getExtension().get(ExtensionColumnConstant.REFUNDSTATUS) == null ?
                            2 : Byte.valueOf(orderCenterDTO.getExtension().get(ExtensionColumnConstant.REFUNDSTATUS)),true,"5.0.1");
            dormIdSet.add(printOrder.getDormId());
            list.add(appOrder);
        }
        // 缓存dorm信息
        List<Integer> param = new ArrayList<Integer>();
        Map<Integer, Dorm> dormMap = new HashMap<Integer, Dorm>();
        if (!CollectionUtils.isEmpty(dormIdSet)) {
            for (int dormId : dormIdSet) {
                Dorm drom = null;
                String dromResultStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + dormId);
                if (StringUtil.isBlank(dromResultStr)) {
                    param.add(dormId);
                } else {
                    drom = JsonUtil.getObjectFromJson(dromResultStr, Dorm.class);
                    dormMap.put(dormId, drom);
                }
            }
            // Result<List<Dorm>> dormListResult =
            // dormApi.getDormListByIdList(param);
            // if (dormListResult == null || dormListResult.getStatus() != 0 ||
            // dormListResult.getData() == null) {
            // throw new AppException(dormListResult.getStatus(),
            // dormListResult.getMsg());
            // }
            if (param.size() > 0) {
                Result<List<Dorm>> dormListResult = dormApi.getDormListByIdList(param);
                if (dormListResult == null || dormListResult.getStatus() != 0 || dormListResult.getData() == null) {
                    throw new AppException(dormListResult.getStatus(), dormListResult.getMsg());
                }
                if (dormListResult.getData() != null && dormListResult.getData().size() > 0) {
                    dormMap.putAll(dormListResult.getData().parallelStream()
                            .collect(Collectors.toMap(Dorm::getDormId, (p) -> p)));
                    for (Dorm dorm : dormListResult.getData()) {
                        valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + dorm.getDormId(),
                                JsonUtil.getJsonFromObject(dorm), cookieMaxAge, TimeUnit.SECONDS);
                    }
                }
            }

        }
        // Map<Integer, Dorm> dormMap = null;
        // if (!CollectionUtils.isEmpty(dormIdSet)) {
        // Result<List<Dorm>> dormListResult = dormApi.getDormListByIdList(new
        // ArrayList<Integer>(dormIdSet));
        // if (dormListResult == null || dormListResult.getStatus() != 0 ||
        // dormListResult.getData() == null) {
        // throw new AppException(dormListResult.getStatus(),
        // dormListResult.getMsg());
        // }
        // if (dormListResult.getData() != null &&
        // dormListResult.getData().size() > 0) {
        // dormMap =
        // dormListResult.getData().parallelStream().collect(Collectors.toMap(Dorm::getDormId,
        // (p) -> p));
        // }
        // }

        // 拼接打印店地址
        for (AppOrder p : list) {
            if (dormMap != null && p.getDorm_id() != null && dormMap.get(p.getDorm_id()) != null) {
                p.setDorm_address(dormMap.get(p.getDorm_id()).getDeliveryAddress());
                p.setDorm_contact(dormMap.get(p.getDorm_id()).getPhone());
            }
        }
        return list;
    }

    /**
     * 取消订单
     *
     * @param uid
     * @return
     */
    public Boolean cancelOrderById(Long uid, String orderId) {
        //接入订单中心
        OrderCenterDTO orderCenterDTO = orderCenterService.getOrderByOrderId(orderId + "", true, true);
        PrintOrder printOrder = orderCenterDTO.getPrintOrder();

        if (printOrder.getUid().longValue() != uid.longValue()) {
            throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "订单与用户不匹配!");
        }

        // step 1 退款,更改订单状态
        logger.info("orderId:{},取消订单");
//        result = printOrderService.update(tmp);
        //接入订单中心
        orderCenterService.cancelOrder(orderId);
        pushToPcCancel(printOrder);
        // step 2
        // 使用了优惠券,回退优惠券
        if (printOrder.getCouponCode() != null) {
            Thread thread = new Thread() {
                public void run() {
                    logger.info("回退优惠券,优惠券code:{}", printOrder.getCouponCode());
                    CouponQueryParam param = new CouponQueryParam();
                    param.setCode(printOrder.getCouponCode());
                    Result<List<Coupon>> result1 = couponService.returnCoupon(param);
                    if (result1 != null && result1.getStatus() == 0) {
                        logger.info("优惠券回退成功!!!");
                    } else {
                        logger.info("哦,low!优惠券回退失败了!!!");
                    }
                }
            };
            thread.start();
        }
        return true;
    }

    /**
     * 取消订单 for app
     *
     * @param uid
     * @return
     */
    public Boolean cancelOrderById_APP(Long uid, String orderId) {
        //接入订单中心
        OrderCenterDTO orderCenterDTO = orderCenterService.getOrderByOrderId(orderId, true, true);
        PrintOrder printOrder = orderCenterDTO.getPrintOrder();

        if (printOrder.getUid().longValue() != uid.longValue()) {
            throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "订单与用户不匹配!");
        }

        // step 1 退款,更改订单状态
        logger.info("orderId:{},取消订单");
//        result = printOrderService.update(tmp);
        //接入订单中心
        orderCenterService.cancelOrder(orderId + "");

        pushToPcCancel(printOrder);
        // step 2
        // 使用了优惠券,回退优惠券
        if (printOrder.getCouponCode() != null) {
            Thread thread = new Thread() {
                public void run() {
                    logger.info("回退优惠券,优惠券code:{}", printOrder.getCouponCode());
                    CouponQueryParam param = new CouponQueryParam();
                    param.setCode(printOrder.getCouponCode());
                    Result<List<Coupon>> result1 = couponService.returnCoupon(param);
                    if (result1 != null && result1.getStatus() == 0) {
                        logger.info("优惠券回退成功!!!");
                    } else {
                        logger.info("哦,low!优惠券回退失败了!!!");
                    }
                }
            };
            thread.start();
        }
        // 如果是微信则推送下单成功消息
        String openid = valueOpsCache.get(CommonConstant.WECHAT_UID_OPENID_PREFIX + String.valueOf(uid));
        logger.info("取消订单消息openid：" + openid);
        if (!StringUtil.isBlank(openid)) {
            TextMessage textMsg = new TextMessage(openid, "您有一个订单已取消。订单编号" + orderId + "。<a href=\""
                    + String.format(payOpenidUrl, URLEncoder.encode(orderListUrl + "?uid=" + uid)) + "\">点击查看订单</a>");
            MessageAPI.messageCustomSend(tokenManager.getToken(appid), textMsg);
        }
        return true;
    }

    public PrintOrder getPrintOrderById(Long orderId) {
        Result<PrintOrder> printOrderResult = printOrderService.findByOrderId(orderId, false);
        if (printOrderResult == null || printOrderResult.getStatus() != 0) {
            if (printOrderResult != null) {
                throw new AppException(printOrderResult.getStatus(), printOrderResult.getMsg());
            }
            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        if (printOrderResult.getData() == null) {
            throw new AppException(-1, "没有找到该订单");
        }
        return printOrderResult.getData();
    }

    /**
     * for app
     *
     * @return
     */
    public AppOrder getOrderDetailsByOrderId(String order_id, Long uid) {
//        Result<PrintOrder> result = printOrderService.findByOrderId(order_id, true);
        //接入订单中心
        OrderCenterDTO orderCenterDTO = orderCenterService.getOrderByOrderId(order_id, true, true);
        PrintOrder printOrder = orderCenterDTO.getPrintOrder();

        // 校验获取的是否当前用户的订单
        if (uid.longValue() != printOrder.getUid().longValue()) {
            throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "订单与当前用户不匹配!");
        }
        // 增加缓存dorm信息
        Result<Dorm> dormResult = null;
        String dromResultStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + printOrder.getDormId());
        if (StringUtil.isBlank(dromResultStr)) {
            dormResult = dormApi.getDorm(printOrder.getDormId());
            if (dormResult.getStatus() != 0) {
                logger.error("获取店长信息失败,dorm_id=" + printOrder.getDormId());
                throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            } else {
                valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + printOrder.getDormId(),
                        JsonUtil.getJsonFromObject(dormResult.getData()), cookieMaxAge + 10, TimeUnit.SECONDS);
            }
        } else {
            dormResult = new Result<Dorm>();
            dormResult.setData(JsonUtil.getObjectFromJson(dromResultStr, Dorm.class));
        }

        // 数据库中的组合状态映射成前端展示的业务逻辑状态
        if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                && printOrder.getPayTime() == null && printOrder.getPayType() == null) {// 未支付
            printOrder.setStatus((byte) 0);
        }
        if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                && printOrder.getPayTime() != null && printOrder.getPayType() != null) {
            printOrder.setStatus((byte) 1);// 未打印= 文档转换中or文档转换完成 + 已支付
        }
        // 其他状态值前后端保持一致
        //此处 REFUNDSTATUS 为空,先给2值,对应无任何状态
        AppOrder appOrder = getAppOrderDetail(printOrder,
                orderCenterDTO.getExtension().get(ExtensionColumnConstant.REFUNDSTATUS) == null ?
                        2 : Byte.valueOf(orderCenterDTO.getExtension().get(ExtensionColumnConstant.REFUNDSTATUS)));
        appOrder.setDorm_address(dormResult.getData().getDeliveryAddress());
        appOrder.setDorm_contact(dormResult.getData().getPhone());
        appOrder.setAttach(null);
        return appOrder;
    }

    public AppOrder getAppOrder(PrintOrder printOrder, byte refundStatus,boolean is_consistent,String app_version) {
        /**
         * 增加单份详情的价格计算,需要获取shopId
         */
        // DormShopFilter filter = new DormShopFilter();
        // filter.setType((byte) 2); // 打印店
        // filter.setDormId(printOrder.getDormId());
        // Result<List<DormShop>> dormShopResult =
        // dormShopService.findByFilter(filter);
        // if (dormShopResult.getStatus() != 0) {
        // logger.error(dormShopResult.getMsg());
        // throw new
        // AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
        // CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        // }
        // if (dormShopResult.getData() == null ||
        // dormShopResult.getData().size() == 0) {
        // throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR,
        // "店铺不存在,店长id为:" + printOrder.getDormId());
        // }
        // Integer shopId = dormShopResult.getData().get(0).getShopId();
        // 默认为文档
        AppOrder appOrder = new AppOrder();
        appOrder.setDoc_type(printOrder.getDocType() == null ? 1 : printOrder.getDocType().intValue());
        appOrder.setAttach(null);
        appOrder.setOrder_sn(printOrder.getOrderId());
        appOrder.setCancel_reason(printOrder.getCancelReason());
        appOrder.setIs_consistent(is_consistent);
        appOrder.setBuyer_name(printOrder.getUname());
        if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                && printOrder.getPayTime() == null) {// 未支付
            appOrder.setStatus((byte) 0);
        } else if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                && printOrder.getPayTime() != null) {
            appOrder.setStatus((byte) 1);// 未打印= 文档转换中or文档转换完成 + 已支付
        } else {
            appOrder.setStatus(printOrder.getStatus());
        }
        if(app_version==null||Double.parseDouble(app_version.substring(0,3))<=5d){
        	appOrder.setType((byte) 8);
        }else{
        	if(printOrder.getDocType().byteValue()==PrintConst.PRINT_ORDER_TYPE_PHOTO)
        		appOrder.setType((byte) 22);
        	if(printOrder.getDocType().byteValue()==PrintConst.PRINT_ORDER_TYPE_DOC)
        		appOrder.setType((byte) 21);
        }
        appOrder.setSource(printOrder.getSource());
        if (printOrder.getPayType() == null) {
            appOrder.setPaytype((byte) 1); // 由于创建订单时没有支付方式,返回前端默认给支付宝
        } else {
            appOrder.setPaytype(printOrder.getPayType());
        }
        appOrder.setPaytime(printOrder.getPayTime());
        appOrder.setPay_trade_no(printOrder.getTradeNo());
        if (printOrder.getStatus() == PrintConst.ORDER_STATUS_CANCELED && printOrder.getPayTime() != null
                && printOrder.getPayType() != null) {
//            OrderPayAbnormalRecordFilter filter1 = new OrderPayAbnormalRecordFilter();
//            List<String> orderIdList = new ArrayList<>();
//            orderIdList.add(printOrder.getOrderId() + "");
//            filter1.setOrderSns(orderIdList);
//            filter1.setOrderType(CommonConstant.ORDERPAY_RECORD_ORDER_TYPE);
//            filter1.setPayTradeNo(printOrder.getTradeNo());
//            Result<List<OrderPayAbnormalRecord>> result = orderPayAbnormalRecordApi.findByFilter(filter1);
//            if (result.getStatus() == 0 && result.getData().size() > 0) {
//                OrderPayAbnormalRecord record = result.getData().get(0);
            if (refundStatus == OrderRefundStatusEnum.REFUNDEING.getCode().byteValue()) { // 退款中
                appOrder.setRefund_status_code((byte) 0); // 返回给前端-->0为退款中，1为已退款
                appOrder.setRefund_status_msg("退款中");
            } else if (refundStatus == OrderRefundStatusEnum.REFUNDED.getCode().byteValue()) { // 已处理
                appOrder.setRefund_status_code((byte) 1); // 返回给前端-->0为退款中，1为已退款
                appOrder.setRefund_status_msg("已退款");
            }
//            } else {
//                log.error("退款记录不存在,订单号:{}", printOrder.getOrderId());
//            }
        }

        int printNum = 0;
        int printPages = 0;
        List<AppCreateOrderDetailParam> items = new ArrayList<>();
        double bwSingleUnitPrice = printOrder.getAdUnitPrice();
        for (PrintOrderDetail detail : printOrder.getDetails()) {
            if (detail.getIsFirst().byteValue() == CommonConstant.ORDER_FIRST_LIST_0) {
                AppCreateOrderDetailParam appDetail = new AppCreateOrderDetailParam();
                appDetail.setPage(detail.getPageNum());
                appDetail.setReduced_type(detail.getReprintType());
                appDetail.setQuantity(detail.getNum());
                appDetail.setPrint_type(detail.getPrintType());
                appDetail.setPdf_size(detail.getPdfSize());
                appDetail.setPdf_path(detail.getUrl());
                appDetail.setPdf_md5(detail.getPdfMD5());
                appDetail.setFile_name(detail.getFileName());
                appDetail.setDoc_path(detail.getSourceUrl());
                appDetail.setDoc_md5(detail.getSourceMD5());
                // double price = getSingleDetailAmount(shopId, detail);
                BigDecimal bd1 = (BigDecimal.valueOf(detail.getAmount())).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd2 = (BigDecimal.valueOf(detail.getNum())).setScale(2, RoundingMode.HALF_UP);
                double price = bd1.divide(bd2).setScale(2, RoundingMode.HALF_UP).doubleValue(); // 单份详情的价格
                appDetail.setPrice(price);
                appDetail.setOrigin_price(price);
                appDetail.setAmount(appDetail.getPrice() * appDetail.getQuantity());
                // 打印规格描述
                StringBuilder specSB = new StringBuilder();
                if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_SINGLE) {
                    specSB.append("黑白单面，");
                    BigDecimal bd3 = (BigDecimal.valueOf(detail.getPageNum())).setScale(2, RoundingMode.HALF_UP);
                    bwSingleUnitPrice=bd1.divide(bd3,2,RoundingMode.HALF_UP).divide(bd2,2,RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue(); // 单份详情的价格;
                } else if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE) {
                    specSB.append("黑白双面，");
                } else if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_COLOR_SINGLE) {
                    specSB.append("彩色单面，");
                } else if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
                    specSB.append("彩色双面，");
                } else if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_PHOTO) {
                    specSB.append("A6，");
                }
                // 缩印
                if (detail.getReprintType().byteValue() == PrintConst.re_print_type_none) {
                    specSB.append("不缩印，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_type_two) {
                    specSB.append("一页两面，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_type_four) {
                    specSB.append("一页四面，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_type_six) {
                    specSB.append("一页六面，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_type_nine) {
                    specSB.append("一页九面，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_photo_default) {
                    specSB.append("默认");
                }
                if (detail.getReprintType().byteValue() != PrintConst.re_print_photo_default)
                    specSB.append(detail.getPageNum()).append("页");
                appDetail.setSpecifications(specSB.toString());
                items.add(appDetail);
                printNum += detail.getNum();
                printPages += (detail.getPageNum() * detail.getNum());
            }
        }
        appOrder.setItems(items);
        appOrder.setPrint_pages(printPages); // 文档总页数
        appOrder.setPrint_num(printNum); // 文档总份数
        appOrder.setPrint_amount(printOrder.getTotalAmount()); // 文档总金额
        appOrder.setDelivery_amount(printOrder.getDeliveryAmount()); // 配送费

        Double couponDiscount = printOrder.getCouponDiscount() == null ? 0.00 : printOrder.getCouponDiscount();
        // 目前折扣金额只有优惠券费用
        appOrder.setDiscount(couponDiscount); // 合计折扣金额=优惠券费用+广告费用+其他折扣
        appOrder.setCoupon_discount(couponDiscount); // 优惠券折扣
        
        BigDecimal documentAmount = new BigDecimal(printOrder.getTotalAmount());
        BigDecimal deliveryAmount = new BigDecimal(printOrder.getDeliveryAmount());
        BigDecimal disCount = new BigDecimal(-appOrder.getDiscount());// 折扣金额
        /**
         * 免费打印增加订单显示金额
         */
        
        BigDecimal BwSinglePrintPrice = new BigDecimal(bwSingleUnitPrice);//黑白单面
        BigDecimal freeAmount = BwSinglePrintPrice.multiply(new BigDecimal(printOrder.getAdPageNum())).setScale(2, RoundingMode.HALF_UP);//免费价格
        appOrder.setDiscount(new BigDecimal(couponDiscount).add(freeAmount).setScale(2, RoundingMode.HALF_UP).doubleValue()); //设置折扣金额喂免费打印金额＋优惠券
        appOrder.setDoc_amount(new BigDecimal(printOrder.getTotalAmount()).add(freeAmount).setScale(2, RoundingMode.HALF_UP).doubleValue());//文档免费打印前价格
        appOrder.setAd_page_num(printOrder.getAdPageNum());
        appOrder.setFree_amount(freeAmount.doubleValue());
        
        // 订单金额=文档价格+配送费-折扣金额，修改：此处不减折扣金额
        double orderAmount = documentAmount.add(deliveryAmount).add(disCount).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();// 实付金额
        double payAmount = documentAmount.add(deliveryAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();// 订单金额
        if (orderAmount <= 0) {
            orderAmount = 0.01;
        }
        if (payAmount < 0) {
            payAmount = 0;
        }
        double itemAmount= documentAmount.add(deliveryAmount).subtract(new BigDecimal(couponDiscount)).setScale(2, RoundingMode.HALF_UP).doubleValue();// 订单金额
        appOrder.setItem_amount(itemAmount<=0?0:itemAmount); // 订单金额-改为总金额减去优惠券
        appOrder.setOrder_amount(orderAmount); // 实付金额
        appOrder.setAdd_time(printOrder.getCreateTime());
        appOrder.setConfirm_time(printOrder.getCreateTime()); // 打印店没有店长确认订单的功能,由于自动打印,可以把确认时间与下单时间统一
        appOrder.setSend_time(printOrder.getFinishTime()); // 打印店没有送达时间记录,直接传订单完成时间
        appOrder.setAddress(printOrder.getAddress());
        appOrder.setPhone(printOrder.getPhone());
        appOrder.setSend_type(printOrder.getDeliveryType());
        StringBuilder delivery_desc = new StringBuilder();
        if (printOrder.getDeliveryType() == CommonConstant.DELIVERY_TYPE_DORMER) {
            delivery_desc.append("店长配送(");
        } else {
            delivery_desc.append("上门自取(");
        }
        delivery_desc.append(printOrder.getDeliveryTime() == null ? "立即配送" : printOrder.getDeliveryTime());
        delivery_desc.append(")");

        appOrder.setDelivery_desc(delivery_desc.toString());
        appOrder.setRemark(printOrder.getRemark());
        appOrder.setDorm_id(printOrder.getDormId());

        logger.info("返回给前端的订单信息Json:" + JsonUtil.getJsonFromObject(appOrder));

        return appOrder;
    }

//    public void updatePrintOrderById(PrintOrder printOrder) {
//        log.info("****开始更新订单状态:" + JsonUtil.getJsonFromObject(printOrder));
//        Result<Boolean> result = printOrderService.update(printOrder);
//        log.info("更新订单结果:" + JsonUtil.getJsonFromObject(result));
//
//        if (result == null || result.getStatus() != 0 || result.getData() == null || !result.getData()) {
//            if (result != null) {
//                throw new AppException(result.getStatus(), result.getMsg());
//            }
//            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
//                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
//        }
//        Result<PrintOrder> result1 = printOrderService.findByOrderId(printOrder.getOrderId(), false);
//        if (result1 == null || result1.getStatus() != 0) {
//            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
//                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
//        }
//        pushToPc(result1.getData());
//    }

    /**
     * 推送到pc端
     */
    public void pushToPc(PrintOrder printOrder) {
        Map content = new HashMap<>();
        Map param = new HashMap<>();
        Map data = new HashMap<>();

        content.put("orderId", printOrder.getOrderId().toString());
        content.put("action", 4);
        if (printOrder.getPayType() == 1) {
            content.put("payType", 1);
        }
        if (printOrder.getPayType() == 6) {
            content.put("payType", 2);
        }
        content.put("shopType", CommonConstant.SHOP_TYPE_PRINT);
        content.put("payStatus", 10);

        param.put("key", "PC");
        param.put("topic", "dorm_" + printOrder.getDormId());
        param.put("content", new JSONObject(content).toString());

        data.put("target", "PUSH");
        data.put("param", param);

        String pushPcString = new JSONObject(data).toString();
        logger.info("支付完成后推送给楼主PC端{}", pushPcString);

        rabbitTemplate.convertAndSend("event-entry", pushPcString);
    }

    // /**
    // * 单份文档价格
    // *
    // * @param shopId
    // * @param detail
    // * @return
    // */
    // private double getSingleDetailAmount(Integer shopId, PrintOrderDetail
    // detail) {
    // Byte printId = detail.getPrintType();
    // //TODO
    //
    // Double unitPrice = detail.(); //打印单张价格
    // Integer pdf_pageNum = detail.getPageNum(); //订单详情中记录的最终打印的pdf页数
    // //单价*pdf页数*份数
    // Double amount = 0.00;
    //
    // BigDecimal bd1 = (BigDecimal.valueOf(unitPrice)).setScale(2,
    // RoundingMode.HALF_UP);
    // BigDecimal bd2 = (BigDecimal.valueOf(pdf_pageNum)).setScale(2,
    // RoundingMode.HALF_UP);
    // amount = bd1.multiply(bd2).setScale(2,
    // RoundingMode.HALF_UP).doubleValue();
    //
    // return amount;
    // }

    /**
     * 我的订单列表新拆分
     *
     * @param uid
     * @return
     */
    public MyOrderResultList getOrderList(Long uid, MyOrderParam myOrderParam) {
        List<MyOrderResult> list = new ArrayList<>();
        List<OrderStatusEnum> statusList = new ArrayList<>();
        if (myOrderParam.getType() != null && myOrderParam.getType().size() > 0) {
            for (int status : myOrderParam.getType()) {
                if (status == 0) {
                    statusList.add(OrderStatusEnum.INIT);
                }
                if (status == 1) {
                    statusList.add(OrderStatusEnum.CONFIRMED);
                }
                if (status == 2) {
                    statusList.add(OrderStatusEnum.DELIVERED);
                }
                if (status == 4) {
                    statusList.add(OrderStatusEnum.FINISHED);
                }
                if (status == 5) {
                    statusList.add(OrderStatusEnum.CANCELED);
                }
                if (status == 6) {
                    statusList.add(OrderStatusEnum.CLOSED);
                }
            }
        } else {
            statusList.add(OrderStatusEnum.CONFIRMED);
        }

        //接入订单中心
        int page = 1;
        int pageSize = 1000;
        if (myOrderParam.getLimit() != null && myOrderParam.getOffset() != null) {
            pageSize = myOrderParam.getLimit();
            page = myOrderParam.getOffset()/myOrderParam.getLimit() + 1;
        }
        //接入订单中心
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setBuyerId(uid + "");
        orderQuery.setWithOrderPays(true);
        orderQuery.setWithOrderItems(true);
        orderQuery.setStatusList(statusList);

        List<OrderCenterDTO> orderCenterDTOList = orderCenterService.getOrderList(orderQuery, page, pageSize);

        if (orderCenterDTOList == null || orderCenterDTOList.size() == 0) {
            MyOrderResultList orderList = new MyOrderResultList();
            return orderList;
        }
//        Result<Integer> countResult = printOrderService.findCountByFilter(filter);

        Set<Integer> dormIdSet = new HashSet<>();
        for (OrderCenterDTO orderCenterDTO : orderCenterDTOList) {
            PrintOrder printOrder = orderCenterDTO.getPrintOrder();
            // 数据库中的组合状态映射成前端展示的业务逻辑状态
            if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                    && printOrder.getPayTime() == null && printOrder.getPayType() == null) {// 未支付
                printOrder.setStatus((byte) 0);
            }
            if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                    && printOrder.getPayTime() != null && printOrder.getPayType() != null) {
                printOrder.setStatus((byte) 1);// 未打印= 文档转换中or文档转换完成 + 已支付
            }
            // 其他状态值前后端保持一致
            MyOrderResult myOrderResult = new MyOrderResult();
            myOrderResult.setOrderId(printOrder.getOrderId() + "");
            myOrderResult.setOrderStatus(printOrder.getStatus());
            myOrderResult.setAddress(printOrder.getAddress());
            myOrderResult.setPhone(printOrder.getPhone());
            myOrderResult.setCreateTime(printOrder.getCreateTime());
            myOrderResult.setPayType(printOrder.getPayType());
            myOrderResult.setCouponDiscount(printOrder.getCouponDiscount());
            myOrderResult.setAdPageNum(printOrder.getAdPageNum());
            myOrderResult.setDocType(printOrder.getDocType());
            BigDecimal orderAmount = new BigDecimal(printOrder.getTotalAmount());
            BigDecimal deliveryAmount = new BigDecimal(printOrder.getDeliveryAmount());
            Double couponDiscount = printOrder.getCouponDiscount() == null ? 0.00 : printOrder.getCouponDiscount();
            BigDecimal couponAmount = new BigDecimal(-couponDiscount);

            myOrderResult.setPrintAmount(printOrder.getTotalAmount());
            myOrderResult.setDeliveryTime(printOrder.getDeliveryTime());
            myOrderResult.setDeliveryType(printOrder.getDeliveryType());
            myOrderResult.setDeliveryAmount(printOrder.getDeliveryAmount());
            // 返回给前端订单总价=文档价格+配送费-优惠券金额
            double totalAmount = orderAmount.add(deliveryAmount).add(couponAmount).setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            if (totalAmount == 0) {
                totalAmount = 0.01;
            }
            myOrderResult.setTotalAmount(totalAmount);
            myOrderResult.setDormId(printOrder.getDormId());
            myOrderResult.setRemark(printOrder.getRemark());
            dormIdSet.add(printOrder.getDormId());
            list.add(myOrderResult);
        }
        Map<Integer, Dorm> dormMap = new HashMap<Integer, Dorm>();
        Map<Integer, DormShop> dormShopMap = new HashMap<Integer, DormShop>();
        Map<Integer, DormShopDelivery> shopDeliveryMap = new HashMap<Integer, DormShopDelivery>();
        if (!CollectionUtils.isEmpty(dormIdSet)) {
            List<Integer> dormSearchList = new ArrayList<>();
            for (Integer dormId : new ArrayList<Integer>(dormIdSet)) {
                if (StringUtil.isBlank(valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + dormId))) {
                    dormSearchList.add(dormId);
                } else {
                    dormMap.put(dormId, JsonUtil.getObjectFromJson(
                            valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + dormId), new TypeReference<Dorm>() {
                            }));
                }
            }

            if (dormSearchList.size() > 0) {
                Result<List<Dorm>> dormListResult = dormApi.getDormListByIdList(dormSearchList);
                if (dormListResult == null || dormListResult.getStatus() != 0 || dormListResult.getData() == null) {
                    throw new BaseException(dormListResult.getStatus(), dormListResult.getMsg());
                }
                if (dormListResult.getData() != null && dormListResult.getData().size() > 0) {
                    for (Dorm dorm : dormListResult.getData()) {
                        if (dorm.getDormId() != null) {
                            dormMap.put(dorm.getDormId(), dorm);
                            valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + dorm.getDormId(),
                                    JsonUtil.getJsonFromObject(dorm), cookieMaxAge, TimeUnit.SECONDS);
                        }
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(dormIdSet)) {
            Result<List<DormShop>> dormshopRet = new Result<List<DormShop>>();
            List<DormShop> dormShopList = new ArrayList<DormShop>();
            List<Integer> dormshopSearchList = new ArrayList<>();
            for (Integer dormId : new ArrayList<Integer>(dormIdSet)) {
                String dormShopStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM_SHOP + dormId);
                if (StringUtil.isBlank(dormShopStr)) {
                    dormshopSearchList.add(dormId);
                } else {
                    dormShopList.add(JsonUtil.getObjectFromJson(dormShopStr, DormShop.class));
                    dormShopMap.put(dormId, JsonUtil.getObjectFromJson(dormShopStr, DormShop.class));
                }
            }

            if (dormshopSearchList.size() > 0) {
                DormShopFilter dormShopFilter = new DormShopFilter();
                dormShopFilter.setDormIds(dormshopSearchList);
                dormShopFilter.setType((byte) 2); // 打印店
                Result<List<DormShop>> dormshopRetLeft = dormShopService.findByFilter(dormShopFilter);
                if (dormshopRetLeft == null || dormshopRetLeft.getStatus() != 0 || dormshopRetLeft.getData() == null) {
                    throw new BaseException(dormshopRetLeft.getStatus(), dormshopRetLeft.getMsg());
                }
                if (dormshopRetLeft.getData() != null && dormshopRetLeft.getData().size() > 0) {
                    dormShopList.addAll(dormshopRetLeft.getData());
                    for (DormShop dormShop : dormshopRetLeft.getData()) {
                        dormShopMap.put(dormShop.getDormId(), dormShop);
                        valueOpsCache.set(CommonConstant.KEY_REDIS_DORM_SHOP + dormShop.getDormId(),
                                JsonUtil.getJsonFromObject(dormShop), cookieMaxAge, TimeUnit.SECONDS);
                    }
                }
            }
            dormshopRet.setData(dormShopList);
            /**
             * 获取店铺配送信息
             */
            Set<Integer> shopIds = dormshopRet.getData().parallelStream().map(DormShop::getShopId)
                    .collect(Collectors.toSet());
            DormShopDeliveryFilter dsdFilter = new DormShopDeliveryFilter();
            ArrayList<Integer> shopIdsSearch = new ArrayList<Integer>();
            for (Integer shopId : shopIds) {
                String shopInfo = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM_DELIVERY + shopId);
                if (StringUtil.isBlank(shopInfo)) {
                    shopIdsSearch.add(shopId);
                } else {
                    shopDeliveryMap.put(shopId, JsonUtil.getObjectFromJson(shopInfo, DormShopDelivery.class));
                }
            }
            if (shopIdsSearch.size() > 0) {
                dsdFilter.setShopIds(shopIdsSearch);
                dsdFilter.setMethod(CommonConstant.DELIVERY_TYPE_YOURSELF);
                dsdFilter.setStatus(ConstDorm.DORM_SHOP_DELIVERY_STATUS_1);// 启用
                Result<List<DormShopDelivery>> dsdListresult = dormShopDeliveryApi.findByFilter(dsdFilter);
                if (dsdListresult.getStatus() != 0) {
                    throw new BaseException(dsdListresult.getStatus(), dsdListresult.getMsg());
                }
                if (dsdListresult.getData() != null && dsdListresult.getData().size() > 0) {
                    shopDeliveryMap.putAll(dsdListresult.getData().parallelStream()
                            .collect(Collectors.toMap(DormShopDelivery::getShopId, (p) -> p)));
                    for (DormShopDelivery di : dsdListresult.getData()) {
                        valueOpsCache.set(CommonConstant.KEY_REDIS_DORM_DELIVERY + di.getShopId(),
                                JsonUtil.getJsonFromObject(di), cookieMaxAge + 10, TimeUnit.SECONDS);
                    }
                }
            }
        }

        // 拼接打印店地址
        for (MyOrderResult p : list) {
            if (dormMap != null && p.getDormId() != null && dormMap.get(p.getDormId()) != null) {
                p.setDormPhone(dormMap.get(p.getDormId()).getPhone());
            }
            if (dormShopMap != null && p.getDormId() != null && dormShopMap.get(p.getDormId()) != null) {
                p.setShopName(dormShopMap.get(p.getDormId()).getName());
            }
            if (dormShopMap != null && shopDeliveryMap != null && p.getDormId() != null
                    && dormShopMap.get(p.getDormId()) != null) {
                Integer shopId = dormShopMap.get(p.getDormId()).getShopId();
                if (shopDeliveryMap.get(shopId) != null) {
                    p.setDormAddress(shopDeliveryMap.get(shopId).getAddress());
                }
            }
        }
        MyOrderResultList orderList = new MyOrderResultList();
        orderList.setMyOrderResultList(list);
        if (list.size() > 0 && myOrderParam.getLimit() != null) {
            if (list.size() == myOrderParam.getLimit()) {
                orderList.setFlag(true);
            } else {
                orderList.setFlag(false);
            }
        } else {
            orderList.setFlag(false);
        }
        return orderList;
    }

    /**
     * PC端获取订单详情
     *
     * @param orderId
     * @param uid
     * @return
     */
    public MyOrderResult myOrderDetail(String orderId, Long uid) {
        //接入订单中心
        OrderCenterDTO orderCenterDTO = orderCenterService.getOrderByOrderId(orderId, true, true);
        PrintOrder printOrder = orderCenterDTO.getPrintOrder();
        // 校验获取的是否当前用户的订单
        if (uid.longValue() != printOrder.getUid().longValue()) {
            throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "订单与当前用户不匹配!");
        }
        // 增加缓存dorm信息
        Result<Dorm> dormResult = null;
        String dromResultStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + printOrder.getDormId());
        if (StringUtil.isBlank(dromResultStr)) {
            dormResult = dormApi.getDorm(printOrder.getDormId());
            if (dormResult.getStatus() != 0) {
                logger.error("获取店长信息失败,dorm_id=" + printOrder.getDormId());
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            } else {
                valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + printOrder.getDormId(),
                        JsonUtil.getJsonFromObject(dormResult.getData()), cookieMaxAge + 10, TimeUnit.SECONDS);
            }
        } else {
            dormResult = new Result<Dorm>();
            dormResult.setData(JsonUtil.getObjectFromJson(dromResultStr, Dorm.class));
        }

        // 数据库中的组合状态映射成前端展示的业务逻辑状态
        if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                && printOrder.getPayTime() == null && printOrder.getPayType() == null) {// 未支付
            printOrder.setStatus((byte) 0);
        }
        if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                && printOrder.getPayTime() != null && printOrder.getPayType() != null) {
            printOrder.setStatus((byte) 1);// 未打印= 文档转换中or文档转换完成 + 已支付
        }
        // 其他状态值前后端保持一致
        MyOrderResult myOrderResult = new MyOrderResult();
        myOrderResult.setOrderId(printOrder.getOrderId());
        myOrderResult.setOrderStatus(printOrder.getStatus());
        myOrderResult.setAddress(printOrder.getAddress());
        myOrderResult.setPhone(printOrder.getPhone());
        myOrderResult.setCreateTime(printOrder.getCreateTime());
        myOrderResult.setPayType(printOrder.getPayType());
        myOrderResult.setCouponDiscount(printOrder.getCouponDiscount());
        myOrderResult.setAdPageNum(printOrder.getAdPageNum());

        BigDecimal orderAmount = new BigDecimal(printOrder.getTotalAmount());
        BigDecimal deliveryAmount = new BigDecimal(printOrder.getDeliveryAmount());
        Double couponDiscount = printOrder.getCouponDiscount() == null ? 0.00 : printOrder.getCouponDiscount();
        BigDecimal couponAmount = new BigDecimal(-couponDiscount);

        myOrderResult.setPrintAmount(printOrder.getTotalAmount());
        myOrderResult.setDeliveryTime(printOrder.getDeliveryTime());
        myOrderResult.setDeliveryType(printOrder.getDeliveryType());
        myOrderResult.setDeliveryAmount(printOrder.getDeliveryAmount());
        // 返回给前端订单总价=文档价格+配送费-优惠券金额
        double totalAmount = orderAmount.add(deliveryAmount).add(couponAmount).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        if (totalAmount == 0) {
            totalAmount = 0.01;
        }
        myOrderResult.setTotalAmount(totalAmount);
        myOrderResult.setDormId(printOrder.getDormId());
        myOrderResult.setRemark(printOrder.getRemark());
        List<PrintOrderDetail> myOrderDetail = new ArrayList<>();
        for (PrintOrderDetail detail : printOrder.getDetails()) {
            if (detail.getIsFirst().byteValue() == CommonConstant.ORDER_FIRST_LIST_0) {
                myOrderDetail.add(detail);
            }
        }
        myOrderResult.setMyOrderDetail(myOrderDetail);
        myOrderResult.setCancelReason(printOrder.getCancelReason());
        return myOrderResult;
    }

    public AppOrder getAppOrderDetail(PrintOrder printOrder, byte refundStatus) {
        /**
         * 增加单份详情的价格计算,需要获取shopId
         */
        //获取店铺信息&此处添加缓存
//        Result<DormShop> result = null;
//        result = dormShopService.findByShopId(Integer.valueOf(printOrder.get), false, false, true, true);//包含店铺价格,配送信息
//        if (result.getStatus() != 0) {
//            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
//                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
//        }
//        //店铺价格
//        List<DormShopPrice> dormShopPrices = result.getData().getDormShopPrices();
//        if (dormShopPrices == null || dormShopPrices.size() == 0) {
//            throw new AppException(CommonConstant.STATUS_SHOPPRINCE_ISBLANK, "店铺打印价格为空");
//        }
//        Map<String, DormShopPrice> priceMap = new HashMap<>();
//        for (DormShopPrice shopPrice : dormShopPrices) {
//            priceMap.put(shopPrice.getType() + "", shopPrice);
//        }
        // DormShopFilter filter = new DormShopFilter();
        // filter.setType((byte) 2); // 打印店
        // filter.setDormId(printOrder.getDormId());
        // Result<List<DormShop>> dormShopResult =
        // dormShopService.findByFilter(filter);
        // if (dormShopResult.getStatus() != 0) {
        // logger.error(dormShopResult.getMsg());
        // throw new
        // AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
        // CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        // }
        // if (dormShopResult.getData() == null ||
        // dormShopResult.getData().size() == 0) {
        // throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR,
        // "店铺不存在,店长id为:" + printOrder.getDormId());
        // }
        // Integer shopId = dormShopResult.getData().get(0).getShopId();
        // 默认为文档
        AppOrder appOrder = new AppOrder();
        appOrder.setDoc_type(printOrder.getDocType() == null ? 1 : printOrder.getDocType().intValue());
        appOrder.setAttach(null);
        appOrder.setOrder_sn(printOrder.getOrderId() + "");
        appOrder.setCancel_reason(printOrder.getCancelReason());
        appOrder.setBuyer_name(printOrder.getUname());

        if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                && printOrder.getPayTime() == null) {// 未支付
            appOrder.setStatus((byte) 0);
        } else if ((printOrder.getStatus().byteValue() == 0 || printOrder.getStatus().byteValue() == 1)
                && printOrder.getPayTime() != null) {
            appOrder.setStatus((byte) 1);// 未打印= 文档转换中or文档转换完成 + 已支付
        } else {
            appOrder.setStatus(printOrder.getStatus());
        }

        appOrder.setType((byte) 8);
        appOrder.setSource(printOrder.getSource());
        if (printOrder.getPayType() == null) {
            appOrder.setPaytype((byte) 1); // 由于创建订单时没有支付方式,返回前端默认给支付宝
        } else {
            appOrder.setPaytype(printOrder.getPayType());
        }
        appOrder.setPaytime(printOrder.getPayTime());
        appOrder.setPay_trade_no(printOrder.getTradeNo());
        if (printOrder.getStatus() == PrintConst.ORDER_STATUS_CANCELED && printOrder.getPayTime() != null
                && printOrder.getPayType() != null) {
//            OrderPayAbnormalRecordFilter filter1 = new OrderPayAbnormalRecordFilter();
//            List<String> orderIdList = new ArrayList<>();
//            orderIdList.add(printOrder.getOrderId() + "");
//            filter1.setOrderSns(orderIdList);
//            filter1.setOrderType(CommonConstant.ORDERPAY_RECORD_ORDER_TYPE);
//            filter1.setPayTradeNo(printOrder.getTradeNo());
//            Result<List<OrderPayAbnormalRecord>> result = orderPayAbnormalRecordApi.findByFilter(filter1);
//            if (result.getStatus() == 0 && result.getData().size() > 0) {
//                OrderPayAbnormalRecord record = result.getData().get(0);
            if (refundStatus == OrderRefundStatusEnum.REFUNDEING.getCode().byteValue()) { // 退款中
                appOrder.setRefund_status_code((byte) 0); // 返回给前端-->0为退款中，1为已退款
                appOrder.setRefund_status_msg("退款中");
            } else if (refundStatus == OrderRefundStatusEnum.REFUNDED.getCode().byteValue()) { // 已处理
                appOrder.setRefund_status_code((byte) 1); // 返回给前端-->0为退款中，1为已退款
                appOrder.setRefund_status_msg("已退款");
            }
//            } else {
//                log.error("退款记录不存在,订单号:{}", printOrder.getOrderId());
//            }
        }

        int printNum = 0;
        int printPages = 0;
        List<AppCreateOrderDetailParam> items = new ArrayList<>();
        double bwSingleUnitPrice = printOrder.getAdUnitPrice();
        for (PrintOrderDetail detail : printOrder.getDetails()) {
            if (detail.getIsFirst().byteValue() == CommonConstant.ORDER_FIRST_LIST_0) {
                AppCreateOrderDetailParam appDetail = new AppCreateOrderDetailParam();
                appDetail.setPage(detail.getPageNum());
                appDetail.setReduced_type(detail.getReprintType());
                appDetail.setQuantity(detail.getNum());
                appDetail.setPrint_type(detail.getPrintType());
                appDetail.setPdf_size(detail.getPdfSize());
                appDetail.setPdf_path(detail.getUrl());
                appDetail.setPdf_md5(detail.getPdfMD5());
                appDetail.setFile_name(detail.getFileName());
                appDetail.setDoc_path(detail.getSourceUrl());
                appDetail.setDoc_md5(detail.getSourceMD5());
                // double price = getSingleDetailAmount(shopId, detail);
                BigDecimal bd1 = (BigDecimal.valueOf(detail.getAmount())).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd2 = (BigDecimal.valueOf(detail.getNum())).setScale(2, RoundingMode.HALF_UP);
                double price = bd1.divide(bd2).setScale(2, RoundingMode.HALF_UP).doubleValue(); // 单份详情的价格
                appDetail.setPrice(price);
                appDetail.setOrigin_price(price);
                appDetail.setAmount(appDetail.getPrice() * appDetail.getQuantity());
                // 打印规格描述
                StringBuilder specSB = new StringBuilder();
                if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_SINGLE) {
                    specSB.append("黑白单面，");
                    BigDecimal bd3 = (BigDecimal.valueOf(detail.getPageNum())).setScale(2, RoundingMode.HALF_UP);
                    bwSingleUnitPrice=bd1.divide(bd3, 10, RoundingMode.HALF_UP).divide(bd2).setScale(2, RoundingMode.HALF_UP).doubleValue(); // 单份详情的价格;
                } else if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE) {
                    specSB.append("黑白双面，");
                } else if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_COLOR_SINGLE) {
                    specSB.append("彩色单面，");
                } else if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
                    specSB.append("彩色双面，");
                } else if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_PHOTO) {
                    specSB.append("A6，");
                }
                // 缩印
                if (detail.getReprintType().byteValue() == PrintConst.re_print_type_none) {
                    specSB.append("不缩印，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_type_two) {
                    specSB.append("一页二面，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_type_four) {
                    specSB.append("一页四面，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_type_six) {
                    specSB.append("一页六面，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_type_nine) {
                    specSB.append("一页九面，");
                } else if (detail.getReprintType().byteValue() == PrintConst.re_print_photo_default) {
                    specSB.append("默认");
                }
                if (detail.getReprintType().byteValue() != PrintConst.re_print_photo_default)
                    specSB.append(detail.getPageNum()).append("页");
                appDetail.setSpecifications(specSB.toString());
                items.add(appDetail);
                printNum += detail.getNum();
                printPages += (detail.getPageNum() * detail.getNum());
            }
        }
        appOrder.setItems(items);
        appOrder.setPrint_pages(printPages); // 文档总页数
        appOrder.setPrint_num(printNum); // 文档总份数
        appOrder.setPrint_amount(printOrder.getTotalAmount()); // 文档总金额
        appOrder.setDelivery_amount(printOrder.getDeliveryAmount()); // 配送费

        Double couponDiscount = printOrder.getCouponDiscount() == null ? 0.00 : printOrder.getCouponDiscount();
        // 目前折扣金额只有优惠券费用
        appOrder.setDiscount(couponDiscount); // 合计折扣金额=优惠券费用+广告费用+其他折扣
        appOrder.setCoupon_discount(couponDiscount); // 优惠券折扣

        BigDecimal documentAmount = new BigDecimal(printOrder.getTotalAmount());
        BigDecimal deliveryAmount = new BigDecimal(printOrder.getDeliveryAmount());
        BigDecimal disCount = new BigDecimal(-appOrder.getDiscount());// 折扣金额
        /**
         * 免费打印增加订单显示金额
         */
        
        BigDecimal BwSinglePrintPrice = new BigDecimal(bwSingleUnitPrice);//黑白单面
        BigDecimal freeAmount = BwSinglePrintPrice.multiply(new BigDecimal(printOrder.getAdPageNum())).setScale(2, RoundingMode.HALF_UP);//免费价格
        appOrder.setDiscount(new BigDecimal(couponDiscount).add(freeAmount).setScale(2, RoundingMode.HALF_UP).doubleValue()); //设置折扣金额喂免费打印金额＋优惠券
        appOrder.setDoc_amount(new BigDecimal(printOrder.getTotalAmount()).add(freeAmount).setScale(2, RoundingMode.HALF_UP).doubleValue());//文档免费打印前价格
        appOrder.setAd_page_num(printOrder.getAdPageNum());
        appOrder.setFree_amount(freeAmount.doubleValue());
        
        // 订单金额=文档价格+配送费-折扣金额，修改：此处不减折扣金额
        double orderAmount = documentAmount.add(deliveryAmount).add(disCount).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();// 实付金额
        double payAmount = documentAmount.add(deliveryAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();// 订单金额
        if (orderAmount <= 0) {
            orderAmount = 0.01;
        }
        if (payAmount < 0) {
            payAmount = 0;
        }
        appOrder.setItem_amount(payAmount==0?0.01:payAmount); // 订单金额
        appOrder.setOrder_amount(orderAmount); // 实付金额
        appOrder.setAdd_time(printOrder.getCreateTime());
        appOrder.setConfirm_time(printOrder.getCreateTime()); // 打印店没有店长确认订单的功能,由于自动打印,可以把确认时间与下单时间统一
        appOrder.setSend_time(printOrder.getFinishTime()); // 打印店没有送达时间记录,直接传订单完成时间
        appOrder.setAddress(printOrder.getAddress());
        appOrder.setPhone(printOrder.getPhone());
        appOrder.setSend_type(printOrder.getDeliveryType());
        StringBuilder delivery_desc = new StringBuilder();
        if (printOrder.getDeliveryType() == CommonConstant.DELIVERY_TYPE_DORMER) {
            delivery_desc.append("店长配送(");
        } else {
            delivery_desc.append("上门自取(");
        }
        delivery_desc.append(printOrder.getDeliveryTime() == null ? "立即配送" : printOrder.getDeliveryTime());
        delivery_desc.append(")");

        appOrder.setDelivery_desc(delivery_desc.toString());
        appOrder.setRemark(printOrder.getRemark());
        appOrder.setDorm_id(printOrder.getDormId());

        logger.info("返回给前端的订单信息Json:" + JsonUtil.getJsonFromObject(appOrder));

        return appOrder;
    }

    /**
     * 推送到pc端
     */
    public void pushToPcCancel(PrintOrder printOrder) {
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        content.put("orderId", printOrder.getOrderId().toString());
        content.put("action", 2);
        if (printOrder.getPayType() != null && printOrder.getPayType() == 1) {
            content.put("payType", 1);
        }
        if (printOrder.getPayType() != null && printOrder.getPayType() == 6) {
            content.put("payType", 2);
        } else {
            content.put("payType", printOrder.getPayType());
        }
        content.put("shopType", CommonConstant.SHOP_TYPE_PRINT);
        if (printOrder.getPayTime() != null && printOrder.getPayType() != null) {
            content.put("payStatus", 10);
        } else {
            content.put("payStatus", 0);
        }

        param.put("key", "PC");
        param.put("topic", "dorm_" + printOrder.getDormId());
        param.put("content", new JSONObject(content).toString());

        data.put("target", "PUSH");
        data.put("param", param);

        String pushPcString = new JSONObject(data).toString();
        logger.info("支付完成后推送给楼主PC端{}", pushPcString);

        rabbitTemplate.convertAndSend("event-entry", pushPcString);
    }

}
