/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/8
 * @since 1.0
 */
@Component
public class EurekaUrlHelper {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    public String getHost(String serviceName) {
        ServiceInstance instance = loadBalancerClient.choose(serviceName);
        return String.format("http://%s:%s", instance.getHost(), instance.getPort());
    }

    public String getUrl(String serviceName) {
        ServiceInstance instance = loadBalancerClient.choose(serviceName);
        return String.format("http://%s:%s%s",
                instance.getHost(),
                instance.getPort(),
                instance.getMetadata().get("contextPath"));
    }
}
