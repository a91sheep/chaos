package com.store59.base.common.api;

import com.store59.base.common.model.Site;
import com.store59.base.common.model.SiteView;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * 学校接口
 * 
 * @author heqingpan
 *
 */
public interface SiteApi {
    /**
     * 获取学校信息
     * 
     * @param siteId
     * @return
     */
    Result<Site> getSite(Integer siteId);

    /**
     * 根据区获取学校列表
     * 
     * @param zoneId
     * @return
     */
    Result<List<Site>> getSiteByZoneId(Integer zoneId);


    /**
     * 通过关键字，模糊检索学校信息
     *
     * @param name
     * @return
     */
    Result<List<Site>> searchSiteList(String name);

    /**
     * 定点学校位置，返回经过排序的学校列表
     *
     * @param longitude 经度
     * @param latitude 纬度
     * @param limit 数量
     * @return
     */
    Result<List<SiteView>> locateSiteList(double longitude, double latitude, int limit);



}
