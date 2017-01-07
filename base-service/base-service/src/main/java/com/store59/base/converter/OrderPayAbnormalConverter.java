/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.converter;

import com.store59.base.common.model.OrderPayAbnormalRecord;
import com.store59.dto.common.refund.RefundMessage;

import java.math.BigDecimal;

/**
 * OrderPayAbnormalRecord 转换类
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 16/5/12
 * @since 1.0
 */
public class OrderPayAbnormalConverter {

    /**
     * message to OrderPayAbnormalRecord
     *
     * @param message
     * @return
     */
    public static OrderPayAbnormalRecord formRefundMessage(RefundMessage message) {
        OrderPayAbnormalRecord record = new OrderPayAbnormalRecord();
        record.setSource(message.getSource());
        record.setOrderSn(message.getOrderId());
        record.setOrderType(message.getOrderType().getCode());
        record.setPayType(message.getPayType().getCode());
        record.setOrderAmount(new BigDecimal(message.getAmount()).movePointLeft(2).doubleValue());
        record.setPayTradeNo(message.getTradeNo());
        record.setPayTime(Math.toIntExact(message.getPayTime()));
        record.setUid(Long.parseLong(message.getPayer()));
        record.setPhone(message.getPayerPhone());
        record.setExtension(message.getExtension());
        return record;
    }
}
