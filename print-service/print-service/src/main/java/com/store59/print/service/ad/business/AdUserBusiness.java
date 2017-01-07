package com.store59.print.service.ad.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store59.print.common.filter.AdUserFilter;
import com.store59.print.common.model.ad.AdUser;
import com.store59.print.dao.ad.AdUserDao;

/**
 * Created on 2016-08-17.
 */
@Service
public class AdUserBusiness {
    @Autowired
    private AdUserDao adUserDao;

    public AdUser findAdUser(Long id){
        return adUserDao.findAdUser(id);
    }
    
    public List<AdUser> findAdUserList(AdUserFilter filter){
        return adUserDao.findAdUserList(filter);
    }

    public AdUser addAdUser(AdUser record){
        return adUserDao.addAdUser(record);
    }

    public Boolean updateAdUser(AdUser record){
        return adUserDao.updateAdUser(record);
    }

    public Boolean deleteAdUser(Long id){
        return adUserDao.deleteAdUser(id);
    }
    public Integer getTotalAmount(AdUserFilter filter){
    	return adUserDao.getTotalAmount(filter);
    }
}

