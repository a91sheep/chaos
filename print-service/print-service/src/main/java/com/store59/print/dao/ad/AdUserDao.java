package com.store59.print.dao.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.filter.AdUserFilter;
import com.store59.print.common.model.ad.AdUser;
import com.store59.print.dao.mapper.AdUserMapper;

/**
 * Created on 2016-08-17.
 */
@Repository
public class AdUserDao {
    @Autowired
    private AdUserMapper masterAdUserMapper;
    @Autowired
    private AdUserMapper slaveAdUserMapper;

    public AdUser findAdUser(Long id){
        return slaveAdUserMapper.select(id);
    }
    
    public List<AdUser> findAdUserList(AdUserFilter filter){
        return slaveAdUserMapper.findListByFilter(filter);
    }

    public AdUser addAdUser(AdUser record){
        return masterAdUserMapper.insert(record) == 0 ? null : record;
    }

    public Boolean updateAdUser(AdUser record){
        return masterAdUserMapper.update(record) == 0 ? false : true;
    }

    public Boolean deleteAdUser(Long id){
        return masterAdUserMapper.delete(id) == 0 ? false : true;
    }
    
    public Integer getTotalAmount(AdUserFilter filter){
    	return slaveAdUserMapper.getTotalAmount(filter);
    }
}

