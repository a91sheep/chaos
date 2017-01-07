/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.form.wxpay;

import com.store59.pay.web.form.PayForm;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 微信刷卡支付表单.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月11日
 * @since 1.0
 */
public class WxpayScanForm extends PayForm {

    /** 授权码. */
    @NotBlank(message = "authCode不能为空")
    private String authCode;

    /**
     * @return the authCode
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * @param authCode the authCode to set
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

}
