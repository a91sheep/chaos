/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.converter;

import com.store59.pay.model.event.PayEvent;
import com.store59.pay.util.annotation.Converter;
import com.store59.pay.util.converter.ModelConverter;
import com.store59.pay.web.form.CreditCardAsyncNotifyForm;
import com.store59.pay.web.form.SpendAsyncNotifyForm;

import java.math.BigDecimal;

/**
 * Convert {@link CreditCardAsyncNotifyForm} to {@link PayEvent}.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月4日
 * @since 1.0
 */
@Converter
public class CreditCardAsyncNotifyForm2PayEventConverter implements ModelConverter<CreditCardAsyncNotifyForm, PayEvent> {

    /**
     * @see ModelConverter#convert(Object)
     */
    @Override
    public PayEvent convert(CreditCardAsyncNotifyForm source) {
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
