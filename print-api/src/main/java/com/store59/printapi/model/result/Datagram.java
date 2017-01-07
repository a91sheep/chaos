package com.store59.printapi.model.result;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
public class Datagram<T> {
    private Integer code;        // 和账号中心之间信息交换专用
    private Integer status;      // 状态
    private String  msg;         // 信息
    private T       data;        // 数据实体
    private Boolean isApp;       // app http status不作处理

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getIsApp() {
        return isApp;
    }

    public void setIsApp(Boolean isApp) {
        this.isApp = isApp;
    }
}