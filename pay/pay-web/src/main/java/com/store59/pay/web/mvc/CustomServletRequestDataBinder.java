/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.mvc;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

/**
 * 自定义的{@link ServletRequestDataBinder}.
 * 
 * <p>
 * 用于处理参数名包含下划线的请求.
 * </p>
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月19日
 * @since 1.0
 */
public class CustomServletRequestDataBinder extends ExtendedServletRequestDataBinder {

    private final Map<String, String> propertyNameCache = new ConcurrentReferenceHashMap<String, String>(64);

    /**
     * @param target
     * @param objectName
     */
    public CustomServletRequestDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    /**
     * @param target
     */
    public CustomServletRequestDataBinder(Object target) {
        super(target);
    }

    /**
     * @see org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder#addBindValues(org.springframework.beans.MutablePropertyValues,
     *      javax.servlet.ServletRequest)
     */
    @Override
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        super.addBindValues(mpvs, request);
        new ArrayList<>(mpvs.getPropertyValueList()).stream().forEach(e -> {
            if (StringUtils.contains(e.getName(), '_')) {
                try {
                    String propertyName = propertyNameCache.get(e.getName());
                    if (propertyName == null) {
                        propertyName = translate(e.getName());
                        if (StringUtils.isNotBlank(propertyName)) {
                            propertyNameCache.put(e.getName(), propertyName);
                        }
                    }
                    mpvs.addPropertyValue(propertyName, e.getValue());
                } catch (Exception e2) {
                    logger.error("异常,mpvs=" + mpvs, e2);
                }
            }
        });
    }

    protected String translate(String input) {
        if (StringUtils.isBlank(input)) {
            return input;
        }

        int length = input.length();
        StringBuilder result = new StringBuilder(length);
        int resultLength = 0;
        boolean needTranslated = false;
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (c == '_') {
                needTranslated = true;
            } else {
                resultLength++;

                if (needTranslated && resultLength > 1) {
                    result.append(Character.toUpperCase(c));
                } else {
                    result.append(c);
                }

                needTranslated = false;
            }
        }

        return resultLength > 0 ? result.toString() : input;
    }

}
