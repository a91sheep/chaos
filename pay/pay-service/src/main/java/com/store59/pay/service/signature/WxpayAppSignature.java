/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.signature;

import com.store59.pay.util.lang.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.store59.pay.service.config.WxpayAppConfig;

import java.util.Map;

/**
 * 微信APP支付签名.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月19日
 * @since 1.0
 */
@Component
public class WxpayAppSignature extends WxpaySignature {

    @Autowired
    private WxpayAppConfig wxpayAppConfig;

    /**
     * @see com.store59.pay.service.signature.WxpaySignature#getSignKey()
     */
    @Override
    protected String getSignKey() {
        return wxpayAppConfig.getKey();
    }

    @Override
    protected Map<String, String> handleParameters(Map<String, String> parameters) {
        //把map中的key转换成小写
        return MapUtils.keyToLowerCase(parameters);
    }
}
