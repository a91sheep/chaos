/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util;

import javax.xml.ws.WebServiceException;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import com.store59.pay.net.WebServiceHandler;

/**
 * Web服务工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月6日
 * @since 1.0
 */
public abstract class WebServiceUtils {

    /**
     * 发起post请求.
     * 
     * @param url 请求地址
     * @param requestData 请求数据
     * @param handler {@link WebServiceHandler}
     * @return 响应数据
     */
    public static <RequestData, ResponseData> ResponseData post(String url, RequestData requestData,
            WebServiceHandler<RequestData, ResponseData> handler) {
        Request request = Request.Post(url);
        handler.handleRequest(request, requestData);

        try {
            Response response = request.execute();
            return handler.handleResponse(response);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

}
