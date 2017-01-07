package com.store59.printapi.model.param.order;

import com.store59.printapi.model.param.AppBaseParam;

import java.util.List;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 16/1/13
 * @since 1.0
 */
public class MyOrderParam extends AppBaseParam {

    private String token;

    private Integer page;

    private Integer num_per_page;

    private List<Integer> type;

    private Integer    offset;

    private Integer    limit;
    
    private String userid;
    
    public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getNum_per_page() {
        return num_per_page;
    }

    public void setNum_per_page(Integer num_per_page) {
        this.num_per_page = num_per_page;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Integer> getType() {
        return type;
    }

    public void setType(List<Integer> type) {
        this.type = type;
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
