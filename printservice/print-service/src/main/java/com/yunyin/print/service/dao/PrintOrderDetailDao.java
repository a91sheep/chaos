package com.yunyin.print.service.dao;

import com.yunyin.print.common.model.PrintOrderDetail;
import com.yunyin.print.service.mapper.PrintOrderDetailMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/12
 * @since 1.0
 */
@Repository
public class PrintOrderDetailDao {
    @Autowired
    private PrintOrderDetailMapper masterPrintOrderDetailMapper;
    @Autowired
    private PrintOrderDetailMapper slavePrintOrderDetailMapper;

    public List<PrintOrderDetail> findByOrderId(String orderId) {
        return slavePrintOrderDetailMapper.findByOrderId(orderId);
    }

    public List<PrintOrderDetail> findByOrderIds(List<String> orderIds) {
        return slavePrintOrderDetailMapper.findByOrderIds(orderIds);
    }

    public int addBatch(String orderId, List<PrintOrderDetail> printOrderDetails) {
        return masterPrintOrderDetailMapper.addBatch(orderId, printOrderDetails);
    }

    public int updateStatus(PrintOrderDetail detail) {
        return masterPrintOrderDetailMapper.updateStatus(detail);
    }


}