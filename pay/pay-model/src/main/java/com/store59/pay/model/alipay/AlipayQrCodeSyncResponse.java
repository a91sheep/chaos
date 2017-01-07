/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.alipay;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 扫码付 同步返回的参数
 * 见 https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.ZjS7dj&treeId=26&articleId=103295&docType=1
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/1/8
 * @since 1.0
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AlipayQrCodeSyncResponse extends AlipaySyncBaseResponse{

    private String code;

    private String msg;

    private String outTradeNo;

    private String qrCode;

    private String subCode;

    private String subMsg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }
}
