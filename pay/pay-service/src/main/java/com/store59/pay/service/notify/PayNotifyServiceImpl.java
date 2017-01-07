/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.notify;

import java.math.BigDecimal;
import java.util.Map;

import com.store59.order.common.service.dto.OrderDTO;
import com.store59.pay.service.order.OrderSystemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.constants.PayConstants;
import com.store59.pay.model.event.PayEvent;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.http.JsonWebServiceHandler;
import com.store59.pay.util.http.NameValuePairUtils;
import com.store59.pay.util.http.WebServiceUtils;
import com.store59.pay.util.lang.BeanMapUtils;

/**
 * 支付通知服务实现类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月30日
 * @since 1.0
 */
@Service
public class PayNotifyServiceImpl implements PayNotifyService {
    private static final Logger logger = LoggerFactory.getLogger(PayNotifyServiceImpl.class);

    @Autowired
    private SignatureService    signatureService;

    @Autowired
    private RabbitTemplate      rabbitTemplate;

    @Autowired
    OrderSystemService orderSystemService;
    
    @Value("${pay.yemaoNotifyUrl}")
    private String yemaoNotifyUrl;

    /**
     * 夜猫店、云印店走消息，不走post请求
     */
    @Override
    public boolean sendEvent(String notifyUrl, PayEvent payEvent) {
    	logger.info("notifyUrl={}", notifyUrl);
        if (StringUtils.isBlank(notifyUrl) || StringUtils.equalsIgnoreCase(notifyUrl, "null") || notifyUrl.equals(yemaoNotifyUrl+"payment/notify")) {
            com.store59.dto.common.pay.PayEvent event = new com.store59.dto.common.pay.PayEvent();
            BeanUtils.copyProperties(payEvent, event);
            return sendEvent(event);
        }
        return callback(notifyUrl, payEvent);
    }

    /**
     * @see com.store59.pay.service.notify.PayNotifyService#sendEvent(com.store59.dto.common.pay.PayEvent)
     */
    @Override
    public boolean sendEvent(com.store59.dto.common.pay.PayEvent payEvent) {
        //能进入这个方法的, 都是接入了订单系统的业务
        //1. 查询订单(已经判断好是否存在, 是否已支付)
        Result<OrderDTO> result = orderSystemService.queryOrder(payEvent.getOrderId());
        if (!result.isSuccess()) {
            logger.error("【发送支付事件】查询订单, 订单异常, payEvent={}, result={}",
                    JsonUtil.getJsonFromObject(payEvent),
                    JsonUtil.getJsonFromObject(result));
            return false;
        }
        OrderDTO orderDTO = result.getData();

        //2. 金额是否一致(转为分, int来比较)
        if (payEvent.getMoney().multiply(new BigDecimal(100)).intValue() !=  orderDTO.getPayAmount()) {
            logger.error("【发送支付事件】查询订单, 订单金额与支付金额不一致, payEvent={}, result={}",
                    JsonUtil.getJsonFromObject(payEvent),
                    JsonUtil.getJsonFromObject(result));
            return false;
        }

        try {
            rabbitTemplate.convertAndSend("topic_pay", "pay." + payEvent.getPayType(), JsonUtil.getJsonFromObject(payEvent));
            logger.info("【发送支付事件】成功：payEvent={}", payEvent);
            return true;
        } catch (Exception e) {
            logger.error("【发送支付事件】失败：payEvent={}", payEvent, e);
            return false;
        }
    }

    /**
     * 回调业务系统.
     * 
     * @param notifyUrl 接受通知的地址
     * @param payEvent {@link PayEvent}
     * @return true：成功，false：失败
     */
    private boolean callback(String notifyUrl, PayEvent payEvent) {
        try {
            Map<String, String> map = BeanMapUtils.toMap(payEvent);
            map.put(PayConstants.SIGN, signatureService.sign(map));

            Result<Map<String, String>> result = sendEvent(notifyUrl, map);
            if (result.getStatus() == 0 && StringUtils.equals(result.getData().get("result"), "success")) {
                logger.info("【发送支付事件】成功：notifyUrl={}, params={}", notifyUrl, map);
                return true;
            }

            logger.error("【发送支付事件】失败：notifyUrl={}, params={}, result={}", notifyUrl, map, JsonUtil.getJsonFromObject(result));
        } catch (Exception e) {
            logger.error("【发送支付事件】失败：notifyUrl={}, payEvent={}", notifyUrl, payEvent, e);
        }

        return false;
    }

    /**
     * 发送支付事件.
     * 
     * @param notifyUrl 接受通知的地址
     * @param map 请求参数
     * @return
     */
    private Result<Map<String, String>> sendEvent(String notifyUrl, Map<String, String> map) {
        return WebServiceUtils.post(notifyUrl, map,
                new JsonWebServiceHandler<Map<String, String>, Result<Map<String, String>>>(new TypeReference<Result<Map<String, String>>>() {
                }) {
                    @Override
                    protected void setRequestData(Request request, Map<String, String> requestData) {
                        request.bodyForm(NameValuePairUtils.convert(requestData), getCharset());
                    }
                });
    }

}
