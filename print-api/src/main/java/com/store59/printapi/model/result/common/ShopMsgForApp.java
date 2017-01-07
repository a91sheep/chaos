package com.store59.printapi.model.result.common;

import java.util.List;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年8月29日 上午10:26:13 类说明
 */
public class ShopMsgForApp {

	private Integer shop_id;

	private String shop_name;

	private String shop_logo;

	private String shop_notice;

	private Byte business_status;

	private Byte cross_building_dist_switch;

	private Double freeship_amount;

	private List<DormShopPriceApp> dorm_shop_prices;

	private List<DormShopDeliveryForApp> dorm_shop_deliveries;

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_logo() {
		return shop_logo;
	}

	public void setShop_logo(String shop_logo) {
		this.shop_logo = shop_logo;
	}

	public String getShop_notice() {
		return shop_notice;
	}

	public void setShop_notice(String shop_notice) {
		this.shop_notice = shop_notice;
	}

	public Byte getBusiness_status() {
		return business_status;
	}

	public void setBusiness_status(Byte business_status) {
		this.business_status = business_status;
	}

	public Byte getCross_building_dist_switch() {
		return cross_building_dist_switch;
	}

	public void setCross_building_dist_switch(Byte cross_building_dist_switch) {
		this.cross_building_dist_switch = cross_building_dist_switch;
	}

	public Double getFreeship_amount() {
		return freeship_amount;
	}

	public void setFreeship_amount(Double freeship_amount) {
		this.freeship_amount = freeship_amount;
	}

	public List<DormShopPriceApp> getDorm_shop_prices() {
		return dorm_shop_prices;
	}

	public void setDorm_shop_prices(List<DormShopPriceApp> dorm_shop_prices) {
		this.dorm_shop_prices = dorm_shop_prices;
	}

	public List<DormShopDeliveryForApp> getDorm_shop_deliveries() {
		return dorm_shop_deliveries;
	}

	public void setDorm_shop_deliveries(List<DormShopDeliveryForApp> dorm_shop_deliveries) {
		this.dorm_shop_deliveries = dorm_shop_deliveries;
	}

}
