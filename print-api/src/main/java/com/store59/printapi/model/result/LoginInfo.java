package com.store59.printapi.model.result;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
public class LoginInfo {
    private UserInfo userInfo;
    private LoginIdInfo loginIdInfo;
    private String errMsg;

    public LoginIdInfo getLoginIdInfo() {
        return loginIdInfo;
    }

    public void setLoginIdInfo(LoginIdInfo loginIdInfo) {
        this.loginIdInfo = loginIdInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
