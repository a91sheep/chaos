/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.notify;

import com.store59.pay.model.event.PayEvent;

/**
 * 支付通知服务.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月29日
 * @since 1.0
 */
public interface PayNotifyService {

    /**
     * 发送支付事件.
     * 
     * @param notifyUrl 接受通知的地址
     * @param payEvent {@link PayEvent}
     * @return true：成功，false：失败
     * @deprecated 使用MQ代替
     */
    @Deprecated
    boolean sendEvent(String notifyUrl, PayEvent payEvent);

    /**
     * 发送支付事件.
     * 
     * @param payEvent {@link PayEvent}
     * @return true：成功，false：失败
     */
    boolean sendEvent(com.store59.dto.common.pay.PayEvent payEvent);

}
