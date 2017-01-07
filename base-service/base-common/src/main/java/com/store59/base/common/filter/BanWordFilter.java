package com.store59.base.common.filter;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import java.util.List;


/**
 * BanWord 查询过滤条件
 *
 * Created on 2016-04-22.
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class BanWordFilter implements Serializable{
    private List<Integer> bidList;
    private Integer offset;
    private Integer limit;

    public Byte getDeny() {
        return deny;
    }

    public void setDeny(Byte deny) {
        this.deny = deny;
    }

    private Byte deny;

    public List<Integer> getBidList() {
        return bidList;
    }

    public void setBidList(List<Integer> bidList) {
        this.bidList = bidList;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
