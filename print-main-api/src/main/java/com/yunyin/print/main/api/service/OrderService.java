package com.yunyin.print.main.api.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.kylin.utils.StringUtil;
import com.yunyin.base.common.model.User;
import com.yunyin.print.common.constant.PrintCommonConstant;
import com.yunyin.print.common.enums.MinimoTypeEnum;
import com.yunyin.print.common.enums.OrderSourceEnum;
import com.yunyin.print.common.enums.OrderStatusEnum;
import com.yunyin.print.common.filter.PrintOrderFilter;
import com.yunyin.print.common.model.PSitePrice;
import com.yunyin.print.common.model.PrintOrder;
import com.yunyin.print.common.model.PrintOrderDetail;
import com.yunyin.print.common.remoting.PrintServiceClient;
import com.yunyin.print.main.api.common.constant.CommonConstant;
import com.yunyin.print.main.api.common.utils.DateUtil;
import com.yunyin.print.main.api.common.utils.FileUtil;
import com.yunyin.print.main.api.model.param.pc.CreateOrderParam;
import com.yunyin.print.main.api.model.param.pc.OrderListParam;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/19
 * @since 1.0
 */
@Service
public class OrderService {

    @Autowired
    private PrintServiceClient printServiceClient;

    private Logger log = LoggerFactory.getLogger(OrderService.class);

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    @Autowired
    private RabbitTemplate                  rabbitTemplate;
    @Value("${rabbitmq.event.print.conver}")
    private String                          queueName;
    @Autowired
    private OSSClient                       ossClient;
    @Value("${aliyun.oss.print.bucketName}")
    private String                          bucketName;
    @Value("${aliyun.oss.print.domainName}")
    private String                          domainName;
    @Value("${aliyun.oss.client.endpoint}")
    private String                          endpoint;
    @Value("${aliyun.oss.client.accessKeyId}")
    private String                          accessId;

