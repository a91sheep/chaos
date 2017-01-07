/**
 * This document and its contents are protected by copyright 2005 and owned by 59store.com Inc. The copying and reproduction of this document and/or
 * its content (whether wholly or partly) or any incorporation of the same into any other material in any media or format of any kind is strictly
 * prohibited. All rights are reserved.
 * <p>
 * Copyright (c) 59store.com Inc. 2015
 */
package com.store59.print;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * spring-boot启动类
 *
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2015年12月30日
 * @since 1.1
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.store59")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class ServiceBootApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ServiceBootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceBootApplication.class, args);
    }

}
