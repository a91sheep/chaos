/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.model;

import com.store59.pay.model.event.PayEvent;

/**
 * 微信刷卡支付结果对象.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月11日
 * @since 1.0
 */
public class WxpayScanResult extends PayEvent {

    private String wxErrCode;

    /**
     * @return the wxErrCode
     */
    public String getWxErrCode() {
        return wxErrCode;
    }

    /**
     * @param wxErrCode the wxErrCode to set
     */
    public void setWxErrCode(String wxErrCode) {
        this.wxErrCode = wxErrCode;
    }

}
