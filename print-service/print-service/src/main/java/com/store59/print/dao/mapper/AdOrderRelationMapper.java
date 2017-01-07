package com.store59.print.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.store59.print.common.filter.AdOrderRelationFilter;
import com.store59.print.common.filter.LineCharFilter;
import com.store59.print.common.model.ad.AdOrderRelation;
import com.store59.print.common.model.ad.LineChar;

/**
 * AdOrderRelation mybatis mapper
 * 
 * Created on 2016-08-17.
 */
public interface AdOrderRelationMapper {

	AdOrderRelation select(Long id);

	int insert(AdOrderRelation record);

	int update(AdOrderRelation record);

	int delete(Long id);

	List<AdOrderRelation> findListByFilter(AdOrderRelationFilter filter);

	int count(@Param("orderId") String orderId, @Param("uid") Long uid, @Param("type") Integer type);

	int countByPerson(@Param("orderId") String orderId, @Param("uid") Long uid, @Param("type") Integer type);

	List<LineChar> lineChar(LineCharFilter filter);
	
	int footerWeight();
	
	int homepageWeight();
	
	int getTotalAmount(AdOrderRelationFilter filter);
}
