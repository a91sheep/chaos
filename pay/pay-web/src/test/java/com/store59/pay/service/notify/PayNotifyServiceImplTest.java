package com.store59.pay.service.notify;

import com.store59.dto.common.pay.PayEvent;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/7/1
 * @since 1.0
 */
public class PayNotifyServiceImplTest {

    @Autowired
    private PayNotifyService payNotifyService;

    @Test
    public void sendEvent() throws Exception {
        PayEvent payEvent = new PayEvent();
        payEvent.setPayType("alipay");
        payEvent.setOrderId("03307393523411584135059");

        payNotifyService.sendEvent(payEvent);

    }
}