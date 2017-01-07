/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.signature;

import java.util.Map;

import com.store59.pay.model.enums.PayChannelEnum;

/**
 * 签名服务接口.
 *
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public interface SignatureService {

    /**
     * 对参数进行签名.
     * 
     * <p>
     * 使用系统默认签名算法.
     * </p>
     * 
     * @param parameters 参数
     * @return 签名
     */
    String sign(Map<String, String> parameters);

    /**
     * 验证参数签名.
     * 
     * <p>
     * 使用系统默认签名算法.
     * </p>
     * 
     * @param parameters 参数
     * @return true：签名正确，false：签名有误
     */
    boolean verify(Map<String, String> parameters);

    /**
     * 对参数进行签名.
     * 
     * @param parameters 参数
     * @param payChannel 支付渠道
     * @return 签名
     */
    String sign(Map<String, String> parameters, PayChannelEnum payChannel);

    /**
     * 验证参数签名.
     * 
     * @param parameters 参数
     * @param payChannel 支付渠道
     * @return true：签名正确，false：签名有误
     */
    boolean verify(Map<String, String> parameters, PayChannelEnum payChannel);

}
