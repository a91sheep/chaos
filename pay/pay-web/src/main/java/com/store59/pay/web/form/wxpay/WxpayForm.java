/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.form.wxpay;

import com.store59.pay.web.form.PayForm;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 微信支付表单.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月4日
 * @since 1.0
 */
public class WxpayForm extends PayForm {

    /** 用户标识. */
    @NotBlank(message = "openId不能为空")
    private String openId;

    /**
     * Gets open id.
     *
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * Sets open id.
     *
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @NotBlank(message = "同步通知地址不能为空")
    @Override
    public String getReturnUrl() {
        return super.getReturnUrl();
    }
}
