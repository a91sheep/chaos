/*
 * Copyright 2015 © 59store.com.
 *
 * RepoDao.java
 *
 */
package com.store59.base.data.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.store59.base.common.filter.RepoFilter;
import com.store59.base.common.model.Repo;
import com.store59.base.data.mapper.RepoMapper;
import com.store59.kylin.redis.RedisExpireProperties;

/**
 * Created by shanren on 15/7/27.
 */
@Repository
public class RepoDao {

    @Autowired
    private RepoMapper masterRepoMapper;
    @Autowired
    private RepoMapper slaveRepoMapper;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> valueOpsCache;

    @Autowired
    RedisExpireProperties expireProperties; 
    
    @Cacheable(value = "repo", key = "'rid_'+#rid")
    public Repo findRepoByRid(int rid) {
        return slaveRepoMapper.findRepoByRid(rid);
    }

    public List<Repo> findRepoListAll() {
        return slaveRepoMapper.findRepoListAll();
    }

    public List<Repo> findRepoListByIds(List<Integer> ridList) {
        List<Repo> result = new ArrayList<>();
        List<Integer> queryList = new ArrayList<>();
        for (Integer rid : new HashSet<Integer>(ridList)) {
            if (null != valueOpsCache.get("repo." + expireProperties.getCachePrefix() + ":" + "rid_" + rid)) {
                result.add((Repo)valueOpsCache.get("repo." + expireProperties.getCachePrefix() + ":" + "rid_" + rid));
            } else {
                queryList.add(rid);
            }
        }
        if (!CollectionUtils.isEmpty(queryList)) {
            List<Repo> temp = slaveRepoMapper.findRepoListByIds(queryList);
            if (!CollectionUtils.isEmpty(temp)) {
                for (Repo repo : temp) {
                    valueOpsCache.set("repo." + expireProperties.getCachePrefix() + ":" + "rid_" + repo.getRid(), repo, expireProperties.getDefaultExpiration(),
                            TimeUnit.SECONDS);
                    result.add(repo);
                }
            }

        }
        return result;
    }

    public List<Repo> findByFilter(RepoFilter filter) {
        return slaveRepoMapper.findByFilter(filter);
    }
}
