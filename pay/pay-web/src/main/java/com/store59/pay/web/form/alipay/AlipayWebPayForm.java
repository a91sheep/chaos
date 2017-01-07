/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.form.alipay;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/5
 * @since 1.0
 */
public class AlipayWebPayForm {
    @NotBlank(message = "订单号不能为空")
    private String orderId;

    private String type;
    private String returnUrl;
    private String notifyUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
