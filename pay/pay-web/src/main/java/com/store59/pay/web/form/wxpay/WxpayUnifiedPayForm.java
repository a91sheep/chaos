/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.form.wxpay;

import javax.validation.constraints.NotNull;

import com.store59.pay.web.form.PayForm;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.store59.pay.model.enums.PayChannelEnum;

/**
 * 统一支付请求.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月20日
 * @since 1.0
 */
public class WxpayUnifiedPayForm extends PayForm {

    // --------------- 微信特有属性 ---------------

    /** 用户标识. */
    private String openId;

    /** 授权码. */
    private String authCode;

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

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

    /**
     * @see com.store59.pay.web.form.PayForm#getChannel()
     */
    @NotNull(message = "支付渠道不能为null")
    @Override
    public PayChannelEnum getChannel() {
        return super.getChannel();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
