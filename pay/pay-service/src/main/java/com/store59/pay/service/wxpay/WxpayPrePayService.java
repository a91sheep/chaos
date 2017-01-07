/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.wxpay;

import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.wxpay.WxpayAppPrepayRequest;
import com.store59.pay.model.wxpay.WxpayWebPayRequest;
import com.store59.pay.model.wxpay.WxpayWebPrePayRequest;
import com.store59.pay.model.wxpay.WxpayUnifiedOrderResponse;
import com.store59.pay.service.config.WxpayConfig;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.lang.BeanMapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

/**
 * 微信预支付service
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/8
 * @since 1.0
 */
@Service
public class WxpayPrePayService {

    @Autowired
    private WxpayConfig wxpayConfig;

    @Autowired
    private SignatureService signatureService;

}
