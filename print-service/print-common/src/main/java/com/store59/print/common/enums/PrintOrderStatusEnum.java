/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.print.common.enums;

/**
 * 打印店订单，枚举码
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 16/1/14
 * @since 1.0
 */
public enum PrintOrderStatusEnum implements EnumCode<Byte>{

    /** 新订单. */
    INIT((byte) 0),

    /** 已确认订单(未打印). */
    CONFIRMED((byte) 1),

    /** 配送中. */
    SENDING((byte) 2),

    /** 订单最终完成. */
    FINISHED((byte) 4),

    /** 订单被取消. */
    CANCELED((byte) 5),

    /** 已打印. */
    PRINTED((byte) 6);

    private final Byte code;

    /**
     * @param code
     */
    private PrintOrderStatusEnum(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

}
