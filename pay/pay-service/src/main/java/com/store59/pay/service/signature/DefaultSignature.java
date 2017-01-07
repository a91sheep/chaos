/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.signature;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.store59.kylin.utils.SignatureUtils;
import com.store59.pay.service.config.PayConfig;

/**
 * 系统默认签名.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月18日
 * @since 1.0
 */
@Component
public class DefaultSignature implements Signature {

    @Autowired
    private PayConfig payConfig;

    /**
     * @see com.store59.pay.service.signature.Signature#sign(java.util.Map)
     */
    @Override
    public String sign(Map<String, String> parameters) {
        return SignatureUtils.sign(parameters, payConfig.getKey());
    }

    /**
     * @see com.store59.pay.service.signature.Signature#verify(java.util.Map, java.lang.String)
     */
    @Override
    public boolean verify(Map<String, String> parameters, String sign) {
        return StringUtils.equals(sign, this.sign(parameters));
    }

}
