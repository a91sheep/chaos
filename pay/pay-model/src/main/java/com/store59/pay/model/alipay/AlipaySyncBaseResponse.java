/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.model.alipay;

/**
 * 支付宝同步返回的参数 基础父类
 * 见 https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.ZjS7dj&treeId=26&articleId=103295&docType=1
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/1/8
 * @since 1.0
 */
public class AlipaySyncBaseResponse {

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
