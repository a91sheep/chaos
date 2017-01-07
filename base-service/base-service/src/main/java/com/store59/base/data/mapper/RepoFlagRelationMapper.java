/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.data.mapper;

import com.store59.base.common.model.RepoFlagRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RepoFlagRelation 数据持久层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/18
 * @since 1.0
 */
public interface RepoFlagRelationMapper {

    List<RepoFlagRelation> findByObjectIdAndType(@Param("objectId")int objectId, @Param("type")int type);
}