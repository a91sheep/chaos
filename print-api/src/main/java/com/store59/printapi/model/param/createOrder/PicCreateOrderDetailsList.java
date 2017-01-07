package com.store59.printapi.model.param.createOrder;

import java.util.List;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月12日 下午2:10:23 
 * 类说明
 */
public class PicCreateOrderDetailsList {
    private List<AppCreateOrderDetailParam> items;

    public List<AppCreateOrderDetailParam> getItems() {
        return items;
    }

    public void setItems(List<AppCreateOrderDetailParam> items) {
        this.items = items;
    }
}
