package com.store59.base.common.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class ExtractTransactionRecord implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 业务类型 新增必传  0:骑士
     */
    private Integer bizType;

    /**
     * 用户id 新增必传
     */
    private String uid;

    /**
     * 引起变化的id
     */
    private String tradeNo;

    /**
     * 银行卡 新增必传
     */
    private String bankCard;

    /**
     * 银行code 新增必传
     */
    private String bankCode;

    /**
     * 银行名称 新增必传
     */
    private String bankName;

    /**
     * 开户城市 新增必传
     */
    private String bankCity;

    /**
     * 开户地址 新增必传
     */
    private String bankSite;

    /**
     * 开户姓名 新增必传
     */
    private String bankUserName;

    /**
     * 提现金额 （单位分）
     */
    private Integer extractMoney;

    /**
     * 0:提现中 1:提现完成 2:提现失败
     */
    private Byte status;

    /**
     * 提现备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Integer addTime;

    /**
     * 提现操作人
     */
    private String handlerId;

    /**
     * 操作时间
     */
    private Integer handleTime;

    //状态
    public final static byte STATUS_INIT     = 0; //提现进行中
    public final static byte STATUS_SUCCCESS = 1;//提现成功
    public final static byte STATUS_FAIL     = 2;//提现失败

    //业务类型常量定义
    public final static int SHOP_MERCHANT = 0;      //便利店商家
    public final static int SHOP_KNIGHT   = 1;      //便利店骑士

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getBankSite() {
        return bankSite;
    }

    public void setBankSite(String bankSite) {
        this.bankSite = bankSite;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public Integer getExtractMoney() {
        return extractMoney;
    }

    public void setExtractMoney(Integer extractMoney) {
        this.extractMoney = extractMoney;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public Integer getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Integer handleTime) {
        this.handleTime = handleTime;
    }

    /**
     * 校验数据是否正确
     *
     * @return
     */
    public boolean check() {
        return !(null == uid
                || null == tradeNo || "".equals(tradeNo)
                || null == bankCard || "".equals(bankCard)
                || null == bankName || "".equals(bankName)
                || null == bankCode || "".equals(bankCode)
                || null == bankUserName || "".equals(bankUserName)
                || null == extractMoney
                );
    }
}