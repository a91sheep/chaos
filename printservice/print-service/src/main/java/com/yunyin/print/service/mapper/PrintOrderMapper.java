package com.yunyin.print.service.mapper;

import com.yunyin.print.common.filter.PrintOrderFilter;
import com.yunyin.print.common.model.PrintOrder;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/12
 * @since 1.0
 */
public interface PrintOrderMapper {
    PrintOrder findByOrderId(String orderId);

    PrintOrder findByPrintCode(String printCode);

    List<PrintOrder> findByFilter(PrintOrderFilter filter);

    int insert(PrintOrder printOrder);

    int update(PrintOrder printOrder);

//    int findCountByFilter(PrintOrderFilter filter);

//    Double findSumAmountByFilter(PrintOrderFilter filter);
}
