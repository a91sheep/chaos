/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.alipay;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 支付宝同步返回的参数 基础父类
 * 见 https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.ZjS7dj&treeId=26&articleId=103295&docType=1
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/1/8
 * @since 1.0
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class AlipaySyncResponse <T extends AlipaySyncBaseResponse>  {

    /* 返回的主要参数 */
    private T alipayTradePrecreateResponse;

    private String sign;

    public T getAlipayTradePrecreateResponse() {
        return alipayTradePrecreateResponse;
    }

    public void setAlipayTradePrecreateResponse(T alipayTradePrecreateResponse) {
        this.alipayTradePrecreateResponse = alipayTradePrecreateResponse;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
