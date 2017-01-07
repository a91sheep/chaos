package com.store59.base.data.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import com.store59.base.common.model.Site;
import com.store59.base.common.model.SiteView;
import com.store59.base.data.mapper.SiteMapper;
import com.store59.kylin.redis.RedisExpireProperties;

@Repository
public class SiteDao {
    @Autowired
    private SiteMapper masterSiteMapper;
    @Autowired
    private SiteMapper slaveSiteMapper;
    
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> valueOpsCache;

    @Autowired
    RedisExpireProperties expireProperties; 

    @Cacheable(value = "site", key = "'siteId_'+#siteId")
    public Site getSite(Integer siteId) {
        return slaveSiteMapper.selectByPrimaryKey(siteId);
    }

    @Cacheable(value = "site", key = "'zoneId_'+#zoneId")
    public List<Site> getSiteByZoneId(Integer zoneId) {
        return slaveSiteMapper.selectByZoneId(zoneId);
    }

    @Cacheable(value = "site", key = "'name_'+#name")
    public List<Site>  searchSiteList(String name){
        return slaveSiteMapper.searchSiteList(name);
    }

    /**
     * 获得以指定坐标为中心的周边站点列表
     * <p>
     * 输入指定经纬度坐标信息, 以此为中心点, 返回东西向偏差各5千米(经度偏差约0.055度),
     * 南北向偏差各5千米(纬度偏差约0.048度)的矩形区域内的所有站点信息
     * </p>
     *
     * @param longitude
     *            经度
     * @param latitude
     *            纬度
     * @return {@link List} {@link Site}
     */
    @Cacheable(value = "site", key = "'longitude_'+#longitude+'_latitude_'+#latitude")
    public List<SiteView> getByPosition(double longitude, double latitude) {

        Map<String, Double> map = new HashMap<>();
        map.put("longitude1", longitude - 0.055);
        map.put("longitude2", longitude + 0.055);
        map.put("latitude1", latitude - 0.048);
        map.put("latitude2", latitude + 0.048);
        return slaveSiteMapper.selectByRegion(map);

    }

    public List<Site> findByZoneIds(List<Integer> zoneIds) {
        List<Site> result = new ArrayList<>();
        List<Integer> queryList = new ArrayList<>();
        for (Integer zoneId : new HashSet<Integer>(zoneIds)) {
            if (null != valueOpsCache.get("site." + expireProperties.getCachePrefix() + ":" + "zoneId_" + zoneId)) {
                result.addAll((List<Site>)valueOpsCache.get("site." + expireProperties.getCachePrefix() + ":" + "zoneId_" + zoneId));
            } else {
                queryList.add(zoneId);
            }
        }
        if (!CollectionUtils.isEmpty(queryList)) {
            List<Site> temp = slaveSiteMapper.findByZoneIds(queryList);
            if (!CollectionUtils.isEmpty(temp)) {
                MultiValueMap<Integer, Site> multiValueMap=CollectionUtils.toMultiValueMap(new HashMap<Integer,List<Site>>());
                for(Site site:temp)
                {
                    multiValueMap.add(site.getZoneId(), site);
                }
                for(Integer zoneId:multiValueMap.keySet())
                {
                    valueOpsCache.set("site." + expireProperties.getCachePrefix() + ":" + "zoneId_" + zoneId, multiValueMap.get(zoneId), expireProperties.getDefaultExpiration(),
                            TimeUnit.SECONDS);
                    result.addAll(multiValueMap.get(zoneId)); 
                }
            }

        }
        return result;
    }
}
