/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.enums;

/**
 * 微信支付交易类型.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月8日
 * @since 1.0
 */
public enum WxpayTradeTypeEnum {

    /** 公众号支付. */
    JSAPI,

    /** 原生扫码支付. */
    NATIVE,

    /** app支付. */
    APP,

    /** 刷卡支付. */
    MICROPAY,

    ;

}
