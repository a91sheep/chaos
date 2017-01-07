package com.store59.pay.service.util;

import java.util.Map;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.common.model.Result;
import com.store59.pay.util.http.JsonWebServiceHandler;
import com.store59.pay.util.http.NameValuePairUtils;
import com.store59.pay.util.http.WebServiceUtils;

public class HttpClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);
	
    public static Result<Map<String, String>> post(String reqUrl, Map<String, String> map) {
        LOGGER.info("reqUrl={}", reqUrl);
    	return WebServiceUtils.post(reqUrl, map,
                new JsonWebServiceHandler<Map<String, String>, Result<Map<String, String>>>(new TypeReference<Result<Map<String, String>>>() {
                }) {
                    @Override
                    protected void setRequestData(Request request, Map<String, String> requestData) {
                        request.bodyForm(NameValuePairUtils.convert(requestData), getCharset());
                    }
                });
    }
}
