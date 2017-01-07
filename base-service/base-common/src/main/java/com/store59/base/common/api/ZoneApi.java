package com.store59.base.common.api;

import java.util.List;

import com.store59.base.common.model.Zone;
import com.store59.kylin.common.model.Result;

/**
 * 区接口
 * 
 * @author heqingpan
 *
 */
public interface ZoneApi {
    /**
     * 获取区信息
     * 
     * @param zoneId
     * @return
     */
    Result<Zone> getZone(Integer zoneId);

    /**
     * 获取区列表
     * 
     * @param cityId
     * @return
     */
    Result<List<Zone>> getZoneList(Integer cityId);

    /**
     * 查询区域信息
     *
     * @param cityId
     * @param isSite 是否关联查询学校
     * @return
     */
    Result<List<Zone>> findByCityId(Integer cityId, boolean isSite);
}
