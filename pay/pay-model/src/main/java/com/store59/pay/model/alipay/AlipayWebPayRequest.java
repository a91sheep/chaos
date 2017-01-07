/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.alipay;

import java.math.BigDecimal;

/**
 * 支付宝发起web支付需要的参数(仅接入订单系统的业务有效)
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/5
 * @since 1.0
 */
public class AlipayWebPayRequest {
    private String orderId;
    private String type;
    private String returnUrl;
    private String notifyUrl;

    private String     foodName;
    private BigDecimal money;

    /* 店铺类型(不应该放到这里,历史原因) */
    private String shopType;

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

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
}
