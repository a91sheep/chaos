/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.signature;

import java.util.HashMap;
import java.util.Map;

import com.store59.pay.model.constants.TianyipayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store59.pay.model.constants.PayConstants;
import com.store59.pay.model.enums.PayChannelEnum;

/**
 * 签名服务实现类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@Service
public class SignatureServiceImpl implements SignatureService, InitializingBean {
    private static final Logger            logger  = LoggerFactory.getLogger(SignatureServiceImpl.class);

    @Autowired
    private AlipayWebSignature             alipayWebSignature;

    @Autowired
    private DefaultSignature               defaultSignature;

    @Autowired
    private AlipayQrCodeSignature          alipayQrCodeSignature;

    @Autowired
    private WxpaySignature                 wxpaySignature;

    @Autowired
    private WxpayAppSignature              wxpayAppSignature;

    @Autowired
    private TianyipaySignature tianyipaySignature;

    private Map<PayChannelEnum, Signature> signMap = new HashMap<>();

    /**
     * @see com.store59.pay.service.signature.SignatureService#sign(java.util.Map)
     */
    @Override
    public String sign(Map<String, String> parameters) {
        return defaultSignature.sign(parameters);
    }

    /**
     * @see com.store59.pay.service.signature.SignatureService#verify(java.util.Map)
     */
    @Override
    public boolean verify(Map<String, String> parameters) {
        Map<String, String> map = new HashMap<>(parameters);
        String sign = map.remove(PayConstants.SIGN);
        return defaultSignature.verify(map, sign);
    }

    /**
     * @see com.store59.pay.service.signature.SignatureService#sign(java.util.Map, com.store59.pay.model.enums.PayChannelEnum)
     */
    @Override
    public String sign(Map<String, String> parameters, PayChannelEnum payChannel) {
        if (!signMap.containsKey(payChannel)) {
            logger.error("该支付渠道不支持签名：{}", payChannel);
            return null;
        }

        // 获取签名类
        Signature signature = signMap.get(payChannel);
        return signature.sign(parameters);
    }

    /**
     * @see com.store59.pay.service.signature.SignatureService#verify(java.util.Map, com.store59.pay.model.enums.PayChannelEnum)
     */
    @Override
    public boolean verify(Map<String, String> parameters, PayChannelEnum payChannel) {
        // 获取签名类
        Signature signature = signMap.get(payChannel);
        if (signature == null) {
            logger.error("该支付渠道不支持签名：{}", payChannel);
            return false;
        }

        //翼支付比较奇葩, 必须是大写的SIGN
        String sign = "";
        if(payChannel == PayChannelEnum.TIANYI_APP){
            sign = parameters.remove(TianyipayConstants.SIGN);
        }else{
            sign = parameters.remove(PayConstants.SIGN);
        }

        // 校验
        return signature.verify(parameters, sign);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        signMap.put(PayChannelEnum.ALIPAY_WAP, alipayWebSignature);
        signMap.put(PayChannelEnum.ALIPAY_PC, alipayWebSignature);
        signMap.put(PayChannelEnum.ALIPAY_APP, alipayWebSignature);
        signMap.put(PayChannelEnum.ALIPAY_SCAN, alipayQrCodeSignature);
        signMap.put(PayChannelEnum.WXPAY_JSAPI, wxpaySignature);
        signMap.put(PayChannelEnum.WXPAY_SCAN, wxpaySignature);
        signMap.put(PayChannelEnum.WXPAY_APP, wxpayAppSignature);
        signMap.put(PayChannelEnum.STORE59_SPEND, defaultSignature);
        signMap.put(PayChannelEnum.TIANYI_APP, tianyipaySignature);
    }
}
