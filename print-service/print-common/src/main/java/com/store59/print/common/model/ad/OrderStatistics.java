package com.store59.print.common.model.ad;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年8月16日 下午2:37:37 类说明
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class OrderStatistics implements Serializable {

	private Integer times;// 浏览总次数
	private Integer people;// 浏览总人数
	private Integer leftpages;// 剩余页数
	private Integer adTotalNum;// 总订单数
	private List<LineChar> linechart;
	private List<AdOrderRelation> corder;

	public Integer getAdTotalNum() {
		return adTotalNum;
	}

	public void setAdTotalNum(Integer adTotalNum) {
		this.adTotalNum = adTotalNum;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getPeople() {
		return people;
	}

	public void setPeople(Integer people) {
		this.people = people;
	}

	public Integer getLeftpages() {
		return leftpages;
	}

	public void setLeftpages(Integer leftpages) {
		this.leftpages = leftpages;
	}

	public List<LineChar> getLinechart() {
		return linechart;
	}

	public void setLinechart(List<LineChar> linechart) {
		this.linechart = linechart;
	}

	public List<AdOrderRelation> getCorder() {
		return corder;
	}

	public void setCorder(List<AdOrderRelation> corder) {
		this.corder = corder;
	}

}
