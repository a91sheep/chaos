package com.store59.printapi.model.result.order;

import java.util.List;

import com.store59.printapi.model.param.coupon.AppCoupon;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月30日 上午11:07:47 
 * 类说明
 */
public class AppCouponList {
    private List<AppCoupon> coupons;

	public List<AppCoupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<AppCoupon> coupons) {
		this.coupons = coupons;
	}
    
}
