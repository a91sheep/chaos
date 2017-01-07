/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.store59.base.data.dao.RepoFlagRelationDao;
import com.store59.base.common.model.RepoFlagRelation;

import java.util.List;

/**
 * RepoFlagRelation 服务层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/18
 * @since 1.0
 */
@Service
public class RepoFlagRelationService {

    @Autowired
    private RepoFlagRelationDao repoFlagRelationDao;

    public List<RepoFlagRelation> findByObjectIdAndType(int objectId, int type) {
        return repoFlagRelationDao.findByObjectIdAndType(objectId, type);
    }
}