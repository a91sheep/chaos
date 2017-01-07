/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.util.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * {@link NameValuePair}工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public abstract class NameValuePairUtils {

    /**
     * 将Map转换为List<{@link NameValuePair}>.
     * 
     * @param map
     * @return
     */
    public static List<NameValuePair> convert(Map<String, String> map) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        map.forEach((key, value) -> {
            nameValuePairs.add(new BasicNameValuePair(key, value));
        });

        return nameValuePairs;
    }

}
