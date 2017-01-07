package com.store59.printapi.common.enums;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/24
 * @since 1.0
 */
public enum DormShopTimeStatusEnum {

    OFF(0, "失效"),

    ON(1, "生效");

    DormShopTimeStatusEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int    type;
    private String desc;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}