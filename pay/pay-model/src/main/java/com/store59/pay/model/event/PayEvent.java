/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.model.event;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.store59.pay.model.enums.BizResultCodeEnum;

/**
 * 支付事件.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月30日
 * @since 1.0
 */
public class PayEvent {

    /** 支付结果码. */
    private Integer    status;

    /** 支付结果描述. */
    private String     msg;

    /** 支付类型. */
    private String     payType;

    /** 订单号. */
    private String     orderId;

    /** 交易号. */
    private String     tradeNo;

    /** 订单总金额. */
    private BigDecimal money;

    /** 付款时间. */
    private String     payTime;

    /** 买家id号(支付宝是以2088开头的纯16位数字, 微信是Openid). */
    private String buyerId = "";

    /** 买家账号(联系方式, 手机号或者邮箱, 目前仅支付宝有内容). */
    private String buyerContact = "";


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
     * @return the payType
     */
    public String getPayType() {
        return payType;
    }

    /**
     * @param payType the payType to set
     */
    public void setPayType(String payType) {
        this.payType = payType;
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
     * Gets buyer id.
     *
     * @return the buyer id
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * Sets buyer id.
     *
     * @param buyerId the buyer id
     */
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * Gets buyer contact.
     *
     * @return the buyer contact
     */
    public String getBuyerContact() {
        return buyerContact;
    }

    /**
     * Sets buyer contact.
     *
     * @param buyerContact the buyer contact
     */
    public void setBuyerContact(String buyerContact) {
        this.buyerContact = buyerContact;
    }

    /**
     * 设置业务处理结果信息.
     * 
     * @param bizResultCode {@link BizResultCodeEnum}
     */
    public void setBizResult(BizResultCodeEnum bizResultCode) {
        if (bizResultCode != null) {
            setStatus(bizResultCode.getCode());
            setMsg(bizResultCode.getDesc());
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
