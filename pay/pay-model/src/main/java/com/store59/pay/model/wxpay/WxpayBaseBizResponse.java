/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;

import com.store59.pay.model.constants.WxpayConstants;

/**
 * 微信支付业务返回的基础信息.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月12日
 * @since 1.0
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public abstract class WxpayBaseBizResponse extends WxpayBaseResponse {

    /** 业务结果. */
    @XmlElement(name = "result_code")
    private String resultCode;

    /** 错误代码. */
    @XmlElement(name = "err_code")
    private String errCode;

    /** 错误代码描述. */
    @XmlElement(name = "err_code_des")
    private String errCodeDes;

    /**
     * @return the resultCode
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @return the errCode
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * @param errCode the errCode to set
     */
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    /**
     * @return the errCodeDes
     */
    public String getErrCodeDes() {
        return errCodeDes;
    }

    /**
     * @param errCodeDes the errCodeDes to set
     */
    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    /**
     * @see com.store59.pay.model.wxpay.WxpayBaseResponse#isSuccess()
     */
    @Override
    public boolean isSuccess() {
        return super.isSuccess() && StringUtils.equals(this.getResultCode(), WxpayConstants.SUCCESS);
    }

}
