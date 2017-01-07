/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.http;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.util.Assert;

import com.store59.pay.util.oxm.OXMUtils;

/**
 * XML格式{@link WebServiceHandler}实现类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月7日
 * @since 1.0
 */
public class XmlWebServiceHandler<REQUEST_DATA, RESPONSE_DATA> extends AbstractWebServiceHandler<REQUEST_DATA, RESPONSE_DATA> {

    /** Web响应数据的类型. */
    private Class<RESPONSE_DATA> classOfResponseData;

    /**
     * @param jaxb2Marshaller
     */
    public XmlWebServiceHandler(Class<RESPONSE_DATA> classOfResponseData) {
        super();
        Assert.notNull(classOfResponseData, "The classOfResponseData must not be null");
        this.classOfResponseData = classOfResponseData;
    }

    /**
     * @see com.store59.pay.util.http.AbstractWebServiceHandler#setRequestData(org.apache.http.client.fluent.Request, java.lang.Object)
     */
    @Override
    protected void setRequestData(Request request, REQUEST_DATA requestData) {
        request.bodyString(OXMUtils.marshal(requestData, getCharset()), ContentType.create("text/xml", getCharset()));
    }

    /**
     * @see com.store59.pay.util.http.AbstractWebServiceHandler#parseResponseData(java.lang.String)
     */
    @Override
    protected RESPONSE_DATA parseResponseData(String responseData) {
        return OXMUtils.unmarshal(responseData, classOfResponseData);
    }

}
