/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.converter;

import com.store59.pay.model.event.PayEvent;
import com.store59.pay.util.annotation.Converter;
import com.store59.pay.util.converter.ModelConverter;
import com.store59.pay.web.form.alipay.AlipayAsyncNotifyForm;

/**
 * Convert {@link AlipayAsyncNotifyForm} to {@link PayEvent}.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月4日
 * @since 1.0
 */
@Converter
public class AlipayAsyncNotifyForm2PayEventConverter implements ModelConverter<AlipayAsyncNotifyForm, PayEvent> {

    /**
     * @see com.store59.pay.util.converter.ModelConverter#convert(java.lang.Object)
     */
    @Override
    public PayEvent convert(AlipayAsyncNotifyForm source) {
        if (source == null) {
            return null;
        }

        PayEvent payEvent = new PayEvent();

        payEvent.setOrderId(source.getOutTradeNo());
        payEvent.setMoney(source.getTotalFee());
        payEvent.setTradeNo(source.getTradeNo());
        payEvent.setPayTime(String.valueOf(source.getGmtPayment().getTime() / 1000));
        payEvent.setBuyerId(source.getBuyerId());
        payEvent.setBuyerContact(source.getBuyerEmail());

        return payEvent;
    }

}
