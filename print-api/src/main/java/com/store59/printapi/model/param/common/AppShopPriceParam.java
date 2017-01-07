package com.store59.printapi.model.param.common;

import com.store59.printapi.common.constant.MessageConstant;
import com.store59.printapi.model.param.AppBaseParam;

import javax.validation.constraints.NotNull;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class AppShopPriceParam extends AppBaseParam {
    @NotNull(message = MessageConstant.Shop_Id)
    private Integer shop_id;

    public Integer getShop_id() {
        return shop_id;
    }

    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }
}
