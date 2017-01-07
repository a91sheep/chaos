package com.store59.printapi.model.result.app;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class ShopDeliveries {
    private List<ShopDelivery> deliveries;

    public List<ShopDelivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<ShopDelivery> deliveries) {
        this.deliveries = deliveries;
    }
}
