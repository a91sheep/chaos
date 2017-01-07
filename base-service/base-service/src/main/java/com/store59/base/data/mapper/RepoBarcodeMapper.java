/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.data.mapper;

import com.store59.base.common.model.RepoBarcode;

import java.util.List;

/**
 * RepoBarcode 数据持久层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/10
 * @since 1.0
 */
public interface RepoBarcodeMapper {

    int insert(RepoBarcode repoBarcode);

    RepoBarcode getByBarcode(String barcode);

    List<RepoBarcode> findByRids(List<Integer> rids);
}