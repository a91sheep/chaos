/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于对象转换成Map时，覆盖对象的属性名.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月6日
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapKey {

    /**
     * 新属性名.
     * 
     * @return
     */
    String value() default "";

}
