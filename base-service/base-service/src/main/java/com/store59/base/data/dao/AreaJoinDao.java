package com.store59.base.data.dao;

import com.store59.base.common.model.AreaJoin;
import com.store59.base.data.mapper.AreaJoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shibing on 15/9/10.
 */

@Repository
public class AreaJoinDao {

    @Autowired
    private AreaJoinMapper masterAreaJoinMapper;

    @Autowired
    private AreaJoinMapper slaveAreaJoinMapper;

    public AreaJoin getBySiteId(int siteId){
        return slaveAreaJoinMapper.getBySiteId(siteId);
    }

    public List<AreaJoin> findBySiteIds(List<Integer> siteIds) {
        return slaveAreaJoinMapper.findBySiteIds(siteIds);
    }
}
