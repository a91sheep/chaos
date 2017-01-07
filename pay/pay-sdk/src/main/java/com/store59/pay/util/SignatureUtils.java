/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 签名工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月14日
 * @since 1.0
 */
public abstract class SignatureUtils {

    /**
     * 对Map参数进行签名.
     * 
     * @param paramsMap Map参数
     * @param key 密钥
     * @return 签名字符串
     */
    public static String sign(Map<String, String> paramsMap, String key) {
        if (MapUtils.isEmpty(paramsMap) || StringUtils.isBlank(key)) {
            return "";
        }

        List<String> paramKeyList = new ArrayList<>(paramsMap.keySet());
        Collections.sort(paramKeyList);

        StringBuilder sb = new StringBuilder();
        for (String paramKey : paramKeyList) {
            sb.append(paramKey).append("=").append(paramsMap.get(paramKey)).append("&");
        }

        sb.append(key);
        return DigestUtils.md5Hex(sb.toString());
    }

}
