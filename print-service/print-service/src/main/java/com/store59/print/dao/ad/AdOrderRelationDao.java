package com.store59.print.dao.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.filter.AdOrderRelationFilter;
import com.store59.print.common.filter.LineCharFilter;
import com.store59.print.common.model.ad.AdOrderRelation;
import com.store59.print.common.model.ad.LineChar;
import com.store59.print.dao.mapper.AdOrderRelationMapper;

/**
 * Created on 2016-08-17.
 */
@Repository
public class AdOrderRelationDao {
	@Autowired
	private AdOrderRelationMapper masterAdOrderRelationMapper;
	@Autowired
	private AdOrderRelationMapper slaveAdOrderRelationMapper;

	public AdOrderRelation findAdOrderRelation(Long id) {
		return slaveAdOrderRelationMapper.select(id);
	}

	public List<AdOrderRelation> findAdOrderRelationList(AdOrderRelationFilter filter) {
		return slaveAdOrderRelationMapper.findListByFilter(filter);
	}

	public AdOrderRelation addAdOrderRelation(AdOrderRelation record) {
		return masterAdOrderRelationMapper.insert(record) == 0 ? null : record;
	}

	public Boolean updateAdOrderRelation(AdOrderRelation record) {
		return masterAdOrderRelationMapper.update(record) == 0 ? false : true;
	}

	public Boolean deleteAdOrderRelation(Long id) {
		return masterAdOrderRelationMapper.delete(id) == 0 ? false : true;
	}

	public Integer count(String orderId, Long uid, Integer type) {
		return slaveAdOrderRelationMapper.count(orderId, uid, type);
	}

	public Integer countByPerson(String orderId, Long uid, Integer type) {
		return slaveAdOrderRelationMapper.countByPerson(orderId, uid, type);
	}

	public List<LineChar> lineChar(LineCharFilter filter) {
		return slaveAdOrderRelationMapper.lineChar(filter);
	}

	public Integer weight() {
		int home = masterAdOrderRelationMapper.homepageWeight();
		int footer = masterAdOrderRelationMapper.footerWeight();
		return home + footer;
	}
	public Integer getTotalAmount(AdOrderRelationFilter filter){
		return slaveAdOrderRelationMapper.getTotalAmount(filter);
	}
}
