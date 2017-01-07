package com.store59.printapi.common.enums;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/26
 * @since 1.0
 */
public enum ResultStatus {

    SUCCESS(0, "成功"),

    NORMAL_ERROR(1, "正常错误"),

    TOKEN_ERROR(2, "token无效"),

    PARAM_ERROR(3, "参数错误"),

    SITE_NOT_EXIST(4, "站点不存在"),

    UNKNOWN_ERROR(-1, "未知错误");

    private int	type;

    private String	desc;

    ResultStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * @return 返回变量type的值
     */
    public int getType() {
        return type;
    }

    /**
     * @return 返回变量desc的值
     */
    public String getDesc() {
        return desc;
    }

    public static ResultStatus getStatusType(int type) {
        for (ResultStatus statusType : values()) {
            if (statusType.getType()==(type)) {
                return statusType;
            }
        }
        return UNKNOWN_ERROR;
    }

}