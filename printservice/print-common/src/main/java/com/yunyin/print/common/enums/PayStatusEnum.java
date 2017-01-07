package com.yunyin.print.common.enums;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
public enum PayStatusEnum {
    /**
     * 未支付
     */
    UN_PAY((byte) 1),

    /**
     * 已支付
     */
    HAS_PAYED((byte) 2);

    private final byte code;

    private PayStatusEnum(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
