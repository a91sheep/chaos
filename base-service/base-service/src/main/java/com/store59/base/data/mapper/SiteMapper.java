package com.store59.base.data.mapper;

import java.util.List;
import java.util.Map;

import com.store59.base.common.model.Site;
import com.store59.base.common.model.SiteView;

public interface SiteMapper {
    int deleteByPrimaryKey(Integer siteId);

    int insert(Site record);

    int insertSelective(Site record);

    Site selectByPrimaryKey(Integer siteId);

    int updateByPrimaryKeySelective(Site record);

    int updateByPrimaryKey(Site record);
    
    List<Site> selectByZoneId(Integer zoneId);

    List<Site> searchSiteList(String name);

    List<SiteView> selectByRegion(Map map);

    List<Site> findByZoneIds(List<Integer> zoneIds);
}
