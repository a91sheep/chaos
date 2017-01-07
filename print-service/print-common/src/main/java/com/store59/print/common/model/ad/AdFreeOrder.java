package com.store59.print.common.model.ad;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AdFreeOrder implements Serializable {

	private AdOrder homeOrder;
	private List<AdOrder> footerOrder;

	public AdOrder getHomeOrder() {
		return homeOrder;
	}

	public void setHomeOrder(AdOrder homeOrder) {
		this.homeOrder = homeOrder;
	}

	public List<AdOrder> getFooterOrder() {
		return footerOrder;
	}

	public void setFooterOrder(List<AdOrder> footerOrder) {
		this.footerOrder = footerOrder;
	}

}
