/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.common.api;

import com.store59.base.common.model.RepoFlagRelation;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * RepoFlagRelation RPC服务接口层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/18
 * @since 1.0
 */
public interface RepoFlagRelationApi {

    /**
     * 根据objectId、type查询记录集合
     *
     * @param objectId objectId
     * @param type type
     * @return
     */
    Result<List<RepoFlagRelation>> findByObjectIdAndType(int objectId, int type);

}