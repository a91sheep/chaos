/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.util.lang;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.store59.kylin.utils.JsonUtil;

/**
 * 对象转字符串工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月29日
 * @since 1.0
 */
public abstract class ObjectToStringUtils {

    /**
     * 通过反射将对象转换成字符串.
     * 
     * @param object
     * @return
     */
    public static String reflectionToString(Object object) {
        return ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * 对象转换成JSON字符串.
     * 
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        return JsonUtil.getJsonFromObject(object);
    }

}
