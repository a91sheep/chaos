package com.yunyin.print.main.api.common.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/23
 * @since 1.0
 */
@Component
public class ReceiverRegisters {
    @Value("${rabbitmq.event.print.conver}")
    private String docQueueName;

    @Autowired
    private DocReveiver docReveiver;

    private Logger logger = LoggerFactory.getLogger(ReceiverRegisters.class);

    @RabbitListener(queues = "${rabbitmq.event.print.conver}")
    public void docReceiver(String message) throws Exception {
        docReveiver.receive(message);
    }
}
