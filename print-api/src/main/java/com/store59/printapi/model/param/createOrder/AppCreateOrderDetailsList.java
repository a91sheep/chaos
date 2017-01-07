package com.store59.printapi.model.param.createOrder;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class AppCreateOrderDetailsList {
    private List<AppCreateOrderDetailParam> items;

    public List<AppCreateOrderDetailParam> getItems() {
        return items;
    }

    public void setItems(List<AppCreateOrderDetailParam> items) {
        this.items = items;
    }
}
