/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service;

import com.store59.base.common.model.RepoFlagSite;
import com.store59.base.data.dao.RepoFlagSiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RepoFlagSite 服务层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
@Service
public class RepoFlagSiteService {

    @Autowired
    private RepoFlagSiteDao repoFlagSiteDao;


    public List<RepoFlagSite> findBySiteId(int siteId) {
        return repoFlagSiteDao.findBySiteId(siteId);
    }

}