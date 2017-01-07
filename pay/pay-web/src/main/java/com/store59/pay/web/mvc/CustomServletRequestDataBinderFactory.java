/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.mvc;

import java.util.List;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

/**
 * 自定义的{@link ServletRequestDataBinderFactory}.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月19日
 * @since 1.0
 */
public class CustomServletRequestDataBinderFactory extends ServletRequestDataBinderFactory {

    /**
     * @param binderMethods
     * @param initializer
     */
    public CustomServletRequestDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer) {
        super(binderMethods, initializer);
    }

    /**
     * @see org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory#createBinderInstance(java.lang.Object,
     *      java.lang.String, org.springframework.web.context.request.NativeWebRequest)
     */
    @Override
    protected ServletRequestDataBinder createBinderInstance(Object target, String objectName, NativeWebRequest request) {
        return new CustomServletRequestDataBinder(target, objectName);
    }

}
