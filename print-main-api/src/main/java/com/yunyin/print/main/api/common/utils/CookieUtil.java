package com.yunyin.print.main.api.common.utils;


import com.store59.kylin.utils.StringUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/19
 * @since 1.0
 */
public class CookieUtil {

    /**
     * 设置cookie
     * @param response
     * @param name  cookie名字
     * @param value cookie值
     * @param maxAge cookie生命周期  以秒为单位
     */
    public static void addCookie (HttpServletResponse response, String domain, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);

        if (!StringUtil.isEmpty(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setPath("/");

        if (maxAge >= 0) {
            cookie.setMaxAge(maxAge);
        }

        response.addCookie(cookie);
    }

    /**
     * 更新多个cookie（有效时间）
     *
     * @param response
     * @param maxAge
     * @param cookies
     */
    public static void updateCookies(HttpServletResponse response, int maxAge, Cookie ... cookies) {
        for (Cookie cookie : cookies) {
            cookie.setPath("/");
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        }
    }

    /**
     * 更新单个cookie（有效时间和值）
     *
     * @param response
     * @param maxAge
     * @param cookies
     */
    public static void updateCookie(HttpServletResponse response, int maxAge, String value, Cookie cookie) {
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setValue(value);
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param cookies
     */
    public static void delCookies(HttpServletResponse response, Cookie ... cookies) {
        updateCookies(response, 0, cookies);
    }

    /**
     * 根据名字获取cookie
     * @param request
     * @param name cookie名字
     * @return Cookie对象
     */
    public static Cookie getCookieByName (HttpServletRequest request,String name) {
        Cookie[] cookies = request.getCookies();
        if (null!=cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    /**
     * 将cookie内容封装到Map里面
     * @param request
     * @return 存放Cookie的name-value对Map
     */
    public static Map<String, Cookie> ReadCookieMap (HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null!=cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}