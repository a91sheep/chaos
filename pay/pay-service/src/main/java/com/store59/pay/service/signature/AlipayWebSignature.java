/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.signature;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.store59.pay.service.config.AlipayConfig;
import com.store59.pay.util.encrypt.RSA;
import com.store59.pay.util.lang.MapUtils;

/**
 * 支付宝web端支付签名
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/1/7
 * @since 1.0
 */

@Component
public class AlipayWebSignature implements Signature {

    private static final Logger logger = LoggerFactory.getLogger(AlipayWebSignature.class);

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private AlipayMessageSignature alipayMessageSignature;

    @Override
    public String sign(Map<String, String> parameters) {
        //Map转Url
        String content = MapUtils.toUrlWithSort(parameters);
        logger.debug("【支付宝web端支付】要签名的内容:{}", content);

        logger.debug("【支付宝web端支付】签名key:{}", alipayConfig.getPrivateKey());

        //使用私钥签名
        String sign = RSA.sign(content, alipayConfig.getPrivateKey(), alipayConfig.getInputCharset());
        logger.debug("【支付宝web端支付】计算出来的签名:{}", sign);

        return sign;
    }

    /**
     * @see com.store59.pay.service.signature.Signature#verify(java.util.Map, java.lang.String)
     */
    @Override
    public boolean verify(Map<String, String> parameters, String sign) {
        //去除不参与签名的参数
        parameters = MapUtils.removeParamsForAlipaySign(parameters);
        //Map转Url
        String content = MapUtils.toUrlWithSort(parameters);
        logger.debug("【支付宝web端支付验证签名】要验证的签名内容:{}", content);

        logger.debug("【支付宝web端支付验证签名】支付宝传过来的sign={}", sign);

        logger.debug("【支付宝web端支付验证签名】验证签名使用的key={}", alipayConfig.getPublicKey());

        //支付宝消息校验
        boolean notifyIdFlag =  alipayMessageSignature.verify(parameters.get("notify_id"));
        //使用公钥验证
        boolean flag = RSA.verify(content, sign, alipayConfig.getPublicKey(), alipayConfig.getInputCharset());

        if(!notifyIdFlag){
            logger.error("【支付宝web端支付验证签名】验证notifyId失败");
            return false;
        }

        if(!flag){
            logger.error("【支付宝web端支付验证签名】验证签名失败");
            return false;
        }

        logger.debug("【支付宝web端支付验证签名】验证notifyId和签名成功");
        return true;
    }
}
