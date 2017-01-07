/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.enums;

import com.store59.kylin.utils.EnumCode;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * 展示用的结果码.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月28日
 * @since 1.0
 */
public enum PayDetailTypeCodeEnum implements EnumCode<Integer> {

    /** 支付. */
    PAY(1, "支付"),

    /** 未知错误. */
    WITHDRAW(2, "提现"),
    ;

    /** 枚举码. */
    private int    code;

    /** 枚举描述. */
    private String msg;

    /**
     * 构造方法.
     * 
     * @param code 枚举码
     * @param msg 枚举描述
     */
    PayDetailTypeCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @see com.store59.kylin.utils.EnumCode#getCode()
     */
    @Override
    public Integer getCode() {
        return code;
    }

    /**
     * 格式化枚举描述信息.
     * 
     * @return the formatArgs 格式化参数
     */
    public String getMsg(Object... formatArgs) {
        if (ArrayUtils.isEmpty(formatArgs) || StringUtils.isBlank(msg)) {
            return msg;
        }

        return MessageFormat.format(msg, formatArgs);
    }

    public String getMsg() {
        return msg;
    }

}
