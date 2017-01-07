package com.store59.printapi.model.result.order;

import java.util.List;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 2016/01/22
 * @since 1.0
 */
public class MyOrderResultList {

    private Boolean flag;

    private List<MyOrderResult> myOrderResultList;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public List<MyOrderResult> getMyOrderResultList() {
        return myOrderResultList;
    }

    public void setMyOrderResultList(List<MyOrderResult> myOrderResultList) {
        this.myOrderResultList = myOrderResultList;
    }
}
