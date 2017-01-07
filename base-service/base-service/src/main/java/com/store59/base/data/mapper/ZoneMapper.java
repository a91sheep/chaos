package com.store59.base.data.mapper;

import java.util.List;

import com.store59.base.common.model.Zone;

public interface ZoneMapper {
    int deleteByPrimaryKey(Integer zoneId);

    int insert(Zone record);

    int insertSelective(Zone record);

    Zone selectByPrimaryKey(Integer zoneId);

    int updateByPrimaryKeySelective(Zone record);

    int updateByPrimaryKey(Zone record);
    
    List<Zone> selectByCityId(Integer cityId);
}
