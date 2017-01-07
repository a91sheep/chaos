/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.enums;

/**
 * 支付渠道.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月18日
 * @since 1.0
 */
public enum PayChannelEnum {

    /** 支付宝手机网页支付. */
    ALIPAY_WAP,

    /** 支付宝PC网页支付. */
    ALIPAY_PC,

    /** 支付宝APP支付. */
    ALIPAY_APP,

    /** 支付宝扫码支付. */
    ALIPAY_QRCODE,

    /** 微信公众号支付. */
    WXPAY_JSAPI,

    /** 微信APP支付. */
    WXPAY_APP,

    /** 白花花支付. */
    SPEND,

    ;

}
