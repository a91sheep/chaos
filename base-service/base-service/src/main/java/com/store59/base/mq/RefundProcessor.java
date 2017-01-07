/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.mq;

import com.store59.base.common.model.OrderPayAbnormalRecord;
import com.store59.base.converter.OrderPayAbnormalConverter;
import com.store59.base.service.OrderPayAbnormalRecordService;
import com.store59.dto.common.refund.RefundMessage;
import com.store59.kylin.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 接收退款消息, 并进行处理
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 16/5/12
 * @since 1.0
 */
@Component
public class RefundProcessor {

    private final static Logger logger = LoggerFactory.getLogger(RefundProcessor.class);

    @Autowired
    private OrderPayAbnormalRecordService orderPayAbnormalRecordService;

    @Autowired
    private Sender sender;

    @RabbitListener(queues = "refund.thirdparty")
    public void receive(String message) {
        logger.info("收到退款请求: {}", message);
        RefundMessage refundMessage = JsonUtil.getObjectFromJson(message, RefundMessage.class);

        if(StringUtils.isBlank(refundMessage.getTradeNo())) {
            logger.warn("第三方交易账号为空, data: {}", message);
        } else {
            //添加异常订单记录
            OrderPayAbnormalRecord record = OrderPayAbnormalConverter.formRefundMessage(refundMessage);
            orderPayAbnormalRecordService.addOrderPayAbnormalRecord(record);
        }
    }


    /**
     * orderPayAbnormalId
     *
     * @param orderPayAbnormalId
     */
    public void send(Integer orderPayAbnormalId) {
        OrderPayAbnormalRecord record = orderPayAbnormalRecordService.findById(orderPayAbnormalId);

        if(record != null
                && StringUtils.isNotBlank(record.getSource())) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", record.getOrderSn());
            map.put("extension", record.getExtension());
            logger.info("发送消息: {}", JsonUtil.getJsonFromObject(map));
            sender.sendMessage(record.getSource(), JsonUtil.getJsonFromObject(map));
        }

    }
}
