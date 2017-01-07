/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.common.model;

import java.io.Serializable;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * RepoFlagRelation 对象实体
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/18
 * @since 1.0
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class RepoFlagRelation implements Serializable  {

    private Integer objectId;

    private Integer flagId;

    private Integer rid;

    private Byte type;

    public void setObjectId(Integer objectId){
        this.objectId = objectId;
    }

    public Integer getObjectId(){
        return this.objectId;
    }

    public void setFlagId(Integer flagId){
        this.flagId = flagId;
    }

    public Integer getFlagId(){
        return this.flagId;
    }

    public void setType(Byte type){
        this.type = type;
    }

    public Byte getType(){
        return this.type;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }
}