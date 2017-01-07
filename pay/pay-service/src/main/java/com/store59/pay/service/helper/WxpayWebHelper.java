/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.helper;

import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.enums.WxpayTradeTypeEnum;
import com.store59.pay.model.wxpay.*;
import com.store59.pay.service.config.PayConfig;
import com.store59.pay.service.config.WxpayAppConfig;
import com.store59.pay.service.config.WxpayConfig;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.lang.BeanMapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.System.currentTimeMillis;

/**
 * 微信网页支付帮助类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@Component
public class WxpayWebHelper {

    @Autowired
    private PayConfig        payConfig;

    @Autowired
    private WxpayConfig      wxpayConfig;

    @Autowired
    private WxpayAppConfig wxpayAppConfig;

    @Autowired
    private SignatureService signatureService;

    /**
     * 构造获取预支付订单的请求(for h5)
     * 普通版
     * @param wxpayWebPayRequest
     * @return
     */
    public WxpayUnifiedOrderRequest buildWxpayUnifiedOrderRequestForH5(WxpayWebPayRequest wxpayWebPayRequest) {
        if (wxpayWebPayRequest == null) {
            return null;
        }

        WxpayUnifiedOrderRequest request = new WxpayUnifiedOrderRequest();

        request.setAppid(wxpayConfig.getAppid());
        request.setMchId(wxpayConfig.getMchId());
        request.setDeviceInfo("WEB");
        request.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
        request.setBody(wxpayWebPayRequest.getFoodName());
        request.setOutTradeNo(wxpayWebPayRequest.getOrderId());
        request.setTotalFee(wxpayWebPayRequest.getMoney()); //单位为分
        request.setSpbillCreateIp("8.8.8.8");//TODO
        request.setNotifyUrl(payConfig.getUrl() + "/jsapi/notify");
        if (StringUtils.isNoneBlank(wxpayWebPayRequest.getNotifyUrl())) {
            request.setAttach(wxpayWebPayRequest.getNotifyUrl());
        }
        request.setTradeType(WxpayTradeTypeEnum.JSAPI.name());
        request.setOpenid(wxpayWebPayRequest.getOpenid());
        request.setSign(signatureService.sign(BeanMapUtils.toMap(request), PayChannelEnum.WXPAY_JSAPI));

        return request;
    }

    /**
     * 构造获取预支付订单的请求(for h5)
     * 服务商版
     * @param wxpayWebPayRequest
     * @return
     */
    public WxpayServerUnifiedOrderRequest buildWxpayServerUnifiedOrderRequestForH5(WxpayWebPayRequest wxpayWebPayRequest) {
        if (wxpayWebPayRequest == null) {
            return null;
        }

        WxpayServerUnifiedOrderRequest request = new WxpayServerUnifiedOrderRequest();

        request.setAppid(wxpayConfig.getAppid());
        request.setMchId(wxpayConfig.getMchId());
        request.setSubAppid(wxpayConfig.getSubAppid());
        request.setSubMchId(wxpayConfig.getSubMchId());
        request.setDeviceInfo("WEB");
        request.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
        request.setBody(wxpayWebPayRequest.getFoodName());
        request.setOutTradeNo(wxpayWebPayRequest.getOrderId());
        request.setTotalFee(wxpayWebPayRequest.getMoney()); //单位为分
        request.setSpbillCreateIp("8.8.8.8");//TODO
        request.setNotifyUrl(payConfig.getUrl() + "/jsapi/notify");
        if (StringUtils.isNoneBlank(wxpayWebPayRequest.getNotifyUrl())) {
            request.setAttach(wxpayWebPayRequest.getNotifyUrl());
        }
        request.setTradeType(WxpayTradeTypeEnum.JSAPI.name());
        request.setSubOpenid(wxpayWebPayRequest.getOpenid());
        request.setSign(signatureService.sign(BeanMapUtils.toMap(request), PayChannelEnum.WXPAY_JSAPI));

        return request;
    }


    /**
     * 构造获取预支付订单的请求(for app)
     * @return
     */
    public WxpayServerUnifiedOrderRequest buildWxpayUnifiedOrderRequestForApp(WxpayWebPayRequest wxpayWebPayRequest) {
        if (wxpayWebPayRequest == null) {
            return null;
        }

        WxpayServerUnifiedOrderRequest request = new WxpayServerUnifiedOrderRequest();

        request.setAppid(wxpayAppConfig.getAppid());
        request.setMchId(wxpayAppConfig.getMchId());
        request.setDeviceInfo("APP");
        request.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
        request.setBody(wxpayWebPayRequest.getFoodName());
        request.setOutTradeNo(wxpayWebPayRequest.getOrderId());
        request.setTotalFee(wxpayWebPayRequest.getMoney()); //单位为分
        request.setSpbillCreateIp("8.8.8.8");//TODO
        request.setNotifyUrl(payConfig.getUrl() + "/app/notify");
        if (StringUtils.isNoneBlank(wxpayWebPayRequest.getNotifyUrl())) {
            request.setAttach(wxpayWebPayRequest.getNotifyUrl());
        }
        request.setTradeType(WxpayTradeTypeEnum.APP.name());
        request.setSign(signatureService.sign(BeanMapUtils.toMap(request), PayChannelEnum.WXPAY_APP));
        return request;
    }


    /**
     * 构造h5预支付对象
     * 返回给前端的内容
     * @param response
     * @param wxpayWebPayRequest
     * @return
     */
    public WxpayWebPrePayRequest buildWxpayWebPrePay(WxpayUnifiedOrderResponse response, WxpayWebPayRequest wxpayWebPayRequest) {
        if (response == null || wxpayWebPayRequest == null) {
            return null;
        }

        WxpayWebPrePayRequest request = new WxpayWebPrePayRequest();

        request.setAppId(wxpayConfig.getAppid());
        request.setTimeStamp(String.valueOf(currentTimeMillis() / 1000));
        request.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
        request.setPackageStr("prepay_id=" + response.getPrepayId());
        request.setSignType("MD5");
        request.setPaySign(signatureService.sign(BeanMapUtils.toMap(request), PayChannelEnum.WXPAY_JSAPI));
        request.setReturnUrl(wxpayWebPayRequest.getReturnUrl());

        return request;
    }

    /**
     * 构造WxpayPrepayResult(app预支付订单)对象
     * @param response
     * @return
     */
    public WxpayAppPrepayRequest buildWxpayAppPrepay(WxpayUnifiedOrderResponse response) {
        WxpayAppPrepayRequest result = new WxpayAppPrepayRequest();

        result.setAppId(response.getAppid());
        result.setPartnerId(response.getMchId());
        result.setTimeStamp(String.valueOf(currentTimeMillis() / 1000));
        result.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
        result.setPackageStr("Sign=WXPay");
        result.setPrepayId(response.getPrepayId());
        result.setSign(signatureService.sign(BeanMapUtils.toMap(result), PayChannelEnum.WXPAY_APP));

        return result;
    }

}
