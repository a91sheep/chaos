/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝配置.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月23日
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {

    /** 合作者身份ID. */
    private String partner;

    /** 签名方式：DSA、RSA、MD5三个值可选，必须大写. */
    private String signType;

    /** 私钥. */
    private String privateKey;

    /** 公钥. */
    private String publicKey;

    /** 参数编码字符集. */
    private String inputCharset;

    /** 扫码支付-客户端号. */
    private String qrAppId;

    /** 扫码支付-公钥. */
    private String qrPublicKey;

    /**
     * @return the partner
     */
    public String getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(String partner) {
        this.partner = partner;
    }

    /**
     * @return the signType
     */
    public String getSignType() {
        return signType;
    }

    /**
     * @param signType the signType to set
     */
    public void setSignType(String signType) {
        this.signType = signType;
    }


    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @return the publicKey
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return the inputCharset
     */
    public String getInputCharset() {
        return inputCharset;
    }

    /**
     * @param inputCharset the inputCharset to set
     */
    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    /**
     * @return the qrAppId
     */
    public String getQrAppId() {
        return qrAppId;
    }

    /**
     * @param qrAppId the qrAppId to set
     */
    public void setQrAppId(String qrAppId) {
        this.qrAppId = qrAppId;
    }

    /**
     * @return the qrPublicKey
     */
    public String getQrPublicKey() {
        return qrPublicKey;
    }

    /**
     * @param qrPublicKey the qrPublicKey to set
     */
    public void setQrPublicKey(String qrPublicKey) {
        this.qrPublicKey = qrPublicKey;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
