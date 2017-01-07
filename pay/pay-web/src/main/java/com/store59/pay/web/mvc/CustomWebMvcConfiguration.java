/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.store59.pay.web.interceptor.LogInterceptor;
import com.store59.pay.web.interceptor.SignInterceptor;

/**
 * 自定义的WebMvcConfiguration.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月19日
 * @since 1.0
 */
@Import(WebMvcAutoConfigurationAdapter.class)
@Configuration
public class CustomWebMvcConfiguration extends EnableWebMvcConfiguration {

    private List<String>    signPathPatterns = new ArrayList<>();

    @Autowired
    private LogInterceptor  logInterceptor;

    @Autowired
    private SignInterceptor signInterceptor;

    /**
     * 构造方法.
     */
    public CustomWebMvcConfiguration() {
        signPathPatterns.add("/pay");
        signPathPatterns.add("/alipay/pay");
        signPathPatterns.add("/alipay/qr/info");
        signPathPatterns.add("/wxpay/pay");
        signPathPatterns.add("/wxpay/order_query");
        signPathPatterns.add("/wxpay/swipe_card");
        signPathPatterns.add("/spend/*");
    }

    /**
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#requestMappingHandlerAdapter()
     */
    @Bean
    @Override
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
        RequestMappingHandlerAdapter customAdapter = new CustomRequestMappingHandlerAdapter();
        BeanUtils.copyProperties(adapter, customAdapter, "applicationContext", "returnValueHandlers");
        return customAdapter;
    }

    /**
     * @see org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(logInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
        registry.addInterceptor(signInterceptor).addPathPatterns(signPathPatterns.toArray(new String[signPathPatterns.size()]))
                .excludePathPatterns("/error");
    }

}
