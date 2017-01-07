package com.store59.printapi.service.createOrder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Part;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PolicyConditions;
import com.store59.ad.common.api.AdApi;
import com.store59.ad.common.api.AdOrderApi;
import com.store59.base.common.api.DormentryApi;
import com.store59.base.common.model.Dormentry;
import com.store59.coupon.model.businessObject.Coupon;
import com.store59.dorm.common.api.DormApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.api.DormShopDeliveryApi;
import com.store59.dorm.common.api.DormShopPriceApi;
import com.store59.dorm.common.model.Dorm;
import com.store59.dorm.common.model.DormShop;
import com.store59.dorm.common.model.DormShopDelivery;
import com.store59.dorm.common.model.DormShopPrice;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.print.common.model.PrintOrderRecord;
import com.store59.print.common.model.ad.AdOrderRelation;
import com.store59.print.common.remoting.AdOrderRelationService;
import com.store59.print.common.remoting.PrintOrderRecordService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.exception.CommonException;
import com.store59.printapi.common.http.HttpClientRequest;
import com.store59.printapi.common.utils.BASE64DecodedMultipartFile;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.DateUtil;
import com.store59.printapi.common.utils.FileUtil;
import com.store59.printapi.common.utils.NotifyEnum;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.utils.UUIDUtil;
import com.store59.printapi.model.param.createOrder.CreateOrderDetailParam;
import com.store59.printapi.model.param.createOrder.CreateOrderDetailsList;
import com.store59.printapi.model.param.createOrder.CreateOrderParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.FileUploadResult;
import com.store59.printapi.model.result.UrlResult;
import com.store59.printapi.model.result.order.AdFreeResult;
import com.store59.printapi.model.result.order.AppUrlRet;
import com.store59.printapi.model.result.order.OrderCenterDTO;
import com.store59.printapi.model.result.order.PcOrderInfo;
import com.store59.printapi.service.BaseService;
import com.store59.printapi.service.OrderCenterService;
import com.store59.printapi.service.coupon.CouponLocalService;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/14
 * @since 1.0
 */
@Service
public class CreateOrderService extends BaseService {

    @Autowired
    private HttpClientRequest   httpClientRequest;
    @Value("${uploadUrl}")
    private String              upLoadUrl;
    //    @Autowired
//    private PrintOrderService   printOrderService;
    @Autowired
    private DormShopDeliveryApi dormShopDeliveryApi;
    @Autowired
    private DormShopPriceApi    dormShopPriceApi;
    @Autowired
    private OSSClient           ossClient;
    @Value("${aliyun.oss.print.bucketName}")
    private String              bucketName;
    @Value("${aliyun.oss.print.domainName}")
    private String              domainName;
    @Value("${aliyun.oss.client.endpoint}")
    private String              endpoint;
    @Value("${aliyun.oss.client.accessKeyId}")
    private String              accessId;
    @Autowired
    private DormShopApi         dormShopService;
    @Autowired
    private RabbitTemplate      rabbitTemplate;
    @Autowired
    private DormentryApi        dormentryService;
    @Autowired
    private CouponLocalService  couponLocalService;
    @Value("${rabbitmq.event.print.entry}")
    private String              queueName;
    @Value("${store59.footer.num.limit}")
    private int                 footerNumLimit;
    @Value("${free.page.unit}")
    private double              freePageUnit;
    @Value("${store59.first.page.ad}")
    private String              store59firstPageAdUrl;
    @Autowired
    private AdApi               adApi;
    @Autowired
    private AdOrderApi          adOrderApi;
    @Autowired
    private DormApi             dormApi;
    @Autowired
    private OrderCenterService  orderCenterServicel;
    @Autowired
    private AdService adService;
    @Autowired
    AdOrderRelationService arlService;
    
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;

    @Value("${cookie.max.age}")
    private int                     cookieMaxAge;
    @Autowired
    private PrintOrderRecordService printOrderRecordService;
    @Autowired
    private AppCreateOrderService appCreateOrderService;
    private Logger logger = LoggerFactory.getLogger(CreateOrderService.class);

