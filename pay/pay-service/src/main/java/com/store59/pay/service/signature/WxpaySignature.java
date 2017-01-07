/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.signature;

import com.store59.pay.model.constants.PayConstants;
import com.store59.pay.service.config.WxpayConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付签名.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月18日
 * @since 1.0
 */
@Component
public class WxpaySignature extends DefaultSignature {

    private static final Logger logger = LoggerFactory.getLogger(WxpaySignature.class);

    @Autowired
    private WxpayConfig wxpayConfig;

    /**
     * 对map的处理
     * @param parameters
     * @return
     */
    protected Map<String, String> handleParameters(Map<String, String> parameters) {

        return parameters;
    }

    /**
     * @see com.store59.pay.service.signature.Signature#sign(java.util.Map)
     */
    @Override
    public String sign(Map<String, String> parameters) {
        if (MapUtils.isEmpty(parameters)) {
            return "";
        }

        parameters = handleParameters(parameters);

        ArrayList<String> list = new ArrayList<>();
        parameters.forEach((k, v) -> {
            if (StringUtils.isNotBlank(k) && StringUtils.isNotBlank(v)) {
                list.add(k + "=" + v);
            }
        });

        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        list.add(PayConstants.KEY + "=" + this.getSignKey());

        logger.debug("【微信支付】签名的key: {}", this.getSignKey());
        logger.debug("【微信支付】要签名的参数: {}", StringUtils.join(list, "&"));
        logger.debug("【微信支付】签名后的结果: {}", StringUtils.upperCase(DigestUtils.md5Hex(StringUtils.join(list, "&"))));

        return StringUtils.upperCase(DigestUtils.md5Hex(StringUtils.join(list, "&")));
    }

    /**
     * 获取签名用的密钥.
     * 
     * @return 密钥
     */
    protected String getSignKey() {
        return wxpayConfig.getKey();
    }

}
