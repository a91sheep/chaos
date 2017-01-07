/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.alipay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.alipay.AlipayQrCodeSyncResponse;
import com.store59.pay.model.alipay.AlipaySyncResponse;
import com.store59.pay.model.constants.AlipayConstants;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.service.config.AlipayConfig;
import com.store59.pay.service.config.PayConfig;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.dataTime.DateTimeUtils;
import com.store59.pay.util.http.JsonWebServiceHandler;
import com.store59.pay.util.http.NameValuePairUtils;
import com.store59.pay.util.http.WebServiceUtils;
import com.store59.pay.web.enums.ViewResultCodeEnum;
import com.store59.pay.web.form.PayForm;
import com.store59.pay.web.model.ViewResult;

/**
 * 支付宝扫码支付控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月25日
 * @since 1.0
 */
@Controller
@RequestMapping(value = "/alipay")
public class AlipayQRCodeController {
    private static final Logger logger = LoggerFactory.getLogger(AlipayQRCodeController.class);

    @Autowired
    private PayConfig           payConfig;

    @Autowired
    private AlipayConfig        alipayConfig;

    @Autowired
    private SignatureService signatureService;

    /**
     * 生成二维码.
     * 
     * @param request
     * @param payForm 要验证的参数
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/qr/info", "/qrcode/pay" }, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Result<?> qrInfo(HttpServletRequest request, @Valid PayForm payForm, BindingResult result) throws Exception {
        ViewResult<Map<String, String>> viewResult = new ViewResult<>();
        if (result.hasErrors()) {
            viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
            return viewResult.toResultIgnoreData();
        }

        AlipaySyncResponse<AlipayQrCodeSyncResponse> alipayQrCodeSyncResponse = qrPay(payForm);
        if (alipayQrCodeSyncResponse == null) {
            logger.error("【支付宝扫码支付】预下单失败：{}", JsonUtil.getJsonFromObject(alipayQrCodeSyncResponse));
            viewResult.setResultCode(ViewResultCodeEnum.UNKNOWN_ERROR);
            return viewResult.toResultIgnoreData();
        }

        if (!StringUtils.equals(alipayQrCodeSyncResponse.getAlipayTradePrecreateResponse().getCode(), AlipayConstants.RESPONSE_CODE_SUCCESS)) {
            logger.error("【支付宝扫码支付】预下单失败：{}", JsonUtil.getJsonFromObject(alipayQrCodeSyncResponse));
            viewResult.setResultCode(ViewResultCodeEnum.ALIPAY_GET_QR_INFO_ERROR);
            return viewResult.toResultIgnoreData();
        }else{
            logger.info("【支付宝扫码支付】预下单成功：{}", JsonUtil.getJsonFromObject(alipayQrCodeSyncResponse));
        }


        Map<String, String> map = new HashMap<>();
        map.put("qr_code", alipayQrCodeSyncResponse.getAlipayTradePrecreateResponse().getQrCode());
        viewResult.setResultCode(ViewResultCodeEnum.SUCCESS);

        viewResult.setData(map);
        return viewResult.toResult();
    }

    /**
     * 预下单.
     * 
     * @param payForm
     * @return
     * @throws AlipayApiException
     */
    private AlipaySyncResponse<AlipayQrCodeSyncResponse> qrPay(PayForm payForm) throws AlipayApiException {

        //时间(毫秒)
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + 6*60*60*1000; // 6小时超时

        //要发送给支付宝的业务参数
        AlipayTradePrecreateBizContent bizContent = new AlipayTradePrecreateBizContent();
        bizContent.setOutTradeNo(payForm.getOrderId());
        bizContent.setTotalAmount(payForm.getMoney().toString());
        bizContent.setSubject(payForm.getFoodName());

        //如果有传shopType参数,则原样返回 即在应用的地址后面加上 ?shopType=
        String AppNotifyUrl = payForm.getNotifyUrl();
        if(StringUtils.isNotBlank(payForm.getShopType())){
            AppNotifyUrl = AppNotifyUrl + "?shopType=" + payForm.getShopType();
        }
        bizContent.setBody(AppNotifyUrl);

        bizContent.setTimeExpire(DateTimeUtils.ToSting(expireTime));

        String bizContentStr = JsonUtil.getJsonFromObject(bizContent);
        logger.debug("【支付宝扫码支付】预下单参数bizContentStr：{}", bizContentStr);

        // 要发送给支付宝的全部参数
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("method", "alipay.trade.precreate");
        requestMap.put("app_id", alipayConfig.getQrAppId());
        requestMap.put("charset", alipayConfig.getInputCharset());
        requestMap.put("sign_type", alipayConfig.getSignType());
        requestMap.put("timestamp", DateTimeUtils.ToSting(currentTime));
        requestMap.put("notify_url", payConfig.getUrl() + "/alipay/qrcode/notify");
        requestMap.put("biz_content", bizContentStr);

        //签名
        requestMap.put("sign", signatureService.sign(requestMap, PayChannelEnum.ALIPAY_SCAN));

        //发送请求
        AlipaySyncResponse<AlipayQrCodeSyncResponse> response = WebServiceUtils.post(AlipayConstants.ALIPAY_GATEWAY_OPEN, requestMap,
                new JsonWebServiceHandler<Map<String, String>, AlipaySyncResponse<AlipayQrCodeSyncResponse>>(new TypeReference<AlipaySyncResponse<AlipayQrCodeSyncResponse>>(){}) {
                    @Override
                    protected void setRequestData(Request request, Map<String, String> requestData) {
                        request.bodyForm(NameValuePairUtils.convert(requestData), getCharset());
                    }

                });

        logger.info("【支付宝扫码支付】获取二维码返回的结果:{}", JsonUtil.getJsonFromObject(response));

        return response;
    }

    /**
     * 支付宝预下单业务内容.
     * 
     * @author <a href="mailto:zhuzm@59store.com">天河</a>
     * @version 1.0 2015年12月29日
     * @since 1.0
     */
    @JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
    protected class AlipayTradePrecreateBizContent {

        /** 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复. */
        private String outTradeNo;

        /** 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]. */
        private String totalAmount;

        /** 订单标题. */
        private String subject;

        /** 订单描述. */
        private String body;

        /** 支付超时时间. */
        private String timeExpire;

        /**
         * @return the outTradeNo
         */
        public String getOutTradeNo() {
            return outTradeNo;
        }

        /**
         * @param outTradeNo the outTradeNo to set
         */
        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        /**
         * @return the totalAmount
         */
        public String getTotalAmount() {
            return totalAmount;
        }

        /**
         * @param totalAmount the totalAmount to set
         */
        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        /**
         * @return the subject
         */
        public String getSubject() {
            return subject;
        }

        /**
         * @param subject the subject to set
         */
        public void setSubject(String subject) {
            this.subject = subject;
        }

        /**
         * @return the body
         */
        public String getBody() {
            return body;
        }

        /**
         * @param body the body to set
         */
        public void setBody(String body) {
            this.body = body;
        }

        /**
         * @return the timeExpire
         */
        public String getTimeExpire() {
            return timeExpire;
        }

        /**
         * @param timeExpire the timeExpire to set
         */
        public void setTimeExpire(String timeExpire) {
            this.timeExpire = timeExpire;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }

    }

}
