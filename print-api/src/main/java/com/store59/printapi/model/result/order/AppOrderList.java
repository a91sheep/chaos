package com.store59.printapi.model.result.order;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/15
 * @since 1.0
 */
public class AppOrderList {
    private List<AppOrder> orders;

    public List<AppOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<AppOrder> orders) {
        this.orders = orders;
    }
}
