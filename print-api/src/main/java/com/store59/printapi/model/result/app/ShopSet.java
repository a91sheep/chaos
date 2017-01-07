package com.store59.printapi.model.result.app;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class ShopSet {
    private List<ShopPrice> print_types;
    private List<ShopPrint> reduced_types;

    public List<ShopPrice> getPrint_types() {
        return print_types;
    }

    public void setPrint_types(List<ShopPrice> print_types) {
        this.print_types = print_types;
    }

    public List<ShopPrint> getReduced_types() {
        return reduced_types;
    }

    public void setReduced_types(List<ShopPrint> reduced_types) {
        this.reduced_types = reduced_types;
    }
}
