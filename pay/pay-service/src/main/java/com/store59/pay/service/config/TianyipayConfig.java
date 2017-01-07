package com.store59.pay.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 翼支付配置项
 * Created by 廖师兄
 * lly835@163.com
 * 2016-04-24 21:13
 */
@Component
@ConfigurationProperties(prefix = "tianyipay")
public class TianyipayConfig {
    /** 密钥. */
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
