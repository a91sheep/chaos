package com.store59.printapi.common.enums;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/23
 * @since 1.0
 */
public enum DormShopStatusEnum {
    /* 休息中 */
    CLOSED(0, "休息中"),

    /* 营业中 */
    OPENED(1, "营业中"),

    /* 自动 */
    AUTO(2, "自动"),

    /* 店铺关闭,简单理解就是倒闭状态 */
    CANCEL(9, "店铺关闭,不营业");

    private Integer type;

    private String desc;

    DormShopStatusEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}