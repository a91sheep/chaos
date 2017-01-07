/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.data.dao;

import com.store59.base.common.filter.RepoFlagFilter;
import com.store59.base.common.model.RepoFlag;
import com.store59.base.data.mapper.RepoFlagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RepoFlag 数据访问层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
@Repository
public class RepoFlagDao {

    @Autowired
    private RepoFlagMapper masterRepoFlagMapper;

    @Autowired
    private RepoFlagMapper slaveRepoFlagMapper;

    public List<RepoFlag> findByFlagIds(RepoFlagFilter dormRepoFlagFilter) {
        return slaveRepoFlagMapper.findByFlagIds(dormRepoFlagFilter);
    }
}