/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信APP支付配置.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月19日
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "wxpay.app")
public class WxpayAppConfig extends WxpayConfig {

}
