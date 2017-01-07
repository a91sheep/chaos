package com.store59.printapi.service.createOrder;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.ad.common.api.AdOrderApi;
import com.store59.ad.common.api.UserWeChatService;
import com.store59.ad.common.filter.UserWeChatFilter;
import com.store59.ad.common.model.Ad;
import com.store59.ad.common.model.AdOrder;
import com.store59.ad.common.model.UserWeChat;
import com.store59.base.common.api.DormentryApi;
import com.store59.base.common.model.Dormentry;
import com.store59.coupon.model.businessObject.Coupon;
import com.store59.coupon.remoting.CouponService;
import com.store59.dorm.common.api.DormApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.model.*;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.print.common.model.PrintOrderRecord;
import com.store59.print.common.remoting.PrintOrderRecordService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.ExtensionColumnConstant;
import com.store59.printapi.common.http.HttpClientRequest;
import com.store59.printapi.common.utils.*;
import com.store59.printapi.common.wechat.TokenManager;
import com.store59.printapi.common.wechat.api.MessageAPI;
import com.store59.printapi.model.param.createOrder.PicCreateOrderDetailParam;
import com.store59.printapi.model.param.createOrder.PicCreateOrderParam;
import com.store59.printapi.model.param.wechat.BaseResult;
import com.store59.printapi.model.param.wechat.message.EventMessage;
import com.store59.printapi.model.param.wechat.message.NewsMessage;
import com.store59.printapi.model.param.wechat.message.NewsMessage.Article;
import com.store59.printapi.model.param.wechat.pic.PicWechat;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.UrlResult;
import com.store59.printapi.model.result.app.ShopTimeInfo;
import com.store59.printapi.model.result.app.ShopTimeView;
import com.store59.printapi.model.result.order.OrderCenterDTO;
import com.store59.printapi.model.result.pic.PicCreateOrderResult;
import com.store59.printapi.service.BaseService;
import com.store59.printapi.service.OrderCenterService;
import com.store59.printapi.service.coupon.CouponLocalService;
import com.store59.printapi.service.order.MyOrderService;
import com.store59.user.common.api.UserApi;
import com.store59.user.common.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/14
 * @since 1.0
 */
@SuppressWarnings("restriction")
@Service
public class PicCreateOrderService extends BaseService {

    //    @Autowired
//    private PrintOrderService  printOrderService;
    @Autowired
    private DormShopApi        dormShopService;
    @Autowired
    private RabbitTemplate     rabbitTemplate;
    @Autowired
    private DormentryApi       dormentryService;
    @Autowired
    private CouponLocalService couponLocalService;
    @Autowired
    private AdOrderApi         adOrderApi;
    @Autowired
    private CouponService      couponService;
    @Autowired
    private MyOrderService     myOrderService;
    @Autowired
    private UserApi            userApi;
    @Autowired
    private HttpClientRequest  httpClientRequest;

    @Value("${print.wechat.orderlist.url}")
    private String                          orderListUrl;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    @Value("${uploadUrl}")
    private String                          upLoadUrl;
    @Autowired
    private OSSClient                       ossClient;
    @Value("${aliyun.oss.print.bucketName}")
    private String                          bucketName;
    @Value("${aliyun.oss.print.domainName}")
    private String                          domainName;
    @Value("${print.wechat.pay.openid.url}")
    private String                          payOpenidUrl;

    @Value("${cookie.max.age}")
    private int cookieMaxAge;
    @Autowired
    UserWeChatService userWeCahtService;
    @Value("${print.wechat.appid}")
    private String appid;
    @Autowired
    TokenManager tokenManager;
    @Autowired
    private DormApi            dormApi;
    @Autowired
    private OrderCenterService orderCenterServicel;

    @Autowired
    private PrintOrderRecordService printOrderRecordService;
    @Autowired
    private AppCreateOrderService appCreateOrderService;
    SimpleDateFormat df = new SimpleDateFormat("HHmmss");

    private Logger logger = LoggerFactory.getLogger(PicCreateOrderService.class);

