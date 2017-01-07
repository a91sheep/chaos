package com.store59.printapi.model.result.app;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class ShopPrint {
    private Byte type;
    private String name;
    private Integer reduced;

    public Integer getReduced() {
        return reduced;
    }

    public void setReduced(Integer reduced) {
        this.reduced = reduced;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
