/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import com.store59.dto.common.enums.BizTypeEnum;

import java.math.BigDecimal;

/**
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/5
 * @since 1.0
 */
public class WxpayWebPayRequest {
    private String orderId;

    /** 支付完成后同步返回的url, 只有h5会用到. */
    private String returnUrl;

    /** 通知给应用的url, 未接入订单系统的业务会用到. */
    private String notifyUrl;

    private String openid;

    /** 支付时显示的中文名.(旧系统使用) */
    private String foodName;

    /** 金额, 单位为分(旧系统使用) */
    private Integer money;

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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}
