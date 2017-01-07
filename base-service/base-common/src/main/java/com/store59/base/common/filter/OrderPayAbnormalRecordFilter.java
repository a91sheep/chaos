/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.base.common.filter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常订单Filter
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 15/12/5
 * @since 1.0
 */
public class OrderPayAbnormalRecordFilter implements Serializable{

    private List<String> orderSns;     //订单号列表

    private Byte orderType;     //订单类型
    
    private String payTradeNo; // 交易流水号

    public List<String> getOrderSns() {
        return orderSns;
    }

    public void setOrderSns(List<String> orderSns) {
        this.orderSns = orderSns;
    }

    public Byte getOrderType() {
        return orderType;
    }

    public void setOrderType(Byte orderType) {
        this.orderType = orderType;
    }

    public String getPayTradeNo() {
        return payTradeNo;
    }

    public void setPayTradeNo(String payTradeNo) {
        this.payTradeNo = payTradeNo;
    }

}
