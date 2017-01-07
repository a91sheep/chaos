/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 请求日志拦截器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月20日
 * @since 1.0
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(request.getMethod());
            sb.append(" ");
            sb.append(request.getRequestURI());
            String query = request.getQueryString();
            if (query != null && query.length() > 0) {
                sb.append("?");
                sb.append(query);
            }
            logger.info(sb.toString());
        }

        return true;
    }

}
