package com.store59.base.data.mapper;

import com.store59.base.common.filter.OrderPayAbnormalRecordFilter;
import com.store59.base.common.model.OrderPayAbnormalRecord;

import java.util.List;

public interface OrderPayAbnormalRecordMapper {
    int insertSelective(OrderPayAbnormalRecord record);

    int updateOrderPayAbnormalRecord(OrderPayAbnormalRecord record);
    
    List<OrderPayAbnormalRecord> findByFilter(OrderPayAbnormalRecordFilter filter);

    OrderPayAbnormalRecord findById(Integer id);
}