    public Datagram<UrlResult> fileUpLoad(MultipartFile file, boolean isAndroid) throws IOException {

        String contentType = file.getContentType();
        String sourFileName = file.getOriginalFilename();

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        // ---------原始文件保存---------
        String key = FileUtil.upload(file, contentType, sourFileName, ossClient, bucketName);
        String doc_path = domainName + "/" + key;
        String doc_md5 = key.substring(0, key.lastIndexOf("."));

        /**
         * 非pdf文档,走文件转换接口
         */
        String ret = "";
        try {
            logger.info("文档上传前****filename:" + file.getOriginalFilename() + ", 文件扩展名:" + ext);
            ret = httpClientRequest.UploadFilePost(upLoadUrl, file, ext);
        } catch (BaseException e) {
            throw new CommonException(e.getMsg());
        }

        /**
         * 原始文档转换成pdf之后,
         */
        // MD5位关键字,不能使用哦
        if (!ret.isEmpty() && ret.contains("MD5")) {
            ret = ret.replace("MD5", "md");
        }
        FileUploadResult result = JsonUtil.getObjectFromJson(ret, FileUploadResult.class);

        if (isAndroid) {
            AppUrlRet appUrlRet = new AppUrlRet();
            appUrlRet.setPage(Integer.valueOf(result.getPage()));
            appUrlRet.setPdf_path(result.getPdf_path());
            appUrlRet.setPdf_md5(result.getMd());
            appUrlRet.setFile_name(sourFileName);
            appUrlRet.setDoc_path(doc_path);
            appUrlRet.setDoc_md5(doc_md5);
            appUrlRet.setDoc_file_name(sourFileName);
            logger.info("----------文件上传android页数缓存：" + result.getMd() + "页数：" + result.getPage());
            valueOpsCache.set(result.getMd(), result.getPage(), cookieMaxAge + 10, TimeUnit.SECONDS);
            logger.info("******文件上传结果:" + JsonUtil.getJsonFromObject(appUrlRet));
            return DatagramHelper.getDatagram(appUrlRet, 0, "", true);
        }
        // PC
        UrlResult urlResult = new UrlResult();
        urlResult.setPage(result.getPage());
        urlResult.setPdf_path(result.getPdf_path());
        urlResult.setPdf_md5(result.getMd());
        urlResult.setDoc_file_name(sourFileName);
        urlResult.setDoc_path(doc_path);
        urlResult.setDoc_md5(doc_md5);
        logger.info("----------文件上传android页数缓存：" + result.getMd() + "页数：" + result.getPage());
        valueOpsCache.set(result.getMd(), result.getPage(), cookieMaxAge + 10, TimeUnit.SECONDS);
        logger.info("******文件上传结果:" + JsonUtil.getJsonFromObject(urlResult));
        return DatagramHelper.getDatagramWithSuccess(urlResult);
    }

    public Datagram<UrlResult> fileUpLoad_IOS(Part part) throws IOException {

        String contentType = part.getContentType();
        String sourFileName = part.getSubmittedFileName();

        String fileName = part.getSubmittedFileName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        // ---------原始文件保存---------
        BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(
                IOUtils.toByteArray(part.getInputStream()));
        String key = FileUtil.upload(multipartFile, contentType, sourFileName, ossClient, bucketName);
        String doc_path = domainName + "/" + key;
        String doc_md5 = key.substring(0, key.lastIndexOf("."));

        /**
         * 非pdf文档,走文件转换接口
         */
        String ret = "";
        try {
            logger.info("文档上传前****filename:" + part.getSubmittedFileName() + ", 文件扩展名:" + ext);
            ret = httpClientRequest.UploadFilePost_IOS(upLoadUrl, multipartFile, ext, part.getSubmittedFileName());
        } catch (BaseException e) {
            throw new CommonException(e.getMsg());
        }

        /**
         * 原始文档转换成pdf之后,
         */
        // MD5位关键字,不能使用哦
        if (!ret.isEmpty() && ret.contains("MD5")) {
            ret = ret.replace("MD5", "md");
        }
        FileUploadResult result = JsonUtil.getObjectFromJson(ret, FileUploadResult.class);
        AppUrlRet appUrlRet = new AppUrlRet();
        appUrlRet.setPage(Integer.valueOf(result.getPage()));
        appUrlRet.setPdf_path(result.getPdf_path());
        appUrlRet.setPdf_md5(result.getMd());
        appUrlRet.setFile_name(sourFileName);
        appUrlRet.setDoc_path(doc_path);
        appUrlRet.setDoc_md5(doc_md5);
        appUrlRet.setDoc_file_name(sourFileName);
        logger.info("----------文件上传IOS页数缓存：" + result.getMd() + "页数：" + result.getPage());
        valueOpsCache.set(result.getMd(), result.getPage(), cookieMaxAge + 10, TimeUnit.SECONDS);
        logger.info("******文件上传结果:" + JsonUtil.getJsonFromObject(appUrlRet));
        return DatagramHelper.getDatagram(appUrlRet, 0, "", true);
    }

