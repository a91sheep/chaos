/**
 * 
 */
package com.store59.print.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.model.gala259user.Print259Repay;
import com.store59.print.dao.mapper.Print259RepayMapper;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月21日 下午4:49:19
 * @since 1.0
 */
@Repository
public class Print259RepayDao {
	@Autowired
	private Print259RepayMapper masterPrint259RepayMapper;

	@Autowired
	private Print259RepayMapper slavePrint259RepayMapper;


	public List<Print259Repay> findRepayByDormId(Integer dormId) {
		return slavePrint259RepayMapper.findRepayByDormId(dormId);
	}
	
	public int updateBatchRepayList(List<Print259Repay> repaylist){
		return masterPrint259RepayMapper.updateBatchRepayList(repaylist);
	}
}
