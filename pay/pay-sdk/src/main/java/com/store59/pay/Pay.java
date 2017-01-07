/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay;

import org.apache.commons.lang3.Validate;

import com.store59.pay.model.PayRequest;
import com.store59.pay.model.PayResponse;
import com.store59.pay.net.JsonWebServiceHandler;
import com.store59.pay.net.WebServiceHandler;
import com.store59.pay.util.BeanMapUtils;
import com.store59.pay.util.SignatureUtils;
import com.store59.pay.util.WebServiceUtils;

/**
 * 支付类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月20日
 * @since 1.0
 */
public abstract class Pay {

    /** 支付系统地址. */
    public static String payUrl;

    /** 支付签名密钥. */
    public static String key;

    /**
     * 创建支付请求.
     * 
     * @param payRequest {@link PayRequest}
     * @return {@link PayResponse}
     */
    public static PayResponse create(PayRequest payRequest) {
        return create(payRequest, new JsonWebServiceHandler<>(PayResponse.class));
    }

    /**
     * 创建支付请求.
     * 
     * @param payRequest {@link PayRequest}
     * @param webServiceHandler {@link WebServiceHandler}
     * @return {@link PayResponse}
     */
    public static PayResponse create(PayRequest payRequest, WebServiceHandler<PayRequest, PayResponse> webServiceHandler) {
        Validate.notBlank(payUrl, "请设置支付系统地址");
        Validate.notBlank(key, "请设置支付签名密钥");
        payRequest.setSign(SignatureUtils.sign(BeanMapUtils.toMap(payRequest), key));
        return WebServiceUtils.post(payUrl, payRequest, webServiceHandler);
    }

}
