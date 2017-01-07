package com.store59.printapi.model.result.order;

import com.store59.print.common.model.PrintOrderDetail;

import java.util.List;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 16/1/13
 * @since 1.0
 */
public class MyOrderResult {

    private String orderId;

    private Byte orderStatus;

    private String address;

    private String phone;

    private Long createTime;

    private Byte payType;

    private Double couponDiscount;  //优惠费用

    private Double printAmount; //打印费

    private Byte deliveryType;  //配送方式

    private String deliveryTime;  //配送时间

    private String shopName;    //店铺名称

    private Double deliveryAmount;  //配送费

    private Double totalAmount;   //总价

    private Integer dormId;

    private String dormPhone;  //店长电话

    private String dormAddress;   //打印店地址

    private String remark;

    private Integer adPageNum;
    
    private String cancelReason;
    
    private byte docType;
    
	public byte getDocType() {
		return docType;
	}

	public void setDocType(byte docType) {
		this.docType = docType;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Integer getAdPageNum() {
        return adPageNum;
    }

    public void setAdPageNum(Integer adPageNum) {
        this.adPageNum = adPageNum;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    private List<PrintOrderDetail> myOrderDetail;

    public Byte getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Byte deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDormPhone() {
        return dormPhone;
    }

    public void setDormPhone(String dormPhone) {
        this.dormPhone = dormPhone;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Double getPrintAmount() {
        return printAmount;
    }

    public void setPrintAmount(Double printAmount) {
        this.printAmount = printAmount;
    }

    public Double getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(Double deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getDormId() {
        return dormId;
    }

    public void setDormId(Integer dormId) {
        this.dormId = dormId;
    }

    public String getDormAddress() {
        return dormAddress;
    }

    public void setDormAddress(String dormAddress) {
        this.dormAddress = dormAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<PrintOrderDetail> getMyOrderDetail() {
        return myOrderDetail;
    }

    public void setMyOrderDetail(List<PrintOrderDetail> myOrderDetail) {
        this.myOrderDetail = myOrderDetail;
    }

}
