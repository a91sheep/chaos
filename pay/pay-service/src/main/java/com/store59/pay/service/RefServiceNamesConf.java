/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author <a href="mailto:shiyb@59store.com">邦邦</a>
 * @version 1.0 16/5/30
 * @since 1.0
 */
@ConfigurationProperties(prefix = "callService.names")
@Component
public class RefServiceNamesConf {
    private String orderService;

    public String getOrderService() {
        return orderService;
    }

    public void setOrderService(String orderService) {
        this.orderService = orderService;
    }
}
