/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 微信支付统一下单请求参数.
 * 服务商版
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class WxpayServerUnifiedOrderRequest {

    /** 公众账号ID. */
    private String appid;

    /** 商户号. */
    @XmlElement(name = "mch_id")
    private String mchId;

    /** 子商户公众账号ID. */
    @XmlElement(name = "sub_appid")
    private String subAppid;

    /** 子商户号. */
    @XmlElement(name = "sub_mch_id")
    private String subMchId;

    /** 设备号. */
    @XmlElement(name = "device_info")
    private String deviceInfo;

    /** 随机字符串. */
    @XmlElement(name = "nonce_str")
    private String nonceStr;

    /** 签名. */
    private String sign;

    /** 商品描述. */
    private String body;

    /** 商品详情. */
    private String detail;

    /** 附加数据. */
    private String attach;

    /** 商户订单号. */
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    /** 货币类型. */
    @XmlElement(name = "fee_type")
    private String feeType;

    /** 总金额. */
    @XmlElement(name = "total_fee")
    private int    totalFee;

    /** 终端IP. */
    @XmlElement(name = "spbill_create_ip")
    private String spbillCreateIp;

    /** 交易起始时间. */
    @XmlElement(name = "time_start")
    private String timeStart;

    /** 交易结束时间. */
    @XmlElement(name = "time_expire")
    private String timeExpire;

    /** 商品标记. */
    @XmlElement(name = "goods_tag")
    private String goodsTag;

    /** 通知地址. */
    @XmlElement(name = "notify_url")
    private String notifyUrl;

    /** 交易类型. */
    @XmlElement(name = "trade_type")
    private String tradeType;

    /** 商品ID. */
    @XmlElement(name = "product_id")
    private String productId;

    /** 指定支付方式. */
    @XmlElement(name = "limit_pay")
    private String limitPay;

    /** 用户标识. */
    private String openid;

    /** 用户子标识 . */
    @XmlElement(name = "sub_openid")
    private String subOpenid;

    /**
     * @return the appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid the appid to set
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * @return the mchId
     */
    public String getMchId() {
        return mchId;
    }

    /**
     * @param mchId the mchId to set
     */
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    /**
     * @return the subAppid
     */
    public String getSubAppid() {
        return subAppid;
    }

    /**
     * @param subAppid the subAppid to set
     */
    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
    }

    /**
     * @return the subMchId
     */
    public String getSubMchId() {
        return subMchId;
    }

    /**
     * @param subMchId the subMchId to set
     */
    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    /**
     * @return the deviceInfo
     */
    public String getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * @param deviceInfo the deviceInfo to set
     */
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    /**
     * @return the nonceStr
     */
    public String getNonceStr() {
        return nonceStr;
    }

    /**
     * @param nonceStr the nonceStr to set
     */
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return the attach
     */
    public String getAttach() {
        return attach;
    }

    /**
     * @param attach the attach to set
     */
    public void setAttach(String attach) {
        this.attach = attach;
    }

    /**
     * @return the outTradeNo
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * @param outTradeNo the outTradeNo to set
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    /**
     * @return the feeType
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * @param feeType the feeType to set
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    /**
     * @return the totalFee
     */
    public int getTotalFee() {
        return totalFee;
    }

    /**
     * @param totalFee the totalFee to set
     */
    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * @return the spbillCreateIp
     */
    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    /**
     * @param spbillCreateIp the spbillCreateIp to set
     */
    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    /**
     * @return the timeStart
     */
    public String getTimeStart() {
        return timeStart;
    }

    /**
     * @param timeStart the timeStart to set
     */
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * @return the timeExpire
     */
    public String getTimeExpire() {
        return timeExpire;
    }

    /**
     * @param timeExpire the timeExpire to set
     */
    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    /**
     * @return the goodsTag
     */
    public String getGoodsTag() {
        return goodsTag;
    }

    /**
     * @param goodsTag the goodsTag to set
     */
    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    /**
     * @return the notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * @param notifyUrl the notifyUrl to set
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * @return the tradeType
     */
    public String getTradeType() {
        return tradeType;
    }

    /**
     * @param tradeType the tradeType to set
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return the limitPay
     */
    public String getLimitPay() {
        return limitPay;
    }

    /**
     * @param limitPay the limitPay to set
     */
    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    /**
     * @return the openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid the openid to set
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return the subOpenid
     */
    public String getSubOpenid() {
        return subOpenid;
    }

    /**
     * @param subOpenid the subOpenid to set
     */
    public void setSubOpenid(String subOpenid) {
        this.subOpenid = subOpenid;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
