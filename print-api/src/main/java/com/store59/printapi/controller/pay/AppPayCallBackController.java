//package com.store59.printapi.controller.pay;
//
//import com.store59.kylin.utils.JsonUtil;
//import com.store59.print.common.data.PrintConst;
//import com.store59.print.common.model.PrintOrder;
//import com.store59.printapi.common.constant.CommonConstant;
//import com.store59.printapi.common.exception.AppException;
//import com.store59.printapi.model.param.PayCallbackParam;
//import com.store59.printapi.model.result.Datagram;
//import com.store59.printapi.service.order.MyOrderService;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.*;
//
///**
// * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
// * @version 1.0 16/3/17
// * @since 1.0
// */
//@RestController
//@RequestMapping("/print/pay/*")
//public class AppPayCallBackController {
//
//    private static Logger logger = LoggerFactory.getLogger(AliPayController.class);
//
//    @Autowired
//    private MyOrderService myOrderService;
//    @Value("${zhifu.key}")
//    private String         zhifuKey;
//
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
////        datagram.setIsApp(true);
//        if (status == 0) {
//            Map<String, String> data = new HashMap<>();
//            data.put("result", "success");
//            datagram.setData(data);
//        }
//        logger.info("返回结果完毕:{}", JsonUtil.getJsonFromObject(datagram));
//        return datagram;
//    }
//
//    /**
//     * 校验 money 和 Order 表里的 money 是否一致的私有方法
//     *
//     * @param printOrderId
//     * @param money
//     */
//    private void validateRequestMoney(Long printOrderId, String money) {
//        PrintOrder oldOrder = myOrderService.getPrintOrderById(printOrderId);
//        logger.info("金额校验,订单信息:" + JsonUtil.getJsonFromObject(oldOrder));
//        BigDecimal oldTotalOrderAmount = new BigDecimal(oldOrder.getTotalAmount());
//        BigDecimal oldDeliveryAmount = new BigDecimal(oldOrder.getDeliveryAmount());
//        Double couponDiscount = oldOrder.getCouponDiscount() == null ? 0.00 : oldOrder.getCouponDiscount();
//        BigDecimal oldCouponAmount = new BigDecimal(-couponDiscount); //优惠券的钱需要减去
//
//        BigDecimal moneyBigDecimal = new BigDecimal(money);
//
//        double moneyDouble = moneyBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
//        double moneyOrderInDB = oldTotalOrderAmount.add(oldDeliveryAmount).add(oldCouponAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
//        logger.info("回调传递进来的金额：{}", moneyDouble);
//        logger.info("数据库查询出来的金额：{}", moneyOrderInDB);
//
//        if (moneyOrderInDB > 0) {
//            //非0元支付
//            if (moneyDouble - moneyOrderInDB != 0) {
//                // 参数不正确（请求参数中的 money 和数据库中的订单金额不一致）
//                throw new AppException(4, "Money Validation failure");//
//            }
//        } else if (moneyOrderInDB == 0) {
//            //0元支付
//            if (moneyDouble - moneyOrderInDB != 0.01) {
//                // 参数不正确（请求参数中的 money 和数据库中的订单金额不一致）
//                throw new AppException(4, "Money Validation failure");//
//            }
//        }
//    }
//
//    private boolean notifyUrl(HttpServletRequest request, String zhifuKey) {
//        Set<String> requestSet = request.getParameterMap().keySet();
//        List<String> strList = new ArrayList<>();
//        strList.addAll(requestSet);
//        Collections.sort(strList);
//        StringBuilder sb = new StringBuilder();
//        String value = null;
//        String signValue = null;
//        for (int i = 0; i < strList.size(); i++) {
//            value = request.getParameter(strList.get(i));
//            if ("sign".equals(strList.get(i))) {
//                signValue = value;
//                continue;
//            }
//            sb.append(strList.get(i)).append("=").append(value).append("&");
//        }
//        sb.append(zhifuKey);
//        String md5Value = DigestUtils.md5Hex(sb.toString());
//        if (signValue.endsWith(md5Value)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
