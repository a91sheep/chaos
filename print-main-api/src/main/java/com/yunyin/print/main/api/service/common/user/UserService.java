/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.yunyin.print.main.api.service.common.user;

import com.yunyin.base.common.api.BaseApi;
import com.yunyin.base.common.api.UserApi;
import com.yunyin.base.common.dto.Result;
import com.yunyin.base.common.utils.ResultUtils;
import com.yunyin.print.main.api.model.viewobject.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:guopf@59store.com">任之</a>
 * @version 1.0 2016/12/21
 * @since 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserApi userApi;

    @Autowired
    private BaseApi baseApi;

    public Result<UserVO> register() {
        return null;
    }

    public Result<UserVO> login() {
        return null;
    }

    public Result<UserVO> logout() {
        return null;
    }

    public Result<String> verificationCode() {
        return ResultUtils.genResultWithSuccess(baseApi.sendVerificationCode(""));
    }


    public Result<String> sendMessage() {
        baseApi.sendMessage("", "");
        return ResultUtils.genResultWithSuccess();
    }
}
