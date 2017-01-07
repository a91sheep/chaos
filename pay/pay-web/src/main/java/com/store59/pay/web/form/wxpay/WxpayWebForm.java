/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.form.wxpay;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 微信web端发起支付(仅对接入了订单系统的业务有效)
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/5
 * @since 1.0
 */
public class WxpayWebForm {
    @NotBlank(message = "订单号不能为空")
    private String orderId;

    private String openid;

    /** 由前端传过来的同步返回url. */
    private String returnUrl;

    /** 由openid系统传入, 为前端设置的同步返回的url. */
    private String returnAppUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getReturnAppUrl() {
        return returnAppUrl;
    }

    public void setReturnAppUrl(String returnAppUrl) {
        this.returnAppUrl = returnAppUrl;
    }
}
