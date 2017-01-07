/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.net;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

/**
 * Web服务处理器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月7日
 * @since 1.0
 */
public interface WebServiceHandler<RequestData, ResponseData> {

    /**
     * 处理请求，比如：设置请求参数和请求数据。
     * 
     * @param request {@link Request}
     * @param requestData 请求数据
     */
    void handleRequest(Request request, RequestData requestData);

    /**
     * 处理响应，比如：将响应内容转换为对象。
     * 
     * @param response {@link Response}
     * @return 响应数据
     */
    ResponseData handleResponse(Response response);

}
