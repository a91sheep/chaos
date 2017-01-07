/**
 * 
 */
package com.store59.print.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.model.gala259user.PrintShopTime;
import com.store59.print.dao.mapper.PrintShopTimeMapper;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月21日 下午4:49:19
 * @since 1.0
 */
@Repository
public class PrintShopTimeDao {
	@Autowired
	private PrintShopTimeMapper masterPrintShopTimeMapper;

	@Autowired
	private PrintShopTimeMapper slavePrintShopTimeMapper;


	public List<PrintShopTime> findPrintShopTimeByDormId(Integer dormId,Long time) {
		return slavePrintShopTimeMapper.findPrintShopTimeByDormId(dormId,time);
	}
	
	public Integer findCountPrintShopTimeByDormId(Integer dormId, Long time){
		return slavePrintShopTimeMapper.findCountPrintShopTimeByDormId(dormId,time);
	}
	
}
