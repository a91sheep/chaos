package com.store59.base.common.api;

import com.store59.base.common.filter.OrderPayAbnormalRecordFilter;
import com.store59.base.common.model.OrderPayAbnormalRecord;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * 支付异常订单记录接口
 * 
 * @author heqingpan
 *
 */
public interface OrderPayAbnormalRecordApi {
    /**
     * 添加支付异常订单记录
     * 
     * @param record
     * @return
     */
    Result<OrderPayAbnormalRecord> addOrderPayAbnormalRecord(
            OrderPayAbnormalRecord record);
    
    /**
     * 更新支付异常订单记录
     * 
     * @param record
     * @return
     */
    Result<Boolean> updateOrderPayAbnormalRecord(
            OrderPayAbnormalRecord record);
    
    /**
     * 根据组合条件，查询异常订单
     *
     * @param filter filter
     * @return
     */
    Result<List<OrderPayAbnormalRecord>> findByFilter(OrderPayAbnormalRecordFilter filter);
}
