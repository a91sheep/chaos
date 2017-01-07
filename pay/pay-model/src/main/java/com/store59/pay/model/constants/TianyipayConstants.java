package com.store59.pay.model.constants;

/**
 * 翼支付常量
 * Created by 凌云
 * lly835@163.com
 * 2016-04-24 21:06
 */
public interface TianyipayConstants {

    /**
     * 请求处理成功.
     * 商户平台在收到后台支付结果后，
     * 请直接在应答时写入格式为UPTRANSEQ_XXXXXX的字符串，
     * 其中UPTRANSEQ_ 为固定写死，
     * XXXXXX为翼支付网关平台发送过去的翼支付网关平台交易流水号
     */
    String SUCCESS = "UPTRANSEQ_";

    /** 请求处理失败. */
    String FAIL               = "fail";

    /** 签名. */
    String SIGN = "SIGN";

    /** 密钥. */
    String KEY  = "KEY";
}
