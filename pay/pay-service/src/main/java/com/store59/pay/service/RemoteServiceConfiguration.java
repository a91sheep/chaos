/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service;

import com.store59.kylin.rpc.client.utils.ProxyBuilder;
import com.store59.order.common.service.facade.BuyerOrderQueryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:shiyb@59store.com">邦邦</a>
 * @version 1.0 16/5/30
 * @since 1.0
 */
@Configuration
public class RemoteServiceConfiguration {

    @Autowired
    private RefServiceNamesConf refServiceNamesConf;

    /** The socket connect timeout in milliseconds. */
    private static final long DEFAULT_CONNECT_TIMEOUT = 1000;

    /** The socket timeout on requests in milliseconds. */
    private static final long DEFAULT_READ_TIMEOUT    = 5000;

    @Bean
    public BuyerOrderQueryFacade buyerOrderQueryFacade() {
        return (BuyerOrderQueryFacade)buildHessianService(refServiceNamesConf.getOrderService(), "queryBuyerOrder", BuyerOrderQueryFacade.class);
    }

    /**
     * 构建hessian服务.
     *
     * @param serviceUrl 服务地址
     * @param serviceInterface 服务接口
     * @return
     */
    private <T> T buildHessianService(String serviceUrl, String exportName, Class<?> serviceInterface) {
        return ProxyBuilder.create()
                .useHttpClient()
                .setServiceName(serviceUrl)
                .setServiceExportName(exportName)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setReadTimeout(DEFAULT_READ_TIMEOUT)
                .setInterfaceClass(serviceInterface)
                .build();
    }

}
