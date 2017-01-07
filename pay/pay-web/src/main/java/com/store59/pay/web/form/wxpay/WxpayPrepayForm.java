/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.form.wxpay;

import com.store59.pay.model.enums.PayChannelEnum;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/7/13
 * @since 1.0
 */
public class WxpayPrepayForm {

    /** 支付渠道. */
    private PayChannelEnum channel;

    /** 订单号. */
    @NotBlank(message = "订单号不能为空")
    private String         orderId;

    /** 商品标题. */
    @NotBlank(message = "商品标题不能为空")
    private String         foodName;

    /** 订单总金额，单位元. */
    @DecimalMin(value = "0.01", message = "订单总金额不能小于一分钱")
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal money;

    /** 异步通知地址(应用的地址). */
    private String         attach;

    public PayChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(PayChannelEnum channel) {
        this.channel = channel;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
}
