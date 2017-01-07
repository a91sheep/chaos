/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

/**
 * JavaBean转化为Map工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月31日
 * @since 1.0
 */
public abstract class BeanMapUtils {

    /**
     * 将JavaBean转化为Map.
     * 
     * <p>
     * JavaBean的属性值直接转化成String格式.
     * </p>
     * 
     * @param bean
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> toMap(Object bean) {
        Map<String, String> map = new HashMap<>();
        BeanMap.create(bean).forEach((key, value) -> {
            if (value != null) {
                map.put(String.valueOf(key), String.valueOf(value));
            }
        });

        return map;
    }

}
