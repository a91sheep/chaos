/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.enums;

/**
 * 微信支付交易状态.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月15日
 * @since 1.0
 */
public enum WxpayTradeStatusEnum {

    /** 支付成功. */
    SUCCESS,

    /** 转入退款. */
    REFUND,

    /** 未支付. */
    NOTPAY,

    /** 已关闭. */
    CLOSED,

    /** 已撤销(刷卡支付). */
    REVOKED,

    /** 用户支付中. */
    USERPAYING,

    /** 支付失败(其他原因，如银行返回失败). */
    PAYERROR,

    /** 已支付. */
    ORDERPAID,

    ;

}
