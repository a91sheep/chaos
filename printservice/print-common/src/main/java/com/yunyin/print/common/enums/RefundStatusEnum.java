package com.yunyin.print.common.enums;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
public enum RefundStatusEnum {
    /**
     * 未退款
     */
    UN_REFUND((byte) 1),

    /**
     * 退款中
     */
    REFUNDING((byte) 2),

    /**
     * 已退款
     */
    HAS_REFUNDED((byte) 3);

    private final byte code;

    private RefundStatusEnum(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
