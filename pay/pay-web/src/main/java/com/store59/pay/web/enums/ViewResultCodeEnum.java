/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.enums;

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
public enum ViewResultCodeEnum implements EnumCode<Integer> {

    /** 成功. */
    SUCCESS(0, "成功"),

    /** 未知错误. */
    UNKNOWN_ERROR(-1, "未知错误"),

    /** 正常错误. */
    NORMAL_ERROR(1, "{0}"),

    /** token无效. */
    TOKEN_INVALID(2, "token无效"),

    /** 请求参数有误. */
    PARAM_INVALID(3, "请求参数有误"),

    /** sign无效. */
    SIGN_INVALID(4, "sign无效"),

    /** 白花花支付失败. */
    SPEND_FAILED(2215, "白花花支付失败:{0}"),

    /* 信用钱包支付失败. */
    CREDIT_CARD_FAILED(2216, "信用钱包支付失败:{0}"),

    ALIPAY_GET_QR_INFO_ERROR(2211, "支付宝扫码付获取二维码失败"),

    WXPAY_SIGN_ERROR(2240, "验证签名无效"),

    
    CREAT_WITHDRAW_RECORD_FAIL(2242, "生成提现记录失败"),
    
    WITHDRAW_CONFIRM_FAIL(2243, "提现确认失败"),
    
    SMSCODE_FAIL(2244, "获取短信验证码失败"),
    
    DOWNLOAD_RESULT_FILE_FAIL(2245, "下载结果文件失败"),


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
    ViewResultCodeEnum(int code, String msg) {
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
