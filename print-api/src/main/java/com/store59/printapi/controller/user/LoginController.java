package com.store59.printapi.controller.user;

import com.store59.base.common.api.SmsAuthApi;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.CommonException;
import com.store59.printapi.common.utils.CookieUtil;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.utils.UUIDUtil;
import com.store59.printapi.common.utils.UrlUtil;
import com.store59.printapi.model.param.LoginParams;
import com.store59.printapi.model.param.PhoneParam;
import com.store59.printapi.model.param.SmsPhoneParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.LoginInfo;
import com.store59.printapi.model.result.UserInfo;
import com.store59.printapi.service.UserService;
import com.store59.user.common.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
@Controller
@RequestMapping("/user/*")
public class LoginController {

	@Autowired
	private UserService userService;

	@Value("${cookie.max.age}")
	private int cookieMaxAge;

	@Value("${account.center.baseurl}")
	private String accountBaseUrl;

	@Value("${print.callback.baseurl}")
	private String callbackBaseUrl;

	@Value("${homepage.baseurl}")
	private String homepageBaseUrl;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Autowired
	private SmsAuthApi smsAuthApi;

	private Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, @Valid LoginParams loginParams,
			BindingResult result) {
		try {
			// 参数检查有误
			if (StringUtil.isBlank(loginParams.getSmartTicket())) {
				return new ModelAndView("redirect:" + UrlUtil.getWechatLoginUrl(accountBaseUrl, URLEncoder.encode(callbackBaseUrl+"/user/login?type="+loginParams.getType())));
			}

			LoginInfo loginInfo = userService.doLogin(loginParams.getSmartTicket());

			if (StringUtil.isBlank(loginInfo.getErrMsg())) {
				this.cacheUserInfo(response, loginInfo);
				if(loginParams.getType()==0){
					return new ModelAndView("redirect:" + homepageBaseUrl+"/#picture");
				}else{
					return new ModelAndView("redirect:" + UrlUtil.getHomePageUrl(homepageBaseUrl));
				}
			} else {
				// 登录的账号不是经销商账号
				CookieUtil.addCookie(response, null, CommonConstant.KEY_COOKIE_NAME_TYPE,
						CommonConstant.VALUE_COOKIE_NOTINVALID, cookieMaxAge);
				return new ModelAndView("redirect:" + homepageBaseUrl + "/loginerror.html");
			}
		} catch (Exception e) {
			// 会跳转至error页面进行错误信息的表示处理
			logger.error("登录异常,{}", e.getMessage());
			// 登录异常
			CookieUtil.addCookie(response, null, CommonConstant.KEY_COOKIE_NAME_TYPE,
					CommonConstant.VALUE_COOKIE_LOGINEXCEPTION, cookieMaxAge);
			return new ModelAndView("redirect:" + homepageBaseUrl + "/loginerror.html");
		}
	}

	/**
	 * 缓存用户信息
	 */
	private void cacheUserInfo(HttpServletResponse response, LoginInfo loginInfo) {
		// 生成新的UUID用于TokenId
		String tokenId = UUIDUtil.getNewValue();
		// 生成新的UUID用于secretKey
		String secretKey = UUIDUtil.getNewValue();
		// 当前系统时间取得
		String currentTime = String.valueOf(System.currentTimeMillis());
		// cookie处理（新建cookie）
		CookieUtil.addCookie(response, null, CommonConstant.KEY_COOKIE_NAME_TOKEN, tokenId, cookieMaxAge);
		CookieUtil.addCookie(response, null, CommonConstant.KEY_COOKIE_NAME_SECRET, secretKey, cookieMaxAge);
		CookieUtil.addCookie(response, null, CommonConstant.KEY_COOKIE_NAME_LAST_MODIFY_TIME, currentTime,
				cookieMaxAge);
		// 保存用户信息至redis
		loginInfo.getLoginIdInfo().setSecretKey(secretKey);

		valueOpsCache.set(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId,
				JsonUtil.getJsonFromObject(loginInfo.getLoginIdInfo()), cookieMaxAge + 10, TimeUnit.SECONDS);
		valueOpsCache.set(CommonConstant.KEY_REDIS_USER_PREFIX + loginInfo.getLoginIdInfo().getUid(),
				JsonUtil.getJsonFromObject(loginInfo.getUserInfo()), cookieMaxAge + 10, TimeUnit.SECONDS);

	}

	/**
	 * 发送手机验证码
	 *
	 * @param param
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/sendSms", method = RequestMethod.GET)
	@ResponseBody
	public Object sendSms(@Valid PhoneParam param, BindingResult result) {
		Datagram<String> datagram = new Datagram<>();
		if (result.hasErrors()) {
			datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
			datagram.setMsg(result.getAllErrors().get(0).getDefaultMessage());
			datagram.setIsApp(false);
			return datagram;
		}
		Result<String> code = smsAuthApi.sendAuthCode("phone_bind", param.getPhone(), null);
		if (code == null || code.getStatus() != 0 || code.getData() == null) {
			throw new CommonException("短信发送失败");
		}
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(false);
		return datagram;
	}

	/**
	 * 验证码校验
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/checkSms", method = RequestMethod.POST)
	@ResponseBody
	public Object sendSms(HttpServletResponse response, @Valid SmsPhoneParam param, BindingResult result) {
		Datagram<Integer> datagram = new Datagram<>();
		if (result.hasErrors()) {
			datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
			datagram.setMsg(result.getAllErrors().get(0).getDefaultMessage());
			datagram.setIsApp(false);
			return datagram;
		}

		Result<Boolean> codeAuth = smsAuthApi.checkAuthCode("phone_bind", param.getPhone(), param.getCode());
		if (codeAuth == null || codeAuth.getStatus() != 0 || codeAuth.getData() == null || !codeAuth.getData()) {
			throw new CommonException("短信验证失败");
		}

		User user = userService.getUserByPhone(param.getPhone());
		LoginInfo loginInfo = new LoginInfo();
		LoginIdInfo loginIdInfo = new LoginIdInfo();

		// 强制登录
		if (user != null) {
			loginIdInfo.setUid(user.getUid());

			UserInfo userInfo = new UserInfo();
			userInfo.setUid(user.getUid());
			userInfo.setUname(user.getUname());
			userInfo.setPhone(user.getPhone());

			loginInfo.setUserInfo(userInfo);
			loginInfo.setLoginIdInfo(loginIdInfo);
			this.cacheUserInfo(response, loginInfo);
			datagram.setData(CommonConstant.LOGIN_SMS_TYPE_AUTOLOGIN);
		} else {
			// 59_user表纪录信息,然后强制登录
			user = new User();
			user.setPhone(param.getPhone());
			user.setNickname(param.getPhone());
			user.setUname(param.getPhone());
			user = userService.saveUser(user);

			loginIdInfo.setUid(user.getUid());

			UserInfo userInfo = new UserInfo();
			userInfo.setUid(user.getUid());
			userInfo.setUname(user.getUname());
			userInfo.setPhone(user.getPhone());

			loginInfo.setUserInfo(userInfo);
			loginInfo.setLoginIdInfo(loginIdInfo);
			this.cacheUserInfo(response, loginInfo);
			datagram.setData(CommonConstant.LOGIN_SMS_TYPE_REGISTER);
		}

		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(false);
		return datagram;
	}

	@RequestMapping("graphics")
	public void getGraphics(HttpServletRequest request, HttpServletResponse response) {
		userService.getGraphics(request, response);
	}

	@RequestMapping("sendSmsGraphic")
	@ResponseBody
	public Object graphicValidate(@RequestParam(required = true) String codeGraphic,
			@RequestParam(required = true) String phone, @CookieValue(required = true) String RANDOMVALIDATECODEKEY) {
		Datagram<String> datagram = new Datagram<>();
		if (valueOpsCache.get(RANDOMVALIDATECODEKEY) == null
				|| !valueOpsCache.get(RANDOMVALIDATECODEKEY).toLowerCase().equals(codeGraphic.toLowerCase())) {
			datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
			datagram.setMsg(CommonConstant.MSG_SIGN_INVALID);
			datagram.setIsApp(false);
			return datagram;
		}
		Result<String> code = smsAuthApi.sendAuthCode("phone_bind", phone, null);
		if (code == null || code.getStatus() != 0 || code.getData() == null) {
			throw new CommonException("短信发送失败");
		}
		valueOpsCache.getOperations().delete(RANDOMVALIDATECODEKEY);
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(false);
		return datagram;
	}
	@RequestMapping("test")
	@ResponseBody
	public String test(@RequestParam(required=false)String shopId,@RequestParam(required=false)String key){
		if(shopId!=null)valueOpsCache.getOperations().delete(CommonConstant.KEY_REDIS_SHOP+shopId);
		if(key!=null)valueOpsCache.getOperations().delete(key);
		return "ok";
	}

}