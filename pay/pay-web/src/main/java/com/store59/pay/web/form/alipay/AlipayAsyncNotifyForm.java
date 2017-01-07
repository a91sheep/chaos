/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.form.alipay;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.store59.pay.model.enums.AlipayTradeStatusEnum;

import javax.validation.constraints.NotNull;

/**
 * 支付宝异步通知表单.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public class AlipayAsyncNotifyForm {

    /** 商户网站唯一订单号. */
    @NotBlank(message = "out_trade_no不能为空")
    private String                outTradeNo;

    /** 支付宝交易号. */
    @NotBlank(message = "trade_no不能为空")
    private String                tradeNo;

    /** 交易状态. */
    @NotNull(message = "trade_status不能为空")
    private AlipayTradeStatusEnum tradeStatus;

    /** 商品描述. */
    private String                body;

    /** 交易金额. */
    @NotNull(message = "total_fee不能为空")
    private BigDecimal            totalFee;

    /** 交易付款时间，格式为：yyyy-MM-dd HH:mm:ss. */
    private Date                  gmtPayment;

    /** 买家支付宝用户号, 以2088开头的纯16位数字. */
    private String buyerId = "";

    /** 买家支付宝账号, 可以是Email或手机号码. */
    private String buyerEmail = "";

    /**
     * @return the outTradeNo
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * @param outTradeNo the outTradeNo to set
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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
     * @return the tradeStatus
     */
    public AlipayTradeStatusEnum getTradeStatus() {
        return tradeStatus;
    }

    /**
     * @param tradeStatus the tradeStatus to set
     */
    public void setTradeStatus(AlipayTradeStatusEnum tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the totalFee
     */
    public BigDecimal getTotalFee() {
        return totalFee;
    }

    /**
     * @param totalFee the totalFee to set
     */
    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * 支付宝扫码付,返回的金额自动是totalAmount
     * @param totalFee the totalFee to set
     */
    public void setTotalAmount(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * @return the gmtPayment
     */
    public Date getGmtPayment() {
        return gmtPayment;
    }

    /**
     * @param gmtPayment the gmtPayment to set
     */
    public void setGmtPayment(Date gmtPayment) {
        this.gmtPayment = gmtPayment;
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
     * Gets buyer email.
     *
     * @return the buyer email
     */
    public String getBuyerEmail() {
        return buyerEmail;
    }

    /**
     * Sets buyer email.
     *
     * @param buyerEmail the buyer email
     */
    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
