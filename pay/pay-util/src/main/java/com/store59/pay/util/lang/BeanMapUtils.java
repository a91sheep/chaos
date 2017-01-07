/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.util.lang;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.store59.pay.util.annotation.MapKey;

/**
 * JavaBean转化为Map工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月31日
 * @since 1.0
 */
public abstract class BeanMapUtils {

    /**
     * 将JavaBean转化为Map,忽略值为空的字段.
     * 
     * <p>
     * JavaBean的属性值直接转化成String格式(null转换为空字符串)，支持{@link MapKey}注解。
     * </p>
     * 
     * @param bean 对象实例
     * @return Map
     * @see {@link MapKey}
     * @see {@link XmlElement}
     */
    public static Map<String, String> toMap(Object bean) {
        return toMap(bean, true);
    }

    /**
     * 将JavaBean转化为Map.
     * 
     * <p>
     * JavaBean的属性值直接转化成String格式(null转换为空字符串)，支持{@link MapKey}注解。
     * </p>
     * 
     * @param bean 对象实例
     * @param ignoreBlankFieldValue 是否忽略值为空的字段
     * @return Map
     * @see {@link MapKey}
     * @see {@link XmlElement}
     */
    public static Map<String, String> toMap(Object bean, boolean ignoreBlankFieldValue) {
        Map<String, String> map = new HashMap<>();
        ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {

            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);

                String fieldValue = String.valueOf(ObjectUtils.defaultIfNull(field.get(bean), ""));
                if (ignoreBlankFieldValue && StringUtils.isBlank(fieldValue)) {
                    return;
                }

                MapKey mapKey = AnnotationUtils.getAnnotation(field, MapKey.class);
                if (mapKey != null && StringUtils.isNotBlank(mapKey.value())) {
                    map.put(mapKey.value(), fieldValue);
                    return;
                }

                XmlElement xmlElement = AnnotationUtils.getAnnotation(field, XmlElement.class);
                if (xmlElement != null && StringUtils.isNotBlank(xmlElement.name())) {
                    map.put(xmlElement.name(), fieldValue);
                    return;
                }

                map.put(field.getName(), fieldValue);
            }
        });

        return map;
    }

}
