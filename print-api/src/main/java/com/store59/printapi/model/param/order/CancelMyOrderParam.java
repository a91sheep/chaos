package com.store59.printapi.model.param.order;

import com.store59.printapi.common.constant.MessageConstant;

import javax.validation.constraints.NotNull;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 16/1/13
 * @since 1.0
 */
public class CancelMyOrderParam {

    @NotNull(message = MessageConstant.ORDER_ID_PARAM_BLANK)
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
