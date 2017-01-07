package com.store59.pay.web.converter;

import com.store59.pay.model.event.PayEvent;
import com.store59.pay.util.annotation.Converter;
import com.store59.pay.util.converter.ModelConverter;
import com.store59.pay.web.form.TianyipayAsyncNotifyForm;

/**
 * 转换器, 翼支付参数转换成我们平台需要的参数
 * Created by 凌云
 * lly835@163.com
 * 2016-04-24 21:59
 */
@Converter
public class TianyipayAsyncNotifyForm2PayEventConverter implements ModelConverter<TianyipayAsyncNotifyForm, PayEvent> {

    /**
     * @see com.store59.pay.util.converter.ModelConverter#convert(java.lang.Object)
     */
    @Override
    public PayEvent convert(TianyipayAsyncNotifyForm source) {
        if (source == null) {
            return null;
        }

        PayEvent payEvent = new PayEvent();

        payEvent.setOrderId(source.getORDERSEQ());
        payEvent.setMoney(source.getORDERAMOUNT());
        payEvent.setTradeNo(source.getUPTRANSEQ());
        payEvent.setPayTime(String.valueOf(System.currentTimeMillis() / 1000));

        return payEvent;
    }
}
