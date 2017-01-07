package com.store59.base.data.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import com.store59.base.common.model.RepoImage;
import com.store59.base.data.mapper.RepoImageMapper;
import com.store59.kylin.redis.RedisExpireProperties;

@Repository
public class RepoImageDao {
    @Autowired
    private RepoImageMapper masterRepoImageMapper;
    @Autowired
    private RepoImageMapper slaveRepoImageMapper;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> valueOpsCache;

    @Autowired
    RedisExpireProperties expireProperties;

    @Cacheable(value = "repoImage", key = "'rid_'+#rid")
    public List<RepoImage> getRepoImageListByRid(Integer rid) {
        return slaveRepoImageMapper.getRepoImageListByRid(rid);
    }

    public List<RepoImage> getRepoImageListByRidList(List<Integer> ridList) {
        List<RepoImage> result = new ArrayList<>();
        List<Integer> queryList = new ArrayList<>();
        for (Integer rid : new HashSet<Integer>(ridList)) {
            if (null != valueOpsCache.get("repoImage." + expireProperties.getCachePrefix() + ":" + "rid_" + rid)) {
                result.addAll((List<RepoImage>) valueOpsCache.get("repoImage." + expireProperties.getCachePrefix() + ":" + "rid_" + rid));
            } else {
                queryList.add(rid);
            }
        }
        if (!CollectionUtils.isEmpty(queryList)) {
            List<RepoImage> temp = slaveRepoImageMapper.getRepoImageListByRidList(queryList);
            if (!CollectionUtils.isEmpty(temp)) {
                MultiValueMap<Integer, RepoImage> multiValueMap=CollectionUtils.toMultiValueMap(new HashMap<Integer,List<RepoImage>>());
                for(RepoImage repoImage:temp)
                {
                    multiValueMap.add(repoImage.getRid(), repoImage);
                }
                for(Integer rid:multiValueMap.keySet())
                {
                    valueOpsCache.set("repoImage." + expireProperties.getCachePrefix() + ":" + "rid_" + rid, multiValueMap.get(rid),
                            expireProperties.getDefaultExpiration(), TimeUnit.SECONDS);
                    result.addAll(multiValueMap.get(rid));
                }
            }

        }
        if(!CollectionUtils.isEmpty(result))
        {
            //添加默认排序
             result = result.parallelStream().sorted(new Comparator<RepoImage>() {
             @Override
             public int compare(RepoImage a, RepoImage b) {
             int i = a.getRid().compareTo(b.getRid());
             if (i > 0) {
             return 1;
             } else if (0 == i) {
             return a.getSort().compareTo(b.getSort());
             } else {
             return -1;
             }
             }
             }).collect(Collectors.toList());
        }
        return result;
    }
}
