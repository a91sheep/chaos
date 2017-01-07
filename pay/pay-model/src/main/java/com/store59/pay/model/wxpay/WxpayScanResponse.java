/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信刷卡支付返回信息.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月11日
 * @since 1.0
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class WxpayScanResponse extends WxpayBaseBizResponse {

    /** 总金额. */
    @XmlElement(name = "total_fee")
    private Integer totalFee;

    /** 微信支付订单号. */
    @XmlElement(name = "transaction_id")
    private String  transactionId;

    /** 商户订单号. */
    @XmlElement(name = "out_trade_no")
    private String  outTradeNo;

    /** 支付完成时间. */
    @XmlElement(name = "time_end")
    private String  timeEnd;

    /**
     * @return the totalFee
     */
    public Integer getTotalFee() {
        return totalFee;
    }

    /**
     * @param totalFee the totalFee to set
     */
    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

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
     * @return the timeEnd
     */
    public String getTimeEnd() {
        return timeEnd;
    }

    /**
     * @param timeEnd the timeEnd to set
     */
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

}
