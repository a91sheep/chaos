/**
 * 
 */
package com.store59.print.dao.mapper;

import java.util.List;

import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.PrintOrder;

/**
 * 打印店订单mapper
 *
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2015年12月30日
 * @since 1.1
 */
public interface PrintOrderMapper {
    PrintOrder findByOrderId(Long orderId);

    List<PrintOrder> findByFilter(PrintOrderFilter filter);

    int insert(PrintOrder printOrder);

    int update(PrintOrder printOrder);

    int findCountByFilter(PrintOrderFilter filter);

    Double findSumAmountByFilter(PrintOrderFilter filter);
}
