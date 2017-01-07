package com.store59.base.common.api;

import com.store59.base.common.model.AreaJoin;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * Created by shibing on 15/9/10.
 */
public interface AreaJoinApi {

    /**
     * 根据学校id 查询省、市、区、学校的信息
     * 
     * @param siteId 学校id
     * @return (AreaJoin)result.getData()
     */
    Result<AreaJoin> getBySiteId(int siteId);


    /**
     * 根据siteIds 查询省、市、区、学校的信息集合
     *
     * @param siteIds
     * @return
     */
    Result<List<AreaJoin>> findBySiteIds(List<Integer> siteIds);
}
