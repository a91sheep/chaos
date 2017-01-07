/**
 * 
 */
package com.store59.print.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.dao.mapper.PrintOrderMapper;

/**
 * 打印店订单order,数据操作类
 *
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2015年12月30日
 * @since 1.1
 */
@Repository
public class PrintOrderDao {

    @Autowired
    private PrintOrderMapper masterPrintOrderMapper;

    @Autowired
    private PrintOrderMapper slavePrintOrderMapper;

    public PrintOrder findByOrderId(Long orderId) {
        return slavePrintOrderMapper.findByOrderId(orderId);
    }

    public List<PrintOrder> findByFilter(PrintOrderFilter filter) {
        return slavePrintOrderMapper.findByFilter(filter);
    }

    public int insert(PrintOrder printOrder) {
        return masterPrintOrderMapper.insert(printOrder);
    }

    public int update(PrintOrder printOrder) {
        return masterPrintOrderMapper.update(printOrder);
    }

    public PrintOrder findMasterByOrderId(Long orderId) {
        return masterPrintOrderMapper.findByOrderId(orderId);
    }

    public int findCountByFilter(PrintOrderFilter filter) {
        return slavePrintOrderMapper.findCountByFilter(filter);
    }

    public Double findSumAmountByFilter(PrintOrderFilter filter) {
        return slavePrintOrderMapper.findSumAmountByFilter(filter);
    }
}
