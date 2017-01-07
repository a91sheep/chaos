package com.store59.printapi.controller.pay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.store59.printapi.model.result.order.OrderCenterDTO;
import com.store59.printapi.service.OrderCenterService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.kylin.utils.SignatureUtils;
import com.store59.print.common.model.PrintOrder;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.MessageConstant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.exception.CommonException;
import com.store59.printapi.model.param.PayCallbackParam;
import com.store59.printapi.model.param.order.AliPayParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.service.order.MyOrderService;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 2016/01/14
 * @since 1.0
 */
@RestController
@RequestMapping("/payment/*")
public class AliPayController {

    private static Logger logger = LoggerFactory.getLogger(AliPayController.class);

    @Autowired
    private MyOrderService myOrderService;

    // 支付应用的主机，示例值：pay.59store.net
    @Value("${alipay.url}")
    private String alipayHost;

    // 支付返回通知的主机名，返回到我自己的这个类的这个方法
    @Value("${alipay.notify.url}")
    private String notifyHost;

    @Value("${alipay.return.url}")
    private String returnHost;

    @Value("${zhifu.key}")
    private String zhifuKey;

    @Autowired
    private OrderCenterService orderCenterService;

    /**
     * 实现需求：接收一个 orderId，向支付宝发起支付请求
     * （该方法未加入到文档中）
     * 根据返回的实体内容进行重定向
     * 对应文档中的 发起支付 GET /alipay/pay
     *
     * @param param
     */
    @RequestMapping(value = "/alipay", method = RequestMethod.GET)
    public ModelAndView paymentAlipay(AliPayParam param) {
        logger.debug("从配置文件中读取的参数：alipay.host：{}", alipayHost);
        logger.debug("从配置文件中读取的参数：alipay.notify.host：{}", notifyHost);
        logger.debug("接收到得请求参数 orderId：{}", param.getOrderId());
        if (param.getOrderId() == null) {
            throw new BaseException(CommonConstant.STATUS_REQUEST_DATA_INVALID,
                    MessageConstant.ORDER_ID_PARAM_BLANK);
        }
        //接入订单中心
        OrderCenterDTO orderCenterDTO = orderCenterService.getOrderByOrderId(param.getOrderId(), true, true);
        PrintOrder printOrder = orderCenterDTO.getPrintOrder();
//        PrintOrder printOrder = myOrderService.getPrintOrderById(param.getOrderId());
//        if (printOrder.getPayType() != PrintConst.ORDER_PAY_ZFB) {
//            throw new CommonException("该订单的支付类型不是支付宝");
//        }
//        if (printOrder.getStatus().byteValue() != PrintConst.ORDER_STATUS_INIT) {
//            throw new CommonException("支付宝：该订单已支付或已取消");
//        }
        if (printOrder.getPayType() != null && printOrder.getPayTime() != null) {
            throw new CommonException("支付宝：该订单已支付或已取消");
        }
        /**
         * 传递到下一个方法的请求参数
         */
        String foodName = "59store打印店订单";

        BigDecimal bd1 = (BigDecimal.valueOf(printOrder.getTotalAmount())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bd2 = (BigDecimal.valueOf(printOrder.getDeliveryAmount())).setScale(2, RoundingMode.HALF_UP);
        Double couponDiscount = printOrder.getCouponDiscount() == null ? 0.00 : printOrder.getCouponDiscount();
        BigDecimal bd3 = (BigDecimal.valueOf(-couponDiscount)).setScale(2, RoundingMode.HALF_UP);

        String moneyStr = bd1.add(bd2).add(bd3).toString();
        if (bd1.add(bd2).add(bd3).doubleValue() == 0) {
            //0元支付修改为一分钱支付
            moneyStr = "0.01";
        }

//        String notifyUrl = notifyHost;
        String returnUrl = returnHost + "index.html#myOrder";
        // 构造请求参数的 Map 键值对，以方便加密
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("orderId", param.getOrderId().toString());
        paramsMap.put("foodName", foodName);
        paramsMap.put("money", moneyStr);
        paramsMap.put("notifyUrl", null);
        paramsMap.put("returnUrl", returnUrl);
        String sign = SignatureUtils.sign(paramsMap, zhifuKey);
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http").setHost(alipayHost).setPath("/alipay/pay")
                    .setParameter("orderId", param.getOrderId())
                    .setParameter("foodName", foodName)
                    .setParameter("money", moneyStr)
                    .setParameter("notifyUrl", null)
                    .setParameter("sign", sign)
                    .setParameter("returnUrl", returnUrl)
                    .build();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        String redirectUrl = sendRequsetToAliPay(uri);
        System.out.println("aipay URL:" + redirectUrl);
        return new ModelAndView("redirect:" + redirectUrl);
    }

    /**
     * 向支付应用发起支付请求
     *
     * @param uri 发起支付请求的 uri
     * @return 页面跳转（重定向的 url）
     */
    private String sendRequsetToAliPay(URI uri) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpGet httpGet = new HttpGet(uri);
        String returnUrl = null;
        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String entity = EntityUtils.toString(httpEntity);
            Result<Map<String, String>> result = JsonUtil.getObjectFromJson(entity, new TypeReference<Result<Map<String, String>>>() {
            });
            if (result.getStatus() != 0) {
                throw new AppException(result.getStatus(), result.getMsg());
            }
            Map<String, String> returnMap = (Map<String, String>) result.getData();
            logger.debug("返回的 url {}", returnMap.get("url"));
            returnUrl = returnMap.get("url");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnUrl;
    }