    public Datagram createOrder(CreateOrderParam param, Long uid, String uname) {
        ConcurrentHashMap<String, Object> paramMap = new ConcurrentHashMap<>(); // 拼接消息队列--文档处理信息

        CreateOrderDetailsList list = new CreateOrderDetailsList();
        try {
            list = JsonUtil.getObjectFromJson(param.getDetails(), CreateOrderDetailsList.class);
        } catch (Exception e) {
            System.out.println("*******Details:" + param.getDetails());
            throw new BaseException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
        }

        // 获取楼栋信息
        Result<Dormentry> dormentryResult = null;
        String dormentryStr = valueOpsCache.get(CommonConstant.KEY_REDIS_DORM + param.getDormEntryId());
        if (StringUtil.isBlank(dormentryStr)) {
            dormentryResult = dormentryService.getDormentry(param.getDormEntryId());
            if (dormentryResult.getStatus() != 0) {
                logger.error("获取用户所在楼栋信息失败,楼栋id=" + param.getDormEntryId());
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            } else {
                valueOpsCache.set(CommonConstant.KEY_REDIS_DORM + param.getDormEntryId(),
                        JsonUtil.getJsonFromObject(dormentryResult.getData()), cookieMaxAge + 10, TimeUnit.SECONDS);
            }
        } else {
            dormentryResult = new Result<Dormentry>();
            dormentryResult.setData(JsonUtil.getObjectFromJson(dormentryStr, Dormentry.class));
        }

        int bw_page_num = 0;
        int free_page_num = 0;
        paramMap.put("freePageNum", free_page_num);// 消息消费者如果接收的freePageNum==0,说明没有广告
        paramMap.put("firstPageUrl", store59firstPageAdUrl);// 如果没有选择广告打印,则首页选择59的广告页
//        List<Ad> updateAdList = new ArrayList<>();
        List<AdOrderRelation> updateAdList =new ArrayList<>();
        /**
         * 广告打印优先使用黑白单面,由于现在没有双面,后期开放双面,需要对免费价格重新计算!!!!!
         */
        if (param.getOpen_ad().byteValue() == CommonConstant.AD_STATUS_OPEN) {
        	//TODO
        	AdFreeResult adFreeResult=adService.calFreePages(param, dormentryResult.getData().getSiteId(), paramMap, uid);
        	free_page_num=adFreeResult.getFreePages();
        	updateAdList=adFreeResult.getList();
        }

        // 构建实体数据
        PrintOrder printOrder = new PrintOrder();
        printOrder.setSource((byte) 0);// pc来源
        printOrder.setStatus(PrintConst.ORDER_STATUS_INIT); // 由于需要生成清单页面,所以订单初始状态都为文档处理中
        printOrder.setAdPageNum(free_page_num);
        printOrder.setAdUnitPrice(Double.valueOf(freePageUnit));
        printOrder.setDeliveryType(Byte.valueOf(param.getDeliveryType()));
        printOrder.setDeliveryTime(param.getDeleveryTime());
        printOrder.setPhone(param.getPhone());
        printOrder.setDocType(param.getDocType());
        StringBuilder address_sb = new StringBuilder();
        address_sb.append(dormentryResult.getData().getAddress1());
        address_sb.append(dormentryResult.getData().getAddress2());
        address_sb.append(dormentryResult.getData().getAddress3());
        address_sb.append(param.getAddress());
        // app用户下单只传寝室号,此处需要拼接用户所在楼栋地址.满足跨楼栋配送需求
        String address = address_sb.toString();
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
        // + param.getShopId());
        // if (StringUtil.isBlank(shopStr)) {
        result = dormShopService.findByShopId(Integer.valueOf(param.getShopId()), false, false, true, true);// 包含店铺价格,配送信息
        if (result.getStatus() != 0) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        // else {
        // valueOpsCache.set(CommonConstant.PRINT_OPTYMIZE_SHOP,
        // JsonUtil.getJsonFromObject(result.getData()),
        // cookieMaxAge, TimeUnit.SECONDS);
        // }
        // logger.info("店铺id:" + param.getShopId() + ",data: " +
        // JsonUtil.getJsonFromObject(result));
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
        printOrder.setDormId(result.getData().getDormId());
        printOrder.setUid(uid);
        printOrder.setUname(uname);

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

        List<PrintOrderDetail> printOrderDetailList = new ArrayList<>();
        double totalAmount = 0.00;
        int index = 1;
        for (CreateOrderDetailParam createOrderDetailParam : list.getDetails()) {
            if (StringUtil.isBlank(createOrderDetailParam.getPdf_path())) {
                throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "文件未上传完成,不能下单!");
            }

            PrintOrderDetail printOrderDetail = new PrintOrderDetail();
            printOrderDetail.setCheckStatus((byte) 0);// 未检测
            printOrderDetail.setIsFirst(CommonConstant.ORDER_FIRST_LIST_0);
            // 如果文档需要缩印,则对文档需要进行缩印操作----此处异步进行,稍后加上
            if (PrintConst.re_print_type_none != createOrderDetailParam.getReprintType().byteValue()) {
                printOrderDetail.setStatus(CommonConstant.PDF_OPERATE_ING);
            } else {
                printOrderDetail.setStatus(CommonConstant.PDF_OPERATE_FINISH);
            }
            // 计算订单详情的价钱
            Byte printId = createOrderDetailParam.getPrintType();
            DormShopPrice dormShopPrice = priceMap.get(printId + "");
            Double unitPrice = dormShopPrice.getUnitPrice(); // 打印单张价格

            // 获取转换后的pdf计算页数
            Integer pdf_pageNum = 0;
            // createOrderDetailParam.getPdf_path();
            // pdf.substring(pdf.indexOf("pdf/")).substring(0,
            // pdf.substring(pdf.indexOf("pdf/")).indexOf("."))
            String pdf_pageNumStr = valueOpsCache.get(createOrderDetailParam.getPdf_md5());
            if (StringUtil.isBlank(pdf_pageNumStr)) {
                // throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
                // "上传文件已超时！");
                String pdf_key = createOrderDetailParam.getPdf_path()
                        .substring(createOrderDetailParam.getPdf_path().lastIndexOf("pdf/"));
                OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
                // 获取转换后的pdf计算页数
                PDDocument doc = null;
                try {
                    doc = PDDocument.load(ossObject.getObjectContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pdf_pageNum = doc.getNumberOfPages(); // pdf后台计算页数,防止被攻击
                // Integer pdf_pageNum = createOrderDetailParam.getPageNum();
                // //pdf页数

            } else {
                pdf_pageNum = Integer.parseInt(pdf_pageNumStr);
            }
            // String pdf_key =
            // createOrderDetailParam.getPdf_path().substring(createOrderDetailParam.getPdf_path().lastIndexOf("pdf/"));
            // OSSObject ossObject = FileUtil.getOssObject(ossClient,
            // bucketName, pdf_key);
            // //获取转换后的pdf计算页数
            // PDDocument doc = null;
            // try {
            // doc = PDDocument.load(ossObject.getObjectContent());
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            // Integer pdf_pageNum = doc.getNumberOfPages(); //pdf后台计算页数,防止被攻击
            // Integer pdf_pageNum = createOrderDetailParam.getPageNum();
            // //pdf页数
            Integer num = createOrderDetailParam.getNum(); // 打印份数

            if (num.intValue() <= 0) {
                throw new BaseException(CommonConstant.STATUS_REQUEST_DATA_INVALID, "文档打印份数不能小于1份!");
            }

            Double final_pageNum = 0.00;
            // 是否缩印
            if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_two) { // 二合一
                final_pageNum = Math.ceil(pdf_pageNum / 2.0);
            }
            if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_four) { // 四合一
                final_pageNum = Math.ceil(pdf_pageNum / 4.0);
            }
            if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_six) { // 六合一
                final_pageNum = Math.ceil(pdf_pageNum / 6.0);
            }
            if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_nine) { // 九合一
                final_pageNum = Math.ceil(pdf_pageNum / 9.0);
            }
            // 考虑单双页
            if (createOrderDetailParam.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE
                    || createOrderDetailParam.getPrintType().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
                if (createOrderDetailParam.getReprintType().byteValue() == PrintConst.re_print_type_none) {
                    final_pageNum = Math.ceil(pdf_pageNum / 2.0);
                } else {
                    final_pageNum = Math.ceil(final_pageNum / 2.0);
                }
            }

            // 单价*pdf页数*份数
            Double amount = 0.00;
            if (final_pageNum.doubleValue() == 0) {
                BigDecimal bd1 = (BigDecimal.valueOf(unitPrice)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd2 = (BigDecimal.valueOf(pdf_pageNum)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd3 = (BigDecimal.valueOf(num)).setScale(2, RoundingMode.HALF_UP);
                amount = bd1.multiply(bd2).multiply(bd3).setScale(2, RoundingMode.HALF_UP).doubleValue();
                printOrderDetail.setPageNum(pdf_pageNum);
            } else {
                BigDecimal bd1 = (BigDecimal.valueOf(unitPrice)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd2 = (BigDecimal.valueOf(final_pageNum)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd3 = (BigDecimal.valueOf(num)).setScale(2, RoundingMode.HALF_UP);
                amount = bd1.multiply(bd2).multiply(bd3).setScale(2, RoundingMode.HALF_UP).doubleValue();
                printOrderDetail.setPageNum(final_pageNum.intValue());
            }
            printOrderDetail.setAmount(amount.floatValue());

            String fileName = index + "_" + createOrderDetailParam.getFileName();
            if (fileName.length() >= 45) {
                fileName = fileName.substring(0, 45);
            }
            printOrderDetail.setFileName(fileName);
            index += 1;
            printOrderDetail.setNum(createOrderDetailParam.getNum());
            printOrderDetail.setPrintType(createOrderDetailParam.getPrintType());
            printOrderDetail.setReprintType(createOrderDetailParam.getReprintType());
            // 转换前后文件的存储
            printOrderDetail.setUrl(createOrderDetailParam.getPdf_path());
            printOrderDetail.setPdfMD5(createOrderDetailParam.getPdf_md5());
            printOrderDetail.setPdfSize(createOrderDetailParam.getPdf_size());
            printOrderDetail.setSourceMD5(createOrderDetailParam.getDoc_md5());
            printOrderDetail.setSourceUrl(createOrderDetailParam.getDoc_path());
            printOrderDetailList.add(printOrderDetail);
            totalAmount += amount;
        }
        printOrder.setDetails(printOrderDetailList);

        BigDecimal total = new BigDecimal(totalAmount);
        totalAmount = total.setScale(2, RoundingMode.HALF_UP).doubleValue();

        // 计算配送费
        DormShopDelivery dormShopDelivery = deliveryMap.get(param.getDeliveryType() + "");

        if (Byte.valueOf(param.getDeliveryType()).byteValue() == CommonConstant.DELIVERY_TYPE_DORMER) {
            if (dormShopDelivery.getThreshold() != null && dormShopDelivery.getThresholdSwitch() != null
                    && 1 == dormShopDelivery.getThresholdSwitch().intValue()
                    && totalAmount >= dormShopDelivery.getThreshold().doubleValue()) {
                // 满足免配送费条件
                logger.info("dormShopDelivery.getThreshold():" + dormShopDelivery.getThreshold());
                logger.info("totalAmount.doubleValue():" + totalAmount);
                logger.info("#########满足免配送费条件###########");
                printOrder.setDeliveryAmount(0.00);
            } else {
                printOrder.setDeliveryAmount(dormShopDelivery.getCharge());
            }
        } else {
            printOrder.setDeliveryAmount(0.00);
        }

        // 如果有使用优惠券
        if (!StringUtil.isBlank(param.getCouponCode())) {
            // 文档费用 比较 优惠券使用门槛
            Coupon coupon = couponLocalService.getCouponByCodeAndUid(uid, param.getCouponCode(), totalAmount);
            if (coupon != null) {
                printOrder.setCouponCode(coupon.getCode());
                printOrder.setCouponDiscount(coupon.getDiscount().doubleValue());
            } else {
                throw new BaseException(CommonConstant.SATTUS_COUPON_NOT_VALID,
                        CommonConstant.MSG_SATTUS_COUPON_NOT_VALID + param.getCouponCode());
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
        Integer siteId = dormentryResult.getData().getSiteId();
        logger.info("********插入前,订单详情:" + JsonUtil.getJsonFromObject(printOrder));
//		Result<PrintOrder> result2 = printOrderService.insert(printOrder);
        //接入订单中心
        printOrder.setAutoConfirmHours(deliveryMap.get("2")==null?null:deliveryMap.get("2").getAutoConfirm());
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

        logger.info("创建订单结果:" + JsonUtil.getJsonFromObject(retDTO.getPrintOrder()));
        PrintOrder printOrder1 = retDTO.getPrintOrder();
        printOrder1.setPayType(Byte.valueOf(param.getPayType()));
        if (payAmount <= 0) {
            printOrder1.setPayType((byte) 0); // 0元支付,货到付款
        }
        // 更新订单广告流水记录表
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
        // 异步消息推送店长端
        Thread thread1 = new Thread() {
            public void run() {
                logger.info("*******异步推送订单信息到店长端");
                pushToPc(retDTO.getPrintOrder());
                logger.info("*******成功推送订单信息到店长端!!!!!!!");
            }
        };
        thread1.start();

//        if (!StringUtil.isBlank(param.getCouponCode())) {
//            // 修改优惠券状态为已使用
//            boolean useResult = couponLocalService.useCoupon(uid, param.getCouponCode(),
//                    retDTO.getPrintOrder().getOrderId(), retDTO.getPrintOrder().getTotalAmount(), siteId, param.getShopId());
//            if (useResult) {
//                logger.info("成功更新优惠券为已使用,优惠券code:{},订单号:{}", param.getCouponCode(), retDTO.getPrintOrder().getOrderId());
//            } else {
//                logger.error("哦,low!更新优惠券为已使用失败了!!!!优惠券code:{},订单号:{}", param.getCouponCode(),
//                        retDTO.getPrintOrder().getOrderId());
//            }
//        }

        // 异步消息推送-处理文档
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
            	appCreateOrderService.pushApp(NotifyEnum.NEW,retDTO);
            }
        };
        thread5.start();
        boolean is_consistent=true;
        if(param.getFreeAdNum()!=null&&free_page_num!=param.getFreeAdNum().intValue()){
        	is_consistent=false;
        }
        return DatagramHelper.getDatagram(getPcOrderInfo(printOrder1,is_consistent), CommonConstant.GLOBAL_STATUS_SUCCESS,
                CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
    }

    private PcOrderInfo getPcOrderInfo(PrintOrder printOrder,boolean isConsistent) {
        PcOrderInfo info = new PcOrderInfo();
        info.setOrderId(printOrder.getOrderId());
        info.setPayType(printOrder.getPayType());
        info.setPayTime(printOrder.getPayTime());
        info.setIsConsistent(isConsistent);
        return info;
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
    private void pushToPc(PrintOrder printOrder) {
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

    private void inputstream2file(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MultipartFile file2MultipartFile(File file) {
        DiskFileItem fileItem = new DiskFileItem("file", "text/plain", false, file.getName(), (int) file.length(),
                file.getParentFile());
        try {
            fileItem.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    public Datagram<?> getOssSign(String suffix, String originName) {
        try {
            long expireTime = 300;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            String dir = DigestUtils.md5Hex(UUIDUtil.getNewValue());
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            // respMap.put("expire", formatISO8601Date(expiration));
            respMap.put("dir", dir);
            respMap.put("host", domainName);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            logger.info(
                    "打印阿里云sign信息：sign－" + dir + ",原文件名－" + originName + ",返回信息" + JsonUtil.getJsonFromObject(respMap));
            return DatagramHelper.getDatagram(respMap, CommonConstant.GLOBAL_STATUS_SUCCESS,
                    CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
        } catch (Exception e) {
            throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "生成阿里云oss签名失败");
        }
    }

    @SuppressWarnings("unchecked")
    public Datagram<UrlResult> fileParams(String file, String sourceFileName, boolean isAndroid) throws Exception {

        String ext = file.substring(file.lastIndexOf(".") + 1);
        String doc_path = file;
        String doc_md5 = file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."));

        /**
         * 非pdf文档,走文件转换接口
         */
        String ret = "";
        try {
            logger.info("文档上传前****filename:" + sourceFileName + ", 文件扩展名:" + ext);
            ret = httpClientRequest.UploadFilePost(upLoadUrl, file);
            logger.info("文档转换后的信息" + ret);
            ///////////////// 测试代码
            // int a=(int)Math.random()*10;
            // FileUploadResult fr=new FileUploadResult();
            // fr.setMd("f13cf1ab725e6a1b4cae58d92bf7e529"+String.valueOf(a));
            // fr.setPage("144");
            // fr.setPdf_path("http://print-identification.oss-cn-hangzhou.aliyuncs.com/pdf/c05ccc800d0a11e6a0560642670a328f.pdf"+String.valueOf(a));
            // ret=JSONObject.toJSONString(fr);
            //////////////////// 测试代码结束
        } catch (BaseException e) {
            throw new CommonException(e.getMsg());
        }

        /**
         * 原始文档转换成pdf之后,
         */
        // MD5位关键字,不能使用哦
        if (!ret.isEmpty() && ret.contains("MD5")) {
            ret = ret.replace("MD5", "md");
        }
        JSONObject json = JSONObject.parseObject(ret);
        if (json.get("msg") != null && json.get("msg").equals("error")) {
            throw new BaseException(CommonConstant.STATUS_DOC_CONVERT_ERROR, CommonConstant.MSG_DOC_CONVERT_ERROR);
        }
        FileUploadResult result = JsonUtil.getObjectFromJson(ret, FileUploadResult.class);

        if (isAndroid) {
            AppUrlRet appUrlRet = new AppUrlRet();
            appUrlRet.setPage(Integer.valueOf(result.getPage()));
            appUrlRet.setPdf_path(result.getPdf_path());
            appUrlRet.setPdf_md5(result.getMd());
            appUrlRet.setFile_name(sourceFileName);
            appUrlRet.setDoc_path(doc_path);
            appUrlRet.setDoc_md5(doc_md5);
            logger.info("******文件上传结果:" + JsonUtil.getJsonFromObject(appUrlRet));
            return DatagramHelper.getDatagram(appUrlRet, 0, "", true);
        }
        // PC
        UrlResult urlResult = new UrlResult();
        urlResult.setPage(result.getPage());
        urlResult.setPdf_path(result.getPdf_path());
        urlResult.setPdf_md5(result.getMd());
        urlResult.setDoc_file_name(sourceFileName);
        urlResult.setDoc_path(doc_path);
        urlResult.setDoc_md5(doc_md5);
        valueOpsCache.set(result.getMd(), result.getPage(), cookieMaxAge + 10, TimeUnit.SECONDS);
        logger.info("******文件上传结果:" + JsonUtil.getJsonFromObject(urlResult));
        return DatagramHelper.getDatagramWithSuccess(urlResult);
    }
}
