/**
 * 
 */
package com.store59.printapi.model.result.common;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月20日
 * @since 1.1
 */
public class ShopInfo {
    private Integer shopId;
    private String  shopName;
    private String logo;
    private Byte businessStatus; //营业状态
    private Byte shopStatus;  //配送方式

    /**
     * @return the shopId
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * @param shopId the shopId to set
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * @return the shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * @param shopName the shopName to set
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Byte getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Byte businessStatus) {
        this.businessStatus = businessStatus;
    }

    /**
     * @return the shopStatus
     */
    public Byte getShopStatus() {
        return shopStatus;
    }

    /**
     * @param shopStatus the shopStatus to set
     */
    public void setShopStatus(Byte shopStatus) {
        this.shopStatus = shopStatus;
    }
}
