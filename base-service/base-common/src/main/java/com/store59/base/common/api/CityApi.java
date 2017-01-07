package com.store59.base.common.api;

import java.util.List;

import com.store59.base.common.model.City;
import com.store59.base.common.model.CityStatisticsInfo;
import com.store59.kylin.common.model.Result;

/**
 * 城市接口
 * 
 * @author heqingpan
 *
 */
public interface CityApi {
    /**
     * 获取城市信息
     * 
     * @param cityId
     * @return
     */
    Result<City> getCity(Integer cityId);

    /**
     * 获取城市列表
     * 
     * @param provinceId
     * @return
     */
    Result<List<City>> getCityList(Integer provinceId);

    /**
     * 根据城市名，获取城市信息
     *
     * @param name 城市名
     * @return
     */
    Result<List<City>> findByName(String name);

    /**
     * 统计各个城市中指定状态的店铺数量
     * @param status 店铺状态0表示关店,1表示开店,null表示所有店铺
     * @return
     */
    Result<List<CityStatisticsInfo>> statisticsCityDormentryCountList(Byte status);
}
