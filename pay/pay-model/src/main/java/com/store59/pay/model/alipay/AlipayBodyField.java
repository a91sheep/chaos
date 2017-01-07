/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.alipay;

/**
 * 和支付宝数据库交互时body字段的内容
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/1/19
 * @since 1.0
 */

public class AlipayBodyField {
    //要同步通知的应用地址
    private String appReturnUrl;
    //要异步通知的应用地址
    private String appNotifyUrl;

    /**
     * Gets app return url.
     *
     * @return the app return url
     */
    public String getAppReturnUrl() {
        return appReturnUrl;
    }

    /**
     * Sets app return url.
     *
     * @param appReturnUrl the app return url
     */
    public void setAppReturnUrl(String appReturnUrl) {
        this.appReturnUrl = appReturnUrl;
    }

    /**
     * Gets app notify url.
     *
     * @return the app notify url
     */
    public String getAppNotifyUrl() {
        return appNotifyUrl;
    }

    /**
     * Sets app notify url.
     *
     * @param appNotifyUrl the app notify url
     */
    public void setAppNotifyUrl(String appNotifyUrl) {
        this.appNotifyUrl = appNotifyUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.appReturnUrl = returnUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.appNotifyUrl = notifyUrl;
    }


}
