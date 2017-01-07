/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.controller;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.event.PayEvent;
import com.store59.pay.service.notify.PayNotifyService;
import com.store59.pay.util.converter.ModelConverterUtils;
import com.store59.pay.web.enums.ViewResultCodeEnum;
import com.store59.pay.web.form.CreditCardAsyncNotifyForm;
import com.store59.pay.web.form.SpendAsyncNotifyForm;
import com.store59.pay.web.model.ViewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 信用钱包通知控制器.
 * 
 * @author <a href="mailto:liaoly@59store.com">凌云</a>
 * @version 1.0 2016年3月28日
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/credit_card")
public class CreditCardNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(CreditCardNotifyController.class);

    @Autowired
    private PayNotifyService    payNotifyService;

    /**
     * 信用钱包异步通知.
     * 
     * @param notifyForm
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public Result<?> asyncNotify(@Valid CreditCardAsyncNotifyForm notifyForm, BindingResult result) throws Exception {
        logger.info("收到【信用钱包异步回调】，参数为：{}", notifyForm);

        ViewResult<?> viewResult = new ViewResult<>();
        if (result.hasErrors()) {
            logger.error("【信用钱包异步回调】参数验证有误: errors={}", result.getAllErrors());
            viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
            return viewResult.toResultIgnoreData();
        }

        if (!notifyForm.isSuccess()) {
            logger.error("【信用钱包异步回调】支付失败：{}", notifyForm);
            viewResult.setResultCode(ViewResultCodeEnum.CREDIT_CARD_FAILED);
            viewResult.setMsgFormatArgs(notifyForm.getMsg());
            return viewResult.toResult();
        }

        PayEvent payEvent = ModelConverterUtils.convert(notifyForm, PayEvent.class);
        payEvent.setBizResult(BizResultCodeEnum.SUCCESS);
        payEvent.setPayType(PayChannelEnum.STORE59_CREDIT_CARD.getCode());

        if (payNotifyService.sendEvent(notifyForm.getNotifyUrl(), payEvent)) {
            viewResult.setResultCode(ViewResultCodeEnum.SUCCESS);
        } else {
            viewResult.setResultCode(ViewResultCodeEnum.CREDIT_CARD_FAILED);
            viewResult.setMsgFormatArgs("业务回调失败");
        }

        return viewResult.toResult();
    }

}
