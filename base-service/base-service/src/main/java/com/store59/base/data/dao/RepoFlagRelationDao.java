/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.data.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import com.store59.base.common.model.RepoFlagRelation;
import com.store59.base.data.mapper.RepoFlagRelationMapper;

import java.util.List;

/**
 * RepoFlagRelation 数据访问层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/18
 * @since 1.0
 */
@Repository
public class RepoFlagRelationDao {

    @Autowired
    private RepoFlagRelationMapper slaveRepoFlagRelationMapper;

    public List<RepoFlagRelation> findByObjectIdAndType(int objectId, int type) {
        return slaveRepoFlagRelationMapper.findByObjectIdAndType(objectId, type);
    }
}