package com.yunyin.print.common.model;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/12
 * @since 1.0
 */
public class PrintOrder {
    private String                 orderId;        // 订单号,19位Long类型id＋买家id后4位
    private Byte                   orderStatus;    // 订单状态 1、已下单  2、订单确认<转码完成>  3、已完成<已支付已打印>  4、已取消<未支付、未退款、退款中>
    private Byte                   payStatus;      // 支付状态 1、未支付 2、已支付
    private Byte                   refundStatus;   // 退款状态 1、未退款 2、退款中 3、已退款
    private Byte                   orderSource;    // 订单来源 1、app 2、web 3、微信H5 4、终端
    private Long                   uid;            // 用户id
    private String                 uname;          // 用户昵称
    private Double                 payAmount;      //订单实际支付金额
    private Double                 discountAmount;   //订单折扣金额
    private Long                   createTime;     // 下单时间
    private Long                   printTime;      // 打印时间
    private Long                   finishTime;     // 完成时间
    private Long                   cancelTime;     // 取消时间
    private String                 cancelReason;    // 取消原因
    private Byte                   payType;        // 支付方式
    private Long                   payTime;        // 支付时间
    private String                 tradeNo;        // 第三方支付交易号
    private String                 buyerId;        //付款者账号
    private String                 buyerContact;    //付款者联系方式
    private String                 deviceId;        //付款者设备号
    private String                 couponCode;     // 优惠码
    private String                 couponDiscount;     // 优惠券金额
    private Integer                adPageNum;      // 订单广告打印张数
    private Double                 adPageUnitprice;      // 订单广告结算单价
    private String                 printCode;  //订单打印码
    private Long                   siteId;         // 区域（学校）id
    private Long                   terminalId;
    private List<PrintOrderDetail> details;

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

    public Byte getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    public Byte getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Byte refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Byte getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Byte orderSource) {
        this.orderSource = orderSource;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public Long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Integer getAdPageNum() {
        return adPageNum;
    }

    public void setAdPageNum(Integer adPageNum) {
        this.adPageNum = adPageNum;
    }

    public Double getAdPageUnitprice() {
        return adPageUnitprice;
    }

    public void setAdPageUnitprice(Double adPageUnitprice) {
        this.adPageUnitprice = adPageUnitprice;
    }

    public String getPrintCode() {
        return printCode;
    }

    public void setPrintCode(String printCode) {
        this.printCode = printCode;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public List<PrintOrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PrintOrderDetail> details) {
        this.details = details;
    }
}
