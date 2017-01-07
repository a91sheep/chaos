package com.store59.printapi.controller.wechat;

import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.store59.ad.common.api.UserWeChatService;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.CookieUtil;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.utils.UrlUtil;
import com.store59.printapi.model.param.wechat.WechatBindingParams;
import com.store59.printapi.model.result.LoginInfo;
import com.store59.printapi.service.wechat.BindingService2;
import com.store59.printapi.service.wechat.SendMsgService;

/**
 * 微信接入用户中心
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月13日 下午2:46:12 
 * 类说明
 */
@Controller
@RequestMapping("wechat")
public class WechatLoginController {

	@Autowired
	BindingService2 bindingService;

	private Logger logger = Logger.getLogger(WechatLoginController.class);

	@Value("${account.center.baseurl}")
	private String accountBaseUrl;

	@Value("${print.wechat.callback.baseurl}")
	private String callbackWechatUrl;

	@Value("${result.wechat.baseurl}")
	private String resultWechatUrl;
	
    @Value("${cookie.max.age}")
    private int cookieMaxAge;
    
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    
	@Autowired
	UserWeChatService userWeCahtService;
	
	@Autowired
	SendMsgService sendMsgService;


	/**
	 * 微信登录接口
	 * 
	 * @param request
	 * @param response
	 * @param loginParams
	 * @param result
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, WechatBindingParams loginParams) {
		try {
			// 参数检查有误
			logger.info("微信登录验证参数："+JsonUtil.getJsonFromObject(loginParams));
			if(StringUtil.isBlank(loginParams.getOpenid())){
				return new ModelAndView("redirect:" + resultWechatUrl+"bind-error.html");
			}
			if (StringUtil.isBlank(loginParams.getSmartTicket())) {
				return new ModelAndView("redirect:" + UrlUtil.getWechatLoginUrl(accountBaseUrl, URLEncoder.encode(callbackWechatUrl+"?openid="+loginParams.getOpenid())));
			}

			LoginInfo loginInfo = bindingService.doLogin(loginParams.getSmartTicket(),loginParams.getOpenid());

			if (StringUtil.isBlank(loginInfo.getErrMsg())) {
				//登录成功
				return new ModelAndView("redirect:" +resultWechatUrl+"bind.html");
			} else {
				// 登录的账号不是经销商账号
				return new ModelAndView("redirect:" + resultWechatUrl +"bind-error.html");
			}
		} catch (Exception e) {
			// 会跳转至error页面进行错误信息的表示处理
			logger.error("登录异常,{}"+e.getMessage());
			// 登录异常
			CookieUtil.addCookie(response, null, CommonConstant.KEY_COOKIE_NAME_TYPE,
					CommonConstant.VALUE_COOKIE_LOGINEXCEPTION, cookieMaxAge);
			return new ModelAndView("redirect:" + resultWechatUrl + "bind-error.html");
		}
	}
}
