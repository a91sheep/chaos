/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.store59.kylin.common.model.Result;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.event.PayEvent;
import com.store59.pay.service.notify.PayNotifyService;
import com.store59.pay.util.converter.ModelConverterUtils;
import com.store59.pay.web.enums.ViewResultCodeEnum;
import com.store59.pay.web.form.SpendAsyncNotifyForm;
import com.store59.pay.web.model.ViewResult;

/**
 * 白花花通知控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月29日
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/spend")
public class SpendNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(SpendNotifyController.class);

    @Autowired
    private PayNotifyService    payNotifyService;

    /**
     * 白花花异步通知.
     * 
     * @param notifyForm
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public Result<?> asyncNotify(@Valid SpendAsyncNotifyForm notifyForm, BindingResult result) throws Exception {
        logger.info("收到【白花花异步回调】，参数为：{}", notifyForm);

        ViewResult<?> viewResult = new ViewResult<>();
        if (result.hasErrors()) {
            logger.error("【白花花异步回调】参数验证有误: errors={}", result.getAllErrors());
            viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
            return viewResult.toResultIgnoreData();
        }

        if (!notifyForm.isSuccess()) {
            logger.error("【白花花异步回调】支付失败：{}", notifyForm);
            viewResult.setResultCode(ViewResultCodeEnum.SPEND_FAILED);
            viewResult.setMsgFormatArgs(notifyForm.getMsg());
            return viewResult.toResult();
        }

        PayEvent payEvent = ModelConverterUtils.convert(notifyForm, PayEvent.class);
        payEvent.setBizResult(BizResultCodeEnum.SUCCESS);
        payEvent.setPayType(PayChannelEnum.STORE59_SPEND.getCode());

        if (payNotifyService.sendEvent(notifyForm.getNotifyUrl(), payEvent)) {
            viewResult.setResultCode(ViewResultCodeEnum.SUCCESS);
        } else {
            viewResult.setResultCode(ViewResultCodeEnum.SPEND_FAILED);
            viewResult.setMsgFormatArgs("业务回调失败");
        }

        return viewResult.toResult();
    }

}
