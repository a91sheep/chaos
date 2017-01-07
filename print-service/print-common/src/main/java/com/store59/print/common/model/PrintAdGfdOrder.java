/**
 * 
 */
package com.store59.print.common.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年8月23日 上午10:12:46
 * @since 1.0
 */
@SuppressWarnings("serial")
public class PrintAdGfdOrder implements Serializable {
	private String orderId; 	// 订单id
	private Integer dormId; 	// 店长id
	private String buyerPhone; 	// 买家手机
	private String buyerName; 	// 买家名字
	private String buyerAddress;// 买家地址
	private String picUrl; 		// 照片地址
	private Double picAmount; 	// 照片结算价格
	private String picSize; 		// 照片尺寸
	private Byte status; 		// 订单状态 （0 新订单，未打印，1 已打印）
	private Byte isProfit; 		// 是否已结算(0: 未结算 1:已结算)
	private String createTime; 	// 订单创建时间
	private String printTime; 	// 订单打印时间

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getDormId() {
		return dormId;
	}

	public void setDormId(Integer dormId) {
		this.dormId = dormId;
	}

	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getPicSize() {
		return picSize;
	}

	public void setPicSize(String picSize) {
		this.picSize = picSize;
	}

	public Byte getIsProfit() {
		return isProfit;
	}

	public void setIsProfit(Byte isProfit) {
		this.isProfit = isProfit;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Double getPicAmount() {
		return picAmount;
	}

	public void setPicAmount(Double picAmount) {
		this.picAmount = picAmount;
	}

}
