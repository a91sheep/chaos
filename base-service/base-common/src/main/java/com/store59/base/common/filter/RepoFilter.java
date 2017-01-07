/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.base.common.filter;

import java.io.Serializable;
import java.util.List;

/**
 * Repo组合条件查询
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 15/11/30
 * @since 1.0
 */
public class RepoFilter implements Serializable{

    private Byte          noSale;       //是否非卖品
    private Byte          cateType;     //夜猫店or饮品店
    private List<Integer> rids;         //商品id集合

    public Byte getNoSale() {
        return noSale;
    }

    public void setNoSale(Byte noSale) {
        this.noSale = noSale;
    }

    public List<Integer> getRids() {
        return rids;
    }

    public void setRids(List<Integer> rids) {
        this.rids = rids;
    }

    public Byte getCateType() {
        return cateType;
    }

    public void setCateType(Byte cateType) {
        this.cateType = cateType;
    }
}
