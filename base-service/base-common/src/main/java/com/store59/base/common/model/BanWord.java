package com.store59.base.common.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * BanWord 实体
 *
 * Created on 2016-04-22.
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class BanWord implements Serializable {
    private Integer bid;
    private String replacefrom;
    private String replaceto;
    private Byte deny;

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getReplacefrom() {
        return replacefrom;
    }

    public void setReplacefrom(String replacefrom) {
        this.replacefrom = replacefrom == null ? null : replacefrom.trim();
    }

    public String getReplaceto() {
        return replaceto;
    }

    public void setReplaceto(String replaceto) {
        this.replaceto = replaceto == null ? null : replaceto.trim();
    }

    public Byte getDeny() {
        return deny;
    }

    public void setDeny(Byte deny) {
        this.deny = deny;
    }

}
