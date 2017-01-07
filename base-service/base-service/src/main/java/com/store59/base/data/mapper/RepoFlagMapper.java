/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.data.mapper;

import com.store59.base.common.filter.RepoFlagFilter;
import com.store59.base.common.model.RepoFlag;

import java.util.List;

/**
 * RepoFlag 数据持久层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
public interface RepoFlagMapper {

    List<RepoFlag> findByFlagIds(RepoFlagFilter dormRepoFlagFilter);
}