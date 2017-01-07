/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 微信刷卡支付请求参数.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月11日
 * @since 1.0
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class WxpayScanRequest {

    /** 公众账号ID. */
    private String  appid;

    /** 子商户公众账号ID. */
    @XmlElement(name = "sub_appid")
    private String  subAppid;

    /** 商户号. */
    @XmlElement(name = "mch_id")
    private String  mchId;

    /** 子商户号. */
    @XmlElement(name = "sub_mch_id")
    private String  subMchId;

    /** 随机字符串. */
    @XmlElement(name = "nonce_str")
    private String  nonceStr;

    /** 签名. */
    private String  sign;

    /** 商品描述. */
    private String  body;

    /** 商户订单号. */
    @XmlElement(name = "out_trade_no")
    private String  outTradeNo;

    /** 总金额. */
    @XmlElement(name = "total_fee")
    private Integer totalFee;

    /** 终端IP. */
    @XmlElement(name = "spbill_create_ip")
    private String  spbillCreateIp;

    /** 授权码. */
    @XmlElement(name = "auth_code")
    private String  authCode;

    /**
     * @return the appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid the appid to set
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * @return the subAppid
     */
    public String getSubAppid() {
        return subAppid;
    }

    /**
     * @param subAppid the subAppid to set
     */
    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
    }

    /**
     * @return the mchId
     */
    public String getMchId() {
        return mchId;
    }

    /**
     * @param mchId the mchId to set
     */
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    /**
     * @return the subMchId
     */
    public String getSubMchId() {
        return subMchId;
    }

    /**
     * @param subMchId the subMchId to set
     */
    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    /**
     * @return the nonceStr
     */
    public String getNonceStr() {
        return nonceStr;
    }

    /**
     * @param nonceStr the nonceStr to set
     */
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
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
     * @return the spbillCreateIp
     */
    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    /**
     * @param spbillCreateIp the spbillCreateIp to set
     */
    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
