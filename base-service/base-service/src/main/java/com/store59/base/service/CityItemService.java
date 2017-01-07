/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.base.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.store59.base.data.dao.CityItemDao;
import com.store59.base.common.model.CityItem;

import java.util.List;

/**
 * CityItem 服务层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 15/11/13
 * @since 1.0
 */
@Service
public class CityItemService {

    @Autowired
    private CityItemDao cityItemDao;

    public List<CityItem> findByCityId(Integer cityId) {
        return cityItemDao.findByCityId(cityId);
    }

    public List<CityItem> findByCityIdAndRid(Integer cityId, Integer rid) {
        return cityItemDao.findByCityIdAndRid(cityId, rid);
    }
}