package com.store59.base.common.api;

import com.store59.kylin.common.model.Result;

public interface SmsAuthApi {
    /**
     * 发送验证码
     * 
     * @param authType
     * @param phone
     * @param timeout
     * @return
     */
    Result<String> sendAuthCode(String authType, String phone, Integer timeout);

    /**
     * 确认验证码
     * 
     * @param authType
     * @param phone
     * @param authCode
     * @return
     */
    Result<Boolean> checkAuthCode(String authType, String phone, String authCode);

}
