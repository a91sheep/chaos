/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.base.common.api;

import com.store59.base.common.model.CityItem;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * CityItem RPC服务接口层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 15/11/13
 * @since 1.0
 */
public interface CityItemApi {

    /**
     * 根据cityId，获取城市商品信息集合
     *
     * @param cityId 城市id
     * @return
     */
    Result<List<CityItem>> findByCityId(Integer cityId);
    /**
     * 根据cityId和rid，获取城市商品信息集合
     * 
     * @param cityId 城市id
     * @param rid 商品id
     * @return
     */
    Result<List<CityItem>> findByCityIdAndRid(Integer cityId,Integer rid);
}