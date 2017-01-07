/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.oxm;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * O/X Mapping工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月8日
 * @since 1.0
 */
public abstract class OXMUtils {

    private static final Jaxb2Mapper MAPPER = new Jaxb2Mapper();

    /**
     * 将对象转换为XML格式的字符串(使用默认编码).
     * 
     * @param jaxb2Marshaller {@link Jaxb2Marshaller}
     * @return XML格式字符串
     */
    public static <T> String marshal(T instance) {
        return marshal(instance, null);
    }

    /**
     * 将对象转换为XML格式的字符串.
     * 
     * @param jaxb2Marshaller {@link Jaxb2Marshaller}
     * @param charset 字符编码
     * @return XML格式字符串
     */
    public static <T> String marshal(T instance, Charset charset) {
        if (instance == null) {
            return null;
        }

        return MAPPER.marshal(instance, charset);
    }

    /**
     * 将XML格式的字符串转换为对象.
     * 
     * @param xml XML格式的字符串
     * @param clazz 转换后的类型
     * @return 转换后对象
     */
    public static <T> T unmarshal(String xml, Class<T> clazz) {
        if (StringUtils.isBlank(xml) || clazz == null) {
            return null;
        }

        return MAPPER.unmarshal(xml, clazz);
    }

}
