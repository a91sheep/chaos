package com.store59.print.common.model.ad;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * AdOrder 实体
 *
 * Created on 2016-08-17.
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AdOrder implements Serializable {
	private Long id;
	private String orderId;
	private String homePageImage;
	private String footerPageImage;
	private String footerPageSlogan;
	private Integer homeAdNum;
	private Integer footerAdNum;
	private String startDate;
	private String endDate;
	private Byte homeShowType;
	private Byte footerShowType;
	private String showArea;
	private Byte status;
	private String createTime;
	private Double amount;
	private Double homeWeight;
	private Double footerWeight;
	private String nopassReason;
	private Double homePageUnitprice;
	private Double footerPageUnitprice;
	private Long uid;
	private String showAreaName;
	private String realName;
	private String phone;
	private Long startDateStamp;
	private Long endDateStamp;
	private Long createTimeStamp;
	private Integer showAreaAmounts;
	private String showAreaNamesAll;
	private String editPdf;
	private String homeUsed;
	private String footerUsed;
	
	public String getHomeUsed() {
		return homeUsed;
	}

	public void setHomeUsed(String homeUsed) {
		this.homeUsed = homeUsed;
	}

	public String getFooterUsed() {
		return footerUsed;
	}

	public void setFooterUsed(String footerUsed) {
		this.footerUsed = footerUsed;
	}

	public String getEditPdf() {
		return editPdf;
	}

	public void setEditPdf(String editPdf) {
		this.editPdf = editPdf;
	}

	public String getShowAreaNamesAll() {
		return showAreaNamesAll;
	}

	public void setShowAreaNamesAll(String showAreaNamesAll) {
		this.showAreaNamesAll = showAreaNamesAll;
	}

	public Integer getShowAreaAmounts() {
		return showAreaAmounts;
	}

	public void setShowAreaAmounts(Integer showAreaAmounts) {
		this.showAreaAmounts = showAreaAmounts;
	}

	public Long getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Long createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Long getStartDateStamp() {
		return startDateStamp;
	}

	public void setStartDateStamp(Long startDateStamp) {
		this.startDateStamp = startDateStamp;
	}

	public Long getEndDateStamp() {
		return endDateStamp;
	}

	public void setEndDateStamp(Long endDateStamp) {
		this.endDateStamp = endDateStamp;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getShowAreaName() {
		return showAreaName;
	}

	public void setShowAreaName(String showAreaName) {
		this.showAreaName = showAreaName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getHomePageImage() {
		return homePageImage;
	}

	public void setHomePageImage(String homePageImage) {
		this.homePageImage = homePageImage == null ? null : homePageImage.trim();
	}

	public String getFooterPageImage() {
		return footerPageImage;
	}

	public void setFooterPageImage(String footerPageImage) {
		this.footerPageImage = footerPageImage == null ? null : footerPageImage.trim();
	}

	public String getFooterPageSlogan() {
		return footerPageSlogan;
	}

	public void setFooterPageSlogan(String footerPageSlogan) {
		this.footerPageSlogan = footerPageSlogan;
	}

	public Integer getHomeAdNum() {
		return homeAdNum;
	}

	public void setHomeAdNum(Integer homeAdNum) {
		this.homeAdNum = homeAdNum;
	}

	public Integer getFooterAdNum() {
		return footerAdNum;
	}

	public void setFooterAdNum(Integer footerAdNum) {
		this.footerAdNum = footerAdNum;
	}

	public Byte getHomeShowType() {
		return homeShowType;
	}

	public void setHomeShowType(Byte homeShowType) {
		this.homeShowType = homeShowType;
	}

	public Byte getFooterShowType() {
		return footerShowType;
	}

	public void setFooterShowType(Byte footerShowType) {
		this.footerShowType = footerShowType;
	}

	public String getShowArea() {
		return showArea;
	}

	public void setShowArea(String showArea) {
		this.showArea = showArea == null ? null : showArea.trim();
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getNopassReason() {
		return nopassReason;
	}

	public void setNopassReason(String nopassReason) {
		this.nopassReason = nopassReason == null ? null : nopassReason.trim();
	}

	public Double getHomePageUnitprice() {
		return homePageUnitprice;
	}

	public void setHomePageUnitprice(Double homePageUnitprice) {
		this.homePageUnitprice = homePageUnitprice;
	}

	public Double getFooterPageUnitprice() {
		return footerPageUnitprice;
	}

	public void setFooterPageUnitprice(Double footerPageUnitprice) {
		this.footerPageUnitprice = footerPageUnitprice;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Double getHomeWeight() {
		return homeWeight;
	}

	public void setHomeWeight(Double homeWeight) {
		this.homeWeight = homeWeight;
	}

	public Double getFooterWeight() {
		return footerWeight;
	}

	public void setFooterWeight(Double footerWeight) {
		this.footerWeight = footerWeight;
	}

}
