package com.store59.print.common.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/20
 * @since 1.0
 */
@SuppressWarnings("serial")
public class PrintOrderRecord implements Serializable {
    private Long   id;          // 主键
    private String orderId;         // 订单号
    private Byte   status;        // 是否已经检测。0:未检测  1:已检测

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
