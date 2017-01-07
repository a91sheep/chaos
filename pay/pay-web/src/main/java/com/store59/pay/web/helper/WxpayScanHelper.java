/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.helper;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.wxpay.WxpayScanRequest;
import com.store59.pay.model.wxpay.WxpayScanResponse;
import com.store59.pay.service.config.WxpayConfig;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.lang.BeanMapUtils;
import com.store59.pay.web.form.wxpay.WxpayScanForm;
import com.store59.pay.web.model.WxpayScanResult;

/**
 * 微信刷卡支付帮助类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月11日
 * @since 1.0
 */
@Component
public class WxpayScanHelper {
    private static final Logger logger = LoggerFactory.getLogger(WxpayScanHelper.class);

    @Autowired
    private WxpayConfig      wxpayConfig;

    @Autowired
    private SignatureService signatureService;

    /**
     * 构造{@link WxpayScanRequest}对象.
     * 
     * @param form {@link WxpayScanForm}
     * @return {@link WxpayScanRequest}
     */
    public WxpayScanRequest buildWxpayScanRequest(WxpayScanForm form) {
        if (form == null) {
            return null;
        }

        WxpayScanRequest request = new WxpayScanRequest();

        request.setAppid(wxpayConfig.getAppid());
        request.setSubAppid(wxpayConfig.getSubAppid());
        request.setMchId(wxpayConfig.getMchId());
        request.setSubMchId(wxpayConfig.getSubMchId());
        request.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
        request.setBody(form.getFoodName());
        request.setOutTradeNo(form.getOrderId());
        request.setTotalFee(form.getMoney().multiply(new BigDecimal("100")).intValue());
        request.setSpbillCreateIp("8.8.8.8");
        request.setAuthCode(form.getAuthCode());
        request.setSign(signatureService.sign(BeanMapUtils.toMap(request), PayChannelEnum.WXPAY_SCAN));

        return request;
    }

    /**
     * 构造{@link WxpayScanResult}对象.
     * 
     * @param result {@link WxpayScanResult}
     * @param {@link WxpayScanResult}
     * @param response {@link WxpayScanResponse}
     */
    public void buildWxpayScanResult(WxpayScanResult result, WxpayScanResponse response) {
        if (result == null || response == null) {
            return;
        }

        //return_code=success && result_code=fail
        if (response == null || !response.isSuccess()) {
            logger.error("【微信刷卡支付】没有返回成功结果, return_code=success && result_code=fail: responseData={} ", response);
            result.setBizResult(BizResultCodeEnum.WXPAY_SCAN_RESULT_CODE_FAIL);

            //同时返回微信错误码和错误描述(调用支付的系统可能使用了错误描述)
            result.setWxErrCode(response.getErrCode());
            result.setMsg(response.getErrCodeDes());

            return;
        }

        //return_code和result_code都为success
        logger.info("【微信刷卡支付】支付成功: responseData={} ", response);

        result.setBizResult(BizResultCodeEnum.SUCCESS);
        result.setPayType(PayChannelEnum.WXPAY_SCAN.getCode());
        result.setOrderId(response.getOutTradeNo());
        result.setMoney(new BigDecimal(response.getTotalFee()).divide(new BigDecimal("100")));
        result.setTradeNo(response.getTransactionId());
        try {
            result.setPayTime(String.valueOf(DateUtils.parseDate(response.getTimeEnd(), new String[] { "yyyyMMddHHmmss" }).getTime() / 1000));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
