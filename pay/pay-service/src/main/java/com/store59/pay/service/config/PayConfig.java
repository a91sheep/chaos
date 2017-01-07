/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付系统配置.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月23日
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "pay")
public class PayConfig {

    /** 支付系统地址. */
    private String  url;

    /** 支付系统密钥. */
    private String  key;

    /** 是否验证签名. */
    private boolean isVerifySign;

    /** 系统host, 用于区分各个环境. */
    private String host;

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the isVerifySign
     */
    public boolean isVerifySign() {
        return isVerifySign;
    }

    /**
     * @param isVerifySign the isVerifySign to set
     */
    public void setVerifySign(boolean isVerifySign) {
        this.isVerifySign = isVerifySign;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
