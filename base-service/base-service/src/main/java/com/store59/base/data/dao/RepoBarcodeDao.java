/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.data.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import com.store59.base.common.model.RepoBarcode;
import com.store59.base.data.mapper.RepoBarcodeMapper;
import com.store59.kylin.redis.RedisExpireProperties;

/**
 * RepoBarcode 数据访问层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/10
 * @since 1.0
 */
@Repository
public class RepoBarcodeDao {

    @Autowired
    private RepoBarcodeMapper masterRepoBarcodeMapper;

    @Autowired
    private RepoBarcodeMapper slaveRepoBarcodeMapper;
    
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> valueOpsCache;

    @Autowired
    RedisExpireProperties expireProperties; 
    

    public int insert(RepoBarcode repoBarcode){
        return masterRepoBarcodeMapper.insert(repoBarcode);
    }

    public RepoBarcode getByBarcode(String barcode) {
        return slaveRepoBarcodeMapper.getByBarcode(barcode);
    }

    public List<RepoBarcode> findByRids(List<Integer> rids) {
        List<RepoBarcode> result = new ArrayList<>();
        List<Integer> queryList = new ArrayList<>();
        for (Integer rid : new HashSet<Integer>(rids)) {
            if (null != valueOpsCache.get("repoBarcode." + expireProperties.getCachePrefix() + ":" + "rid_" + rid)) {
                result.addAll((List<RepoBarcode>)valueOpsCache.get("repoBarcode." + expireProperties.getCachePrefix() + ":" + "rid_" + rid));
            } else {
                queryList.add(rid);
            }
        }
        if (!CollectionUtils.isEmpty(queryList)) {
            List<RepoBarcode> temp = slaveRepoBarcodeMapper.findByRids(queryList);
            if (!CollectionUtils.isEmpty(temp)) {
                MultiValueMap<Integer, RepoBarcode> multiValueMap=CollectionUtils.toMultiValueMap(new HashMap<Integer,List<RepoBarcode>>());
                for(RepoBarcode barcode:temp)
                {
                    multiValueMap.add(barcode.getRid(), barcode);
                }
                for(Integer rid:multiValueMap.keySet())
                {
                    valueOpsCache.set("repoBarcode." + expireProperties.getCachePrefix() + ":" + "rid_" + rid, multiValueMap.get(rid), expireProperties.getDefaultExpiration(),
                            TimeUnit.SECONDS);
                    result.addAll(multiValueMap.get(rid)); 
                }
            }

        }
        return result; 
    }
}