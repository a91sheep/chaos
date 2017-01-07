/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.common.filter;

import java.io.Serializable;
import java.util.List;

/**
 * ExtractTransaction filter
 *
 * @author <a href="mailto:jiangzq@59store.com">刑天</a>
 * @version 1.0 16/5/4
 * @since 1.0
 */
public class ExtractTransactionFilter implements Serializable{

    private Integer  bizType;       //业务类型, 请参照ExtractTransactionRecord中的常量定义

    private Byte status;            //状态, 请参照ExtractTransactionRecord中的常量定义

    private Integer date;           //时间戳,单位:秒. 检索大于date的所有记录

    private String tradeNo;         //业务流水id
    
    private List<String> tradeNoList;

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<String> getTradeNoList() {
        return tradeNoList;
    }

    public void setTradeNoList(List<String> tradeNoList) {
        this.tradeNoList = tradeNoList;
    }
    
    
}
