/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.converter;

import java.math.BigDecimal;
import java.text.ParseException;


import com.store59.pay.model.event.PayEvent;
import com.store59.pay.util.annotation.Converter;
import com.store59.pay.util.converter.ModelConverter;
import com.store59.pay.web.form.wxpay.WxpayAsyncNotifyForm;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Convert {@link WxpayAsyncNotifyForm} to {@link PayEvent}.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月8日
 * @since 1.0
 */
@Converter
public class WxpayAsyncNotifyForm2PayEventConverter implements ModelConverter<WxpayAsyncNotifyForm, PayEvent> {

    /**
     * @see com.store59.pay.util.converter.ModelConverter#convert(java.lang.Object)
     */
    @Override
    public PayEvent convert(WxpayAsyncNotifyForm source) {
        if (source == null) {
            return null;
        }

        PayEvent payEvent = new PayEvent();

        payEvent.setOrderId(source.getOutTradeNo());
        payEvent.setMoney(new BigDecimal(source.getTotalFee()).divide(new BigDecimal("100")));
        payEvent.setTradeNo(source.getTransactionId());
        payEvent.setBuyerId(source.getOpenid());

        try {
            payEvent.setPayTime(String.valueOf(DateUtils.parseDate(source.getTimeEnd(), new String[] { "yyyyMMddHHmmss" }).getTime() / 1000));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return payEvent;
    }

}
