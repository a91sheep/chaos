package com.yunyin.print.common.enums;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
public enum OrderSourceEnum {
    /**
     * APP
     */
    APP((byte) 1),

    /**
     * WEB
     */
    WEB((byte) 2),

    /**
     * H5
     */
    H5((byte) 3),
    /**
     * 终端
     */
    TERMINAL((byte) 4);

    private final byte code;

    private OrderSourceEnum(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
