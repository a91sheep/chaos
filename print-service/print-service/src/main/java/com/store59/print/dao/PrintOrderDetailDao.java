/**
 * 
 */
package com.store59.print.dao;

import java.util.List;

import com.store59.print.common.model.PrintOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.dao.mapper.PrintOrderDetailMapper;

/**
 * 打印店订单详情,数据操作类
 *
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2016年1月5日
 * @since 1.1
 */
@Repository
public class PrintOrderDetailDao {
    @Autowired
    private PrintOrderDetailMapper masterPrintOrderDetailMapper;
    @Autowired
    private PrintOrderDetailMapper slavePrintOrderDetailMapper;

    public List<PrintOrderDetail> findByOrderId(Long orderId) {
        return slavePrintOrderDetailMapper.findByOrderId(orderId);
    }

    public int addBatch(Long orderId, List<PrintOrderDetail> printOrderDetails) {
        return masterPrintOrderDetailMapper.addBatch(orderId, printOrderDetails);
    }

    public List<PrintOrderDetail> findByOrderIds(List<Long> orderIds) {
        return slavePrintOrderDetailMapper.findByOrderIds(orderIds);
    }

    public int updateProfit(PrintOrderDetail detail) {
        return masterPrintOrderDetailMapper.updateProfit(detail);
    }

    public int updateStatus(PrintOrderDetail detail) {
        return masterPrintOrderDetailMapper.updateStatus(detail);
    }
}
