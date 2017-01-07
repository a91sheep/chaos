/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.util;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author <a href="mailto:chenyb@59store.com">山人</a>
 * @version 1.0 15/11/6
 * @since 1.0
 */
public class JsonUtils {

    private static ObjectMapper JSON = new ObjectMapper();

    public static String getJsonFromObject(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return JSON.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getObjectFromJson(String json, Class<T> valueType) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return JSON.readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getObjectFromJson(String json, TypeReference<T> valueTupeRef) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return JSON.readValue(json, valueTupeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
