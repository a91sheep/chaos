/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.util.CollectionUtils;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;

/**
 * {@link ServletRequest}相关工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public abstract class ServletRequestUtils {
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    /**
     * 获取请求参数映射.
     * 
     * @param request {@link ServletRequest}
     * @return
     */
    public static Map<String, String> getParameterMap(ServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        CollectionUtils.toIterator(request.getParameterNames()).forEachRemaining(e -> {
            map.put(e, request.getParameter(e));
        });

        return map;
    }

    /**
     * 将result对象写入response.
     * 
     * @param response {@code ServletResponse}
     * @param result {@link Result}
     * @throws IOException
     */
    public static void writeResult(ServletResponse response, Result<?> result) throws IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        response.getWriter().write(JsonUtil.getJsonFromObject(result));
    }

}
