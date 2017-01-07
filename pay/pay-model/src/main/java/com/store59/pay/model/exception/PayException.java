/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.exception;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.pay.model.enums.BizResultCodeEnum;

/**
 * 支付异常类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
public class PayException extends ServiceException {
    private static final long serialVersionUID = -4441845564395418835L;

    private BizResultCodeEnum resultCode;

    /**
     * 无参构造.
     */
    public PayException() {
        super();
    }

    /**
     * @param message
     */
    public PayException(String message) {
        super(-1, message);
    }

    /**
     * @param message
     * @param cause
     */
    public PayException(String message, Throwable cause) {
        super(-1, message, cause);
    }

    /**
     * @param status
     * @param msg
     */
    public PayException(BizResultCodeEnum resultCode) {
        super(resultCode.getCode(), resultCode.getDesc());
        this.resultCode = resultCode;
    }

    /**
     * @param resultCode
     * @param msg
     */
    public PayException(BizResultCodeEnum resultCode, String msg) {
        super(resultCode.getCode(), msg);
        this.resultCode = resultCode;
    }

    /**
     * @param resultCode
     * @param cause
     */
    public PayException(BizResultCodeEnum resultCode, Throwable cause) {
        super(resultCode.getCode(), resultCode.getDesc(), cause);
        this.resultCode = resultCode;
    }

    /**
     * @return the resultCode
     */
    public BizResultCodeEnum getResultCode() {
        return resultCode;
    }

}
