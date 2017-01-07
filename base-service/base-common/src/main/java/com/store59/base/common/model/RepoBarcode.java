/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.common.model;

import java.io.Serializable;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * RepoBarcode 对象实体
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/10
 * @since 1.0
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class RepoBarcode implements Serializable  {

    private String barcode;

    private Integer rid;

    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public String getBarcode(){
        return this.barcode;
    }

    public void setRid(Integer rid){
        this.rid = rid;
    }

    public Integer getRid(){
        return this.rid;
    }

}