/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.util.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.util.Assert;

/**
 * 模型转换器注册对象.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月4日
 * @since 1.0
 */
public class ModelConverterRegistry {
    private final Map<ConvertiblePair, ModelConverter<?, ?>> converters = new HashMap<ConvertiblePair, ModelConverter<?, ?>>();

    public ModelConverterRegistry() {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> T convert(Object source, Class<T> targetType) {
        if (source == null) {
            return null;
        }

        ConvertiblePair key = new ConvertiblePair(source.getClass(), targetType);
        ModelConverter converter = converters.get(key);
        Assert.notNull(converter, "Unsupported convert：" + key);

        return (T) converter.convert(source);
    }

    public void register(ModelConverter<?, ?> converter) {
        Assert.notNull(converter, "The ModelConverter must not be null");
        ConvertiblePair typeInfo = getRequiredTypeInfo(converter, ModelConverter.class);
        Assert.notNull(typeInfo,
                "Unable to the determine sourceType <S> and targetType <T> which your ModelConverter<S, T> converts between; declare these generic types.");
        converters.put(typeInfo, converter);
    }

    private ConvertiblePair getRequiredTypeInfo(Object converter, Class<?> genericIfc) {
        Class<?>[] args = GenericTypeResolver.resolveTypeArguments(converter.getClass(), genericIfc);
        return (args != null ? new ConvertiblePair(args[0], args[1]) : null);
    }

}
