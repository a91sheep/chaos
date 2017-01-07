/**
 * 
 */
package com.store59.print.dao.mapper;

import java.util.List;

import com.store59.print.common.filter.FreePrintOrderFilter;
import com.store59.print.common.model.PrintAdGfdOrder;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年8月23日 上午10:28:31
 * @since 1.0
 */
public interface PrintAdGfdMapper {
	
	int insert(PrintAdGfdOrder gfdOrder);
	
	int update(PrintAdGfdOrder gfdOrder);
	
	PrintAdGfdOrder findByOrderId(String order_id);
	
	int calFreePrintCount(FreePrintOrderFilter filter);
	
	List<PrintAdGfdOrder> findFreeOrderByFilter(FreePrintOrderFilter filter);
}
