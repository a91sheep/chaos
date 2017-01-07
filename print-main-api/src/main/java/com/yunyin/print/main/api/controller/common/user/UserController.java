/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.yunyin.print.main.api.controller.common.user;

import com.yunyin.base.common.dto.Result;
import com.yunyin.print.main.api.model.viewobject.UserVO;
import com.yunyin.print.main.api.service.common.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:guopf@59store.com">任之</a>
 * @version 1.0 2016/12/20
 * @since 1.0
 */
@RestController
@RequestMapping("/print/user/*")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public Result<UserVO> register() {
        return userService.register();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public Result<UserVO> login() {
        return userService.login();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public Result<UserVO> logout() {
        return userService.logout();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/verification")
    public Result<String> verificationCode() {
        return userService.verificationCode();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/message")
    public Result<String> sendMessage() {
        return userService.sendMessage();
    }

}
