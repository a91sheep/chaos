/*
 * Copyright 2015 © 59store.com.
 *
 * Repo.java
 *
 */
package com.store59.base.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Created by shanren on 15/7/24.
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class Repo implements Serializable {

    private Integer rid;
    private Byte status;
    private Byte type;
    private Byte close;
    private Integer cateId;
    private Double price;
    private Double marketPrice;
    private String code;
    private String name;
    private String defaultImage;
    private String tip;
    private String pinyin;
    private Double profitRatio;
    private Byte isNew;
    private Integer noSale;
    private Byte   cateType;
    private String  relationRids;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Byte getClose() {
        return close;
    }

    public void setClose(Byte close) {
        this.close = close;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Double getProfitRatio() {
        return profitRatio;
    }

    public void setProfitRatio(Double profitRatio) {
        this.profitRatio = profitRatio;
    }

    public Byte getIsNew() {
        return isNew;
    }

    public void setIsNew(Byte isNew) {
        this.isNew = isNew;
    }

    public Integer getNoSale() {
        return noSale;
    }

    public void setNoSale(Integer noSale) {
        this.noSale = noSale;
    }

    public Byte getCateType() {
        return cateType;
    }

    public void setCateType(Byte cateType) {
        this.cateType = cateType;
    }

    public String getRelationRids() {
        return relationRids;
    }

    public void setRelationRids(String relationRids) {
        this.relationRids = relationRids;
    }
}
