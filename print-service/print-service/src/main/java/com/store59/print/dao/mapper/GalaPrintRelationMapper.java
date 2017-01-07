/**
 * 
 */
package com.store59.print.dao.mapper;

import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.gala.GalaPrintRelation;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年4月26日 下午11:27:30
 * @since 1.0
 */
public interface GalaPrintRelationMapper {
	
	int insert(GalaPrintRelation printRelation);
	
	int findGalaAmountByFilter(PrintOrderFilter filter);

}
