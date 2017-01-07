/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.common.api;

import com.store59.base.common.filter.RepoFlagFilter;
import com.store59.base.common.model.RepoFlag;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * RepoFlag RPC服务接口层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
public interface RepoFlagApi {

    /**
     * 根据filter查询repoFlag信息
     *
     * @param repoFlagFilter flagId集合
     * @return
     */
    Result<List<RepoFlag>> findByFlagIds(RepoFlagFilter repoFlagFilter);
}