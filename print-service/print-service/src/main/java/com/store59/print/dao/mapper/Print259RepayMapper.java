/**
 * 
 */
package com.store59.print.dao.mapper;

import java.util.List;

import com.store59.print.common.model.gala259user.Print259Repay;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月21日 下午4:50:57
 * @since 1.0
 */
public interface Print259RepayMapper {
	
	List<Print259Repay> findRepayByDormId(Integer dormId);
	
	int updateBatchRepayList(List<Print259Repay> repaylist);
}
