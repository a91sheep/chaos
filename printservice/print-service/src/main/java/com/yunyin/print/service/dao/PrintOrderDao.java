package com.yunyin.print.service.dao;

import com.yunyin.print.common.filter.PrintOrderFilter;
import com.yunyin.print.common.model.PrintOrder;
import com.yunyin.print.service.mapper.PrintOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/12
 * @since 1.0
 */
@Repository
public class PrintOrderDao {
    @Autowired
    private PrintOrderMapper masterPrintOrderMapper;
    @Autowired
    private PrintOrderMapper slavePrintOrderMapper;

    public PrintOrder findByOrderId(String orderId) {
        return slavePrintOrderMapper.findByOrderId(orderId);
    }

    public PrintOrder findMasterByOrderId(String orderId) {
        return masterPrintOrderMapper.findByOrderId(orderId);
    }

    public PrintOrder findByPrintCode(String printCode) {
        return slavePrintOrderMapper.findByPrintCode(printCode);
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
}
