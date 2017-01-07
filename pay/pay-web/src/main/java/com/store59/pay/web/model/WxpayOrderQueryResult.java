/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.model;

import com.store59.pay.model.enums.WxpayTradeStatusEnum;
import com.store59.pay.model.event.PayEvent;

/**
 * 微信支付查询订单结果对象.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月15日
 * @since 1.0
 */
public class WxpayOrderQueryResult extends PayEvent {

    private String               wxErrCode;

    private WxpayTradeStatusEnum tradeState;

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

    /**
     * @return the tradeState
     */
    public WxpayTradeStatusEnum getTradeState() {
        return tradeState;
    }

    /**
     * @param tradeState the tradeState to set
     */
    public void setTradeState(WxpayTradeStatusEnum tradeState) {
        this.tradeState = tradeState;
    }

}
