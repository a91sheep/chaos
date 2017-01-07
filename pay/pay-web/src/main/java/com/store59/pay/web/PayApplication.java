/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * 构建SpringBoot应用.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月18日
 * @since 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.store59")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class PayApplication extends SpringBootServletInitializer {

    /**
     * @see org.springframework.boot.context.web.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PayApplication.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

}
