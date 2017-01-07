package com.store59.base.data.dao;

import com.store59.base.common.filter.OrderPayAbnormalRecordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.base.common.model.OrderPayAbnormalRecord;
import com.store59.base.data.mapper.OrderPayAbnormalRecordMapper;

import java.util.List;

@Repository
public class OrderPayAbnormalRecordDao {
    @Autowired
    private OrderPayAbnormalRecordMapper masterOrderPayAbnormalRecordMapper;
    @Autowired
    private OrderPayAbnormalRecordMapper slaveOrderPayAbnormalRecordMapper;

    public OrderPayAbnormalRecord addOrderPayAbnormalRecord(
            OrderPayAbnormalRecord record) {
        return masterOrderPayAbnormalRecordMapper.insertSelective(record) == 0 ? null
                : record;
    }
    
    public Boolean updateOrderPayAbnormalRecord(OrderPayAbnormalRecord record) {
        return masterOrderPayAbnormalRecordMapper.updateOrderPayAbnormalRecord(record) == 0 ? false : true;
    }

    public List<OrderPayAbnormalRecord> findByFilter(OrderPayAbnormalRecordFilter filter) {
        return slaveOrderPayAbnormalRecordMapper.findByFilter(filter);
    }

    public OrderPayAbnormalRecord findById(Integer id) {
        return slaveOrderPayAbnormalRecordMapper.findById(id);
    }
}
