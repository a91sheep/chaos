/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import com.store59.pay.util.annotation.MapKey;

/**
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/8
 * @since 1.0
 */
public class WxpayWebPrePayRequest {

    /** 公众号id. */
    private String appId;

    /** 时间戳. */
    private String timeStamp;

    /** 随机字符串. */
    private String nonceStr;

    /** 订单详情扩展字符串. */
    @MapKey("package")
    private String packageStr;

    /** 签名方式. */
    private String signType;

    /** 签名. */
    private String paySign;

    /** 同步跳转的url **/
    private String returnUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
