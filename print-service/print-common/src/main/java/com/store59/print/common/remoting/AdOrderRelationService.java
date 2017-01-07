package com.store59.print.common.remoting;

import java.util.List;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.filter.AdOrderRelationFilter;
import com.store59.print.common.filter.LineCharFilter;
import com.store59.print.common.model.ad.AdOrderRelation;
import com.store59.print.common.model.ad.AdStatistics;
import com.store59.print.common.model.ad.LineChar;

/**
 * AdOrderRelation 接口
 * 
 * Created on 2016-08-17.
 */
public interface AdOrderRelationService {

	/**
	 * 查询AdOrderRelation信息列表
	 *
	 * @param filter
	 * @return
	 */
	Result<List<AdOrderRelation>> findAdOrderRelationList(AdOrderRelationFilter filter);

	/**
	 * 添加AdOrderRelation信息
	 *
	 * @param record
	 * @return
	 */
	Result<AdOrderRelation> addAdOrderRelation(AdOrderRelation record);

	/**
	 * 订单统计信息接口
	 * 
	 * @param orderId
	 * @param uid
	 * @return
	 */
	Result<AdStatistics> statistics(String orderId, Long uid);

	/**
	 * 线性图接口
	 * 
	 * @return
	 */
	Result<List<LineChar>> lineChar(LineCharFilter filter);

	/**
	 * 批量添加关系数据
	 * 
	 * @param list
	 * @return
	 */
	Result<Integer> addAdOrderRelationList(List<AdOrderRelation> list);

	/**
	 * 获取总数
	 * 
	 * @param filter
	 * @return
	 */
	Result<Integer> getTotalAmounts(AdOrderRelationFilter filter);

}
