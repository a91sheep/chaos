package com.store59.printapi.model.param.gala;

import javax.validation.constraints.NotNull;

import com.store59.printapi.common.constant.MessageConstant;

/**
 * 
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年4月19日 上午11:49:52 
 * 类说明:gala支付入参
 */
public class GalaPayParam {

	@NotNull(message = MessageConstant.ORDER_ID_PARAM_BLANK)
	private String orderId;
//	@NotNull(message=MessageConstant.ORDER_PAYTYPE_PARAM_BLANK)
//	private String payType;
	@NotNull(message=MessageConstant.PAY_WXPAY_OPENID_BLANK)
	private String openId;
	@NotNull
	private String returnUrl;
	
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
//	public String getPayType() {
//		return payType;
//	}
//	public void setPayType(String payType) {
//		this.payType = payType;
//	}
	
		
}
