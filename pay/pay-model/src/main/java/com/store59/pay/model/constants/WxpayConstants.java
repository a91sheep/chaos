/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.constants;

/**
 * 微信支付常量.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
public interface WxpayConstants {

    /** 成功标识. */
    String SUCCESS         = "SUCCESS";

    /** 失败标识. */
    String FAIL            = "FAIL";

    /** 下单地址. */
    String ORDER_URL       = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /** 刷卡支付地址. */
    String SCAN_PAY_URL    = "https://api.mch.weixin.qq.com/pay/micropay";

    /** 查询订单地址. */
    String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

}
