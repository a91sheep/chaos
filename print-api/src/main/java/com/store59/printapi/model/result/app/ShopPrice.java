package com.store59.printapi.model.result.app;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class ShopPrice {
    private String name;
    private Byte type;
    private Double unit_price;
    private Integer page_side;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public Integer getPage_side() {
        return page_side;
    }

    public void setPage_side(Integer page_side) {
        this.page_side = page_side;
    }
}
