/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.common.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * RepoFlag 对象实体
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class RepoFlag implements Serializable  {

    private Integer flagId;

    private Byte status;

    private Byte type;

    private Integer rid;

    private String flagName;

    private Integer addTime;

    private Integer startTime;

    private Integer endTime;

    private Byte relationType;

    public void setFlagId(Integer flagId){
        this.flagId = flagId;
    }

    public Integer getFlagId(){
        return this.flagId;
    }

    public void setStatus(Byte status){
        this.status = status;
    }

    public Byte getStatus(){
        return this.status;
    }

    public void setType(Byte type){
        this.type = type;
    }

    public Byte getType(){
        return this.type;
    }

    public void setRid(Integer rid){
        this.rid = rid;
    }

    public Integer getRid(){
        return this.rid;
    }

    public void setFlagName(String flagName){
        this.flagName = flagName;
    }

    public String getFlagName(){
        return this.flagName;
    }

    public void setAddTime(Integer addTime){
        this.addTime = addTime;
    }

    public Integer getAddTime(){
        return this.addTime;
    }

    public void setStartTime(Integer startTime){
        this.startTime = startTime;
    }

    public Integer getStartTime(){
        return this.startTime;
    }

    public void setEndTime(Integer endTime){
        this.endTime = endTime;
    }

    public Integer getEndTime(){
        return this.endTime;
    }

    public Byte getRelationType() {
        return relationType;
    }

    public void setRelationType(Byte relationType) {
        this.relationType = relationType;
    }
}