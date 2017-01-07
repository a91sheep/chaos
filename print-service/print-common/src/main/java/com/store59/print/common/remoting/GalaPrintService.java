package com.store59.print.common.remoting;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.gala.GalaPrintRelation;

/**
 * 59打印店活动接口
 * 
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.1 2016年4月19日
 */
public interface GalaPrintService {

	/**
	 * 添加购买用户与店家关系记录
	 * 
	 * @param printRelation
	 * @return
	 */
    Result<GalaPrintRelation> insert(GalaPrintRelation printRelation);
    
    /**
     * 统计参加某个店家活动的用户数量
     * 
     * @param filter
     * @return
     */
    Result<Integer> findGalaAmountByFilter(PrintOrderFilter filter);
}
