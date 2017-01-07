package com.store59.printapi.common.message;

import com.store59.dto.common.order.OrderEvent;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.model.PrintOrder;
import com.store59.printapi.model.result.order.OrderCenterDTO;
import com.store59.printapi.service.OrderCenterService;
import com.store59.printapi.service.createOrder.PicCreateOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/22
 * @since 1.0
 */
@Component
public class OrderPicReceiver {
    @Autowired
    private OrderCenterService    orderCenterService;
    @Autowired
    private PicCreateOrderService picCreateOrderService;

    private static Logger logger = LoggerFactory.getLogger(OrderPicReceiver.class);

    public void orderPicReceive(String data) {
        OrderEvent orderEvent = JsonUtil.getObjectFromJson(data, OrderEvent.class);
        if (orderEvent == null || orderEvent.getType() == null) {
            return;
        }
        OrderCenterDTO orderCenterDTO = orderCenterService.getOrderByOrderId(orderEvent.getId(), true, true);
        PrintOrder printOrder = orderCenterDTO.getPrintOrder();
        logger.info("图片订单:" + printOrder.getOrderId() + " 支付完成后推送消息到店长端!");
        picCreateOrderService.pushToPcConvert(printOrder);
        logger.info("图片订单:" + printOrder.getOrderId() + "推送成功!");
    }
}
