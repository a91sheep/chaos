/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.form.wxpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.store59.pay.model.constants.WxpayConstants;
import com.store59.pay.model.enums.WxpayTradeTypeEnum;
import com.store59.pay.model.wxpay.WxpayBaseResponse;

/**
 * 微信支付异步通知表单.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月8日
 * @since 1.0
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class WxpayAsyncNotifyForm extends WxpayBaseResponse {

    /** 业务结果. */
    @NotBlank(message = "result_code不能为空")
    @XmlElement(name = "result_code")
    private String             resultCode;

    /** 交易类型. */
    @NotBlank(message = "trade_type不能为空")
    @XmlElement(name = "trade_type")
    private WxpayTradeTypeEnum tradeType;

    /** 订单总金额，单位为分. */
    @NotBlank(message = "total_fee不能为空")
    @XmlElement(name = "total_fee")
    private int                totalFee;

    /** 微信支付订单号. */
    @NotBlank(message = "transaction_id不能为空")
    @XmlElement(name = "transaction_id")
    private String             transactionId;

    /** 商户订单号. */
    @NotBlank(message = "out_trade_no不能为空")
    @XmlElement(name = "out_trade_no")
    private String             outTradeNo;

    /** 商家数据包. */
    @NotBlank(message = "attach不能为空")
    @XmlElement(name = "attach")
    private String             attach;

    /** 支付完成时间. */
    @NotBlank(message = "time_end不能为空")
    @XmlElement(name = "time_end")
    private String             timeEnd;

    @XmlElement(name = "openid")
    private String openid = "";

    /**
     * @return the resultCode
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @return the tradeType
     */
    public WxpayTradeTypeEnum getTradeType() {
        return tradeType;
    }

    /**
     * @param tradeType the tradeType to set
     */
    public void setTradeType(WxpayTradeTypeEnum tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * @return the totalFee
     */
    public int getTotalFee() {
        return totalFee;
    }

    /**
     * @param totalFee the totalFee to set
     */
    public void setTotalFee(int totalFee) {
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
     * @return the attach
     */
    public String getAttach() {
        return attach;
    }

    /**
     * @param attach the attach to set
     */
    public void setAttach(String attach) {
        this.attach = attach;
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

    /**
     * Gets openid.
     *
     * @return the openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * Sets openid.
     *
     * @param openid the openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @see com.store59.pay.model.wxpay.WxpayBaseResponse#isSuccess()
     */
    @Override
    public boolean isSuccess() {
        return super.isSuccess() && StringUtils.equals(this.getResultCode(), WxpayConstants.SUCCESS);
    }

}
