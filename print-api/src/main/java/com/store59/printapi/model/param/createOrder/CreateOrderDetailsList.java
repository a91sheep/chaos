package com.store59.printapi.model.param.createOrder;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/16
 * @since 1.0
 */
public class CreateOrderDetailsList {
    private List<CreateOrderDetailParam> details;

    public List<CreateOrderDetailParam> getDetails() {
        return details;
    }

    public void setDetails(List<CreateOrderDetailParam> details) {
        this.details = details;
    }
}
