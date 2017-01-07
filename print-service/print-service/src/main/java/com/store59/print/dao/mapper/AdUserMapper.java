package com.store59.print.dao.mapper;

import java.util.List;

import com.store59.print.common.filter.AdUserFilter;
import com.store59.print.common.model.ad.AdUser;

/**
 * AdUser mybatis mapper
 * 
 * Created on 2016-08-17.
 */
public interface AdUserMapper {

    AdUser select(Long id);

    int insert(AdUser record);

    int update(AdUser record);

    int delete(Long id);

    List<AdUser> findListByFilter(AdUserFilter filter);
    
    int getTotalAmount(AdUserFilter filter);
}
