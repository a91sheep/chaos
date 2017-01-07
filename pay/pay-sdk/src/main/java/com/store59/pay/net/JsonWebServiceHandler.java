/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.net;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.pay.util.BeanMapUtils;
import com.store59.pay.util.JsonUtils;
import com.store59.pay.util.NameValuePairUtils;

/**
 * GSON格式{@link WebServiceHandler}实现类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月7日
 * @since 1.0
 */
public class JsonWebServiceHandler<RequestData, ResponseData> extends AbstractWebServiceHandler<RequestData, ResponseData> {

    /** Web响应数据的类型. */
    private Class<ResponseData>         classOfResponseData;

    /** Web响应数据的类型引用. */
    private TypeReference<ResponseData> typeReferenceOfResponseData;

    /**
     * @param responseDataClass
     */
    public JsonWebServiceHandler(Class<ResponseData> classOfResponseData) {
        super();
        Validate.notNull(classOfResponseData, "The class of response data must not be null");
        this.classOfResponseData = classOfResponseData;
    }

    /**
     * @param typeReferenceOfResponseData
     */
    public JsonWebServiceHandler(TypeReference<ResponseData> typeReferenceOfResponseData) {
        super();
        Validate.notNull(typeReferenceOfResponseData, "The type reference of response data must not be null");
        this.typeReferenceOfResponseData = typeReferenceOfResponseData;
    }

    /**
     * @see com.store59.pay.util.http.AbstractWebServiceHandler#setRequestData(org.apache.http.client.fluent.Request, java.lang.Object)
     */
    @Override
    protected void setRequestData(Request request, RequestData requestData) {
        request.bodyForm(NameValuePairUtils.convert(BeanMapUtils.toMap(requestData)), getCharset());
    }

    /**
     * @see com.store59.pay.util.http.AbstractWebServiceHandler#parseResponseData(java.lang.String)
     */
    @Override
    protected ResponseData parseResponseData(String responseData) {
        if (classOfResponseData != null) {
            return JsonUtils.getObjectFromJson(responseData, classOfResponseData);
        }

        return JsonUtils.getObjectFromJson(responseData, typeReferenceOfResponseData);
    }

}
