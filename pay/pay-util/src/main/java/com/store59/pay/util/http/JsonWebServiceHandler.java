/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.http;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.utils.JsonUtil;

/**
 * GSON格式{@link WebServiceHandler}实现类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月7日
 * @since 1.0
 */
public class JsonWebServiceHandler<REQUEST_DATA, RESPONSE_DATA> extends AbstractWebServiceHandler<REQUEST_DATA, RESPONSE_DATA> {

    /** Web响应数据的类型. */
    private Class<RESPONSE_DATA>         classOfResponseData;

    /** Web响应数据的类型引用. */
    private TypeReference<RESPONSE_DATA> typeReferenceOfResponseData;

    /**
     * @param responseDataClass
     */
    public JsonWebServiceHandler(Class<RESPONSE_DATA> classOfResponseData) {
        super();
        Assert.notNull(classOfResponseData, "The class of response data must not be null");
        this.classOfResponseData = classOfResponseData;
    }

    /**
     * @param typeReferenceOfResponseData
     */
    public JsonWebServiceHandler(TypeReference<RESPONSE_DATA> typeReferenceOfResponseData) {
        super();
        Assert.notNull(typeReferenceOfResponseData, "The type reference of response data must not be null");
        this.typeReferenceOfResponseData = typeReferenceOfResponseData;
    }

    /**
     * @see com.store59.pay.util.http.AbstractWebServiceHandler#setRequestData(org.apache.http.client.fluent.Request, java.lang.Object)
     */
    @Override
    protected void setRequestData(Request request, REQUEST_DATA requestData) {
        String json = JsonUtil.getJsonFromObject(requestData);
        request.bodyString(json, ContentType.create("application/json", getCharset()));
    }

    /**
     * @see com.store59.pay.util.http.AbstractWebServiceHandler#parseResponseData(java.lang.String)
     */
    @Override
    protected RESPONSE_DATA parseResponseData(String responseData) {
        if (classOfResponseData != null) {
            return JsonUtil.getObjectFromJson(responseData, classOfResponseData);
        }

        return JsonUtil.getObjectFromJson(responseData, typeReferenceOfResponseData);
    }

}
