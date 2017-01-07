/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.mvc;

import java.util.List;

import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 自定义的{@link RequestMappingHandlerAdapter}.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月19日
 * @since 1.0
 */
public class CustomRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

    /**
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#createDataBinderFactory(java.util.List)
     */
    @Override
    protected InitBinderDataBinderFactory createDataBinderFactory(List<InvocableHandlerMethod> binderMethods) throws Exception {
        return new CustomServletRequestDataBinderFactory(binderMethods, getWebBindingInitializer());
    }

}
