/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service;

import com.store59.base.common.filter.RepoFlagFilter;
import com.store59.base.common.model.RepoFlag;
import com.store59.base.data.dao.RepoFlagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RepoFlag 服务层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
@Service
public class RepoFlagService {

    @Autowired
    private RepoFlagDao repoFlagDao;

    public List<RepoFlag> findByFlagIds(RepoFlagFilter dormRepoFlagFilter) {
        return repoFlagDao.findByFlagIds(dormRepoFlagFilter);
    }
}