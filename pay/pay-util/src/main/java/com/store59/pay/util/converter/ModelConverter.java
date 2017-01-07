/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.util.converter;

/**
 * 模型转换器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月31日
 * @since 1.0
 */
public interface ModelConverter<S, T> {

    /**
     * Convert the source of type S to target type T.
     * 
     * @param source the source object to convert, which must be an instance of S (never {@code null})
     * @return the converted object, which must be an instance of T (potentially {@code null})
     * @throws IllegalArgumentException if the source could not be converted to the desired target type
     */
    T convert(S source);

}
