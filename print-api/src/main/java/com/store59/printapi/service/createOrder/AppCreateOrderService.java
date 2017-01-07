package com.store59.printapi.service.createOrder;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lowagie.text.DocumentException;
import com.store59.ad.common.api.AdApi;
import com.store59.ad.common.api.AdOrderApi;
import com.store59.ad.common.model.Ad;
import com.store59.ad.common.model.AdOrder;
import com.store59.base.common.api.DormentryApi;
import com.store59.base.common.api.SiteApi;
import com.store59.base.common.model.Dormentry;
import com.store59.base.common.model.Site;
import com.store59.coupon.model.businessObject.Coupon;
import com.store59.dorm.common.api.DormApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.api.DormShopDeliveryApi;
import com.store59.dorm.common.api.DormShopPriceApi;
import com.store59.dorm.common.data.ConstDorm;
import com.store59.dorm.common.model.Dorm;
import com.store59.dorm.common.model.DormShop;
import com.store59.dorm.common.model.DormShopDelivery;
import com.store59.dorm.common.model.DormShopPrice;
import com.store59.event.push.common.enums.PushTargetEnum;
import com.store59.event.push.common.helper.PushEntityHelper;
import com.store59.event.push.common.model.PushContent;
import com.store59.event.push.common.model.PushEntity;
import com.store59.event.push.common.rabbit.RabbitSender;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.order.common.service.dto.OrderDTO;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.print.common.model.PrintOrderRecord;
import com.store59.print.common.model.ad.AdOrderRelation;
import com.store59.print.common.remoting.AdOrderRelationService;
import com.store59.print.common.remoting.PrintOrderRecordService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.ExtensionColumnConstant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.http.HttpClientRequest;
import com.store59.printapi.common.utils.BASE64DecodedMultipartFile;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.DateUtil;
import com.store59.printapi.common.utils.FileUtil;
import com.store59.printapi.common.utils.NotifyEnum;
import com.store59.printapi.common.utils.PDFUtil;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.model.param.createOrder.AppCreateOrderDetailParam;
import com.store59.printapi.model.param.createOrder.AppCreateOrderParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.PDFConverResult;
import com.store59.printapi.model.result.order.AdFreeResult;
import com.store59.printapi.model.result.order.OrderCenterDTO;
import com.store59.printapi.service.OrderCenterService;
import com.store59.printapi.service.coupon.CouponLocalService;
import com.store59.printapi.service.order.MyOrderService;
import com.store59.user.common.api.UserApi;
import com.store59.user.common.model.User;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/24
 * @since 1.0
 */
@Service
public class AppCreateOrderService {

    @Autowired
    private HttpClientRequest               httpClientRequest;
    @Value("${uploadUrl}")
    private String                          upLoadUrl;
    //    @Autowired
//    private PrintOrderService               printOrderService;
    @Autowired
    private DormShopDeliveryApi             dormShopDeliveryApi;
    @Autowired
    private DormShopPriceApi                dormShopPriceApi;
    @Autowired
    private DormentryApi                    dormentryService;
    @Autowired
    private OSSClient                       ossClient;
    @Value("${aliyun.oss.print.bucketName}")
    private String                          bucketName;
    @Value("${aliyun.oss.print.domainName}")
    private String                          domainName;
    @Autowired
    private DormShopApi                     dormShopService;
    @Autowired
    private RabbitTemplate                  rabbitTemplate;
    @Autowired
    private CouponLocalService              couponLocalService;
    @Autowired
    private UserApi                         userApi;
    @Autowired
    private MyOrderService                  myOrderService;
    @Value("${rabbitmq.event.print.entry}")
    private String                          queueName;
    @Value("${store59.footer.num.limit}")
    private int                             footerNumLimit;
    @Value("${free.page.unit}")
    private double                          freePageUnit;
    @Value("${store59.first.page.ad}")
    private String                          store59firstPageAdUrl;
    @Autowired
    private AdApi                           adApi;
    @Autowired
    private AdOrderApi                      adOrderApi;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    @Autowired
    private DormApi                         dormApi;
    @Autowired
    private AdService adService;
    @Autowired
    private AdOrderRelationService arlService;
    