    /**
     * 照片打印购物车接口
     *
     * @param param
     * @param uid
     * @return
     */
    public Datagram<?> cart(PicCreateOrderParam param, Long uid) {
        PicCreateOrderResult createOrderResult = new PicCreateOrderResult();
        // 0不开启 1开启免费打印
        List<PicCreateOrderDetailParam> list = JsonUtil.getObjectFromJson(param.getItems().toString(),
                new TypeReference<List<PicCreateOrderDetailParam>>() {
                });

        if (param.getOpen_ad() == null) {
            param.setOpen_ad((byte) 0);
        }
        // 获取楼栋信息
        Result<Dormentry> dormentryResult = null;
        String dormentryStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + param.getDormentry_id());
        if (StringUtil.isBlank(dormentryStr)) {
            dormentryResult = dormentryService.getDormentry(param.getDormentry_id());
            if (dormentryResult.getStatus() != 0) {
                logger.error("获取用户所在楼栋信息失败,楼栋id=" + param.getDormentry_id());
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            } else {
                valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + param.getDormentry_id(),
                        JsonUtil.getJsonFromObject(dormentryResult.getData()), cookieMaxAge + 10, TimeUnit.SECONDS);
            }
        } else {
            dormentryResult = new Result<Dormentry>();
            dormentryResult.setData(JsonUtil.getObjectFromJson(dormentryStr, Dormentry.class));
        }

        // 获取店铺信息&此处添加缓存
        Result<DormShop> result = null;
        // String shopStr = valueOpsCache.get(param.getShop_id());
        // if (StringUtil.isBlank(shopStr)) {
        result = dormShopService.findByShopId(Integer.valueOf(param.getShop_id()), false, false, true, true);// 包含店铺价格,配送信息
        if (result.getStatus() != 0) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        // } else {
        // result = new Result<DormShop>();
        // result.setData(JsonUtil.getObjectFromJson(shopStr, DormShop.class));
        // }
        // 店铺价格
        List<DormShopPrice> dormShopPrices = result.getData().getDormShopPrices();
        if (dormShopPrices == null || dormShopPrices.size() == 0) {
            throw new BaseException(CommonConstant.STATUS_SHOPPRINCE_ISBLANK, "店铺打印价格为空");
        }
        Map<String, Double> priceMap = new HashMap<>();
        for (DormShopPrice shopPrice : dormShopPrices) {
            priceMap.put(shopPrice.getType() + "", shopPrice.getUnitPrice());
        }

        createOrderResult.setDelivery_type(param.getDelivery_type());
        // 处理详情
        operateDetails(list, priceMap, createOrderResult);

        createOrderResult.setItems(list);

        // 计算配送费
        Map<String, DormShopDelivery> deliveryMap = new HashMap<>();
        for (DormShopDelivery delivery : result.getData().getDormShopDeliveries()) {
            if (delivery.getStatus().byteValue() == 1) { /// 启用
                deliveryMap.put(delivery.getMethod() + "", delivery);
            }
        }

        // 如果创建购物车没有传配送方式,默认为店长配送
        if (param.getSend_type() == null) {
            if (deliveryMap.get(CommonConstant.DELIVERY_TYPE_DORMER + "") != null
                    && deliveryMap.get(CommonConstant.DELIVERY_TYPE_DORMER + "").getStatus().byteValue() == 1) {
                createOrderResult.setSend_type(CommonConstant.DELIVERY_TYPE_DORMER);
            } else {
                createOrderResult.setSend_type(CommonConstant.DELIVERY_TYPE_YOURSELF);
            }
        } else {
            createOrderResult.setSend_type(param.getSend_type());
        }

        if (Byte.valueOf(createOrderResult.getSend_type()).byteValue() == CommonConstant.DELIVERY_TYPE_DORMER) {
            DormShopDelivery dormShopDelivery = deliveryMap.get(createOrderResult.getSend_type() + "");
            if (dormShopDelivery.getThreshold() != null && dormShopDelivery.getThresholdSwitch() != null
                    && 1 == dormShopDelivery.getThresholdSwitch().intValue() && createOrderResult.getDocument_amount()
                    .doubleValue() >= dormShopDelivery.getThreshold().doubleValue()) {
                // 满足免配送费条件
                logger.info("dormShopDelivery.getThreshold():" + dormShopDelivery.getThreshold());
                logger.info("documentAmount.doubleValue():" + createOrderResult.getDocument_amount().doubleValue());
                logger.info("#########满足免配送费条件###########");
                createOrderResult.setDelivery_amount(0.00);
            } else {
                createOrderResult.setDelivery_amount(dormShopDelivery.getCharge());
            }
        } else {
            createOrderResult.setDelivery_amount(0.00);
        }
        // 计算订单下的总金额=文档价格+配送费-优惠券金额
        double freeAdAmounut = 0;
        createOrderResult.setOpen_ad(CommonConstant.AD_STATUS_CLOSE);

        BigDecimal freeAmount = new BigDecimal(freeAdAmounut);
        BigDecimal oldDocumentAmount = new BigDecimal(createOrderResult.getDocument_amount());
        double lastDocumentAmount = oldDocumentAmount.subtract(freeAmount).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        createOrderResult.setDocument_amount(lastDocumentAmount);
        BigDecimal documentAmount = new BigDecimal(lastDocumentAmount); // 文档的金额=原始文档价格
        // -
        // 免费打印价格
        BigDecimal deliveryAmount = new BigDecimal(createOrderResult.getDelivery_amount());

        if (uid.intValue() != 0) {// 已经登录
            Coupon coupon = new Coupon();
            if (!StringUtil.isBlank(param.getCoupon_code())) {
                coupon = couponLocalService.getCouponByCodeAndUid(uid, param.getCoupon_code(),
                        createOrderResult.getDocument_amount());
            } else {
                // 最优优惠券匹配规则
                // 没有传优惠券code,选择最优优惠券 param:uid,文档价格
                // coupon = getBestCouponByUidAndAmount(uid,
                // createOrderResult.getDocument_amount());
            }

            if (coupon == null && !StringUtil.isBlank(param.getCoupon_code())) {
                throw new BaseException(CommonConstant.SATTUS_COUPON_NOT_VALID,
                        CommonConstant.MSG_SATTUS_COUPON_NOT_VALID + param.getCoupon_code());
            }
            if (coupon != null) {
                createOrderResult.setCoupon_code(coupon.getCode());
                createOrderResult.setCoupon_discount(coupon.getDiscount().doubleValue());
            }

            Double couponDiscount = 0.00;
            if (coupon != null) {
                couponDiscount = coupon.getDiscount() == null ? 0.00 : coupon.getDiscount().doubleValue();
            }
            BigDecimal couponAmount = new BigDecimal(-couponDiscount);

            // 优惠券只能减免文档价格
            double lastDocAmount = documentAmount.add(couponAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
            if (lastDocAmount <= 0) {
                lastDocAmount = 0;
                createOrderResult.setCoupon_discount(documentAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
            }

            double totalAmount = BigDecimal.valueOf(lastDocAmount).add(deliveryAmount).setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            if (totalAmount <= 0) {
                totalAmount = 0;
            }
            createOrderResult.setTotal_amount(totalAmount);
        } else {
            // 未登录,不能使用优惠券
            double totalAmount = documentAmount.add(deliveryAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
            if (totalAmount < 0) {
                totalAmount = 0;
            }
            createOrderResult.setTotal_amount(totalAmount);
        }

        /**
         * 处理配送时间相关逻辑
         */
        // 店长配送
        if (CommonConstant.DELIVERY_TYPE_DORMER == createOrderResult.getSend_type()) {
            // 如果前端已经选择了配送时间,则使用前端时间
            if (!StringUtil.isBlank(param.getExpect_time_name())) {
                createOrderResult.setExpect_time_name(param.getExpect_time_name());
                createOrderResult.setExpect_start_time(param.getExpect_start_time());
                createOrderResult.setExpect_end_time(param.getExpect_end_time());
            } else {
                // 对营业时间进行处理
                DeliveryTimeUtil util = new DeliveryTimeUtil();
                Datagram<ShopTimeView> datagram = util.getExpectTimeList(result);
                List<ShopTimeInfo> delivery_times = new ArrayList<>();
                if (datagram.getData() != null) {
                    delivery_times = datagram.getData().getDelivery_times();
                }

                if (delivery_times.size() > 0) {
                    ShopTimeInfo shopTimeInfo = delivery_times.get(0);

                    createOrderResult.setDelivery_type(shopTimeInfo.getType());
                    if (shopTimeInfo.getType() == 2) {
                        // 立即送出
                        createOrderResult.setExpect_time_name(shopTimeInfo.getName());
                    } else {
                        // 预定
                        createOrderResult.setExpect_time_name(shopTimeInfo.getName());
                        createOrderResult.setExpect_start_time(shopTimeInfo.getExpect_start_time());
                        createOrderResult.setExpect_end_time(shopTimeInfo.getExpect_end_time());
                    }
                }
            }
        }

        // 上门自取
        if (CommonConstant.DELIVERY_TYPE_YOURSELF == createOrderResult.getSend_type()) {
            DormShopDelivery dormShopDelivery = deliveryMap.get(createOrderResult.getSend_type() + "");
            // 上门自取,需要获取取货地址和取货时间
            // 取货地址
            createOrderResult.setPick_address(dormShopDelivery.getAddress());

            if (StringUtil.isBlank(param.getExpect_time_name())) {
                // 上门自取需要返回自取时间和地点
                List<DormShopTime> dormShopTimes = result.getData().getDormShopTimes();
                StringBuilder sb = new StringBuilder();
                for (DormShopTime shopTime : dormShopTimes) {
                    if (shopTime.getTimeSwitch().byteValue() == 1) {
                        int left_start = shopTime.getStartTime() % 100;
                        int pre_start = (shopTime.getStartTime() - left_start) / 100;
                        if (left_start < 10) {
                            sb.append(pre_start + ":0" + left_start + "~");
                        } else {
                            sb.append(pre_start + ":" + left_start + "~");
                        }

                        int left_end = shopTime.getEndTime() % 100;
                        int pre_end = (shopTime.getEndTime() - left_end) / 100;
                        if (left_end < 10) {
                            sb.append(pre_end + ":0" + left_end + " ");
                        } else {
                            sb.append(pre_end + ":" + left_end + " ");
                        }
                    }
                }
                createOrderResult.setExpect_time_name(sb.toString());
            } else {
                createOrderResult.setExpect_time_name(param.getExpect_time_name());
            }
        }
        createOrderResult.setShop_id(param.getShop_id());

        return DatagramHelper.getDatagram(createOrderResult, 0, "", true);
    }

    public void operateDetails(List<PicCreateOrderDetailParam> list, Map<String, Double> priceMap,
                               PicCreateOrderResult createOrderResult) {

        int printNum = 0;
        int printPages = 0;
        Double documentAmount = 0.00;
        for (PicCreateOrderDetailParam detail : list) {

            double price = priceMap.get(detail.getPrint_type() + "").doubleValue();
            detail.setPrice(price);
            detail.setOrigin_price(price);
            detail.setAmount(detail.getPrice() * detail.getQuantity());
            printNum += detail.getQuantity();
            printPages += detail.getPrint_page() * detail.getQuantity();
            documentAmount = Double.sum(documentAmount, detail.getAmount());
        }
        createOrderResult.setPrint_num(printNum);
        createOrderResult.setPrint_pages(printPages); // 页数*份数
        createOrderResult.setDocument_amount(documentAmount);
    }

    /**
     * 照片打印下单接口
     *
     * @param param
     * @param uid
     * @return
     */
    public Datagram<?> createOrder(PicCreateOrderParam param, Long uid) {
        List<PicCreateOrderDetailParam> list = JsonUtil.getObjectFromJson(param.getItems(),
                new TypeReference<List<PicCreateOrderDetailParam>>() {
                });

        // 获取楼栋信息
        Result<Dormentry> dormentryResult = null;
        String dormentryStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + param.getDormentry_id());
        if (StringUtil.isBlank(dormentryStr)) {
            dormentryResult = dormentryService.getDormentry(param.getDormentry_id());
            if (dormentryResult.getStatus() != 0) {
                logger.error("获取用户所在楼栋信息失败,楼栋id=" + param.getDormentry_id());
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            } else {
                valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + param.getDormentry_id(),
                        JsonUtil.getJsonFromObject(dormentryResult.getData()), cookieMaxAge + 10, TimeUnit.SECONDS);
            }
        } else {
            dormentryResult = new Result<Dormentry>();
            dormentryResult.setData(JsonUtil.getObjectFromJson(dormentryStr, Dormentry.class));
        }

        int free_page_num = 0;
        ConcurrentHashMap<String, Object> paramMap = new ConcurrentHashMap<>(); // 拼接消息队列--文档处理信息
        paramMap.put("freePageNum", free_page_num);// 消息消费者如果接收的freePageNum==0,说明没有广告
        // paramMap.put("firstPageUrl", store59firstPageAdUrl);//
        // 如果没有选择广告打印,则首页选择59的广告页
        List<Ad> updateAdList = new ArrayList<>();
        // 构建实体数据
        PrintOrder printOrder = new PrintOrder();
        // printOrder.setPayType(param.getPay_type());
        printOrder.setDeviceId(param.getDevice_id());
        printOrder.setSource(param.getSource());
        printOrder.setAdPageNum(free_page_num);
        // printOrder.setAdUnitPrice(Double.valueOf(freePageUnit));
        printOrder.setStatus(PrintConst.ORDER_STATUS_CONFIRMED); // 由于需要生成清单页面,所以订单初始状态都为文档处理中
        printOrder.setDeliveryType(param.getSend_type());
        printOrder.setDeliveryTime(param.getExpect_time_name());
        printOrder.setPhone(param.getPhone());
        printOrder.setDocType(param.getDocType());
//        StringBuilder address_sb = new StringBuilder();
//        address_sb.append(dormentryResult.getData().getAddress1());
//        address_sb.append(dormentryResult.getData().getAddress2());
//        address_sb.append(dormentryResult.getData().getAddress3());
//        address_sb.append(param.getAddress());
        // app用户下单只传寝室号,此处需要拼接用户所在楼栋地址.满足跨楼栋配送需求
        String address = param.getAddress();
        if (address.length() >= 90) {
            address = address.substring(0, 90);
        }
        printOrder.setAddress(appCreateOrderService.replaceAddress(address, dormentryResult.getData().getSiteId()));
        String remark = param.getRemark() == null ? "" : param.getRemark();
        if (remark.length() >= 45) {
            remark = remark.substring(0, 45);
        }
        printOrder.setRemark(remark);
        printOrder.setCreateTime(Long.valueOf(DateUtil.getCurrentTimeSeconds() + ""));
        // 获取店铺信息&此处添加缓存
        Result<DormShop> result = null;
        // String shopStr = valueOpsCache.get(CommonConstant.PRINT_OPTYMIZE_SHOP
        // + param.getShop_id());
        // if (StringUtil.isBlank(shopStr)) {
        result = dormShopService.findByShopId(Integer.valueOf(param.getShop_id()), false, false, true, true);// 包含店铺价格,配送信息
        if (result.getStatus() != 0) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        // } else {
        // valueOpsCache.set(CommonConstant.PRINT_OPTYMIZE_SHOP,
        // JsonUtil.getJsonFromObject(result.getData()),
        // cookieMaxAge, TimeUnit.SECONDS);
        // }
        // } else {
        // result = new Result<DormShop>();
        // result.setData(JsonUtil.getObjectFromJson(shopStr, new
        // TypeReference<DormShop>() {
        // }));
        // }
        /**
         * 此处校验店铺是否开业
         */
        if (result.getData().getStatus().byteValue() == CommonConstant.DORMSHOP_STATUS_REST
                || result.getData().getStatus().byteValue() == CommonConstant.DORMSHOP_STATUS_STOP) {
            throw new BaseException(CommonConstant.STATUS_DORMSHOP_NOT_WORKING, "店铺处于非营业状态!");
        }

        // 店铺价格
        List<DormShopPrice> dormShopPrices = result.getData().getDormShopPrices();

        if (dormShopPrices == null || dormShopPrices.size() == 0) {
            throw new BaseException(CommonConstant.STATUS_SHOPPRINCE_ISBLANK, "店铺打印价格为空");
        }
        Map<String, DormShopPrice> priceMap = new HashMap<>();
        for (DormShopPrice shopPrice : dormShopPrices) {
            priceMap.put(shopPrice.getType() + "", shopPrice);
        }
        // 店铺配送信息
        List<DormShopDelivery> dormShopDeliverys = result.getData().getDormShopDeliveries();

        if (dormShopDeliverys == null || dormShopDeliverys.size() == 0) {
            throw new BaseException(CommonConstant.STATUS_SHOPPRINCE_ISBLANK, "店铺配送方式为空");
        }
        Map<String, DormShopDelivery> deliveryMap = new HashMap<>();
        for (DormShopDelivery shopDelivery : dormShopDeliverys) {
            deliveryMap.put(shopDelivery.getMethod() + "", shopDelivery);
        }

        printOrder.setDormId(result.getData().getDormId());
        printOrder.setUid(uid);

        // 获取用户信息
        if(param.getBuyer_name()==null){
        	Result<User> userResult = userApi.getUser(uid);
	        if (userResult != null && userResult.getStatus() == 0) {
	            printOrder.setUname(userResult.getData().getUname());
	        }        
        }else{
        	printOrder.setUname(param.getBuyer_name());
        }

        List<PrintOrderDetail> printOrderDetailList = new ArrayList<>();
        Double totalAmount = 0.00;
        int index = 1;
        for (PicCreateOrderDetailParam picCreateOrderDetailParam : list) {
            if (StringUtil.isBlank(picCreateOrderDetailParam.getPdf_path())) {
                throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "文件未上传完成,不能下单!");
            }

            if (picCreateOrderDetailParam.getQuantity().intValue() <= 0) {
                throw new BaseException(CommonConstant.STATUS_REQUEST_DATA_INVALID, "文档打印份数不能小于1份!");
            }

            PrintOrderDetail printOrderDetail = new PrintOrderDetail();
            printOrderDetail.setCheckStatus((byte) 0);// 未检测
            printOrderDetail.setIsFirst(CommonConstant.ORDER_FIRST_LIST_0);
            // 计算订单详情的价钱
            Byte printId = picCreateOrderDetailParam.getPrint_type();
            DormShopPrice dormShopPrice = priceMap.get(printId + "");

            Double unitPrice = dormShopPrice.getUnitPrice(); // 打印单张价格
            // 获取转换后的pdf计算页数
            Integer pdf_pageNum = 1;
            // Integer pdf_pageNum = picCreateOrderDetailParam.getPage();
            // 单价*pdf页数*份数
            Double amount = 0.00;
            BigDecimal bd1 = (BigDecimal.valueOf(unitPrice)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bd2 = (BigDecimal.valueOf(pdf_pageNum)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bd3 = (BigDecimal.valueOf(picCreateOrderDetailParam.getQuantity())).setScale(2,
                    RoundingMode.HALF_UP);
            amount = bd1.multiply(bd2).multiply(bd3).setScale(2, RoundingMode.HALF_UP).doubleValue();
            picCreateOrderDetailParam.setPage(pdf_pageNum);

            printOrderDetail.setAmount(amount.floatValue());
            String fileName = index + "_" + picCreateOrderDetailParam.getFile_name();
            if (fileName.length() >= 45) {
                String ext = fileName
                        .substring(fileName.lastIndexOf(".") < 0 ? fileName.length() : fileName.lastIndexOf("."));
                fileName = fileName.substring(0, 35) + (ext.length() > 10 ? ext.substring(0, 10) : ext);
            }
            printOrderDetail.setFileName(fileName);
            index += 1;
            printOrderDetail.setStatus(CommonConstant.PDF_OPERATE_FINISH);
            printOrderDetail.setNum(picCreateOrderDetailParam.getQuantity());
            printOrderDetail.setPageNum(picCreateOrderDetailParam.getPage());
            printOrderDetail.setPrintType(picCreateOrderDetailParam.getPrint_type());
            printOrderDetail.setReprintType(picCreateOrderDetailParam.getReduced_type());
            // 转换前后文件的存储
            printOrderDetail.setUrl(picCreateOrderDetailParam.getPdf_path());
            printOrderDetail.setPdfMD5(picCreateOrderDetailParam.getPdf_md5());
            printOrderDetail.setPdfSize(picCreateOrderDetailParam.getPdf_size());
            printOrderDetail.setSourceMD5(picCreateOrderDetailParam.getDoc_md5());
            printOrderDetail.setSourceUrl(picCreateOrderDetailParam.getDoc_path());
            printOrderDetailList.add(printOrderDetail);
            totalAmount += amount;
        }

        BigDecimal total = new BigDecimal(totalAmount);
        totalAmount = total.setScale(2, RoundingMode.HALF_UP).doubleValue();
        // 计算配送费
        DormShopDelivery dormShopDelivery = deliveryMap.get(param.getSend_type() + "");
        logger.info("-----------文档总金额：" + totalAmount + "------文档配送方式" + param.getSend_type() + "----threshold"
                + JsonUtil.getJsonFromObject(dormShopDelivery));
        if (Byte.valueOf(param.getSend_type()).byteValue() == CommonConstant.DELIVERY_TYPE_DORMER) {
            if (dormShopDelivery.getThreshold() != null && dormShopDelivery.getThresholdSwitch() != null
                    && 1 == dormShopDelivery.getThresholdSwitch().intValue()
                    && totalAmount.doubleValue() >= dormShopDelivery.getThreshold().doubleValue()) {
                // 满足免配送费条件
                logger.info("dormShopDelivery.getThreshold():" + dormShopDelivery.getThreshold());
                logger.info("totalAmount.doubleValue():" + totalAmount.doubleValue());
                logger.info("#########满足免配送费条件###########");
                printOrder.setDeliveryAmount(0.00);
            } else {
                printOrder.setDeliveryAmount(dormShopDelivery.getCharge());
            }
        } else {
            printOrder.setDeliveryAmount(0.00);
        }
        printOrder.setDetails(printOrderDetailList);

        // 如果有使用优惠券
        if (!StringUtil.isBlank(param.getCoupon_code())) {
            // 文档费用 比较 优惠券使用门槛
            Coupon coupon = couponLocalService.getCouponByCodeAndUid(uid, param.getCoupon_code(), totalAmount);
            if (coupon != null) {
                printOrder.setCouponCode(coupon.getCode());
                printOrder.setCouponDiscount(coupon.getDiscount().doubleValue());
            } else {
                throw new BaseException(CommonConstant.SATTUS_COUPON_NOT_VALID,
                        CommonConstant.MSG_SATTUS_COUPON_NOT_VALID + param.getCoupon_code());
            }
        }

        // 文档价格减去免费打印的价格
        BigDecimal docAmount = new BigDecimal(totalAmount);
        BigDecimal freeNum = new BigDecimal(free_page_num);
        // 目前只考虑黑白单面的价格,后期需要增加黑白双面
        double bwSingleUnitPrice = priceMap.get(PrintConst.PRINT_TYPE_BW_SINGLE + "").getUnitPrice();
        BigDecimal BwSinglePrintPrice = new BigDecimal(bwSingleUnitPrice);// 黑白单面
        BigDecimal freeAmount = BwSinglePrintPrice.multiply(freeNum).setScale(2, RoundingMode.HALF_UP);
        totalAmount = docAmount.subtract(freeAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
        printOrder.setTotalAmount(totalAmount); // 最终文档价格

        BigDecimal docuAomunt = new BigDecimal(totalAmount);
        BigDecimal deliveryAomunt = new BigDecimal(printOrder.getDeliveryAmount());
        double couponDiscount = printOrder.getCouponDiscount() == null ? 0 : printOrder.getCouponDiscount();
        BigDecimal couponAmount = new BigDecimal(-couponDiscount);
        // 优惠券只能减免文档价格
        double lastDocAmount = docuAomunt.add(couponAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
        if (lastDocAmount <= 0) {
            lastDocAmount = 0;
            printOrder.setCouponDiscount(totalAmount);
        }

        double payAmount = BigDecimal.valueOf(lastDocAmount).add(deliveryAomunt).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        if (payAmount <= 0) {
            // 为了获取支付账号信息,修改0元支付 为 0.01元支付
            payAmount = BigDecimal.valueOf(0.01).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 设置订单支付状态
            // printOrder.setPayType((byte) 0); // 0元支付,货到付款
            // printOrder.setPayTime((long) DateUtil.getCurrentTimeSeconds());
            // printOrder.setTradeNo("");
        }
        printOrder.setAdUnitPrice(0.00);
        Integer siteId = dormentryResult.getData().getSiteId();
        logger.info("********插入前,订单详情:" + JsonUtil.getJsonFromObject(printOrder));
//		Result<PrintOrder> result2 = printOrderService.insert(printOrder);
        //接入订单中心
        OrderCenterDTO orderCenterDTO = new OrderCenterDTO();
        orderCenterDTO.setPrintOrder(printOrder);
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
        orderCenterDTO.setSellerId(dormResult.getData().getUid() + "");
        orderCenterDTO.setSellerSiteId(dormResult.getData().getSiteId());
        orderCenterDTO.setSellerDormentryId(dormResult.getData().getDormentryId());
        orderCenterDTO.setSellerAddress(dormResult.getData().getDeliveryAddress());  // 此处地址需要验证正确性
        orderCenterDTO.setSellerPhone(dormResult.getData().getPhone());
        orderCenterDTO.setSellerName(dormResult.getData().getName());
        orderCenterDTO.setSellerShopId(String.valueOf(result.getData().getShopId()));
        OrderCenterDTO retDTO = orderCenterServicel.createOrder(orderCenterDTO);
        logger.info("创建订单结果:" + JsonUtil.getJsonFromObject(retDTO));

        PrintOrder printOrder1 = retDTO.getPrintOrder();
        printOrder1.setPayType(param.getPay_type());
        if (payAmount <= 0) {
            printOrder1.setPayType((byte) 0); // 0元支付,货到付款
        }
        // 更新订单广告流水记录表
        if (updateAdList.size() > 0) {
            List<AdOrder> adOrderList = new ArrayList<>();
            for (Ad ad : updateAdList) {
                AdOrder adOrder = new AdOrder();
                adOrder.setAdId(ad.getId());
                adOrder.setOrderId(retDTO.getPrintOrder().getOrderId());
                adOrderList.add(adOrder);
            }
            Thread thread = new Thread() {
                public void run() {
                    Result<List<AdOrder>> listResult = adOrderApi.insertAdOrderList(adOrderList);
                    if (listResult.getStatus() != 0) {
                        logger.error("插入广告订单流水记录失败,订单号:{}", retDTO.getPrintOrder().getOrderId());
                    }
                }
            };
            thread.start();
        }
        // 异步消息推送店长端
        Thread thread1 = new Thread() {
            public void run() {
                logger.info("*******异步推送订单信息到店长端");
                pushToPc(retDTO.getPrintOrder());
                logger.info("*******成功推送订单信息到店长端!!!!!!!");
            }
        };
        thread1.start();

        // 新增的订单需要记录在文档待检测记录表
        Thread thread4 = new Thread() {
            public void run() {
                PrintOrderRecord printOrderRecord = new PrintOrderRecord();
                printOrderRecord.setOrderId(printOrder1.getOrderId() + "");
                printOrderRecordService.insert(printOrderRecord);
            }
        };
        thread4.start();

//        if (!StringUtil.isBlank(param.getCoupon_code())) {
//            // 异步修改优惠券状态为已使用
//            // Thread thread2 = new Thread() {
//            // public void run() {
//            boolean useResult = couponLocalService.useCoupon_app(uid, param.getCoupon_code(),
//                    retDTO.getPrintOrder().getOrderId(), retDTO.getPrintOrder().getTotalAmount(), siteId, param.getShop_id());
//            if (useResult) {
//                logger.info("成功更新优惠券为已使用,优惠券code:{},订单号:{}", param.getCoupon_code(),
//                        retDTO.getPrintOrder().getOrderId());
//            } else {
//                logger.error("哦,low!更新优惠券为已使用失败了!!!!优惠券code:{},订单号:{}", param.getCoupon_code(),
//                        retDTO.getPrintOrder().getOrderId());
//            }
//            // }
//            // };
//            // thread2.start();
//        }
        logger.info("照片下单流程结束，开始返回数据：");
        // 如果是微信则推送下单成功消息
        new Thread() {
            public void run() {
                String openid = valueOpsCache.get(CommonConstant.WECHAT_UID_OPENID_PREFIX + String.valueOf(uid));
                if (!StringUtil.isBlank(openid)) {
                    String picStr = valueOpsCache.get(CommonConstant.WECHAT_OPENID_PREFIX + openid);
                    logger.info("---开始推送消息：" + openid + "---" + picStr);
                    if (!StringUtil.isBlank(picStr)) {
                        PicWechat pic = JsonUtil.getObjectFromJson(picStr, new TypeReference<PicWechat>() {
                        });
                        List<Article> articles = new ArrayList<Article>();
                        Article art = new NewsMessage.Article("您已成功下单,共" + list.size() + "张照片待打印",
                                "订单号" + retDTO.getPrintOrder().getOrderId() + " 点击查看订单",
                                String.format(payOpenidUrl, URLEncoder.encode(orderListUrl + "?uid=" + uid)),
                                list.get(0).getPdf_path());
                        articles.add(art);
                        NewsMessage newsMsg = new NewsMessage(openid, articles);
                        BaseResult rs = MessageAPI.messageCustomSend(tokenManager.getToken(appid), newsMsg);
                        logger.info("推送结果：" + JsonUtil.getJsonFromObject(rs));
                        valueOpsCache.getOperations().delete(CommonConstant.WECHAT_OPENID_PREFIX + openid);
                    }
                }
            }
        }.start();
        Thread thread5 = new Thread() {
            public void run() {
            	appCreateOrderService.pushApp(NotifyEnum.NEW,retDTO);
            }
        };
        thread5.start();
        //此处 REFUNDSTATUS 为空,先给2值,对应无任何状态
        return DatagramHelper.getDatagram(myOrderService.getAppOrder(printOrder1,
                        retDTO.getExtension().get(ExtensionColumnConstant.REFUNDSTATUS) == null ?
                                2 : Byte.valueOf(retDTO.getExtension().get(ExtensionColumnConstant.REFUNDSTATUS)),true,param.getApp_version()),
                CommonConstant.GLOBAL_STATUS_SUCCESS, CommonConstant.GLOBAL_STATUS_SUCCESS_MSG, true);
    }

    /**
     * 推送到pc端
     */
    private void pushToPc(PrintOrder printOrder) {
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        content.put("orderId", printOrder.getOrderId());
        content.put("action", 1);
        content.put("payType", 1);
        content.put("shopType", CommonConstant.SHOP_TYPE_PRINT);
        content.put("payStatus", 0);

        param.put("key", "PC");
        param.put("topic", "dorm_" + printOrder.getDormId());
        param.put("content", new JSONObject(content).toString());

        data.put("target", "PUSH");
        data.put("param", param);

        String pushPcString = new JSONObject(data).toString();
        logger.info("下单后推送给楼主PC端{}", pushPcString);

        rabbitTemplate.convertAndSend("event-entry", pushPcString);
    }

    public Datagram<?> getCartList(String uid, String shopId) {
        String openid = valueOpsCache.get(CommonConstant.WECHAT_UID_OPENID_PREFIX + uid);
        if (StringUtil.isBlank(openid)) {
            // 此处查询数据库判断是否能拿到openid
            UserWeChatFilter filter = new UserWeChatFilter();
            filter.setSource((byte) 1);
            filter.setUid(Long.parseLong(uid));
            Result<List<UserWeChat>> list = userWeCahtService.findByFilter(filter);
            if (list.getStatus() == 0 && list.getData().size() > 0) {
                openid = list.getData().get(0).getOpenId().toString();
            } else {
                return DatagramHelper.getDatagram(CommonConstant.STATUS_H5_CART_LIST,
                        CommonConstant.MSG_H5_OPENID_NOT_FOUND);
            }
        }
        PicWechat pic = null;
        String picStr = valueOpsCache.get(CommonConstant.WECHAT_OPENID_PREFIX + openid);
        if (!StringUtil.isBlank(picStr)) {
            pic = JsonUtil.getObjectFromJson(picStr, new TypeReference<PicWechat>() {
            });
        } else {
            return DatagramHelper.getDatagram(CommonConstant.STATUS_H5_CART_LIST, CommonConstant.MSG_H5_CART_LIST);
        }
        // 获取店铺信息，为了价格
        Result<DormShop> result = null;
        // String shopStr = valueOpsCache.get(CommonConstant.PRINT_OPTYMIZE_SHOP
        // + shopId);
        // if (StringUtil.isBlank(shopStr)) {
        result = dormShopService.findByShopId(Integer.valueOf(shopId), false, false, true, true);// 包含店铺价格,配送信息
        // if (result.getStatus() != 0) {
        // throw new
        // BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
        // CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        // } else {
        // valueOpsCache.set(CommonConstant.PRINT_OPTYMIZE_SHOP + shopId,
        // JsonUtil.getJsonFromObject(result.getData()), cookieMaxAge,
        // TimeUnit.SECONDS);
        // }
        // } else {
        // result = new Result<DormShop>();
        // result.setData(JsonUtil.getObjectFromJson(shopStr, new
        // TypeReference<DormShop>() {
        // }));
        // }
        logger.info("获取的店铺价格：" + JsonUtil.getJsonFromObject(result));
        // 店铺价格
        List<DormShopPrice> dormShopPrices = result.getData().getDormShopPrices();
        if (dormShopPrices == null || dormShopPrices.size() == 0) {
            throw new BaseException(CommonConstant.STATUS_SHOPPRINCE_ISBLANK, "店铺打印价格为空");
        }
        Map<String, Double> priceMap = new HashMap<>();
        for (DormShopPrice shopPrice : dormShopPrices) {
            priceMap.put(shopPrice.getType() + "", shopPrice.getUnitPrice());
        }

        List<PrintOrderDetail> list = new LinkedList<PrintOrderDetail>();
        for (EventMessage event : pic.getList()) {
            PrintOrderDetail pd = new PrintOrderDetail();
            pd.setAmount(priceMap.get(String.valueOf(DormShopPrice.TYPE_PHOTO_PRINT)) == null ? null
                    : Float.parseFloat(priceMap.get(String.valueOf(DormShopPrice.TYPE_PHOTO_PRINT)).toString()));
            pd.setUrl(event.getPicUrl());
            pd.setNum(1);
            pd.setPageNum(1);
            pd.setFileName(df.format(new Date(Long.parseLong(event.getCreateTime().toString()) * 1000L)));
            list.add(pd);
        }
        return DatagramHelper.getDatagram(list, CommonConstant.GLOBAL_STATUS_SUCCESS,
                CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
    }

    /**
     * 截图上传
     *
     * @param file
     * @param sourFileName
     * @param ext
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public Datagram<UrlResult> fileupload(String file, String sourFileName, String ext) throws IOException {
        file = file.replace(String.format("data:image/%s;base64,", ext), "");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(file);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {// 调整异常数据
                b[i] += 256;
            }
        }
        BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(b);
        String key = FileUtil.upload(multipartFile, "image/" + ext, sourFileName, ossClient, bucketName);
        String doc_path = domainName + "/" + key;
        String doc_md5 = key.substring(0, key.lastIndexOf("."));
        logger.info("截图阿里云地址：" + doc_path);
        /**
         * 非pdf文档,走文件转换接口
         */
        // String ret = "";
        // try {
        // ret = httpClientRequest.UploadFilePost(upLoadUrl,doc_path );
        // } catch (BaseException e) {
        // throw new CommonException(e.getMsg());
        // }

        /**
         * 原始文档转换成pdf之后,
         */
        // if (!ret.isEmpty() && ret.contains("MD5")) {
        // ret = ret.replace("MD5", "md");
        // }
        // FileUploadResult result = JsonUtil.getObjectFromJson(ret,
        // FileUploadResult.class);

        UrlResult urlResult = new UrlResult();
        // urlResult.setPage(result.getPage());
        // urlResult.setPdf_path(result.getPdf_path());
        // urlResult.setPdf_md5(result.getMd());
        urlResult.setDoc_file_name(sourFileName);
        urlResult.setDoc_path(doc_path);
        urlResult.setDoc_md5(doc_md5);
        logger.info("******文件上传结果:" + JsonUtil.getJsonFromObject(urlResult));
        return DatagramHelper.getDatagramWithSuccess(urlResult);
    }

    /**
     * 推送到pc端
     */
    public void pushToPcConvert(PrintOrder printOrder) {
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        content.put("orderId", printOrder.getOrderId());
        content.put("action", 3);
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

        rabbitTemplate.convertAndSend("event-entry", pushPcString);
    }
}
