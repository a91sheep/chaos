package com.yunyin.print.main.api.model.param.pc;

import com.yunyin.print.main.api.common.constant.MessageConstant;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/22
 * @since 1.0
 */
public class OrderCancelParam {
    @NotBlank(message = MessageConstant.ORDER_ID_PARAM_BLANK)
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
