/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付统一下单返回信息.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class WxpayUnifiedOrderResponse extends WxpayBaseBizResponse {

    // 以下字段在return_code为SUCCESS的时候有返回

    /** 公众账号ID. */
    private String appid;

    /** 商户号. */
    @XmlElement(name = "mch_id")
    private String mchId;

    /** 子商户公众账号ID. */
    @XmlElement(name = "sub_appid")
    private String subAppid;

    /** 子商户号. */
    @XmlElement(name = "sub_mch_id")
    private String subMchId;

    /** 设备号. */
    @XmlElement(name = "device_info")
    private String deviceInfo;

    /** 随机字符串. */
    @XmlElement(name = "nonce_str")
    private String nonceStr;

    /** 签名. */
    private String sign;

    // 以下字段在return_code 和result_code都为SUCCESS的时候有返回

    /** 交易类型. */
    @XmlElement(name = "trade_type")
    private String tradType;

    /** 预支付交易会话标识. */
    @XmlElement(name = "prepay_id")
    private String prepayId;

    /** 二维码链接. */
    @XmlElement(name = "code_url")
    private String codeUrl;

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
     * @return the deviceInfo
     */
    public String getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * @param deviceInfo the deviceInfo to set
     */
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
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
     * @return the tradType
     */
    public String getTradType() {
        return tradType;
    }

    /**
     * @param tradType the tradType to set
     */
    public void setTradType(String tradType) {
        this.tradType = tradType;
    }

    /**
     * @return the prepayId
     */
    public String getPrepayId() {
        return prepayId;
    }

    /**
     * @param prepayId the prepayId to set
     */
    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    /**
     * @return the codeUrl
     */
    public String getCodeUrl() {
        return codeUrl;
    }

    /**
     * @param codeUrl the codeUrl to set
     */
    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

}
