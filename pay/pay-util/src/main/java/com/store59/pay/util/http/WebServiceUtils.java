/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.http;

import javax.xml.ws.WebServiceException;

import org.apache.http.Consts;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

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
    public static <REQUEST_DATA, RESPONSE_DATA> RESPONSE_DATA post(String url, REQUEST_DATA requestData,
            WebServiceHandler<REQUEST_DATA, RESPONSE_DATA> handler) {
        Request request = Request.Post(url);
        handler.handleRequest(request, requestData);

        try {
            Response response = request.execute();
            return handler.handleResponse(response);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }
    /**
     * 发起get请求.
     *
     * @param url 请求地址
     * @return 响应数据
     */
    public static String get(String url) {
        Request request = Request.Get(url);

        try {
            Response response = request.execute();
            return response.returnContent().asString(Consts.UTF_8);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

}
