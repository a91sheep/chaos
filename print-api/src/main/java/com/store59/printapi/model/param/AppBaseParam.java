package com.store59.printapi.model.param;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/25
 * @since 1.0
 */
public class AppBaseParam {

    private String sign;

    private Integer time;

    private String app_version;

    private Byte device_type;

    private String device_id;

    private String protocol_version;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public Byte getDevice_type() {
        return device_type;
    }

    public void setDevice_type(Byte device_type) {
        this.device_type = device_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getProtocol_version() {
        return protocol_version;
    }

    public void setProtocol_version(String protocol_version) {
        this.protocol_version = protocol_version;
    }
}
