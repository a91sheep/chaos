/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.common.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * RepoFlagSite 对象实体
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class RepoFlagSite implements Serializable  {

    private Integer siteId;

    private Integer flagId;

    public void setSiteId(Integer siteId){
        this.siteId = siteId;
    }

    public Integer getSiteId(){
        return this.siteId;
    }

    public void setFlagId(Integer flagId){
        this.flagId = flagId;
    }

    public Integer getFlagId(){
        return this.flagId;
    }

}