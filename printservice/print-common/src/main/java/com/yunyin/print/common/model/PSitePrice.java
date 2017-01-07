package com.yunyin.print.common.model;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
public class PSitePrice {
    private Long   id;
    private Long   siteId;
    private Byte   printType;   //打印规格编码  1-黑白 2-彩色 3-照片
    private Byte   paperType;   //打印纸规格  1-A4 2-A6
    private Byte   printSide;   //单双面  1-单面 2-双面
    private Double unitPrice;   //单价
    private Byte   isActive;    //是否有效 0-无效 1-有效
    private String createUser;
    private Long   createTime;
    private String updateUser;
    private Long   updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Byte getPrintType() {
        return printType;
    }

    public void setPrintType(Byte printType) {
        this.printType = printType;
    }

    public Byte getPaperType() {
        return paperType;
    }

    public void setPaperType(Byte paperType) {
        this.paperType = paperType;
    }

    public Byte getPrintSide() {
        return printSide;
    }

    public void setPrintSide(Byte printSide) {
        this.printSide = printSide;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
