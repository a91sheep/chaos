/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.alipay;

import com.store59.kylin.common.model.Result;
import com.store59.pay.service.config.AlipayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 处理支付宝退款的控制器
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/1/19
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/alipay")
public class AlipayRefundController {
    private static final Logger logger = LoggerFactory.getLogger(AlipayRefundController.class);

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 处理支付宝退款
     * @return
     */
    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    public Result<Map<String, String>> refund(){

        return new Result<>();
    }

}
