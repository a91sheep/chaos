/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.helper;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.store59.kylin.utils.EnumUtils;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.enums.WxpayTradeStatusEnum;
import com.store59.pay.model.wxpay.WxpayOrderQueryRequest;
import com.store59.pay.model.wxpay.WxpayOrderQueryResponse;
import com.store59.pay.service.config.WxpayConfig;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.lang.BeanMapUtils;
import com.store59.pay.web.form.wxpay.WxpayOrderQueryForm;
import com.store59.pay.web.model.WxpayOrderQueryResult;

/**
 * 微信支付查询订单帮助类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月15日
 * @since 1.0
 */
@Component
public class WxpayOrderQueryHelper {

    private static final Logger logger = LoggerFactory.getLogger(WxpayOrderQueryHelper.class);

    @Autowired
    private WxpayConfig      wxpayConfig;

    @Autowired
    private SignatureService signatureService;

    /**
     * 构造{@link WxpayOrderQueryRequest}对象.
     * 
     * @param form {@link WxpayOrderQueryForm}
     * @return {@link WxpayOrderQueryRequest}
     */
    public WxpayOrderQueryRequest buildWxpayOrderQueryRequest(WxpayOrderQueryForm form) {
        if (form == null) {
            return null;
        }

        WxpayOrderQueryRequest request = new WxpayOrderQueryRequest();

        request.setAppid(wxpayConfig.getAppid());
        request.setSubAppid(wxpayConfig.getSubAppid());
        request.setMchId(wxpayConfig.getMchId());
        request.setSubMchId(wxpayConfig.getSubMchId());
        request.setOutTradeNo(form.getOrderId());
        request.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
        request.setSign(signatureService.sign(BeanMapUtils.toMap(request), EnumUtils.getEnumByCode(PayChannelEnum.class, form.getPayType())));

        return request;
    }

    /**
     * 构建{@link WxpayOrderQueryResult}对象.
     * 
     * @param wxpayOrderQueryResult {@link WxpayOrderQueryResult}
     * @param wxpayOrderQueryResponse {@link WxpayOrderQueryResponse}
     */
    public void buildWxpayOrderQueryResult(WxpayOrderQueryResult wxpayOrderQueryResult, WxpayOrderQueryResponse wxpayOrderQueryResponse) {
        if (wxpayOrderQueryResult == null || wxpayOrderQueryResponse == null) {
            return;
        }

        //return_code=success && result_code=fail
        if (wxpayOrderQueryResponse == null || !wxpayOrderQueryResponse.isSuccess()) {
            wxpayOrderQueryResult.setBizResult(BizResultCodeEnum.ORDER_QUERY_WXPAY_SCAN_RESULT_CODE_FAIL);
            logger.error("【查询微信支付订单】返回结果失败, result_code=fail：responseData={} ", wxpayOrderQueryResponse);

            //同时返回微信错误码和错误描述(调用支付的系统可能使用了错误描述)
            wxpayOrderQueryResult.setWxErrCode(wxpayOrderQueryResponse.getErrCode());
            wxpayOrderQueryResult
                        .setMsg(ObjectUtils.defaultIfNull(wxpayOrderQueryResponse.getReturnMsg(), wxpayOrderQueryResponse.getErrCodeDes()));

            return;
        }

        wxpayOrderQueryResult.setTradeState(wxpayOrderQueryResponse.getTradeState());
        if (wxpayOrderQueryResponse.getTradeState() != WxpayTradeStatusEnum.SUCCESS) {
            if (wxpayOrderQueryResponse.getTradeState() == WxpayTradeStatusEnum.USERPAYING) {
                //用户支付中
                wxpayOrderQueryResult.setBizResult(BizResultCodeEnum.WXPAY_SCAN_RESULT_CODE_FAIL);
            } else {
                //状态不是支付成功
                wxpayOrderQueryResult.setBizResult(BizResultCodeEnum.ORDER_QUERY_WXPAY_SCAN_TRADE_STATE_NO_SUCCESS);
            }
            logger.warn("【查询微信支付订单】状态不是支付成功状态：responseData={} ", wxpayOrderQueryResponse);

            wxpayOrderQueryResult.setMsg(wxpayOrderQueryResponse.getTradeStateDesc());
            return;
        }

        //状态是支付成功
        logger.info("【查询微信支付订单】状态是支付成功：responseData={} ", wxpayOrderQueryResponse);
        wxpayOrderQueryResult.setBizResult(BizResultCodeEnum.SUCCESS);
        wxpayOrderQueryResult.setOrderId(wxpayOrderQueryResponse.getOutTradeNo());
        wxpayOrderQueryResult.setMoney(new BigDecimal(wxpayOrderQueryResponse.getTotalFee()).divide(new BigDecimal("100")));
        wxpayOrderQueryResult.setTradeNo(wxpayOrderQueryResponse.getTransactionId());
        try {
            wxpayOrderQueryResult.setPayTime(
                    String.valueOf(DateUtils.parseDate(wxpayOrderQueryResponse.getTimeEnd(), new String[] { "yyyyMMddHHmmss" }).getTime() / 1000));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
