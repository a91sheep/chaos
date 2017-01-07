/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.base.data.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.store59.base.common.model.CityItem;
import com.store59.base.data.mapper.CityItemMapper;

import java.util.List;

/**
 * CityItem 数据访问层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 15/11/13
 * @since 1.0
 */
@Repository
public class CityItemDao {

    @Autowired
    private CityItemMapper masterCityItemMapper;

    @Autowired
    private CityItemMapper slaveCityItemMapper;
    
    @Cacheable(value = "cityitem", key = "'cityId_'+#cityId")
    public List<CityItem> findByCityId(Integer cityId) {
        return slaveCityItemMapper.findByCityId(cityId);
    }

    @Cacheable(value = "cityitem", key = "'cityId_'+#cityId+'_rid_'+#rid")
    public List<CityItem> findByCityIdAndRid(Integer cityId, Integer rid) {
        return slaveCityItemMapper.findByCityIdAndRid(cityId, rid);
    }
}