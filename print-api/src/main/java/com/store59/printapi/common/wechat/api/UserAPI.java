package com.store59.printapi.common.wechat.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import com.store59.printapi.common.wechat.LocalHttpClient;
import com.store59.printapi.model.param.wechat.user.WeChatUser;

public class UserAPI extends BaseAPI{

	/**
	 * 获取用户基本信息
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static WeChatUser userInfo(String access_token,String openid){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setUri(BASE_URI+"/cgi-bin/user/info")
				.addParameter(getATPN(),access_token)
				.addParameter("openid",openid)
				.addParameter("lang","zh_CN")
				.build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,WeChatUser.class);
	}

	
}
