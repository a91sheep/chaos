package com.store59.print.common.remoting;

import java.util.List;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.filter.AdOrderFilter;
import com.store59.print.common.model.ad.AdFreeOrder;
import com.store59.print.common.model.ad.AdOrder;

/**
 * AdOrder 接口
 * 
 * Created on 2016-08-17.
 */
public interface AdOrderService {
	/**
	 * 查询AdOrder信息
	 *
	 * @param id
	 * @return
	 */
	Result<AdOrder> findAdOrder(String id, Long uid);

	/**
	 * 查询AdOrder信息列表
	 *
	 * @param filter
	 * @return
	 */
	Result<List<AdOrder>> findAdOrderList(AdOrderFilter filter);

	/**
	 * 添加AdOrder信息
	 *
	 * @param record
	 * @return
	 */
	Result<AdOrder> addAdOrder(AdOrder record);

	/**
	 * 更新AdOrder信息
	 *
	 * @param record
	 * @return
	 */
	Result<Boolean> updateAdOrder(AdOrder record);

	/**
	 * 获取免费打印广告信息
	 * 
	 * @param filter
	 * @return
	 */
	Result<AdFreeOrder> findFreeOrder(AdOrderFilter filter);

	/**
	 * 获取总条数
	 * 
	 * @param filter
	 * @return
	 */
	Result<Integer> getTotalAmounts(AdOrderFilter filter);

	/**
	 * 订单统计列表
	 * 
	 * @param filter
	 * @return
	 */
	Result<List<AdOrder>> findOrderUsedList(AdOrderFilter filter);

}
