package com.store59.print.common.remoting;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.model.PrintOrderRecord;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/20
 * @since 1.0
 */
public interface PrintOrderRecordService {

    /**
     * 新增待检测订单记录
     *
     * @param printOrderRecord
     * @return
     */
    Result<PrintOrderRecord> insert(PrintOrderRecord printOrderRecord);

    /**
     * 获取未检测订单号列表
     *
     * @return
     */
    Result<List<String>> getUncheckOrderIds();

    /**
     * 修改订单记录为已检测
     *
     * @param orderIdList
     * @return
     */
    Result<Boolean> updateRecordStatus(List<String> orderIdList);
}
