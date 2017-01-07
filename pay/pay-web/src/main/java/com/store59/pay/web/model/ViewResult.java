/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.model;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.store59.kylin.common.model.Result;
import com.store59.pay.web.enums.ViewResultCodeEnum;

/**
 * 视图对象.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月28日
 * @since 1.0
 */
public class ViewResult<T> {
    private ViewResultCodeEnum resultCode;
    private String             msg;
    private T                  data;
    private Object[]           msgFormatArgs;

    public ViewResult() {
    }

    /**
     * @param resultCode
     */
    public ViewResult(ViewResultCodeEnum resultCode) {
        this.resultCode = resultCode;
    }

    public ViewResult(ViewResultCodeEnum resultCode, T data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public ViewResult(ViewResultCodeEnum resultCode, Object... msgFormatArgs) {
        this.resultCode = resultCode;
        this.msgFormatArgs = msgFormatArgs;
    }

    public boolean isSuccess() {
        return resultCode == ViewResultCodeEnum.SUCCESS;
    }

    /**
     * Convert this to {@link Result} object.
     * 
     * @return
     */
    public Result<T> toResult() {
        Result<T> result = toResultIgnoreData();
        result.setData(data);
        return result;
    }

    /**
     * Convert this to {@link Result} object ignore the data field.
     * 
     * @return
     */
    public <D> Result<D> toResultIgnoreData() {
        Result<D> result = new Result<>();
        if (StringUtils.isNotBlank(msg)) {
            result.setMsg(MessageFormat.format(msg, msgFormatArgs));
        }

        if (resultCode != null) {
            result.setStatus(resultCode.getCode());
            if (StringUtils.isBlank(result.getMsg())) {
                result.setMsg(resultCode.getMsg(msgFormatArgs));
            }
        }

        return result;
    }

    /**
     * @return the resultCode
     */
    public ViewResultCodeEnum getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public void setResultCode(ViewResultCodeEnum resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the msgFormatArgs
     */
    public Object[] getMsgFormatArgs() {
        return msgFormatArgs;
    }

    /**
     * @param msgFormatArgs the msgFormatArgs to set
     */
    public void setMsgFormatArgs(Object... msgFormatArgs) {
        this.msgFormatArgs = msgFormatArgs;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
