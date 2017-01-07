/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.model.enums;

import com.store59.kylin.utils.EnumCode;

/**
 * 业务结果码.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月4日
 * @since 1.0
 */
public enum BizResultCodeEnum implements EnumCode<Integer> {

    /** 成功. */
    SUCCESS(0, "成功"),

    /** 未知异常. */
    UNKNOWN_EXCEPTION(-1, "未知异常"),

    /** 参数不正确. */
    PARAM_INVALID(1, "参数不正确"),

    /** 签名有误. */
    SIGN_INVALID(2, "签名有误"),

    WXPAY_SCAN_RETURN_CODE_FAIL(2221, "微信刷卡支付返回错误"),

    WXPAY_SCAN_RESULT_CODE_FAIL(2222, "微信刷卡支付返回错误结果"),

    ORDER_QUERY_WXPAY_SCAN_RETURN_CODE_FAIL(2231, "查询微信刷卡支付订单返回错误"),

    ORDER_QUERY_WXPAY_SCAN_RESULT_CODE_FAIL(2232, "查询微信刷卡支付订单返回错误结果"),

    ORDER_QUERY_WXPAY_SCAN_TRADE_STATE_NO_SUCCESS(2233, "订单不是支付成功状态"),
    ORDER_IS_NOT_EXITIS(2250, "该订单不存在"),
    ORDER_PAY_STATUS_FINISHED(2251, "该订单已支付"),
    WXPAY_CREAT_PRE_ORDER_FAIL(2241, "生成预支付订单失败"),
    PREPAY_RETURN_CODE_FAIL(2242, "获取预支付订单return_code!=SUCCESS"),
    PREPAY_RESULT_CODE_FAIL(2243, "获取预支付订单result_code!=SUCCESS"),

    ;

    private int    code;

    private String desc;

    /**
     * @param code
     * @param desc
     */
    private BizResultCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @see com.store59.kylin.utils.EnumCode#getCode()
     */
    @Override
    public Integer getCode() {
        return code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

}
