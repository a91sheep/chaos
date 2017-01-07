/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.model.enums;

import com.store59.kylin.utils.EnumCode;

/**
 * 支付平台.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月29日
 * @since 1.0
 */
public enum PayPlatformEnum implements EnumCode<String> {

    /** 支付宝. */
    ALIPAY("alipay", "支付宝"),

    /** 微信支付. */
    WXPAY("wxpay", "微信支付"),

    /** 59store. */
    STORE59("59store", "59store"),

    /** 天翼翼支付. */
    TIANYI("tianyi", "天翼"),

    ;

    private String code;

    private String desc;

    /**
     * @param code
     * @param desc
     */
    private PayPlatformEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @see com.store59.kylin.utils.EnumCode#getCode()
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

}
