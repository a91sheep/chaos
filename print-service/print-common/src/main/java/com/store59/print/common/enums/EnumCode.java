/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.print.common.enums;

/**
 * 枚举类，基础接口
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 16/1/14
 * @since 1.0
 */
public interface EnumCode<T> {

    /**
     * 获取枚举码.
     *
     * @return 枚举码
     */
    T getCode();
}
