package com.store59.print.common.remoting;

import java.util.List;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.filter.AdOrderFilter;
import com.store59.print.common.filter.AdUserFilter;
import com.store59.print.common.model.ad.AdUser;

/**
 * AdUser 接口
 * 
 * Created on 2016-08-17.
 */
public interface AdUserService {

	/**
	 * 查询AdUser信息列表
	 *
	 * @param filter
	 * @return
	 */
	Result<List<AdUser>> findAdUserList(AdUserFilter filter);

	/**
	 * 添加AdUser信息
	 *
	 * @param record
	 * @return
	 */
	Result<AdUser> addAdUser(AdUser record);
	/**
	 * 更新用户信息
	 * @param record
	 * @return
	 */
	Result<AdUser> updateAdUser(AdUser record);
	/**
	 * 获取总条数
	 * @param filter
	 * @return
	 */
	Result<Integer> getTotalAmounts(AdUserFilter filter);
	/**
	 * 查询用户详情
	 * @param uid
	 * @return
	 */
	Result<AdUser> findAdUser(Long uid);

}
