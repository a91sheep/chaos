/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.base.data.mapper;

import com.store59.base.common.model.CityItem;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * CityItem 数据持久层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 15/11/13
 * @since 1.0
 */
public interface CityItemMapper {

    List<CityItem> findByCityId(Integer cityId);

    List<CityItem> findByCityIdAndRid(@Param("cityId") Integer cityId,@Param("rid") Integer rid);
}