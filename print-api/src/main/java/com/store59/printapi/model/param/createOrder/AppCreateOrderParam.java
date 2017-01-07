package com.store59.printapi.model.param.createOrder;

import org.hibernate.validator.constraints.NotBlank;

import com.store59.printapi.common.constant.MessageConstant;
import com.store59.printapi.model.param.AppBaseParam;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class AppCreateOrderParam extends AppBaseParam {
	@NotBlank(message = MessageConstant.APP_TOKEN_NOT_BLANK)
	private String token;
	private String coupon_code;
	private Byte pay_type;
	private Byte send_type; // 1 店长配送 2 上门自取
	private Integer delivery_type; // 2 立即配送 3 上门自取
	private Long expect_start_time; // 配送开始时间
	private Long expect_end_time; // 配送结束时间
	private String expect_time_name; // 配送时间显示的字符串(包括立即送出)
	private Byte open_ad; // 是否免费打印 0不开启 1开启免费打印
	@NotBlank(message = MessageConstant.ORDER_PHONE_PARAM_BLANK)
	private String phone;
//	@NotBlank(message = MessageConstant.ORDER_NAME_PARAM_BLANK)
	private String buyer_name;
	@NotBlank(message = MessageConstant.ORDER_ADDRESS_PARAM_BLANK)
	private String address;
	private String remark;
	@NotBlank(message = MessageConstant.ORDER_SHOPID_PARAM_BLANK)
	private String shop_id;
	@NotBlank(message = MessageConstant.ORDER_DETAILS_PARAM_BLANK)
	private String items;
	private Byte doc_type = 1;// 订单类型：0:图片，1:文档；默认为1
	private Integer cart_free;//用户购物车看到的免费张数
	
	// 使用优惠券后返回结果需要增加的
	private Double delivery_amount;
	private Double document_amount;
	private Double total_amount;
	private Double coupon_discount;
	private String pick_address;
	private String pick_time_string;
	private Integer dormentry_id;
	private Byte source;
	private String device_id;

	public Integer getCart_free() {
		return cart_free;
	}

	public void setCart_free(Integer cart_free) {
		this.cart_free = cart_free;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public Byte getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(Byte doc_type) {
		this.doc_type = doc_type;
	}

	@Override
	public String getDevice_id() {
		return device_id;
	}

	@Override
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public Byte getPay_type() {
		return pay_type;
	}

	public void setPay_type(Byte pay_type) {
		this.pay_type = pay_type;
	}

	public Byte getSource() {
		return source;
	}

	public void setSource(Byte source) {
		this.source = source;
	}

	public Integer getDormentry_id() {
		return dormentry_id;
	}

	public void setDormentry_id(Integer dormentry_id) {
		this.dormentry_id = dormentry_id;
	}

	public Integer getDelivery_type() {
		return delivery_type;
	}

	public void setDelivery_type(Integer delivery_type) {
		this.delivery_type = delivery_type;
	}

	public String getExpect_time_name() {
		return expect_time_name;
	}

	public void setExpect_time_name(String expect_time_name) {
		this.expect_time_name = expect_time_name;
	}

	public Double getCoupon_discount() {
		return coupon_discount;
	}

	public void setCoupon_discount(Double coupon_discount) {
		this.coupon_discount = coupon_discount;
	}

	public String getPick_address() {
		return pick_address;
	}

	public void setPick_address(String pick_address) {
		this.pick_address = pick_address;
	}

	public String getPick_time_string() {
		return pick_time_string;
	}

	public void setPick_time_string(String pick_time_string) {
		this.pick_time_string = pick_time_string;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public Byte getSend_type() {
		return send_type;
	}

	public void setSend_type(Byte send_type) {
		this.send_type = send_type;
	}

	public Long getExpect_start_time() {
		return expect_start_time;
	}

	public void setExpect_start_time(Long expect_start_time) {
		this.expect_start_time = expect_start_time;
	}

	public Long getExpect_end_time() {
		return expect_end_time;
	}

	public void setExpect_end_time(Long expect_end_time) {
		this.expect_end_time = expect_end_time;
	}

	public Byte getOpen_ad() {
		return open_ad;
	}

	public void setOpen_ad(Byte open_ad) {
		this.open_ad = open_ad;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public Double getDelivery_amount() {
		return delivery_amount;
	}

	public void setDelivery_amount(Double delivery_amount) {
		this.delivery_amount = delivery_amount;
	}

	public Double getDocument_amount() {
		return document_amount;
	}

	public void setDocument_amount(Double document_amount) {
		this.document_amount = document_amount;
	}

	public Double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(Double total_amount) {
		this.total_amount = total_amount;
	}
}
