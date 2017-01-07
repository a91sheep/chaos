/**
 *
 */
package com.store59.print.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2015年12月25日
 * @since 1.1
 */
@SuppressWarnings("serial")
public class PrintOrder implements Serializable {

    private String  orderId;        // 订单号,19位Long类型id＋买家id后4位
    private Byte    status;         // 订单状态 （0 新订单，未确认，1 已确认，未打印，2 配送中，4 订单最终完成， 5 订单取消， 6 已打印）
    private Byte    docType;        // 文档类型  (0 照片，1文档)
    private Byte    source;            // 订单来源 （0 pc,1 手机网页(微信),2 shop端快速下单(废弃),3 android,4 ios）
    private Byte    deliveryType;   // 配送方式 （1 店长配送，2 上门自取)
    private String  deliveryTime;   // 配送时间
    private String  phone;          // 手机号码
    private String  address;        // 寝室地址
    private String  remark;         // 备注
    private Integer adPageNum;      // 订单广告打印张数
    private Double  adUnitPrice;      // 订单广告结算单价
    private String  couponCode;     // 优惠码
    private Double  couponDiscount; // 优惠费用
    private Double  deliveryAmount; // 配送费
    private Double  totalAmount;    // 总价
    private Long    createTime;     // 下单时间
    private Byte    payType;        // 支付方式 (1 支付宝，6 微信支付)
    private Long    payTime;        // 支付时间
    private Long    printTime;      // 打印时间
    private Long    finishTime;     // 完成时间
    private String  tradeNo;        // 第三方支付交易号
    private Integer dormId;         // 店长id
    private Long    uid;            // 用户id
    private String  uname;          // 用户昵称
    private String  cancelReason;    // 取消原因
    private Integer autoConfirmHours;       //打印完成后，自动确认完成的小时数
    private String  buyerId;        //付款者账号
    private String  buyerContact;    //付款者联系方式
    private String  deviceId;        //付款者设备号

    private List<PrintOrderDetail> details;

    public Integer getAdPageNum() {
        return adPageNum;
    }

    public void setAdPageNum(Integer adPageNum) {
        this.adPageNum = adPageNum;
    }

    public Double getAdUnitPrice() {
        return adUnitPrice;
    }

    public void setAdUnitPrice(Double adUnitPrice) {
        this.adUnitPrice = adUnitPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Byte deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
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

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Long printTime) {
        this.printTime = printTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getDormId() {
        return dormId;
    }

    public void setDormId(Integer dormId) {
        this.dormId = dormId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public List<PrintOrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PrintOrderDetail> details) {
        this.details = details;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Integer getAutoConfirmHours() {
        return autoConfirmHours;
    }

    public void setAutoConfirmHours(Integer autoConfirmHours) {
        this.autoConfirmHours = autoConfirmHours;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerContact() {
        return buyerContact;
    }

    public void setBuyerContact(String buyerContact) {
        this.buyerContact = buyerContact;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Byte getDocType() {
        return docType;
    }

    public void setDocType(Byte docType) {
        this.docType = docType;
    }

}
