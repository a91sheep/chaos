/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.util.converter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 模型转换器工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月4日
 * @since 1.0
 */
public abstract class ModelConverterUtils {
    private static final ModelConverterRegistry REGISTRY = new ModelConverterRegistry();

    /**
     * 注册模型转换器.
     * 
     * @param converter {@link ModelConverter}
     */
    public static void register(ModelConverter<?, ?> converter) {
        REGISTRY.register(converter);
    }

    /**
     * 将原对象转换成目标类型对象.
     * 
     * @param source 原对象
     * @param targetType 目标类型
     * @return 目标类型对象
     */
    public static <T> T convert(Object source, Class<T> targetType) {
        return REGISTRY.convert(source, targetType);
    }

    /**
     * Convert List<{@code S}> to List<{@code T}>.
     * 
     * @param sourceList 原对象列表
     * @param targetType 目标类型
     * @return 目标类型对象列表
     */
    public static <S, T> List<T> convert(List<S> sourceList, Class<T> targetType) {
        if (sourceList == null) {
            return null;
        }

        return sourceList.stream().filter(e -> e != null).map(e -> REGISTRY.convert(e, targetType)).collect(Collectors.toList());
    }

}
