/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.print.common.model;

import java.io.Serializable;

/**
 * ProfitRule 对象实体
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 *         16/02/27
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ProfitRule implements Serializable {

    private Integer id;
    private Integer dormId;         //店长id
    private Byte    type;           //类型 0：不返利 1：返利
    private Integer blackDatum;     //黑白 上限阀值
    private Integer colorDatum;     //彩页 上限阀值
    private Integer curBlackNum;    //黑白 当前值
    private Integer curColorNum;    //彩页 当前值
    private Integer createTime;

    //常量定义
    public final static byte TYPE_NO_PROFIT   = 0;      //不返利
    public final static byte TYPE_HAVE_PROFIT = 1;      //返利

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setDormId(Integer dormId) {
        this.dormId = dormId;
    }

    public Integer getDormId() {
        return this.dormId;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getType() {
        return this.type;
    }

    public void setBlackDatum(Integer blackDatum) {
        this.blackDatum = blackDatum;
    }

    public Integer getBlackDatum() {
        return this.blackDatum;
    }

    public void setColorDatum(Integer colorDatum) {
        this.colorDatum = colorDatum;
    }

    public Integer getColorDatum() {
        return this.colorDatum;
    }

    public void setCurBlackNum(Integer curBlackNum) {
        this.curBlackNum = curBlackNum;
    }

    public Integer getCurBlackNum() {
        return this.curBlackNum;
    }

    public void setCurColorNum(Integer curColorNum) {
        this.curColorNum = curColorNum;
    }

    public Integer getCurColorNum() {
        return this.curColorNum;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }
}