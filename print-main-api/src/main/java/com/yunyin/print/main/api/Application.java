/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.yunyin.print.main.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author <a href="mailto:guopf@59store.com">任之</a>
 * @version 1.0 2016/12/9
 * @since 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.yunyin")
@EnableFeignClients(basePackages = "com.yunyin")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