    /**
     * 对应文档中的接收通知 POST 应用的地址/notifyUrl
     * <p/>
     * 该方法完成的操作：
     * （1）根据 url 进行签名校验，若校验不通过，返回全局错误码 6；
     * （2）根据请求参数 orderId 查询表 59_order 对应记录的 orderAmount 字段值， 和请求参数 money 比较，如果不一致，返回全局错误码 4；
     * （3） 以上两步校验通过以后，根据请求参数修改 orderId 对应的记录的 payType 、tradeNo、paystatus。
     *
     * @param request
     * @param status  订单状态： 0, 新订单，还未确认或发货
     * @param msg
     * @param payType 支付类型： 0 货到付款，1 支付宝，2 微信支付， alipay是支付宝，wxpay是微信支付，spend是59花
     * @param sign    校验码
     * @param orderId 订单号
     * @param money   订单金额
     * @param tradeNo 第三方支付交易号
     * @param payTime 付款时间
     * @return
     * @throws UnsupportedEncodingException
     */
//    @RequestMapping(value = "/notify", method = RequestMethod.POST)
//    public Object orderNotify(HttpServletRequest request, @Valid PayCallbackParam param)
//            throws UnsupportedEncodingException {
//        logger.info("接收到的参数 status : {}", param.getStatus());
//        logger.info("接收到的参数 msg : {}", param.getMsg());
//        logger.info("接收到的参数 payType : {}", param.getPayType());
//        logger.info("接收到的参数 orderId : {}", param.getOrderId());
//        logger.info("接收到的参数 sign : {}", param.getSign());
//        logger.info("接收到的参数 money : {}", param.getMoney());
//        logger.info("接收到的参数 tradeNo : {}", param.getTradeNo());
//        logger.info("接收到的参数 payTime : {}", param.getPayTime());
//        logger.info("接收到的参数 buyerId : {}", param.getBuyerId());
//        logger.info("接收到的参数 buyerContact : {}", param.getBuyerContact());
//        String orderId = param.getOrderId();
//        String money = param.getMoney();
//        String buyerId = param.getBuyerId();
//        String buyerContact = param.getBuyerContact();
//        String payType = param.getPayType();
//        String tradeNo = param.getTradeNo();
//        Integer payTime = param.getPayTime();
//        String msg = param.getMsg();
//        Integer status = param.getStatus();
//
//        // 这种处理后面要修改
//        Long printOrderd = Long.valueOf(orderId.replaceAll(":", ""));
//        logger.debug("printOrderd : {}", printOrderd);
//        // 对 url 的合法性进行校验
//        boolean notify = this.notifyUrl(request, zhifuKey);
//        if (notify) {
//            logger.info("验证参数通过");
//            // 校验 money 和 PrintOrder 表里的 money 是否一致
//            validateRequestMoney(printOrderd, money);
//            PrintOrder updatePrintOrder = new PrintOrder();
//            updatePrintOrder.setOrderId(printOrderd);
//            updatePrintOrder.setBuyerId(buyerId);
//            updatePrintOrder.setBuyerContact(buyerContact);
//
//            if ("alipay".equals(payType)) {//支付宝
//                updatePrintOrder.setPayType(CommonConstant.ORDER_PAY_ALIPAY);  // 1
//            }
//            if ("wxpay".equals(payType)) {//微信h5支付
//                updatePrintOrder.setPayType(CommonConstant.ORDER_PAY_WXPAY);  // 2
//            }
//            if ("spend".equals(payType)) {//59花
//                updatePrintOrder.setPayType(CommonConstant.ORDER_PAY_SPEND);  // 3
//            }
//            if ("wxpay_app".equals(payType)) {//微信支付app
//                updatePrintOrder.setPayType(CommonConstant.ORDER_PAY_WXPAY_APP);  // 6
//            }
//            if ("credit_card".equals(payType)) {//信用支付
//                updatePrintOrder.setPayType(CommonConstant.ORDER_PAY_WXPAY_CREDIT);  // 8
//            }
//
//            updatePrintOrder.setTradeNo(tradeNo);
//            //异步处理文档,支付回调不再修改订单状态
////            updatePrintOrder.setStatus(PrintConst.ORDER_STATUS_CONFIRMED); // 1
//            updatePrintOrder.setPayTime(payTime.longValue()); //纪录付款时间
//            logger.info("更新订单状态");
//            myOrderService.updatePrintOrderById(updatePrintOrder);
//        } else {
//            throw new AppException(6, "Signature Validation failure");
//        }
//        logger.info("开始返回结果。");
//        Datagram<Map<String, String>> datagram = new Datagram<>();
//        datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
//        datagram.setMsg(msg);
////        datagram.setIsApp(false);
//        if (status == 0) {
//            Map<String, String> data = new HashMap<>();
//            data.put("result", "success");
//            datagram.setData(data);
//        }
//        logger.info("返回结果完毕:{}", JsonUtil.getJsonFromObject(datagram));
//        return datagram;
//    }

