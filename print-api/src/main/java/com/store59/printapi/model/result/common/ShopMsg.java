/**
 * 
 */
package com.store59.printapi.model.result.common;

import com.store59.dorm.common.model.DormShopDelivery;
import com.store59.dorm.common.model.DormShopPrice;

import java.util.List;

/**
 * 
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @version 1.1 2016年1月21日
 * @since 1.1
 */
public class ShopMsg {

    private Integer shopId;

    private String shopName;

    private String shopLogo;

    private String shopNotice;

    private Byte businessStatus;

    private Byte cross_building_dist_switch;

    private Double freeship_amount;

    private List<DormShopPrice> dormShopPrices;

    private List<DormShopDeliveryForPc> dormShopDeliveries;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopNotice() {
        return shopNotice;
    }

    public void setShopNotice(String shopNotice) {
        this.shopNotice = shopNotice;
    }

    public Byte getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Byte businessStatus) {
        this.businessStatus = businessStatus;
    }

    public List<DormShopPrice> getDormShopPrices() {
        return dormShopPrices;
    }

    public void setDormShopPrices(List<DormShopPrice> dormShopPrices) {
        this.dormShopPrices = dormShopPrices;
    }

    public List<DormShopDeliveryForPc> getDormShopDeliveries() {
        return dormShopDeliveries;
    }

    public void setDormShopDeliveries(List<DormShopDeliveryForPc> dormShopDeliveries) {
        this.dormShopDeliveries = dormShopDeliveries;
    }

    public Byte getCross_building_dist_switch() {
        return cross_building_dist_switch;
    }

    public void setCross_building_dist_switch(Byte cross_building_dist_switch) {
        this.cross_building_dist_switch = cross_building_dist_switch;
    }

    public Double getFreeship_amount() {
        return freeship_amount;
    }

    public void setFreeship_amount(Double freeship_amount) {
        this.freeship_amount = freeship_amount;
    }
}
