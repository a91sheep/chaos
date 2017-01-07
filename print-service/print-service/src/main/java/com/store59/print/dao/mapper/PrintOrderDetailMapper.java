/**
 * 
 */
package com.store59.print.dao.mapper;

import java.util.List;

import com.store59.print.common.model.PrintOrderDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 打印店订单详情mapper
 *
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2016年1月5日
 * @since 1.1
 */
public interface PrintOrderDetailMapper {

    List<PrintOrderDetail> findByOrderId(Long orderId);

    int addBatch(@Param("orderId") Long orderId, @Param("printOrderDetails") List<PrintOrderDetail> printOrderDetails);

    List<PrintOrderDetail> findByOrderIds(List<Long> orderIds);

    int updateProfit(PrintOrderDetail detail);

    int updateStatus(PrintOrderDetail detail);
}
