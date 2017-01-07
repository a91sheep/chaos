/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 转换器注解.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Converter {

    /**
     * The value may indicate a suggestion for a logical component name, to be turned into a Spring bean in case of an autodetected component.
     * 
     * @return the suggested component name, if any
     */
    String value() default "";

}
