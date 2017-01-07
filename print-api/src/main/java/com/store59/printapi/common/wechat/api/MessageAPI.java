package com.store59.printapi.common.wechat.api;

import java.nio.charset.Charset;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;

import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.wechat.LocalHttpClient;
import com.store59.printapi.model.param.wechat.BaseResult;
import com.store59.printapi.model.param.wechat.message.Message;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年5月12日 下午7:51:19 消息发送api
 */
public class MessageAPI extends BaseAPI {

	/**
	 * 消息发送
	 * 
	 * @param access_token
	 * @param messageJson
	 * @return
	 */
	public static BaseResult messageCustomSend(String access_token, String messageJson) {
		HttpUriRequest httpUriRequest = RequestBuilder.post().setHeader(jsonHeader)
				.setUri(BASE_URI + "/cgi-bin/message/custom/send").addParameter(getATPN(), access_token)
				.setEntity(new StringEntity(messageJson, Charset.forName("utf-8"))).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, BaseResult.class);
	}

	/**
	 * 消息发送
	 * 
	 * @param access_token
	 * @param message
	 * @return
	 */
	public static BaseResult messageCustomSend(String access_token, Message message) {
		String str = JsonUtil.getJsonFromObject(message);
		return messageCustomSend(access_token, str);
	}

}