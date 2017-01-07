/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.pay.util.lang.ObjectToStringUtils;
import com.store59.pay.web.enums.ViewResultCodeEnum;

/**
 * View层异常处理器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月20日
 * @since 1.0
 */
@ControllerAdvice
public class ViewExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ViewExceptionHandler.class);

    /**
     * 处理异常.
     * 
     * @param req {@link HttpServletRequest}
     * @param ex {@link Exception}
     * @return {@link Result}
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleException(HttpServletRequest req, Exception ex) {
        Result<Object> result = new Result<>();
        if (ex instanceof BaseException) {
            BaseException e = (BaseException) ex;
            result.setStatus(e.getStatus());
            result.setMsg(e.getMsg());
        } else {
            result.setStatus(ViewResultCodeEnum.UNKNOWN_ERROR.getCode());
            result.setMsg(ViewResultCodeEnum.UNKNOWN_ERROR.getMsg());
        }

        logger.error("Controller发生异常: " + ObjectToStringUtils.reflectionToString(result), ex);
        return result;
    }

}
