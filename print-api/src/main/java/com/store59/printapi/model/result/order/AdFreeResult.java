package com.store59.printapi.model.result.order;

import java.util.List;

import com.store59.print.common.model.ad.AdOrderRelation;

public class AdFreeResult {
	private List<AdOrderRelation> list;
	private Integer freePages;

	public List<AdOrderRelation> getList() {
		return list;
	}

	public void setList(List<AdOrderRelation> list) {
		this.list = list;
	}

	public Integer getFreePages() {
		return freePages;
	}

	public void setFreePages(Integer freePages) {
		this.freePages = freePages;
	}

}
