/**
 * 
 */
package com.store59.printapi.model.result.common;

import java.util.List;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月15日
 * @since 1.1
 */
public class DormentryInfo {
    
    private Integer dormentryId;

    private String dormentryName;

    private Byte dormentryStatus; //0未开通;1营业中;2可预定;3休息中

    private List<ShopInfo> dormShopList;

    public Integer getDormentryId() {
        return dormentryId;
    }

    public void setDormentryId(Integer dormentryId) {
        this.dormentryId = dormentryId;
    }

    public String getDormentryName() {
        return dormentryName;
    }

    public void setDormentryName(String dormentryName) {
        this.dormentryName = dormentryName;
    }

    public Byte getDormentryStatus() {
        return dormentryStatus;
    }

    public void setDormentryStatus(Byte dormentryStatus) {
        this.dormentryStatus = dormentryStatus;
    }

    public List<ShopInfo> getDormShopList() {
        return dormShopList;
    }

    public void setDormShopList(List<ShopInfo> dormShopList) {
        this.dormShopList = dormShopList;
    }
}
