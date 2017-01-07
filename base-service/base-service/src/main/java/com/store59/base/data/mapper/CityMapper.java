package com.store59.base.data.mapper;

import java.util.List;

import com.store59.base.common.model.City;
import com.store59.base.common.model.CityStatisticsInfo;
import org.apache.ibatis.annotations.Param;

public interface CityMapper {
    int deleteByPrimaryKey(Integer cityId);

    int insert(City record);

    int insertSelective(City record);

    City selectByPrimaryKey(Integer cityId);

    int updateByPrimaryKeySelective(City record);

    int updateByPrimaryKey(City record);

    List<City> selectByProvinceId(Integer provinceId);

    List<City> findByName(String name);

    List<CityStatisticsInfo> statisticsCityDormentryCountList(@Param("status")Byte status);
}
