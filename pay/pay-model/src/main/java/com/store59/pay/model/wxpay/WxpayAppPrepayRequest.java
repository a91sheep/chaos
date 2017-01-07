/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.store59.pay.util.annotation.MapKey;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * app预支付请求结果.
 * <p>
 * <p>
 * 参考文档：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
 * <p>
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class WxpayAppPrepayRequest implements Serializable {

    /** 公众号id. */
    private String appId;

    /** 商户号. */
    private String partnerId;

    /** 时间戳. */
    private String timeStamp;

    /** 随机字符串. */
    private String nonceStr;

    /** 订单详情扩展字符串. */
    @JsonProperty("package")
    @MapKey("package")
    private String packageStr;

    private String prepayId;

    /** 签名. */
    private String sign;

    /**
     * Gets app id.
     *
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Sets app id.
     *
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * Gets time stamp.
     *
     * @return the timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets time stamp.
     *
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Gets nonce str.
     *
     * @return the nonceStr
     */
    public String getNonceStr() {
        return nonceStr;
    }

    /**
     * Sets nonce str.
     *
     * @param nonceStr the nonceStr to set
     */
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    /**
     * Gets package str.
     *
     * @return the packageStr
     */
    public String getPackageStr() {
        return packageStr;
    }

    /**
     * Sets package str.
     *
     * @param packageStr the packageStr to set
     */
    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
