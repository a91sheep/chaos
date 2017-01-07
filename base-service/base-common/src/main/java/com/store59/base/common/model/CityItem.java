/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.base.common.model;

import java.io.Serializable;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * CityItem 对象实体
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 15/11/13
 * @since 1.0
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class CityItem implements Serializable {

    private Integer itemId;

    private Integer cityId;

    private Integer rid;

    private Double  profitRatio;

    private Double  price;

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemId() {
        return this.itemId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCityId() {
        return this.cityId;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getRid() {
        return this.rid;
    }

    public void setProfitRatio(Double profitRatio) {
        this.profitRatio = profitRatio;
    }

    public Double getProfitRatio() {
        return this.profitRatio;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}