package com.store59.base.common.api;

import java.util.List;

import com.store59.base.common.filter.DormentryFloorFilter;
import com.store59.base.common.model.Dormentry;
import com.store59.kylin.common.model.Result;

/**
 * 楼栋接口
 * 
 * @author heqingpan
 *
 */
public interface DormentryApi {
    /**
     * 获取学校楼栋列表
     * 
     * @param siteId
     * @return
     */
    Result<List<Dormentry>> getDormentryListBySiteId(int siteId);

    /**
     * 获取楼栋信息
     * 
     * @param dormentryId
     * @return
     */
    Result<Dormentry> getDormentry(Integer dormentryId);

    /**
     * 获取楼层列表
     * 
     * @param filter
     * @return
     */
    Result<List<Dormentry>> getFloorListByFilter(DormentryFloorFilter filter);
}
