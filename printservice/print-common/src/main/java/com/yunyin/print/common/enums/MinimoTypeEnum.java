package com.yunyin.print.common.enums;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/22
 * @since 1.0
 */
public enum MinimoTypeEnum {
    /**
     * 不缩印
     */
    NO((byte) 1),

    /**
     * 二合一
     */
    TWO((byte) 2),

    /**
     * 四合一
     */
    FOUR((byte) 3);

    private final byte code;

    private MinimoTypeEnum(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
