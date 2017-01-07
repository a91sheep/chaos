/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.alipay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.alipay.AlipayBodyField;
import com.store59.pay.model.constants.AlipayConstants;
import com.store59.pay.model.enums.AlipayTradeStatusEnum;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.enums.PayPlatformEnum;
import com.store59.pay.model.event.PayEvent;
import com.store59.pay.service.notify.PayNotifyService;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.converter.ModelConverterUtils;
import com.store59.pay.web.form.alipay.AlipayAsyncNotifyForm;
import com.store59.pay.web.utils.ServletRequestUtils;

/**
 * 支付宝通知控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
@Controller
@RequestMapping(value = "/alipay")
public class AlipayNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(AlipayNotifyController.class);

    @Autowired
    private SignatureService    signatureService;

    @Autowired
    private PayNotifyService    payNotifyService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    /**
     * 支付宝网页支付异步回调.
     * 
     * @param request
     * @param notifyForm
     * @param result
     * @return
     * @throws Exception
     * @deprecated Use {@link #asyncNotify(HttpServletRequest, String, AlipayAsyncNotifyForm, BindingResult)} instead
     */
    @Deprecated
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public String web(HttpServletRequest request, @Valid AlipayAsyncNotifyForm notifyForm, BindingResult result) throws Exception {
        return asyncNotify(request, PayChannelEnum.ALIPAY_PC.getShortName(), notifyForm, result);
    }

    /**
     * 支付宝异步回调.
     * 
     * @param request
     * @param channel
     * @param notifyForm
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{channel}/notify", method = RequestMethod.POST)
    @ResponseBody
    public String asyncNotify(HttpServletRequest request, @PathVariable String channel, @Valid AlipayAsyncNotifyForm notifyForm, BindingResult result)
            throws Exception {
        Map<String, String> parameterMap = ServletRequestUtils.getParameterMap(request);
        logger.info("【支付宝异步回调】接收到回调，参数为：{}", parameterMap);

        if (result.hasErrors()) {
            logger.error("【支付宝异步回调】参数有误：notifyForm={}，errors={}", notifyForm, result.getAllErrors());
            return result.getFieldError().getDefaultMessage();
        }

        PayChannelEnum payChannel = PayChannelEnum.getEnum(PayPlatformEnum.ALIPAY, channel);
        logger.info("【支付宝异步回调】本次支付方式: {}", JsonUtil.getJsonFromObject(payChannel));
        if (payChannel == null) {
            logger.error("【支付宝异步回调】支付宝支付渠道不支持：{}", channel);
            return AlipayConstants.FAIL;
        }

        if (!signatureService.verify(parameterMap, payChannel)) {
            logger.error("【支付宝异步回调】验证签名失败，参数为：{}", parameterMap);
            return AlipayConstants.FAIL;
        }

        // 忽略非成功的交易状态
        if (notifyForm.getTradeStatus() != AlipayTradeStatusEnum.TRADE_SUCCESS) {
            logger.info("【支付宝异步回调】交易状态不是完成, {}", notifyForm.getTradeStatus());
            return AlipayConstants.SUCCESS;
        }

        String body = notifyForm.getBody();
        logger.info("【支付宝异步回调】body里面的字段内容 {}", body);

        /**
         * 此处注意版本兼容问题
         * body字段里可能是个url也可能是个json格式的字符
         * 若为json格式,包含app_notify_url和app_return_url两字段
         *
         */
        String notifyUrl = "";
        if (StringUtils.startsWith(body, "{")) {
            //body是个josn字符
            AlipayBodyField alipayBodyField = JsonUtil.getObjectFromJson(body, new TypeReference<AlipayBodyField>(){});
            notifyUrl = alipayBodyField.getAppNotifyUrl();
        }else{
            //body 是个链接
            notifyUrl = body;
        }
        logger.info("【支付宝异步回调】异步通知应用的地址:{}", notifyUrl);

        PayEvent payEvent = ModelConverterUtils.convert(notifyForm, PayEvent.class);
        payEvent.setBizResult(BizResultCodeEnum.SUCCESS);
        //这里有历史兼容性问题 除支付宝扫码付返回alipay_scan  其它的都返回alipay
        if(payChannel == PayChannelEnum.ALIPAY_SCAN){
            payEvent.setPayType(PayChannelEnum.ALIPAY_SCAN.getCode());
        }else{
            payEvent.setPayType(payChannel.getPlatform().getCode());
        }

        return payNotifyService.sendEvent(notifyUrl, payEvent) ? AlipayConstants.SUCCESS : AlipayConstants.FAIL;
    }

}
