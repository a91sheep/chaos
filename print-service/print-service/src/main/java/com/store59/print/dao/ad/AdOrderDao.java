package com.store59.print.dao.ad;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.filter.AdOrderFilter;
import com.store59.print.common.model.ad.AdOrder;
import com.store59.print.dao.mapper.AdOrderMapper;

/**
 * Created on 2016-08-17.
 */
@Repository
public class AdOrderDao {
	@Autowired
	private AdOrderMapper masterAdOrderMapper;
	@Autowired
	private AdOrderMapper slaveAdOrderMapper;

	public AdOrder findAdOrder(@Param("id") String id, @Param("uid") Long uid) {
		return slaveAdOrderMapper.select(id, uid);
	}

	public List<AdOrder> findAdOrderList(AdOrderFilter filter) {
		return slaveAdOrderMapper.findListByFilter(filter);
	}

	public AdOrder addAdOrder(AdOrder record) {
		return masterAdOrderMapper.insert(record) == 0 ? null : record;
	}

	public Boolean updateAdOrder(AdOrder record) {
		return masterAdOrderMapper.update(record) == 0 ? false : true;
	}

	public Boolean deleteAdOrder(Long id) {
		return masterAdOrderMapper.delete(id) == 0 ? false : true;
	}
	public List<AdOrder> freeAd(AdOrderFilter filter ){
		return slaveAdOrderMapper.freeAd(filter);
	}
	public Integer getTotalAmount(AdOrderFilter filter ){
		return slaveAdOrderMapper.getTotalAmount(filter);
	}
	public List<AdOrder> findOrderUsedList(AdOrderFilter filter) {
		return slaveAdOrderMapper.findOrderUsedList(filter);
	}
}
