/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 16/5/12
 * @since 1.0
 */
@Component
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * mq. exchange
     */
    private final static String EXCHNAGE = "topic_refund";

    private final static String ROUTEKEY_PREFIX = "refund.callback";

    private final static String MQ_SEND_ENTRY = "event-entry";

    public void sendMessage(String routeKey, String message) {
        rabbitTemplate.convertAndSend(EXCHNAGE, String.format("%s%s%s", ROUTEKEY_PREFIX, ".", routeKey), message);
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MQ_SEND_ENTRY, message);
    }

}
