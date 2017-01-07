package com.yunyin.print.service.mapper;

import com.yunyin.print.common.model.PrintOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/12
 * @since 1.0
 */
public interface PrintOrderDetailMapper {
    List<PrintOrderDetail> findByOrderId(String orderId);

    List<PrintOrderDetail> findByOrderIds(List<String> orderIds);

    int addBatch(@Param("orderId") String orderId, @Param("printOrderDetails") List<PrintOrderDetail> printOrderDetails);

    int updateStatus(PrintOrderDetail detail);
}
