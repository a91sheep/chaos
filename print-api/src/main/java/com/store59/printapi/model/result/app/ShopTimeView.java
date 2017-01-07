package com.store59.printapi.model.result.app;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/24
 * @since 1.0
 */
public class ShopTimeView {
    private List<ShopTimeInfo> delivery_times;

    public List<ShopTimeInfo> getDelivery_times() {
        return delivery_times;
    }

    public void setDelivery_times(List<ShopTimeInfo> delivery_times) {
        this.delivery_times = delivery_times;
    }
}
