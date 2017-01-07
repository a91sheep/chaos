package com.store59.printapi.common.utils;

import com.store59.printapi.common.constant.CommonConstant;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
public class UrlUtil {
    private final static String LOGIN_PATH = "/user/login";
    private final static String REDIRECT_KEY = "smartRedirect";

    /**
     * login url取得
     *
     * @param accountBaseUrl
     * @param callbackBaseUrl
     * @return 取得的login url
     */
    public static String getLoginUrl(String accountBaseUrl, String callbackBaseUrl) {
        StringBuilder loginUrl = new StringBuilder().append(accountBaseUrl)
                .append(CommonConstant.PATH_ACCOUNT_CENTER_SIGNIN)
                .append("?" + REDIRECT_KEY + "=")
                .append(callbackBaseUrl)
                .append(LOGIN_PATH);
        return loginUrl.toString();
    }

    /**
     * logout url取得
     *
     * @param accountBaseUrl
     * @param callbackBaseUrl
     * @return
     */
    public static String getLogoutUrl(String accountBaseUrl, String callbackBaseUrl) {
        StringBuilder logoutUrl = new StringBuilder().append(accountBaseUrl)
                .append(CommonConstant.PATH_ACCOUNT_CENTER_SIGNOUT)
                .append("?" + REDIRECT_KEY + "=")
                .append(callbackBaseUrl)
                .append(LOGIN_PATH);
        return logoutUrl.toString();
    }

    /**
     * login check url取得
     *
     * @param baseUrl
     * @param checkPath
     * @return 取得的 login check url
     */
    public static String getLoginCheckUrl(String baseUrl, String checkPath) {
        StringBuilder loginCheckUrl = new StringBuilder().append(baseUrl)
                .append(CommonConstant.PATH_ACCOUNT_CENTER_SIGNIN_CHECK);
        return loginCheckUrl.toString();
    }

    /**
     * Home Page url取得
     *
     * @param baseUrl
     * @param path
     * @return 取得的Home Page url
     */
    public static String getHomePageUrl(String baseUrl) {
        StringBuilder serviceUrl = new StringBuilder().append(baseUrl).append("/index.html");
        return serviceUrl.toString();
    }
    /**
     * wechat绑定
     * @param accountBaseUrl
     * @param callbackBaseUrl
     * @return 取得的login url
     */
    public static String getWechatLoginUrl(String accountBaseUrl, String callbackBaseUrl) {
        StringBuilder loginUrl = new StringBuilder().append(accountBaseUrl)
                .append(CommonConstant.PATH_ACCOUNT_CENTER_SIGNIN)
                .append("?" + REDIRECT_KEY + "=")
                .append(callbackBaseUrl);
        return loginUrl.toString();
    }
}