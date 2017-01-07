/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.store59.pay.model.enums.PayChannelEnum;

/**
 * 支付表单.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月22日
 * @since 1.0
 */
public class PayForm {

    /** 支付渠道. */
    private PayChannelEnum channel;

    /** 订单号. */
    @NotBlank(message = "订单号不能为空")
    private String         orderId;

    /** 商品标题. */
    @NotBlank(message = "商品标题不能为空")
    private String         foodName;

    /** 订单总金额，单位分. */
    @DecimalMin(value = "0.01", message = "订单总金额不能小于一分钱")
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal     money;

    /** 通知地址. */
    private String         notifyUrl;

    /** 支付完成的跳转地址. */
    private String         returnUrl;

    /* web支付类型  wap表示手机h5支付*/
    @Deprecated
    private String         type;

    /* 店铺类型(不应该放到这里,历史原因) */
    private String shopType;

    /**
     * @return the channel
     */
    public PayChannelEnum getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(PayChannelEnum channel) {
        this.channel = channel;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the foodName
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * @param foodName the foodName to set
     */
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    /**
     * @return the money
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * @param money the money to set
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * @return the notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * @param notifyUrl the notifyUrl to set
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * @return the returnUrl
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * @param returnUrl the returnUrl to set
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Deprecated
    public String getType() {
        return type;
    }

    @Deprecated
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets shop type.
     *
     * @return the shop type
     */
    public String getShopType() {
        return shopType;
    }

    /**
     * Sets shop type.
     *
     * @param shopType the shop type
     */
    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
