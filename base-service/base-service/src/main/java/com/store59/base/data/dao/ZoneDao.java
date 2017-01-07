package com.store59.base.data.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.store59.base.common.model.Zone;
import com.store59.base.data.mapper.ZoneMapper;

@Repository
public class ZoneDao {
    @Autowired
    private ZoneMapper masterZoneMapper;
    @Autowired
    private ZoneMapper slaveZoneMapper;

    @Cacheable(value = "zone", key = "'zoneId_'+#zoneId")
    public Zone getZone(Integer zoneId) {
        return slaveZoneMapper.selectByPrimaryKey(zoneId);
    }

    @Cacheable(value = "zone", key = "'cityId_'+#cityId")
    public List<Zone> getZoneList(Integer cityId) {
        return slaveZoneMapper.selectByCityId(cityId);
    }

}