    /**
     * 校验 money 和 Order 表里的 money 是否一致的私有方法
     *
     * @param printOrderId
     * @param money
     */
    private void validateRequestMoney(Long printOrderId, String money) {
        PrintOrder oldOrder = myOrderService.getPrintOrderById(printOrderId);
        logger.info("金额校验,订单信息:" + JsonUtil.getJsonFromObject(oldOrder));
        BigDecimal oldTotalOrderAmount = new BigDecimal(oldOrder.getTotalAmount());
        BigDecimal oldDeliveryAmount = new BigDecimal(oldOrder.getDeliveryAmount());
        Double couponDiscount = oldOrder.getCouponDiscount() == null ? 0.00 : oldOrder.getCouponDiscount();
        BigDecimal oldCouponAmount = new BigDecimal(-couponDiscount); //优惠券的钱需要减去

        BigDecimal moneyBigDecimal = new BigDecimal(money);

        double moneyDouble = moneyBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
        double moneyOrderInDB = oldTotalOrderAmount.add(oldDeliveryAmount).add(oldCouponAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
        logger.info("回调传递进来的金额：{}", moneyDouble);
        logger.info("数据库查询出来的金额：{}", moneyOrderInDB);

        if (moneyOrderInDB > 0) {
            //非0元支付
            if (moneyDouble - moneyOrderInDB != 0) {
                // 参数不正确（请求参数中的 money 和数据库中的订单金额不一致）
                throw new AppException(4, "Money Validation failure");//
            }
        } else if (moneyOrderInDB == 0) {
            //0元支付
            if (moneyDouble - moneyOrderInDB != 0.01) {
                // 参数不正确（请求参数中的 money 和数据库中的订单金额不一致）
                throw new AppException(4, "Money Validation failure");//
            }
        }
    }

    private boolean notifyUrl(HttpServletRequest request, String zhifuKey) {
        Set<String> requestSet = request.getParameterMap().keySet();
        List<String> strList = new ArrayList<>();
        strList.addAll(requestSet);
        Collections.sort(strList);
        StringBuilder sb = new StringBuilder();
        String value = null;
        String signValue = null;
        for (int i = 0; i < strList.size(); i++) {
            value = request.getParameter(strList.get(i));
            if ("sign".equals(strList.get(i))) {
                signValue = value;
                continue;
            }
            sb.append(strList.get(i)).append("=").append(value).append("&");
        }
        sb.append(zhifuKey);
        String md5Value = DigestUtils.md5Hex(sb.toString());
        if (signValue.endsWith(md5Value)) {
            return true;
        } else {
            return false;
        }
    }
}
