/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.common.api;

import com.store59.base.common.model.RepoBarcode;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * RepoBarcode RPC服务接口层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/10
 * @since 1.0
 */
public interface RepoBarcodeApi {

    /**
     * 根据条形码查询记录
     *
     * @param barcode 条形码
     * @return
     */
    Result<RepoBarcode> getByBarcode(String barcode);

    /**
     * 添加条形码记录
     *
     * @param repoBarcode 条形码对象
     * @return
     */
    Result<RepoBarcode> insert(RepoBarcode repoBarcode);
    /**
     * 根据rids查询条形码列表
     *
     * @param rids 商品id集合
     * @return
     */
    Result<List<RepoBarcode>> findByRids(List<Integer> rids);
}
