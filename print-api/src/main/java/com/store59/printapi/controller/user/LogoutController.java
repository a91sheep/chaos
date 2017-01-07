package com.store59.printapi.controller.user;

import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.CookieUtil;
import com.store59.printapi.common.utils.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
@Controller
@RequestMapping("/user/*")
public class LogoutController {
    //    @Autowired
//    private ICache cache;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;

    @Value("${cookie.max.age}")
    private int    cookieMaxAge;
    @Value("${account.center.baseurl}")
    private String accountBaseUrl;
    @Value("${print.callback.baseurl}")
    private String callbackBaseUrl;
    @Value("${homepage.baseurl}")
    private String homepageBaseUrl;
    private Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 取得Cookie的Map集合
            Map<String, Cookie> cookieMap = CookieUtil.ReadCookieMap(request);
            // Token Cookie、私钥以及最后更新时间Cookie取得
            Cookie cookieToken = cookieMap.get(CommonConstant.KEY_COOKIE_NAME_TOKEN);
            Cookie cookeiSecretKey = cookieMap.get(CommonConstant.KEY_COOKIE_NAME_SECRET);
            Cookie cookieTime = cookieMap.get(CommonConstant.KEY_COOKIE_NAME_LAST_MODIFY_TIME);
            Cookie type = cookieMap.get(CommonConstant.KEY_COOKIE_NAME_TYPE);
            // 删除用户信息（只删除token信息，用户信息和楼主信息因为同一账号多人使用时都是在使用的所以不能删除）
            valueOpsCache.getOperations().delete(CommonConstant.KEY_REDIS_TOKEN_PREFIX + cookieToken.getValue());
            CookieUtil.delCookies(response, cookieToken, cookeiSecretKey, cookieTime);
            if (type != null) {
                CookieUtil.delCookies(response, type);
            }

            return new ModelAndView("redirect:" + UrlUtil.getLogoutUrl(accountBaseUrl, callbackBaseUrl));
        } catch (Exception e) {
            // 会跳转至error页面进行错误信息的表示处理
            logger.error("登出异常,{}", e.getMessage());
            CookieUtil.addCookie(response, null, CommonConstant.KEY_COOKIE_NAME_TYPE, CommonConstant.VALUE_COOKIE_LOGOUTEXCEPTION, cookieMaxAge);
            return new ModelAndView("redirect:" + homepageBaseUrl + "/loginerror.html");
        }
    }
}