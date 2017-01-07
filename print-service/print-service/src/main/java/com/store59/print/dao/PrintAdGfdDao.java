/**
 * 
 */
package com.store59.print.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.filter.FreePrintOrderFilter;
import com.store59.print.common.model.PrintAdGfdOrder;
import com.store59.print.dao.mapper.PrintAdGfdMapper;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年8月23日 上午10:27:00
 * @since 1.0
 */
@Repository
public class PrintAdGfdDao {
	@Autowired
	private PrintAdGfdMapper masterPrintAdGfdMapper;
	
	@Autowired
	private PrintAdGfdMapper slavePrintAdGfdMapper;

    public int insert(PrintAdGfdOrder gfdOrder) {
        return masterPrintAdGfdMapper.insert(gfdOrder);
    }

    public int update(PrintAdGfdOrder gfdOrder) {
        return masterPrintAdGfdMapper.update(gfdOrder);
    }
    
    public PrintAdGfdOrder findByOrderId(String order_id){
    	return slavePrintAdGfdMapper.findByOrderId(order_id);
    }
    
    public int calFreePrintCount(FreePrintOrderFilter filter){
    	return slavePrintAdGfdMapper.calFreePrintCount(filter);
    }
    
    public List<PrintAdGfdOrder> findFreeOrderByFilter(FreePrintOrderFilter filter){
    	return slavePrintAdGfdMapper.findFreeOrderByFilter(filter);
    }
}
