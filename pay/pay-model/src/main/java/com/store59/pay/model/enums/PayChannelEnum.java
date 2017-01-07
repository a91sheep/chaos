/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.model.enums;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.store59.kylin.utils.EnumCode;

/**
 * 支付渠道.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月18日
 * @since 1.0
 */
public enum PayChannelEnum implements EnumCode<String> {

    /** 支付宝手机网页支付. */
    ALIPAY_WAP("alipay_wap", PayPlatformEnum.ALIPAY, "wap", "支付宝手机网页支付"),

    /** 支付宝PC网页支付. */
    ALIPAY_PC("alipay_pc", PayPlatformEnum.ALIPAY, "pc", "支付宝PC网页支付"),

    /** 支付宝APP支付. */
    ALIPAY_APP("alipay_app", PayPlatformEnum.ALIPAY, "app", "支付宝APP支付"),

    /** 支付宝扫码支付. */
    ALIPAY_SCAN("alipay_scan", PayPlatformEnum.ALIPAY, "qrcode", "支付宝扫码支付"),

    /** 微信公众号支付. */
    WXPAY_JSAPI("wxpay_jsapi", PayPlatformEnum.WXPAY, "jsapi", "微信公众号支付"),

    /** 微信APP支付. */
    WXPAY_APP("wxpay_app", PayPlatformEnum.WXPAY, "app", "微信APP支付"),

    /** 微信刷卡支付. */
    WXPAY_SCAN("wxpay_swipe_card", PayPlatformEnum.WXPAY, "scan", "微信刷卡支付"),

    /** 59store白花花支付. */
    STORE59_SPEND("spend", PayPlatformEnum.STORE59, "spend", "白花花支付"),

    /* 信用付 */
    STORE59_CREDIT_CARD("credit_card", PayPlatformEnum.STORE59, "credit_card", "信用支付"),

    TIANYI_APP("tianyi_app", PayPlatformEnum.TIANYI, "tianyi_app", "翼支付app支付"),

    ;

    private String          code;

    private PayPlatformEnum platform;

    private String          shortName;

    private String          desc;

    /**
     * 有参构造.
     * 
     * <p>
     * platform和shortName必须唯一.
     * </p>
     * 
     * @param code
     * @param platform
     * @param shortName
     * @param desc
     */
    private PayChannelEnum(String code, PayPlatformEnum platform, String shortName, String desc) {
        this.code = code;
        this.platform = platform;
        this.shortName = shortName;
        this.desc = desc;
    }

    public static PayChannelEnum getEnum(PayPlatformEnum platform, String shortName) {
        if (platform == null || StringUtils.isBlank(shortName) || values() == null) {
            return null;
        }

        return Arrays.stream(values()).filter(e -> e.getPlatform() == platform && StringUtils.equals(e.getShortName(), shortName)).findAny()
                .orElse(null);
    }

    /**
     * @see com.store59.kylin.utils.EnumCode#getCode()
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @return the platform
     */
    public PayPlatformEnum getPlatform() {
        return platform;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 获取请求URL.
     * 
     * @param prefix URL前缀
     * @param postfix URL后缀
     * @return URL
     */
    public String getRequestUrl(String prefix, String postfix) {
        prefix = StringUtils.removeEnd(prefix, "/");
        postfix = StringUtils.removeStart(postfix, "/");
        return StringUtils.join(new String[] { prefix, platform.getCode(), shortName, postfix }, "/");
    }

}
