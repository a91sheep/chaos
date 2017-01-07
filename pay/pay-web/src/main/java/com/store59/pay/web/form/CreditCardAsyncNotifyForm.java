/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.form;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 信用钱包异步通知表单.
 * 
 * @author <a href="mailto:liaoly@59store.com">凌云</a>
 * @version 1.0 2016年3月28日
 * @since 1.0
 */
public class CreditCardAsyncNotifyForm {

    /** 支付结果码. */
    @NotNull(message = "status不能为空")
    private Integer status;

    /** 支付结果描述. */
    @NotBlank(message = "msg不能为空")
    private String  msg;

    /** 订单号. */
    @NotBlank(message = "orderId不能为空")
    private String  orderId;

    /** 白花花交易号. */
    @NotBlank(message = "tradeNo不能为空")
    private String  tradeNo;

    /** 订单总金额. */
    @NotBlank(message = "money不能为空")
    private String  money;

    /** 通知地址. */
    private String  notifyUrl;

    /** 付款时间. */
    @NotBlank(message = "payTime不能为空")
    private String  payTime;

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
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
     * @return the tradeNo
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * @param tradeNo the tradeNo to set
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * @return the money
     */
    public String getMoney() {
        return money;
    }

    /**
     * @param money the money to set
     */
    public void setMoney(String money) {
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
     * @return the payTime
     */
    public String getPayTime() {
        return payTime;
    }

    /**
     * @param payTime the payTime to set
     */
    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    /**
     * 判断白花花支付是否成功.
     *
     * @return true：成功，false：失败
     */
    public boolean isSuccess() {
        return ObjectUtils.equals(status, 0);
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
