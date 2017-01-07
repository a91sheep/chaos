package com.store59.print.common.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/8/2
 * @since 1.0
 */
@SuppressWarnings("serial")
public class PrintIdRelation implements Serializable {
    private Long   orderIdOld;
    private String orderIdNew;

    public Long getOrderIdOld() {
        return orderIdOld;
    }

    public void setOrderIdOld(Long orderIdOld) {
        this.orderIdOld = orderIdOld;
    }

    public String getOrderIdNew() {
        return orderIdNew;
    }

    public void setOrderIdNew(String orderIdNew) {
        this.orderIdNew = orderIdNew;
    }
}
