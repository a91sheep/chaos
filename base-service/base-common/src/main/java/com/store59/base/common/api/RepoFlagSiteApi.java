/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.common.api;

import com.store59.base.common.model.RepoFlagSite;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * RepoFlagSite RPC服务接口层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
public interface RepoFlagSiteApi {

    /**
     * 根据siteId查询列表
     *
     * @param siteId 学校id
     * @return
     */
    Result<List<RepoFlagSite>> findBySiteId(int siteId);

}