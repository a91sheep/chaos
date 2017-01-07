package com.store59.pay.model.enums;

/**
 * 翼支付回调支付状态说明
 * Created by 凌云
 * lly835@163.com
 * 2016-04-24 21:43
 */
public enum TianyipayTradeStatusEnum {
    SUCCESS("0000", "支付成功")
    ;

    private String code;
    private String desc;

    TianyipayTradeStatusEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
