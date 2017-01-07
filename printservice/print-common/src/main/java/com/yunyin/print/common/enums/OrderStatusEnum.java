package com.yunyin.print.common.enums;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/12
 * @since 1.0
 */
public enum OrderStatusEnum {
    /**
     * 新订单.
     */
    INIT((byte) 0),

    /**
     * 已确认订单(订单文档处理完成).
     */
    CONFIRMED((byte) 1),

    /**
     * 订单最终完成.
     */
    FINISHED((byte) 2),

    /**
     * 订单被取消.
     */
    CANCELED((byte) 3);


    private final Byte code;

    private OrderStatusEnum(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
