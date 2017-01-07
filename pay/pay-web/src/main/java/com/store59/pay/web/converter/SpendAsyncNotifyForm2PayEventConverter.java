/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.converter;

import java.math.BigDecimal;

import com.store59.pay.model.event.PayEvent;
import com.store59.pay.util.annotation.Converter;
import com.store59.pay.util.converter.ModelConverter;
import com.store59.pay.web.form.SpendAsyncNotifyForm;

/**
 * Convert {@link SpendAsyncNotifyForm} to {@link PayEvent}.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月4日
 * @since 1.0
 */
@Converter
public class SpendAsyncNotifyForm2PayEventConverter implements ModelConverter<SpendAsyncNotifyForm, PayEvent> {

    /**
     * @see com.store59.pay.util.converter.ModelConverter#convert(java.lang.Object)
     */
    @Override
    public PayEvent convert(SpendAsyncNotifyForm source) {
        if (source == null) {
            return null;
        }

        PayEvent payEvent = new PayEvent();

        payEvent.setOrderId(source.getOrderId());
        payEvent.setMoney(new BigDecimal(source.getMoney()));
        payEvent.setTradeNo(source.getTradeNo());
        payEvent.setPayTime(source.getPayTime());

        return payEvent;
    }

}
