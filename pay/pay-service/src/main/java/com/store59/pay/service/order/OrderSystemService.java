/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.order;

import com.store59.dto.common.order.OrderPayStatusEnum;
import com.store59.kylin.common.model.Result;
import com.store59.order.common.service.dto.OrderDTO;
import com.store59.order.common.service.dto.OrderQueryWith;
import com.store59.order.common.service.facade.BuyerOrderQueryFacade;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.service.util.ServiceResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单类服务(主要用于调用订单系统)
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/4
 * @since 1.0
 */
@Service
public class OrderSystemService {
    private static final Logger logger = LoggerFactory.getLogger(OrderSystemService.class);

    @Autowired
    BuyerOrderQueryFacade buyerOrderQueryFacade;

    public Result<OrderDTO> queryOrder(String orderId) {

        OrderQueryWith queryWith = new OrderQueryWith();
        queryWith.setWithOrderItems(false);
        queryWith.setWithOrderPays(true);

        Result<OrderDTO> result = buyerOrderQueryFacade.queryOrder(orderId,  queryWith);
        if (!result.isSuccess()) {
            logger.error("【订单service】调用服务order-service出错, result={}", result);
            return ServiceResultUtil.toResult(BizResultCodeEnum.UNKNOWN_EXCEPTION);
        }
        OrderDTO orderDTO = result.getData();

        //1. 是否有该订单
        if (orderDTO == null) {
            logger.warn("【订单service】查无订单, orderId={}, result={}", orderId, result);
            return ServiceResultUtil.toResult(BizResultCodeEnum.ORDER_IS_NOT_EXITIS);
        }

        return result;
    }
}
