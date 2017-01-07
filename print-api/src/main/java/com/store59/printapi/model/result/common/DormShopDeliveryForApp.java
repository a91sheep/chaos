package com.store59.printapi.model.result.common;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.store59.printapi.model.result.app.ShopTimeInfo;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年8月29日 上午10:32:29 类说明
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class DormShopDeliveryForApp implements Serializable {
	private Integer id;
	private Integer shop_id;
	private Byte method;
	private Byte status;
	private String content;
	private Double charge;
	private Integer threshold_switch;
	private Double threshold;
	private List<Integer> area;
	private String address;
	private Integer auto_confirm_switch;
	private Integer auto_confirm;
	private List<ShopTimeInfo> delivery_times;
	private String pick_time_str; // 上门自取时间段

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public Byte getMethod() {
		return method;
	}

	public void setMethod(Byte method) {
		this.method = method;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getCharge() {
		return charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public Integer getThreshold_switch() {
		return threshold_switch;
	}

	public void setThreshold_switch(Integer threshold_switch) {
		this.threshold_switch = threshold_switch;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public List<Integer> getArea() {
		return area;
	}

	public void setArea(List<Integer> area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAuto_confirm_switch() {
		return auto_confirm_switch;
	}

	public void setAuto_confirm_switch(Integer auto_confirm_switch) {
		this.auto_confirm_switch = auto_confirm_switch;
	}

	public Integer getAuto_confirm() {
		return auto_confirm;
	}

	public void setAuto_confirm(Integer auto_confirm) {
		this.auto_confirm = auto_confirm;
	}

	public List<ShopTimeInfo> getDelivery_times() {
		return delivery_times;
	}

	public void setDelivery_times(List<ShopTimeInfo> delivery_times) {
		this.delivery_times = delivery_times;
	}

	public String getPick_time_str() {
		return pick_time_str;
	}

	public void setPick_time_str(String pick_time_str) {
		this.pick_time_str = pick_time_str;
	}

}
