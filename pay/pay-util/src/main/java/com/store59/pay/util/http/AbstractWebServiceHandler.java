/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.http;

import java.nio.charset.Charset;

import javax.xml.ws.WebServiceException;

import org.apache.http.Consts;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

/**
 * {@link WebServiceHandler}抽象实现类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月7日
 * @since 1.0
 */
public abstract class AbstractWebServiceHandler<REQUEST_DATA, RESPONSE_DATA> implements WebServiceHandler<REQUEST_DATA, RESPONSE_DATA> {

    /** 连接超时时间，单位毫秒. */
    private int     connectTimeout = 3000;

    /** 传输超时时间，单位毫秒. */
    private int     socketTimeout  = 10000;

    /** 字符集. */
    private Charset charset        = Consts.UTF_8;

    /**
     * @see com.store59.pay.util.http.WebServiceHandler#handleRequest(org.apache.http.client.fluent.Request, java.lang.Object)
     */
    @Override
    public void handleRequest(Request request, REQUEST_DATA requestData) {
        request.connectTimeout(connectTimeout);
        request.socketTimeout(socketTimeout);
        setRequestData(request, requestData);
    }

    /**
     * 设置请求数据.
     * 
     * @param request {@link Request}
     * @param requestData 请求数据
     */
    protected abstract void setRequestData(Request request, REQUEST_DATA requestData);

    /**
     * @see com.store59.pay.util.http.WebServiceHandler#handleResponse(org.apache.http.client.fluent.Response)
     */
    @Override
    public RESPONSE_DATA handleResponse(Response response) {
        try {
            String responseData = response.returnContent().asString(charset);
            return parseResponseData(responseData);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * 解析响应数据.
     * 
     * @param responseData 响应数据
     * @return
     */
    protected abstract RESPONSE_DATA parseResponseData(String responseData);

    /**
     * @return the connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * @param connectTimeout the connectTimeout to set
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * @return the socketTimeout
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * @param socketTimeout the socketTimeout to set
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

}