    @Value("${cookie.max.age}")
    private int cookieMaxAge;

    @Autowired
    private OrderCenterService      orderCenterServicel;
    @Autowired
    private PrintOrderRecordService printOrderRecordService;

    private Logger logger = LoggerFactory.getLogger(AppCreateOrderService.class);
	@Autowired
	SiteApi siteApi;
    public PDFConverResult PDFMerger(InputStream inputStream, byte reprintType, String sourFileName) {
        PDFConverResult pdfConverResult = new PDFConverResult();
        //上传前判断
        int w = 1;  //缩印变量
        int h = 1;  //缩印变量
        if (PrintConst.re_print_type_two == reprintType) {
            w = 2;
            h = 1;
        } else if (PrintConst.re_print_type_four == reprintType) {
            w = 2;
            h = 2;
        } else if (PrintConst.re_print_type_six == reprintType) {
            w = 3;
            h = 2;
        } else if (PrintConst.re_print_type_nine == reprintType) {
            w = 3;
            h = 3;
        }
        try {
            byte[] river = new byte[0];
            try {
                river = PDFUtil.manipulatePdf(inputStream, w, h);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(river);
            //---------缩印文件保存---------
            String conver_key = FileUtil.upload(multipartFile, "application/pdf", sourFileName, ossClient, bucketName);
            logger.info("缩印文件保存成功,conver_key:" + conver_key);
            String pdf_path = domainName + "/" + conver_key;
            String pdf_md5 = conver_key.substring(0, conver_key.lastIndexOf("."));

            pdfConverResult.setPdf_path(pdf_path);
            pdfConverResult.setPdf_md5(pdf_md5);

            //获取转换后的pdf计算页数
            PDDocument doc = null;

            try {
                //此处的流是否可以使用上传之前的?待测试
                doc = PDDocument.load(multipartFile.getInputStream());
                pdfConverResult.setPdf_number(doc.getNumberOfPages());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return pdfConverResult;
    }

    public Datagram createOrder_App(AppCreateOrderParam param, Long uid) {
        List<AppCreateOrderDetailParam> list = JsonUtil.getObjectFromJson(param.getItems(), new TypeReference<List<AppCreateOrderDetailParam>>() {
        });

        //获取楼栋信息
        Result<Dormentry> dormentryResult = null;
        String dormentryStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + param.getDormentry_id());
        if (StringUtil.isBlank(dormentryStr)) {
            dormentryResult = dormentryService.getDormentry(param.getDormentry_id());
            if (dormentryResult.getStatus() != 0) {
                logger.error("获取用户所在楼栋信息失败,楼栋id=" + param.getDormentry_id());
                throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            } else {
                valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + param.getDormentry_id(), JsonUtil.getJsonFromObject(dormentryResult.getData()), cookieMaxAge + 10,
                        TimeUnit.SECONDS);
            }
        } else {
            dormentryResult = new Result<Dormentry>();
            dormentryResult.setData(JsonUtil.getObjectFromJson(dormentryStr, Dormentry.class));
        }

        int bw_page_num = 0;
        int free_page_num = 0;
        ConcurrentHashMap<String, Object> paramMap = new ConcurrentHashMap<>();  //拼接消息队列--文档处理信息
        paramMap.put("freePageNum", free_page_num);//消息消费者如果接收的freePageNum==0,说明没有广告
        paramMap.put("firstPageUrl", store59firstPageAdUrl);//如果没有选择广告打印,则首页选择59的广告页
//        List<Ad> updateAdList = new ArrayList<>();
        List<AdOrderRelation> updateAdList =new ArrayList<>();
        /**
         * 广告打印优先使用黑白单面,由于现在没有双面,后期开放双面,需要对免费价格重新计算!!!!!
         */
        boolean is_consistent=true;
        if (param.getOpen_ad().byteValue() == CommonConstant.AD_STATUS_OPEN) {
        	//TODO
        	AdFreeResult adFreeResult=adService.calFreePagesApp(param, dormentryResult.getData().getSiteId(), paramMap, uid,2);
        	free_page_num=adFreeResult.getFreePages();
        	updateAdList=adFreeResult.getList();
        	if(param.getCart_free()!=null&&free_page_num!=param.getCart_free().intValue()){
        		is_consistent=false;
        	}
        }
        //构建实体数据
        PrintOrder printOrder = new PrintOrder();
//        printOrder.setPayType(param.getPay_type());
        printOrder.setDeviceId(param.getDevice_id());
        printOrder.setSource(param.getSource());
        printOrder.setAdPageNum(free_page_num);
        printOrder.setAdUnitPrice(Double.valueOf(freePageUnit));
        printOrder.setStatus(PrintConst.ORDER_STATUS_INIT);  //由于需要生成清单页面,所以订单初始状态都为文档处理中
        printOrder.setDeliveryType(param.getSend_type());
        printOrder.setDeliveryTime(param.getExpect_time_name());
        printOrder.setPhone(param.getPhone());
        printOrder.setDocType(param.getDoc_type());
//        StringBuilder address_sb = new StringBuilder();
//        address_sb.append(dormentryResult.getData().getAddress1());
//        address_sb.append(dormentryResult.getData().getAddress2());
//        address_sb.append(dormentryResult.getData().getAddress3());
//        address_sb.append(param.getAddress());
        //app用户下单只传寝室号,此处需要拼接用户所在楼栋地址.满足跨楼栋配送需求
//        String address = address_sb.toString();
        String address = param.getAddress();
        if (address.length() >= 90) {
            address = address.substring(0, 90);
        }
        printOrder.setAddress(replaceAddress(address, dormentryResult.getData().getSiteId()));
        String remark = param.getRemark() == null ? "" : param.getRemark();
        if (remark.length() >= 45) {
            remark = remark.substring(0, 45);
        }
        printOrder.setRemark(remark);
        printOrder.setCreateTime(Long.valueOf(DateUtil.getCurrentTimeSeconds() + ""));
        //获取店铺信息&此处添加缓存
        Result<DormShop> result = null;
//        String shopStr=valueOpsCache.get(CommonConstant.PRINT_OPTYMIZE_SHOP+param.getShop_id());
//        if(StringUtil.isBlank(shopStr)){
        result = dormShopService.findByShopId(Integer.valueOf(param.getShop_id()), false, false, true, true);//包含店铺价格,配送信息
		if(result.getData().getStatus()==ConstDorm.DORM_SHOP_STATUS_CLOSE){
			throw new AppException(CommonConstant.STATUS_DORM_CLOSED_ERROR,CommonConstant.MSG_DORM_CLOSED_ERROR);
		}
        if (result.getStatus() != 0) {
            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
//        	}else{
//        		valueOpsCache.set(CommonConstant.PRINT_OPTYMIZE_SHOP, JsonUtil.getJsonFromObject(result.getData()),cookieMaxAge,TimeUnit.SECONDS);
//        	}
//        	logger.info("店铺id:" + param.getShop_id() + ",data: " + JsonUtil.getJsonFromObject(result));
//        }else{
//        	result=new Result<DormShop>();
//        	result.setData(JsonUtil.getObjectFromJson(shopStr, new TypeReference<DormShop>(){}));
//        }
        /**
         * 此处校验店铺是否开业
         */
        if (result.getData().getStatus().byteValue() == CommonConstant.DORMSHOP_STATUS_REST ||
                result.getData().getStatus().byteValue() == CommonConstant.DORMSHOP_STATUS_STOP) {
            throw new AppException(CommonConstant.STATUS_DORMSHOP_NOT_WORKING,
                    "店铺处于非营业状态!");
        }

        //店铺价格
        List<DormShopPrice> dormShopPrices = result.getData().getDormShopPrices();

        if (dormShopPrices == null || dormShopPrices.size() == 0) {
            throw new AppException(CommonConstant.STATUS_SHOPPRINCE_ISBLANK, "店铺打印价格为空");
        }
        Map<String, DormShopPrice> priceMap = new HashMap<>();
        for (DormShopPrice shopPrice : dormShopPrices) {
            priceMap.put(shopPrice.getType() + "", shopPrice);
        }
        //店铺配送信息
        List<DormShopDelivery> dormShopDeliverys = result.getData().getDormShopDeliveries();

        if (dormShopDeliverys == null || dormShopDeliverys.size() == 0) {
            throw new AppException(CommonConstant.STATUS_SHOPPRINCE_ISBLANK, "店铺配送方式为空");
        }
        Map<String, DormShopDelivery> deliveryMap = new HashMap<>();
        for (DormShopDelivery shopDelivery : dormShopDeliverys) {
            deliveryMap.put(shopDelivery.getMethod() + "", shopDelivery);
        }

        printOrder.setDormId(result.getData().getDormId());
        printOrder.setUid(uid);

        //获取用户信息
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
        for (AppCreateOrderDetailParam appCreateOrderDetailParam : list) {
            if (StringUtil.isBlank(appCreateOrderDetailParam.getPdf_path())) {
                throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "文件未上传完成,不能下单!");
            }

            if (appCreateOrderDetailParam.getQuantity().intValue() <= 0) {
                throw new AppException(CommonConstant.STATUS_REQUEST_DATA_INVALID, "文档打印份数不能小于1份!");
            }

            PrintOrderDetail printOrderDetail = new PrintOrderDetail();
            printOrderDetail.setCheckStatus((byte) 0);//未检测
            printOrderDetail.setIsFirst(CommonConstant.ORDER_FIRST_LIST_0);
            //如果文档需要缩印,则对文档需要进行缩印操作----此处异步进行,稍后加上
            if (PrintConst.re_print_type_none != appCreateOrderDetailParam.getReduced_type().byteValue()) {
                printOrderDetail.setStatus(CommonConstant.PDF_OPERATE_ING);
            } else {
                printOrderDetail.setStatus(CommonConstant.PDF_OPERATE_FINISH);
            }
            //计算订单详情的价钱
            Byte printId = appCreateOrderDetailParam.getPrint_type();
            DormShopPrice dormShopPrice = priceMap.get(printId + "");

            Double unitPrice = dormShopPrice.getUnitPrice();  //打印单张价格
            //获取转换后的pdf计算页数
            Integer pdf_pageNum = 0;
            String pdf_pageNumStr = valueOpsCache.get(appCreateOrderDetailParam.getPdf_md5());
            if (StringUtil.isBlank(pdf_pageNumStr)) {
//            	throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "上传文件已超时！");
                String pdf_key = appCreateOrderDetailParam.getPdf_path().substring(appCreateOrderDetailParam.getPdf_path().lastIndexOf("pdf/"));
                OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
                //获取转换后的pdf计算页数
                PDDocument doc = null;
                try {
                    doc = PDDocument.load(ossObject.getObjectContent());
                } catch (IOException e) {
                    logger.error("获取pdf页数失败,pdf_url:" + appCreateOrderDetailParam.getPdf_path());
                    e.printStackTrace();
                }
                logger.error("App:"+appCreateOrderDetailParam.getPdf_path());
                pdf_pageNum = doc.getNumberOfPages();  //pdf后台计算页数,防止被攻击
            } else {
                logger.info("从缓存获取了页数" + pdf_pageNumStr);
                pdf_pageNum = Integer.parseInt(pdf_pageNumStr);
            }

//            Integer pdf_pageNum = appCreateOrderDetailParam.getPage();  //原始文档缩印处理后的pdf页数
            Double final_pageNum = 0.00;
            //是否缩印
            if (appCreateOrderDetailParam.getReduced_type() == PrintConst.re_print_type_two) {  //二合一
                final_pageNum = Math.ceil(pdf_pageNum / 2.0);
            }
            if (appCreateOrderDetailParam.getReduced_type() == PrintConst.re_print_type_four) {  //四合一
                final_pageNum = Math.ceil(pdf_pageNum / 4.0);
            }
            if (appCreateOrderDetailParam.getReduced_type() == PrintConst.re_print_type_six) {  //六合一
                final_pageNum = Math.ceil(pdf_pageNum / 6.0);
            }
            if (appCreateOrderDetailParam.getReduced_type() == PrintConst.re_print_type_nine) {  //九合一
                final_pageNum = Math.ceil(pdf_pageNum / 9.0);
            }
            //考虑单双页
            if (appCreateOrderDetailParam.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE
                    || appCreateOrderDetailParam.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
                if (appCreateOrderDetailParam.getReduced_type().byteValue() == PrintConst.re_print_type_none) {
                    final_pageNum = Math.ceil(pdf_pageNum / 2.0);
                } else {
                    final_pageNum = Math.ceil(final_pageNum / 2.0);
                }
            }
            //单价*pdf页数*份数
            Double amount = 0.00;
            if (final_pageNum.doubleValue() == 0) {
                BigDecimal bd1 = (BigDecimal.valueOf(unitPrice)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd2 = (BigDecimal.valueOf(pdf_pageNum)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd3 = (BigDecimal.valueOf(appCreateOrderDetailParam.getQuantity())).setScale(2, RoundingMode.HALF_UP);
                amount = bd1.multiply(bd2).multiply(bd3).setScale(2, RoundingMode.HALF_UP).doubleValue();
                appCreateOrderDetailParam.setPage(pdf_pageNum);
            } else {
                BigDecimal bd1 = (BigDecimal.valueOf(unitPrice)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd2 = (BigDecimal.valueOf(final_pageNum)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd3 = (BigDecimal.valueOf(appCreateOrderDetailParam.getQuantity())).setScale(2, RoundingMode.HALF_UP);
                amount = bd1.multiply(bd2).multiply(bd3).setScale(2, RoundingMode.HALF_UP).doubleValue();
                appCreateOrderDetailParam.setPage(final_pageNum.intValue());
            }

            printOrderDetail.setAmount(amount.floatValue());
            String fileName = index + "_" + appCreateOrderDetailParam.getFile_name();
            if (fileName.length() >= 45) {
                String ext = fileName.substring(fileName.lastIndexOf(".") < 0 ? fileName.length() : fileName.lastIndexOf("."));
                fileName = fileName.substring(0, 35) + (ext.length() > 10 ? ext.substring(0, 10) : ext);
            }
            printOrderDetail.setFileName(fileName);
            index += 1;
            printOrderDetail.setNum(appCreateOrderDetailParam.getQuantity());
            printOrderDetail.setPageNum(appCreateOrderDetailParam.getPage());
            printOrderDetail.setPrintType(appCreateOrderDetailParam.getPrint_type());
            printOrderDetail.setReprintType(appCreateOrderDetailParam.getReduced_type());
            //转换前后文件的存储
            printOrderDetail.setUrl(appCreateOrderDetailParam.getPdf_path());
            printOrderDetail.setPdfMD5(appCreateOrderDetailParam.getPdf_md5());
            printOrderDetail.setPdfSize(appCreateOrderDetailParam.getPdf_size());
            printOrderDetail.setSourceMD5(appCreateOrderDetailParam.getDoc_md5());
            printOrderDetail.setSourceUrl(appCreateOrderDetailParam.getDoc_path());
            printOrderDetailList.add(printOrderDetail);
            totalAmount += amount;
        }
        BigDecimal total = new BigDecimal(totalAmount);
        totalAmount = total.setScale(2, RoundingMode.HALF_UP).doubleValue();

        //计算配送费
        DormShopDelivery dormShopDelivery = deliveryMap.get(param.getSend_type() + "");

        if (Byte.valueOf(param.getSend_type()).byteValue() == CommonConstant.DELIVERY_TYPE_DORMER) {
            if (dormShopDelivery.getThreshold() != null && dormShopDelivery.getThresholdSwitch() != null
                    && 1 == dormShopDelivery.getThresholdSwitch().intValue() &&
                    totalAmount.doubleValue() >= dormShopDelivery.getThreshold().doubleValue()) {
                //满足免配送费条件
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

        //如果有使用优惠券
        if (!StringUtil.isBlank(param.getCoupon_code())) {
            //文档费用  比较  优惠券使用门槛
            Coupon coupon = couponLocalService.getCouponByCodeAndUid(uid, param.getCoupon_code(),
                    totalAmount);
            if (coupon != null) {
                printOrder.setCouponCode(coupon.getCode());
                printOrder.setCouponDiscount(coupon.getDiscount().doubleValue());
            } else {
                throw new AppException(CommonConstant.SATTUS_COUPON_NOT_VALID,
                        CommonConstant.MSG_SATTUS_COUPON_NOT_VALID + param.getCoupon_code());
            }
        }

        //文档价格减去免费打印的价格
        BigDecimal docAmount = new BigDecimal(totalAmount);
        BigDecimal freeNum = new BigDecimal(free_page_num);
        //目前只考虑黑白单面的价格,后期需要增加黑白双面
        double bwSingleUnitPrice = priceMap.get(PrintConst.PRINT_TYPE_BW_SINGLE + "").getUnitPrice();
        BigDecimal BwSinglePrintPrice = new BigDecimal(bwSingleUnitPrice);//黑白单面
        BigDecimal freeAmount = BwSinglePrintPrice.multiply(freeNum).setScale(2, RoundingMode.HALF_UP);
        totalAmount = docAmount.subtract(freeAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
        printOrder.setTotalAmount(totalAmount);  //最终文档价格

        BigDecimal docuAomunt = new BigDecimal(totalAmount);
        BigDecimal deliveryAomunt = new BigDecimal(printOrder.getDeliveryAmount());
        double couponDiscount = printOrder.getCouponDiscount() == null ? 0 : printOrder.getCouponDiscount();
        BigDecimal couponAmount = new BigDecimal(-couponDiscount);
        //优惠券只能减免文档价格
        double lastDocAmount = docuAomunt.add(couponAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
        if (lastDocAmount <= 0) {
            lastDocAmount = 0;
            printOrder.setCouponDiscount(totalAmount);
        }

        double payAmount = BigDecimal.valueOf(lastDocAmount).add(deliveryAomunt).setScale(2, RoundingMode.HALF_UP).doubleValue();
        if (payAmount <= 0) {
            //为了获取支付账号信息,修改0元支付 为 0.01元支付
            payAmount = BigDecimal.valueOf(0.01).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 设置订单支付状态
//			printOrder.setPayType((byte) 0); // 0元支付,货到付款
//			printOrder.setPayTime((long) DateUtil.getCurrentTimeSeconds());
//			printOrder.setTradeNo("");
        }
        Integer siteId = dormentryResult.getData().getSiteId();
        logger.info("********插入前,订单详情:" + JsonUtil.getJsonFromObject(printOrder));
//        Result<PrintOrder> result2 = printOrderService.insert(printOrder);
        //接入订单中心
        OrderCenterDTO orderCenterDTO = new OrderCenterDTO();
        printOrder.setAutoConfirmHours(deliveryMap.get("2")==null?null:deliveryMap.get("2").getAutoConfirm());
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

        logger.info("创建订单结果:" + JsonUtil.getJsonFromObject(retDTO.getPrintOrder()));
        PrintOrder printOrder1 = retDTO.getPrintOrder();
        printOrder1.setPayType(param.getPay_type());
        if (payAmount <= 0) {
            printOrder1.setPayType((byte) 0); //0元支付,货到付款
        }
        //更新订单广告流水记录表
        if (updateAdList!=null&&updateAdList.size() > 0) {
            List<AdOrderRelation> adOrderList = new ArrayList<>();
            for (AdOrderRelation ad : updateAdList) {
                ad.setCenterOrderId(retDTO.getPrintOrder().getOrderId());
                adOrderList.add(ad);
            }
            Thread thread = new Thread() {
                public void run() {
//                    Result<List<AdOrder>> listResult = adOrderApi.insertAdOrderList(adOrderList);
                	Result<Integer> listResult=arlService.addAdOrderRelationList(adOrderList);
                    if (listResult.getStatus() != 0) {
                        logger.error("插入广告订单流水记录失败,订单号:{}", retDTO.getPrintOrder().getOrderId());
                    }
                }
            };
            thread.start();
        }
        //异步消息推送店长端
        Thread thread1 = new Thread() {
            public void run() {
                logger.info("*******异步推送订单信息到店长端");
                pushToPc(retDTO.getPrintOrder());
                logger.info("*******成功推送订单信息到店长端!!!!!!!");
            }
        };
        thread1.start();

//        if (!StringUtil.isBlank(param.getCoupon_code())) {
//            //修改优惠券状态为已使用
//            boolean useResult = couponLocalService.useCoupon_app(uid, param.getCoupon_code(),
//                    retDTO.getPrintOrder().getOrderId(), retDTO.getPrintOrder().getTotalAmount(), siteId, param.getShop_id());
//            if (useResult) {
//                logger.info("成功更新优惠券为已使用,优惠券code:{},订单号:{}", param.getCoupon_code(), retDTO.getPrintOrder().getOrderId());
//            } else {
//                logger.error("哦,low!更新优惠券为已使用失败了!!!!优惠券code:{},订单号:{}", param.getCoupon_code(), retDTO.getPrintOrder().getOrderId());
//            }
//        }

        //异步消息推送-处理文档
        paramMap.put("orderId", retDTO.getPrintOrder().getOrderId());
        Thread thread3 = new Thread() {
            public void run() {
                pushToPDFConver(paramMap);
            }
        };
        thread3.start();

        // 新增的订单需要记录在文档待检测记录表
        Thread thread4 = new Thread() {
            public void run() {
                PrintOrderRecord printOrderRecord = new PrintOrderRecord();
                printOrderRecord.setOrderId(printOrder1.getOrderId());
                printOrderRecordService.insert(printOrderRecord);
            }
        };
        thread4.start();
        Thread thread5 = new Thread() {
            public void run() {
            	pushApp(NotifyEnum.NEW,retDTO);
            }
        };
        thread5.start();
        //此处 REFUNDSTATUS 为空,先给2值,对应无任何状态
        return DatagramHelper.getDatagram(myOrderService.getAppOrder(printOrder1,
                        retDTO.getExtension().get(ExtensionColumnConstant.REFUNDSTATUS) == null ?
                                2 : Byte.valueOf(retDTO.getExtension().get(ExtensionColumnConstant.REFUNDSTATUS)),is_consistent,param.getApp_version()),
                CommonConstant.GLOBAL_STATUS_SUCCESS, CommonConstant.GLOBAL_STATUS_SUCCESS_MSG, true);
    }

    /**
     * 消息队列处理文档操作
     */
    private void pushToPDFConver(Map<String, Object> param) {
        String pushPcString = JsonUtil.getJsonFromObject(param);
        logger.info("消息队列处理订单文档缩印,{}", pushPcString);
        rabbitTemplate.convertAndSend(queueName, pushPcString);
    }

    /**
     * 推送到pc端
     */
    public void pushToPc(PrintOrder printOrder) {
        Map content = new HashMap<>();
        Map param = new HashMap<>();
        Map data = new HashMap<>();

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
    public String replaceAddress(String address,Integer siteId){
        // 获取学校名并进行替换
    	if(valueOpsCache.get(CommonConstant.PRINT_DORM_SITE_NAME+siteId)!=null){
    		return address.replace(valueOpsCache.get(CommonConstant.PRINT_DORM_SITE_NAME+siteId), "");
    	}
        Result<Site> siteInfo=siteApi.getSite(siteId);
        if(siteInfo!=null&&siteInfo.getStatus()==0){
        	valueOpsCache.set(CommonConstant.PRINT_DORM_SITE_NAME+siteId, siteInfo.getData().getSiteName(),cookieMaxAge,TimeUnit.SECONDS);
        	return address.replace(siteInfo.getData().getSiteName(), "");
        }
        return address;
    }
    /**
     * 推送到店长APP
     * @param notifyEnum
     * @param order
     * @param type
     */
    public void pushApp(NotifyEnum notifyEnum, OrderCenterDTO order) {
        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("order_id", order.getId());
//        data.put("order_amount",
//                new BigDecimal(order.getPayAmount()).movePointLeft(2).setScale(2,RoundingMode.HALF_UP).doubleValue());
//        data.put("type", order.getType());
//        data.put("status", order.getStatus().getCode());
        data.put("link", "hxstore://order/detail?type=8&order_sn="+order.getPrintOrder().getOrderId());
        data.put("code", 21);
//        OrderCenterDTO orderCenterDTO = orderCenterServicel.getOrderByOrderId(order.getPrintOrder().getOrderId(), true, true);
        pushToMobile(notifyEnum, data, order.getSellerId());
    }
    /**
     * 推送到app端
     *
     */
	@Autowired
	private RabbitSender rabbitSender;
    private void pushToMobile(NotifyEnum notifyEnum, Map<String, Object> data, String uid) {
    	try{
    		logger.info(StringUtil.replace("下单后推送给楼主app端{0}", JsonUtil.getJsonFromObject(data)));
    		PushContent content = PushEntityHelper.buildContent(21, null,  notifyEnum.getMsg(), JsonUtil.getJsonFromObject(data));
    		PushEntity pushEntity = PushEntityHelper.buildSingleToMobile(PushTargetEnum.DORMAPP,
    				uid, content);
    		PushEntity pushEntityDorm = PushEntityHelper.buildSingleToMobile(PushTargetEnum.DORM,
    				uid, content);
    		rabbitSender.send(pushEntity);
    		rabbitSender.send(pushEntityDorm);
    	}catch(Exception e){
    		logger.error(e.getMessage()+"推送到店长APP失败");
    	}
//        Map<String, Object> content = new HashMap<String, Object>();
//        Map<String, Object> param = new HashMap<String, Object>();
//        Map<String, Object> pushData = new HashMap<String, Object>();
//
//        content.put("msg", notifyEnum.getMsg());
//        content.put("data", data);
//        content.put("code", notifyEnum.getCode());
//
//        param.put("key", "mobile");
//        param.put("type", "single");
//        param.put("target", "dorm");
//        param.put("content", JsonUtil.getJsonFromObject(content));
//        param.put("uid", uid);
//
//        pushData.put("target", "PUSH");
//        pushData.put("param", param);
//
//        String pushMobileString = JsonUtil.getJsonFromObject(pushData);
//        logger.info(StringUtil.replace("下单后推送给楼主app端{0}", pushMobileString));
//
//        // rabbitMq推送
//        try {
//            rabbitTemplate.convertAndSend("event-entry", pushMobileString);
//        } catch (Exception e) {
//            logger.error(StringUtil.replace("下单后推送给楼主app端{0} ", pushMobileString), e);
//        }
    }
}
