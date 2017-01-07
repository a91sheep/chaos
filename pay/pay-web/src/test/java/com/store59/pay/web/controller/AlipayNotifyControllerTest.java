package com.store59.pay.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.alipay.AlipayBodyField;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/4/20
 * @since 1.0
 */
public class AlipayNotifyControllerTest {


    @Test
    public void asyncNotify() throws Exception {
        //body是个josn字符
        String body = "{\"notifyUrl\":\"http://dorm.59store.com/cerberus/callback/alipay/dormRecharge\",\"returnUrl\":\"\"}";
        AlipayBodyField alipayBodyField = JsonUtil.getObjectFromJson(body, new TypeReference<AlipayBodyField>(){});
        System.out.println(JsonUtil.getJsonFromObject(alipayBodyField));
    }
}