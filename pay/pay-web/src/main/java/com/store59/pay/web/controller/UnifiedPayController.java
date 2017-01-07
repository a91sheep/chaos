/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.store59.pay.web.enums.ViewResultCodeEnum;
import com.store59.pay.web.form.wxpay.WxpayUnifiedPayForm;
import com.store59.pay.web.model.ViewResult;
import com.store59.pay.web.utils.ServletRequestUtils;

/**
 * 统一支付控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月20日
 * @since 1.0
 */
@Controller
public class UnifiedPayController {
    private static final Logger logger = LoggerFactory.getLogger(UnifiedPayController.class);

    /**
     * 发起支付.
     * 
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param form {@link WxpayUnifiedPayForm}
     * @param result {@link BindingResult}
     * @throws Exception
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public void pay(HttpServletRequest request, HttpServletResponse response, @Valid WxpayUnifiedPayForm form, BindingResult result) throws Exception {
        ViewResult<?> viewResult = new ViewResult<>();
        if (result.hasErrors()) {
            logger.error("【统一支付】参数有误: form={}, error={}", form, result.getFieldError().getDefaultMessage());
            viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
            viewResult.setMsg(result.getFieldError().getDefaultMessage());
            ServletRequestUtils.writeResult(response, viewResult.toResult());
        } else {
            request.getRequestDispatcher(form.getChannel().getRequestUrl("", "pay")).forward(request, response);
        }
    }

}
