/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.common.api;

import com.store59.base.common.filter.ExtractTransactionFilter;
import com.store59.base.common.model.ExtractTransactionRecord;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 *提现钱交易情况
 * @author <a href="mailto:jiangzq@59store.com">刑天</a>
 * @version 1.0 16/4/27
 * @since 1.0
 */
public interface ExtractTransactionApi {
    /**
     * 增加提现记录
     * @param extractTransactionRecord
     * @return
     */
    Result<ExtractTransactionRecord> addRecord(ExtractTransactionRecord extractTransactionRecord);

    /**
     * 更新提现信息
     *
     * @param id            主键id
     * @param status        状态(1: 成功 2:失败)
     * @param handlerId     处理人id
     * @return
     */
    Result<?> updateStatus(String id, Byte status, String handlerId);

    /**
     * 指定条件查询
     *
     * @param filter
     * @return
     */
    Result<List<ExtractTransactionRecord>> findByFilter(ExtractTransactionFilter filter);
}
