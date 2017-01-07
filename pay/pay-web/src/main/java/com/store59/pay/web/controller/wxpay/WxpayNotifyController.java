/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.wxpay;

import java.util.Map;

import com.store59.pay.model.constants.WxpayConstants;
import com.store59.pay.model.enums.PayPlatformEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.enums.WxpayTradeTypeEnum;
import com.store59.pay.model.event.PayEvent;
import com.store59.pay.model.exception.PayException;
import com.store59.pay.model.wxpay.WxpayResponse;
import com.store59.pay.service.notify.PayNotifyService;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.converter.ModelConverterUtils;
import com.store59.pay.util.lang.XmlUtils;
import com.store59.pay.util.oxm.OXMUtils;
import com.store59.pay.web.enums.ViewResultCodeEnum;
import com.store59.pay.web.form.wxpay.WxpayAsyncNotifyForm;

/**
 * 微信支付通知控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月8日
 * @since 1.0
 */
@Controller
public class WxpayNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(WxpayNotifyController.class);

    @Autowired
    private Validator           validator;

    @Autowired
    private SignatureService    signatureService;

    @Autowired
    private PayNotifyService    payNotifyService;

    @RequestMapping(value = "/wxpay/notify", method = RequestMethod.POST, produces = { MediaType.TEXT_XML_VALUE })
    @ResponseBody
    public WxpayResponse asyncNotify(@RequestBody String notify, BindingResult result) throws Exception {
        return asyncNotify(notify, PayChannelEnum.WXPAY_APP.getShortName(), result);
    }

    /**
     * 微信支付异步通知回调.
     * 
     * @param notify 异步通知
     * @param result {@link BindingResult}
     * @return {@link WxpayResponse}
     * @throws Exception
     */
    @RequestMapping(value = "/{channel}/notify", method = RequestMethod.POST, produces = { MediaType.TEXT_XML_VALUE })
    @ResponseBody
    public WxpayResponse asyncNotify(@RequestBody String notify, @PathVariable String channel, BindingResult result) throws Exception {
        logger.info("【微信支付异步回调】支付方式: {}, 收到到参数为: {}", channel, notify);

        WxpayAsyncNotifyForm notifyForm = OXMUtils.unmarshal(notify, WxpayAsyncNotifyForm.class);
        ValidationUtils.invokeValidator(validator, notify, result);
        if (result.hasErrors()) {
            logger.error("【微信支付异步回调】参数有误：notify={}，errors={}", notify, result.getAllErrors());
            return new WxpayResponse(false, result.getFieldError().getDefaultMessage());
        }

        if (!notifyForm.isSuccess()) {
            logger.error("【微信支付异步回调】交易失败：notify={}", notify);
            return new WxpayResponse(false, ViewResultCodeEnum.UNKNOWN_ERROR.getMsg());
        }

        PayChannelEnum payChannel = PayChannelEnum.getEnum(PayPlatformEnum.WXPAY, channel);
        if (payChannel == null) {
            logger.error("【微信支付异步回调】微信支付渠道不支持：{}", channel);
            return new WxpayResponse(false, WxpayConstants.FAIL);
        }

        Map<String, String> parameters = XmlUtils.parse2Map(notify);
        if (!signatureService.verify(parameters, payChannel)) {
            return new WxpayResponse(false, ViewResultCodeEnum.SIGN_INVALID.getMsg());
        }

        PayEvent payEvent = buildPayEvent(notifyForm);
        if (payNotifyService.sendEvent(notifyForm.getAttach(), payEvent)) {
            return new WxpayResponse(true, ViewResultCodeEnum.SUCCESS.getMsg());
        } else {
            return new WxpayResponse(false, ViewResultCodeEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    /**
     * 构建{@link PayEvent}对象.
     * 
     * @param notifyForm {@link WxpayAsyncNotifyForm}
     * @return {@link PayEvent}
     */
    private PayEvent buildPayEvent(WxpayAsyncNotifyForm notifyForm) {
        PayEvent payEvent = ModelConverterUtils.convert(notifyForm, PayEvent.class);
        payEvent.setBizResult(BizResultCodeEnum.SUCCESS);
        //历史原因 wxpay是h5 wxpay_app是微信app支付
        PayChannelEnum payChannelEnum = getPayChannel(notifyForm);
        if(payChannelEnum == PayChannelEnum.WXPAY_APP){
            payEvent.setPayType(payChannelEnum.getCode());
        }else{
            payEvent.setPayType(payChannelEnum.getPlatform().getCode());
        }
        return payEvent;
    }

    /**
     * 获取支付渠道.
     * 
     * @param notifyForm {@link WxpayAsyncNotifyForm}
     * @return {@link PayChannelEnum}
     */
    private PayChannelEnum getPayChannel(WxpayAsyncNotifyForm notifyForm) {
        if (notifyForm.getTradeType() == WxpayTradeTypeEnum.APP) {
            return PayChannelEnum.WXPAY_APP;
        }

        if (notifyForm.getTradeType() == WxpayTradeTypeEnum.JSAPI) {
            return PayChannelEnum.WXPAY_JSAPI;
        }

        throw new PayException("不支持该交易类型：" + notifyForm.getTradeType());
    }

}
