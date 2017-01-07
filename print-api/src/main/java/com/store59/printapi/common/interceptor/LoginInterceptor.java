package com.store59.printapi.common.interceptor;

import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.*;
import com.store59.printapi.model.result.LoginIdInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Value("${account.center.baseurl}")
    private String accountBaseUrl;
    @Value("${print.callback.baseurl}")
    private String callbackBaseUrl;
    @Value("${cookie.max.age}")
    private int    cookieMaxAge;
    @Value("${cookie.refresh.age}")
    private int    cookieRefreshAge;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;

    /**
     * 在执行Controller里面的处理逻辑之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 调用父类处理
        boolean ret = super.preHandle(request, response, handler);
        if (!ret) {
            return false;
        }

        // Login Url取得
        String loginUrl = UrlUtil.getLoginUrl(accountBaseUrl, callbackBaseUrl);
        // Logout url取得
        String logoutUrl = UrlUtil.getLogoutUrl(accountBaseUrl, callbackBaseUrl);

        // logout标识
        boolean isLogout = false;
        // Ajax请求标识
        boolean isAjaxRequest = false;
        // 跳转地址设置为Login url
        String redirectUrl = loginUrl;
        if (StringUtil.contains(request.getRequestURI(), "/logout")) {
            // 如果是logout处理的情况，跳转地址设置为Logout url
            redirectUrl = logoutUrl;
            // logout标识设置为true
            isLogout = true;
        } else {
            // 如果是logout处理以外的情况（根据spring-mvc.xml的设置其他都为Ajax请求），Ajax请求标识设置为true
            isAjaxRequest = true;
        }

        // 取得Cookie的Map集合
        Map<String, Cookie> cookieMap = CookieUtil.ReadCookieMap(request);
        // Token Cookie和最后更新时间Cookie取得
        Cookie cookieToken = cookieMap.get(CommonConstant.KEY_COOKIE_NAME_TOKEN);
        Cookie cookieSecret = cookieMap.get(CommonConstant.KEY_COOKIE_NAME_SECRET);
        Cookie cookieTime = cookieMap.get(CommonConstant.KEY_COOKIE_NAME_LAST_MODIFY_TIME);
        // Token和最后更新时间的Cookie为null的情况，跳转至登录页面，提示用户进行登录
        if (cookieToken == null || cookieSecret == null || cookieTime == null) {
            return invalidAccessHandler(response, isAjaxRequest, redirectUrl);
        }

        // 取不到Token和最后更新时间信息的情况，跳转至登录页面，提示用户进行登录
        if (StringUtil.isBlank(cookieToken.getValue()) || StringUtil.isBlank(cookieSecret.getValue()) || StringUtil.isBlank(cookieTime.getValue())) {
            return invalidAccessHandler(response, isAjaxRequest, redirectUrl);
        }
        // 根据token信息取得用户登录ID信息，如果取不到就跳转到登录页面，提示用户进行登录
        // String loginIds = cache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + cookieToken.getValue());
        String loginIds = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + cookieToken.getValue());
        if (StringUtil.isBlank(loginIds)) {
            return invalidAccessHandler(response, isAjaxRequest, redirectUrl);
        }

        // 计算时间差（当前时间 - 最后更新时间）
        long seconds = DateUtil.getDiff(cookieTime.getValue());
        // 时间差在cookie.max.age以上的情况跳转到登录页面，提示用户进行登录
        if (seconds >= cookieMaxAge) {
            return invalidAccessHandler(response, isAjaxRequest, redirectUrl, CommonConstant.STATUS_SESSION_TIMEOUT_INVALID,
                    CommonConstant.MSG_SESSION_TIMEOUT_INVALID);
        }
        // Login id信息取得
        LoginIdInfo loginIdInfo = JsonUtil.getObjectFromJson(loginIds, LoginIdInfo.class);
        // 时间差在cookie.refresh.age以上的情况，更新Cookie和Redis的有效时间
        if (!isLogout && seconds >= cookieRefreshAge) {
            // 更新Cookie的有效时间
            CookieUtil.updateCookies(response, cookieMaxAge, cookieToken, cookieSecret);
            CookieUtil.updateCookie(response, cookieMaxAge, TypeConverter.long2String(System.currentTimeMillis()), cookieTime);
            // 更新Redis的有效时间
            valueOpsCache.getOperations().expire(CommonConstant.KEY_REDIS_TOKEN_PREFIX + cookieToken.getValue(), cookieMaxAge + 10, TimeUnit.SECONDS);
            valueOpsCache.getOperations().expire(CommonConstant.KEY_REDIS_USER_PREFIX + loginIdInfo.getUid(), cookieMaxAge + 10, TimeUnit.SECONDS);
        }

        return true;
    }

    /**
     * 非法请求时的处理。
     *
     * @param response
     * @param isAjaxRequest
     * @param redirectUrl
     * @return 固定值false
     * @throws IOException
     */
    private boolean invalidAccessHandler(HttpServletResponse response, boolean isAjaxRequest, String redirectUrl, int invalidStatus,
                                         String invalidMsg) throws IOException {
        if (isAjaxRequest) {
            throw new BaseException(invalidStatus, invalidMsg);
        } else {
            response.sendRedirect(redirectUrl);
        }
        return false;
    }

    /**
     * token无效时的非法请求处理。
     *
     * @param response
     * @param isAjaxRequest
     * @param redirectUrl
     * @return
     * @throws IOException
     */
    private boolean invalidAccessHandler(HttpServletResponse response, boolean isAjaxRequest, String redirectUrl) throws IOException {
        return invalidAccessHandler(response, isAjaxRequest, redirectUrl, CommonConstant.STATUS_TOKEN_INVALID, CommonConstant.MSG_TOKEN_INVALID);
    }

    /**
     * 在执行Controller里面的逻辑后返回视图之前执行（Controller里产生异常的话就不执行）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        // TODO:自定义处理

    }

    /**
     * 在Controller返回视图后执行（Controller里即使产生异常也执行）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);

        // TODO:自定义处理

    }

    /**
     * 会在Controller方法异步执行时开始执行
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);

        // TODO:自定义处理

    }
}
