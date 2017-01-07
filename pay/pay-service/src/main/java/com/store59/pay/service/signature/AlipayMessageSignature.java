/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.signature;

import com.store59.pay.model.constants.AlipayConstants;
import com.store59.pay.service.config.AlipayConfig;
import com.store59.pay.util.http.WebServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 支付宝消息id检验, 检查回调的通知是否来着支付宝官方
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/4/6
 * @since 1.0
 */
@Component
public class AlipayMessageSignature {

    private static final Logger logger = LoggerFactory.getLogger(AlipayMessageSignature.class);

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 校验, 合法返回true
     * @return
     */
    public boolean verify(String notify_id){
        String veryfyUrl = AlipayConstants.ALIPAY_VERIFY_URL + "partner=" + alipayConfig.getPartner() + "&notify_id=" + notify_id;
        logger.debug("【支付宝消息id检验】veryfyUrl={}", veryfyUrl);

        String result = WebServiceUtils.get(veryfyUrl);
        logger.debug("【支付宝消息id检验】支付宝返回的信息, result={}", result);

        if(result.equals("true")){
            return true;
        }

        return false;
    }
}
