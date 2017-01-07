/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.wxpay;

import javax.xml.bind.annotation.XmlRootElement;

import com.store59.pay.model.constants.WxpayConstants;

/**
 * 微信支付响应信息.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月11日
 * @since 1.0
 */
@XmlRootElement(name = "xml")
public class WxpayResponse extends WxpayBaseResponse {

    /**
     * 无参构造.
     */
    public WxpayResponse() {
        super();
    }

    /**
     * 有参构造.
     * 
     * @param isSuccess true：成功，false：失败
     * @param returnMsg 返回信息
     */
    public WxpayResponse(boolean isSuccess, String returnMsg) {
        super();
        super.setReturnCode(isSuccess ? WxpayConstants.SUCCESS : WxpayConstants.FAIL);
        super.setReturnMsg(returnMsg);
    }

}
