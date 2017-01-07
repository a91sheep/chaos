/**
 * 
 */
package com.store59.printapi.model.param;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.store59.printapi.common.constant.MessageConstant;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月15日
 * @since 1.1
 */
public class DormentryParams {
    @NotBlank(message = MessageConstant.Dormentry_Id)
    private String dormentryId;
    @NotBlank(message = MessageConstant.Shop_Id)
    private String shopId;

    public String getDormentryId() {
        return dormentryId;
    }

    public void setDormentryId(String dormentryId) {
        this.dormentryId = dormentryId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

}
