/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.store59.pay.model.constants.WxpayConstants;

/**
 * 微信支付返回的基础信息.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public abstract class WxpayBaseResponse {

    /** 返回状态码. */
    @XmlElement(name = "return_code")
    private String returnCode;

    /** 返回信息. */
    @XmlElement(name = "return_msg")
    private String returnMsg;

    /**
     * @return the returnCode
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * @param returnCode the returnCode to set
     */
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @return the returnMsg
     */
    public String getReturnMsg() {
        return returnMsg;
    }

    /**
     * @param returnMsg the returnMsg to set
     */
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    /**
     * 判断返回结果是否成功.
     * 
     * @return true：成功，false：失败
     */
    public boolean isSuccess() {
        return StringUtils.equals(this.getReturnCode(), WxpayConstants.SUCCESS);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
