package com.store59.base.service;

import com.store59.base.common.filter.OrderPayAbnormalRecordFilter;
import com.store59.base.mq.RefundProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store59.base.common.model.OrderPayAbnormalRecord;
import com.store59.base.data.dao.OrderPayAbnormalRecordDao;

import java.util.List;

@Service
public class OrderPayAbnormalRecordService {
    @Autowired
    private OrderPayAbnormalRecordDao orderPayAbnormalRecordDao;

    @Autowired
    private RefundProcessor refundClient;

    public OrderPayAbnormalRecord addOrderPayAbnormalRecord(
            OrderPayAbnormalRecord record) {
        record.setId(null);
        return orderPayAbnormalRecordDao.addOrderPayAbnormalRecord(record);
    }
    
    public Boolean updateOrderPayAbnormalRecord(OrderPayAbnormalRecord record) {
        if (record.getId() != null || record.getOrderSn() != null) {
            boolean result = orderPayAbnormalRecordDao.updateOrderPayAbnormalRecord(record);

            //验证数据,推送回调完成的信息
            if(result && record.getStatus() == OrderPayAbnormalRecord.STATUS_SUCCESS) {
                refundClient.send(record.getId());
            }

            return result;
        } else {
            return false;
        }
    }
    
    public List<OrderPayAbnormalRecord> findByFilter(OrderPayAbnormalRecordFilter filter) {
        return orderPayAbnormalRecordDao.findByFilter(filter);
    }


    public OrderPayAbnormalRecord findById(Integer id) {
        return orderPayAbnormalRecordDao.findById(id);
    }
}
