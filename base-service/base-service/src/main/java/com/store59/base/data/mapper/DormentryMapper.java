package com.store59.base.data.mapper;

import java.util.List;

import com.store59.base.common.filter.DormentryFloorFilter;
import com.store59.base.common.model.Dormentry;

public interface DormentryMapper {
    int deleteByPrimaryKey(Integer dormentryId);

    int insert(Dormentry record);

    int insertSelective(Dormentry record);

    Dormentry selectByPrimaryKey(Integer dormentryId);

    int updateByPrimaryKeySelective(Dormentry record);

    int updateByPrimaryKey(Dormentry record);

    List<Dormentry> selectByDormID(int dormId);

    List<Dormentry> selectBySiteId(int siteId);
    
    List<Dormentry> getFloorListByFilter(DormentryFloorFilter filter);
}
