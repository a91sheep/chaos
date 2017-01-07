package com.store59.base.data.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.store59.base.common.model.City;
import com.store59.base.common.model.CityStatisticsInfo;
import com.store59.base.data.mapper.CityMapper;

@Repository
public class CityDao {
    @Autowired
    private CityMapper masterCityMapper;
    @Autowired
    private CityMapper slaveCityMapper;

    @Cacheable(value = "city", key = "'cityId_'+#cityId")
    public City getCity(Integer cityId) {
        return slaveCityMapper.selectByPrimaryKey(cityId);
    }
    
    @Cacheable(value = "city", key = "'provinceId_'+#provinceId")
    public List<City> getCityList(Integer provinceId) {
        return slaveCityMapper.selectByProvinceId(provinceId);
    }

    @Cacheable(value = "city", key = "'name_'+#name")
    public List<City> findByName(String name) {
        return slaveCityMapper.findByName(name);
    }

    @Cacheable(value = "city", key = "'status_'+#status")
    public List<CityStatisticsInfo> statisticsCityDormentryCountList(Byte status){
        return slaveCityMapper.statisticsCityDormentryCountList(status);
    }

}
