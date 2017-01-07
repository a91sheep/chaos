package com.store59.printapi.model.param.createOrder;

import com.store59.printapi.common.constant.MessageConstant;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/15
 * @since 1.0
 */
public class CreateOrderParam {
    private String couponCode;
    @NotBlank(message = MessageConstant.ORDER_DELIVERYTYPE_PARAM_BLANK)
    private String deliveryType;
    private String deleveryTime;
    @NotBlank(message = MessageConstant.ORDER_PHONE_PARAM_BLANK)
    private String phone;
    @NotBlank(message = MessageConstant.ORDER_ADDRESS_PARAM_BLANK)
    private String address;
    private String remark;
    private String payType;
    @NotBlank(message = MessageConstant.ORDER_SHOPID_PARAM_BLANK)
    private String shopId;
    @NotBlank(message = MessageConstant.ORDER_DETAILS_PARAM_BLANK)
    private String details;
    private Integer dormEntryId;
    private Byte    open_ad;
    private Byte source;  //订单来源平台, 0:网站; 1:手机网页(微信); 2:shop端快速下单(废弃); 3:iOS  4:android 5饿了么平台  6到店付
    private Byte docType=1;//订单类型：0:图片，1:文档；默认为1
    private Integer freeAdNum;
    
    public Integer getFreeAdNum() {
		return freeAdNum;
	}

	public void setFreeAdNum(Integer freeAdNum) {
		this.freeAdNum = freeAdNum;
	}

	public Byte getDocType() {
		return docType;
	}

	public void setDocType(Byte docType) {
		this.docType = docType;
	}

	public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeleveryTime() {
        return deleveryTime;
    }

    public void setDeleveryTime(String deleveryTime) {
        this.deleveryTime = deleveryTime;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getDormEntryId() {
        return dormEntryId;
    }

    public void setDormEntryId(Integer dormEntryId) {
        this.dormEntryId = dormEntryId;
    }

    public Byte getOpen_ad() {
        return open_ad;
    }

    public void setOpen_ad(Byte open_ad) {
        this.open_ad = open_ad;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }
}
