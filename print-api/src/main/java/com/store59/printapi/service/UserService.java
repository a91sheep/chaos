package com.store59.printapi.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.MessageConstant;
import com.store59.printapi.common.exception.CommonException;
import com.store59.printapi.common.http.HttpClientRequest;
import com.store59.printapi.common.utils.RandomValidateCode;
import com.store59.printapi.common.utils.TypeConverter;
import com.store59.printapi.common.utils.UUIDUtil;
import com.store59.printapi.common.utils.UrlUtil;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.LoginInfo;
import com.store59.printapi.model.result.UserInfo;
import com.store59.user.common.api.UserApi;
import com.store59.user.common.model.User;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
@Service
public class UserService {

	private static Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private HttpClientRequest httpClientRequest;

	@Value("${account.center.internal}")
	private String accountInternalBaseUrl;
	@Value("${account.center.key}")
	private String accountKey;
	@Autowired
	private UserApi userApi;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Value("${cookie.max.age}")
	private int cookieMaxAge;

	public LoginInfo doLogin(String smartTicket) throws JsonParseException, JsonMappingException, IOException {
		LoginInfo loginInfo = new LoginInfo();
		// 进行signin check
		LinkedList<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("smartTicket", smartTicket));
		params.add(new BasicNameValuePair("token", accountKey));
		String ret = "";
		try {
			ret = httpClientRequest.conn(
					UrlUtil.getLoginCheckUrl(accountInternalBaseUrl, CommonConstant.PATH_ACCOUNT_CENTER_SIGNIN_CHECK),
					CommonConstant.HTTP_METHOD_POST, params);
		} catch (BaseException e) {
			throw new CommonException(e.getMsg());
		}
		Datagram<Map<String, Object>> datagramWork = JsonUtil.getObjectFromJson(ret,
				new TypeReference<Datagram<Map<String, Object>>>() {
				});

		// signin检查错误，返回相应错误信息（跳转至错误信息页面）
		if (datagramWork.getCode() != 0) {
			loginInfo.setErrMsg(MessageConstant.LOGIN_SIGNIN_INVALID);
			return loginInfo;
		}

		// uid取得
		Object uidWork = datagramWork.getData().get("id");
		long uid = TypeConverter.str2Long(String.valueOf(uidWork));
		log.info("登录用户的uid:" + uid);

		// 获取用户信息
		Result<User> userResult = userApi.getUser(uid);
		if (userResult == null || userResult.getStatus() != 0 || userResult.getData() == null) {
			loginInfo.setErrMsg(MessageConstant.USER_NORECORD);
			return loginInfo;
		}

		LoginIdInfo loginIdInfo = new LoginIdInfo();
		loginIdInfo.setUid(uid);

		User user = userResult.getData();

		UserInfo userInfo = new UserInfo();
		userInfo.setUid(user.getUid());
		userInfo.setUname(user.getUname());
		userInfo.setPhone(user.getPhone());

		loginInfo.setUserInfo(userInfo);
		loginInfo.setLoginIdInfo(loginIdInfo);

		return loginInfo;
	}

	/**
	 * 根据电话号码获取用户
	 * 
	 * @param phone
	 * @return
	 */
	public User getUserByPhone(String phone) {
		Result<User> userResult = userApi.getUserByPhone(phone);
		if (userResult == null && userResult.getStatus() != 0) {
			throw new BaseException(userResult.getStatus(), userResult.getMsg());
		}
		return userResult.getData();
	}

	/**
	 * 注册用户
	 * 
	 * @return
	 */
	public User saveUser(User user) {
		Result<User> userResult = userApi.createUser(user);
		if (userResult == null || userResult.getStatus() != 0 || userResult.getData() == null) {
			throw new BaseException(userResult.getStatus(), userResult.getMsg());
		}
		return userResult.getData();
	}

	/**
	 * 生成图形验证码
	 * 
	 * @param request
	 * @param response
	 */
	public void getGraphics(HttpServletRequest request, HttpServletResponse response) {
		String key = UUIDUtil.getNewValue();
		String randomStr = RandomValidateCode.RandomString(4);
		valueOpsCache.set(key, randomStr, 5*60, TimeUnit.SECONDS);
		RandomValidateCode.getRandcode(request, response, randomStr, key, 5*60);
	}
}
