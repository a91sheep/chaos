/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 16/5/12
 * @since 1.0
 */
@Configuration
@EnableRabbit
public class RabbitMqConfiguration {

    @Value("${mq.event.push.concurrentConsumers:5}")
    private int concurrentConsumers;

    @Value("${mq.event.push.maxConcurrentConsumers:20}")
    private int maxConcurrentConsumers;


    @Bean
    SimpleRabbitListenerContainerFactory container(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory container = new SimpleRabbitListenerContainerFactory();
        container.setConnectionFactory(connectionFactory);
        container.setConcurrentConsumers(concurrentConsumers);
        container.setMaxConcurrentConsumers(maxConcurrentConsumers);
        return container;
    }
}
