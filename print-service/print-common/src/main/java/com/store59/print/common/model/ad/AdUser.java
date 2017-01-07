package com.store59.print.common.model.ad;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.Date;

/**
 * AdUser 实体
 *
 * Created on 2016-08-17.
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AdUser implements Serializable {
	private Long id;
	private Long uid;
	private String realName;
	private String phone;
	private Byte status;// 0:待审核，1:审核通过，2:审核不通过
	private Double amount;
	private String companyName;
	private Byte companyType;
	private String idcardprosImage;
	private String idcardconsImage;
	private String businessLicenseImage;
	private Date createTime;
	private String openId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid ;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName == null ? null : realName.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName == null ? null : companyName.trim();
	}

	public Byte getCompanyType() {
		return companyType;
	}

	public void setCompanyType(Byte companyType) {
		this.companyType = companyType;
	}

	public String getIdcardprosImage() {
		return idcardprosImage;
	}

	public void setIdcardprosImage(String idcardprosImage) {
		this.idcardprosImage = idcardprosImage;
	}

	public String getIdcardconsImage() {
		return idcardconsImage;
	}

	public void setIdcardconsImage(String idcardconsImage) {
		this.idcardconsImage = idcardconsImage;
	}

	public String getBusinessLicenseImage() {
		return businessLicenseImage;
	}

	public void setBusinessLicenseImage(String businessLicenseImage) {
		this.businessLicenseImage = businessLicenseImage == null ? null : businessLicenseImage.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId == null ? null : openId.trim();
	}

}
