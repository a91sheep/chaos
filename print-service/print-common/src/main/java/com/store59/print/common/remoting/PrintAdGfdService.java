/**
 * 
 */
package com.store59.print.common.remoting;

import java.util.List;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.filter.FreePrintOrderFilter;
import com.store59.print.common.model.PrintAdGfdOrder;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年8月23日 上午10:10:57
 * @since 1.0
 */
public interface PrintAdGfdService {
    /**
     * 新增免费打印记录
     *
     * @param adGfdOrder
     * @return
     */
    Result<PrintAdGfdOrder> insert(PrintAdGfdOrder adGfdOrder);
    

    /**
     * 修改免费打印记录
     *
     * @param adGfdOrder
     * @return
     */
    Result<Boolean> update(PrintAdGfdOrder adGfdOrder);
    
    /**
     * 查询免费打印订单
     * @param order_id
     * @return
     */
    Result<PrintAdGfdOrder> findByOrderId(String order_id);
    
    /**
     * 统计用户已免费打印的张数
     * @param filter
     * @return
     */
    Result<Integer> calFreePrintCount(FreePrintOrderFilter filter);
    
    /**
     * 查询免费订单
     * @param filter
     * @return
     */
    Result<List<PrintAdGfdOrder>> findFreeOrderByFilter(FreePrintOrderFilter filter);
}
