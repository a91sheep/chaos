/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.data.mapper;

import com.store59.base.common.model.RepoFlagSite;

import java.util.List;

/**
 * RepoFlagSite 数据持久层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
public interface RepoFlagSiteMapper {

    List<RepoFlagSite> findBySiteId(int siteId);


}