package com.store59.print.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.store59.print.common.filter.AdOrderFilter;
import com.store59.print.common.model.ad.AdOrder;

/**
 * AdOrder mybatis mapper
 * 
 * Created on 2016-08-17.
 */
public interface AdOrderMapper {

	AdOrder select(@Param("orderId") String orderId, @Param("uid") Long uid);

	int insert(AdOrder record);

	int update(AdOrder record);

	int delete(Long id);

	List<AdOrder> findListByFilter(AdOrderFilter filter);

	List<AdOrder> freeAd(AdOrderFilter filter);
	
	int getTotalAmount(AdOrderFilter filter);
	
	List<AdOrder> findOrderUsedList(AdOrderFilter filter);
}
