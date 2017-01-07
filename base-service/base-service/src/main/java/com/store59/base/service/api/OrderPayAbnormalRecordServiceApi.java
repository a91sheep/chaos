package com.store59.base.service.api;

import com.store59.base.common.filter.OrderPayAbnormalRecordFilter;
import com.store59.kylin.common.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;

import com.store59.base.common.api.OrderPayAbnormalRecordApi;
import com.store59.base.common.model.OrderPayAbnormalRecord;
import com.store59.base.service.OrderPayAbnormalRecordService;
import com.store59.kylin.common.model.Result;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

import java.util.List;

@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = OrderPayAbnormalRecordApi.class, exportPath = "/orderpayabnormalrecord")
public class OrderPayAbnormalRecordServiceApi implements
        OrderPayAbnormalRecordApi {
    @Autowired
    private OrderPayAbnormalRecordService orderPayAbnormalRecordService;

    public Result<OrderPayAbnormalRecord> addOrderPayAbnormalRecord(
            OrderPayAbnormalRecord record) {
        return ResultHelper.genResultWithSuccess(orderPayAbnormalRecordService.addOrderPayAbnormalRecord(record));
    }

    @Override
    public Result<List<OrderPayAbnormalRecord>> findByFilter(OrderPayAbnormalRecordFilter filter) {
        return ResultHelper.genResultWithSuccess(orderPayAbnormalRecordService.findByFilter(filter));
    }

    @Override
    public Result<Boolean> updateOrderPayAbnormalRecord(OrderPayAbnormalRecord record) {
        return ResultHelper.genResultWithSuccess(orderPayAbnormalRecordService.updateOrderPayAbnormalRecord(record));
    }
}
