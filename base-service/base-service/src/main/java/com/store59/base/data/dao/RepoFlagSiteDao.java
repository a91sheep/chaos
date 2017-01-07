/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.data.dao;

import com.store59.base.common.model.RepoFlagSite;
import com.store59.base.data.mapper.RepoFlagSiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RepoFlagSite 数据访问层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
@Repository
public class RepoFlagSiteDao {

    @Autowired
    private RepoFlagSiteMapper masterRepoFlagSiteMapper;

    @Autowired
    private RepoFlagSiteMapper slaveRepoFlagSiteMapper;

    public List<RepoFlagSite> findBySiteId(int siteId) {
        return slaveRepoFlagSiteMapper.findBySiteId(siteId);
    }
}