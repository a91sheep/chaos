package com.store59.print.common.model.ad;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AdStatistics implements Serializable {

	private OrderStatistics footer;
	private OrderStatistics home;

	public OrderStatistics getFooter() {
		return footer;
	}

	public void setFooter(OrderStatistics footer) {
		this.footer = footer;
	}

	public OrderStatistics getHome() {
		return home;
	}

	public void setHome(OrderStatistics home) {
		this.home = home;
	}

}