    /**
     * 获取订单
     *
     * @param orderId
     * @param uid
     * @return
     */
    public Result<PrintOrder> getOrderByOrderId(String orderId, Long uid) {
        Result<PrintOrder> result = printServiceClient.getOrderByOrderId(orderId, true);
        if (result == null || result.getStatus() != 0) {
            throw new ServiceException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        if (result.getData() == null) {
            throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "当前订单不存在，订单号:" + orderId);
        }
        if (uid.longValue() != result.getData().getUid().longValue()) {
            throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "订单与当前用户不匹配");
        }
        return result;
    }

    /**
     * 获取订单列表
     */
    public Result<List<PrintOrder>> getMyOrderList(OrderListParam param, Long uid) {

        PrintOrderFilter filter = new PrintOrderFilter();
        filter.setUid(uid);
        if (param.getLimit() != null && param.getOffset() != null) {
            filter.setLimit(param.getLimit());
            int offset = param.getOffset() * param.getLimit();
            filter.setOffset(offset);
        }
        if (param.getStatusList() != null && param.getStatusList().size() > 0) {
            filter.setStatusList(param.getStatusList());
        }
        Result<List<PrintOrder>> result = printServiceClient.getOrderByFilter(filter, true);
        if (result == null || result.getStatus() != 0) {
            throw new ServiceException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        return result;
    }

    /**
     * 取消订单
     */
    public Result<Boolean> cancelOrder(Long uid, String orderId) {
        Result<PrintOrder> result = printServiceClient.getOrderByOrderId(orderId, false);
        if (result == null || result.getStatus() != 0) {
            throw new ServiceException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        if (result.getData() == null) {
            throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "当前订单不存在，订单号:" + orderId);
        }
        if (uid.longValue() != result.getData().getUid().longValue()) {
            throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "订单与当前用户不匹配");
        }
        PrintOrder order = new PrintOrder();
        order.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        Result<Boolean> cancelRet = printServiceClient.update(order);
        return cancelRet;
    }

    /**
     * 创建订单
     */
    public Result<PrintOrder> createOrder(CreateOrderParam param, User user) {
        String details = param.getDetails();
        List<PrintOrderDetail> detailList = new ArrayList<>();
        try {
            detailList = JsonUtil.getObjectFromJson(details, new TypeReference<List<PrintOrderDetail>>() {
            });
        } catch (Exception e) {
            log.error("创建订单，商品详情json转换失败。details-->>  " + details);
            throw new ServiceException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
        }

        //获取学校价格
        Result<List<PSitePrice>> priceRet = printServiceClient.getPriceList(param.getSiteId());
        if (priceRet == null || priceRet.getStatus() != 0) {
            throw new ServiceException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        List<PSitePrice> priceList = priceRet.getData();

        double totalAmount = 0.00;
        //STEP 1 计算支付金额
        for (PrintOrderDetail detail : detailList) {
            //check
            if (StringUtil.isEmpty(detail.getPdfUrl())) {
                throw new ServiceException(CommonConstant.STATUS_REQUEST_DATA_INVALID, "文件未上传完成,不能下单!");
            }
            if (detail.getPrintNum().intValue() <= 0) {
                throw new ServiceException(CommonConstant.STATUS_REQUEST_DATA_INVALID, "文档打印份数不能小于1份!");
            }
            //detail init
            detail.setIsFirst(PrintCommonConstant.IS_FIRST_NO);

            //获取商品单价
            PSitePrice pSitePrice = getPrice(detail.getPrintType(), detail.getPaperType(), detail.getPrintSide(), priceList);
            if (pSitePrice == null) {
                throw new ServiceException(CommonConstant.STATUS_REQUEST_DATA_INVALID,
                        "当前打印选项价格不存在!printType=" + detail.getPrintType() +
                                ",paperType=" + detail.getPaperType() + ",printSide=" + detail.getPrintSide());
            }

            int pdfPageNum = 0;  //转换后的pdf页数
            String pdfPageNumStr = valueOpsCache.get(detail.getPdfMD5());
            if (StringUtil.isEmpty(pdfPageNumStr)) {
                String pdf_key = detail.getPdfUrl()
                        .substring(detail.getPdfUrl().lastIndexOf("pdf/"));
                OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
                // 获取转换后的pdf计算页数
                PDDocument doc = null;
                try {
                    doc = PDDocument.load(ossObject.getObjectContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pdfPageNum = doc.getNumberOfPages(); // pdf后台计算页数,防止被攻击
                // Integer pdf_pageNum = createOrderDetailParam.getPageNum();
                // //pdf页数

            } else {
                pdfPageNum = Integer.parseInt(pdfPageNumStr);
            }

            Double final_pageNum = 0.00;
            if (detail.getMinimoType().byteValue() == MinimoTypeEnum.NO.getCode().byteValue()) {
                //不缩印，文档不需要处理
                detail.setPrintStatus(PrintCommonConstant.DOC_DEAL_FINISHED);
            } else if (detail.getMinimoType().byteValue() == MinimoTypeEnum.TWO.getCode().byteValue()) {
                //二合一  计算页数
                final_pageNum = Math.ceil(pdfPageNum / 2.0);

                detail.setPrintStatus(PrintCommonConstant.DOC_DEALING);
            } else if (detail.getMinimoType().byteValue() == MinimoTypeEnum.FOUR.getCode().byteValue()) {
                //四合一  计算页数
                final_pageNum = Math.ceil(pdfPageNum / 4.0);

                detail.setPrintStatus(PrintCommonConstant.DOC_DEALING);
            }
            //考虑单双页
            if (detail.getPrintSide().byteValue() == PrintCommonConstant.PRINT_SIDE_DOUBLE) {
                if (detail.getMinimoType().byteValue() == MinimoTypeEnum.NO.getCode().byteValue()) {
                    final_pageNum = Math.ceil(pdfPageNum / 2.0);
                } else {
                    final_pageNum = Math.ceil(final_pageNum / 2.0);
                }
            }

            //计算商品详情金额  = 单价 * pdf页数 * 份数
            Double amount = 0.00;
            if (final_pageNum.doubleValue() == 0) {
                BigDecimal bd1 = (BigDecimal.valueOf(pSitePrice.getUnitPrice())).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd2 = (BigDecimal.valueOf(pdfPageNum)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd3 = (BigDecimal.valueOf(detail.getPrintNum())).setScale(2, RoundingMode.HALF_UP);
                amount = bd1.multiply(bd2).multiply(bd3).setScale(2, RoundingMode.HALF_UP).doubleValue();
                detail.setPdfNum(pdfPageNum);
            } else {
                BigDecimal bd1 = (BigDecimal.valueOf(pSitePrice.getUnitPrice())).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd2 = (BigDecimal.valueOf(final_pageNum)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal bd3 = (BigDecimal.valueOf(detail.getPrintNum())).setScale(2, RoundingMode.HALF_UP);
                amount = bd1.multiply(bd2).multiply(bd3).setScale(2, RoundingMode.HALF_UP).doubleValue();
                detail.setPdfNum(final_pageNum.intValue());
            }
            detail.setAmount(amount.floatValue());
            totalAmount += amount;
        }
        BigDecimal total = new BigDecimal(totalAmount);
        totalAmount = total.setScale(2, RoundingMode.HALF_UP).doubleValue();

        PrintOrder order = new PrintOrder();
        order.setOrderStatus(OrderStatusEnum.INIT.getCode());
        order.setUid(user.getUid());
        order.setUname(user.getUname());
        order.setOrderSource(OrderSourceEnum.WEB.getCode());
        order.setCreateTime(Long.valueOf(DateUtil.getCurrentTimeSeconds()));
        order.setDetails(detailList);
        order.setPayAmount(totalAmount);
        order.setSiteId(param.getSiteId());

        Result<PrintOrder> result = printServiceClient.insert(order);

        log.info("创建订单结果:" + JsonUtil.getJsonFromObject(result.getData()));
        if (result == null || result.getStatus() != 0) {
            throw new ServiceException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }

        //异步处理文档<创建首页／缩印>
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("orderId", result.getData().getOrderId());
        pushToPDFConver(map);

        return result;
    }

    private PSitePrice getPrice(byte printType, byte paperType, byte printSide, List<PSitePrice> list) {
        for (PSitePrice sitePrice : list) {
            if (sitePrice.getPrintType().byteValue() == printType &&
                    sitePrice.getPaperType().byteValue() == paperType &&
                    sitePrice.getPrintSide().byteValue() == printSide) {
                return sitePrice;
            }
        }
        return null;
    }

    /**
     * 消息队列处理文档操作
     */
    private void pushToPDFConver(Map<String, Object> param) {
        String pushPcString = JsonUtil.getJsonFromObject(param);
        log.info("消息队列处理订单文档缩印,{}", pushPcString);
        rabbitTemplate.convertAndSend(queueName, pushPcString);
    }
}
