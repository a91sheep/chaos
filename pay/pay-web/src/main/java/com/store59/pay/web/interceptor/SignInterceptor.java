/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.store59.pay.service.config.PayConfig;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.web.enums.ViewResultCodeEnum;
import com.store59.pay.web.model.ViewResult;
import com.store59.pay.web.utils.ServletRequestUtils;

/**
 * 签名拦截器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月20日
 * @since 1.0
 */
@Component
public class SignInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private PayConfig           payConfig;

    @Autowired
    private SignatureService    signatureService;

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!payConfig.isVerifySign()) {
            return true;
        }

        Map<String, String> parameterMap = ServletRequestUtils.getParameterMap(request);
        if (!signatureService.verify(parameterMap)) {
            logger.error("签名无效，请求参数为：{}", parameterMap);
            ServletRequestUtils.writeResult(response, new ViewResult<>(ViewResultCodeEnum.SIGN_INVALID).toResult());
            return false;
        }

        return true;
    }

}
