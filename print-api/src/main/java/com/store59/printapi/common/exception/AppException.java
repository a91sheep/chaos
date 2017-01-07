package com.store59.printapi.common.exception;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/4/8
 * @since 1.0
 */
public class AppException extends RuntimeException {
    private int     status;
    private String  msg;
    private boolean isApp;

    public AppException() {
        this(-1);
    }

    public AppException(int status) {
        this(status, (String) null, false);
    }

    public AppException(int status, String msg, boolean isApp) {
        this.status = status;
        this.msg = msg;
        this.isApp = isApp;
    }

    public AppException(int status, String msg) {
        this.status = status;
        this.msg = msg;
        this.isApp = true;
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(int status, String msg, boolean isApp, Throwable cause) {
        super(cause);
        this.status = status;
        this.msg = msg;
        this.isApp = isApp;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return this.msg != null ? this.msg : this.getMessage();
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isApp() {
        return isApp;
    }

    public void setIsApp(boolean isApp) {
        this.isApp = isApp;
    }
}
