/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.converter;

import com.store59.pay.model.wxpay.WxpayWebPayRequest;
import com.store59.pay.util.annotation.Converter;
import com.store59.pay.util.converter.ModelConverter;
import com.store59.pay.web.form.wxpay.WxpayForm;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/10
 * @since 1.0
 */
@Converter
public class WxpayForm2WxpayWebPayRequestConverter implements ModelConverter<WxpayForm, WxpayWebPayRequest> {

    @Override
    public WxpayWebPayRequest convert(WxpayForm source) {

        WxpayWebPayRequest wxpayWebPayRequest = new WxpayWebPayRequest();
        BeanUtils.copyProperties(source, wxpayWebPayRequest);

        //前端传过来的是元, 要转换为分
        wxpayWebPayRequest.setMoney(source.getMoney().multiply(new BigDecimal(100)).intValue());
        //以前定义的是openId, 现在统一为openid
        wxpayWebPayRequest.setOpenid(source.getOpenId());

        return wxpayWebPayRequest;
    }
}
