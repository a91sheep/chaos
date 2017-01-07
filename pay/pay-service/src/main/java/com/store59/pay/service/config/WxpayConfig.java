/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "wxpay")
public class WxpayConfig {

    /** 公众账号名字(传给wechat-openid项目获取openid). */
    private String mpName;

    /** 密钥. */
    private String key;

    /** 公众账号ID. */
    private String appid;

    /** 子商户公众账号ID. */
    private String subAppid;

    /** 商户号. */
    private String mchId;

    /** 子商户号. */
    private String subMchId;

    /**是否使用服务商模式*/
    private boolean isUseServerModel;

    public String getMpName() {
        return mpName;
    }

    public void setMpName(String mpName) {
        this.mpName = mpName;
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

    public boolean isUseServerModel() {
        return isUseServerModel;
    }

    public void setUseServerModel(boolean useServerModel) {
        isUseServerModel = useServerModel;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
