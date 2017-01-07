/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.service.api;

import com.store59.base.common.api.ExtractTransactionApi;
import com.store59.base.common.filter.ExtractTransactionFilter;
import com.store59.base.common.model.ExtractTransactionRecord;
import com.store59.base.data.dao.ExtractTransactionRecordDao;
import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.CommonKeyUtils;
import com.store59.kylin.utils.JsonUtil;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:jiangzq@59store.com">刑天</a>
 * @version 1.0 16/4/27
 * @since 1.0
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = ExtractTransactionApi.class, exportPath = "/extract")
public class ExtractTransactionServiceApi implements ExtractTransactionApi {

    private Logger logger = LoggerFactory.getLogger(ExtractTransactionServiceApi.class);
    @Autowired
    private ExtractTransactionRecordDao extractTransactionRecordDao;

    @Override
    public Result<ExtractTransactionRecord> addRecord(ExtractTransactionRecord record) {
        if (record == null || !record.check()) {
            logger.error("参数不正确, data: {}", JsonUtil.getJsonFromObject(record));
            throw new ServiceException(4, "参数不正确");
        }

        record.setId(CommonKeyUtils.genUniqueKey());
        //设置默认状态
        record.setStatus(ExtractTransactionRecord.STATUS_INIT);

        if(extractTransactionRecordDao.insert(record) != 1) {
            logger.error("添加提现记录失败, data: ", JsonUtil.getJsonFromObject(record));
            throw new ServiceException(2, "添加提现记录失败");
        }

        return ResultHelper.genResultWithSuccess(record);
    }

    @Override
    public Result<?> updateStatus(String id, Byte status, String handlerId) {
        if(StringUtils.isBlank(id)
                || (status != ExtractTransactionRecord.STATUS_SUCCCESS && status != ExtractTransactionRecord.STATUS_FAIL)
                || StringUtils.isBlank(handlerId)) {
            logger.error("参数不正确, data: id={}, status={}, handlerId={}", id, status, handlerId);
            throw new ServiceException(4, "参数不正确");
        }

        ExtractTransactionRecord record = new ExtractTransactionRecord();
        record.setId(id);
        record.setStatus(status);
        record.setHandlerId(handlerId);
        record.setHandleTime((int) (System.currentTimeMillis() / 1000));

        if(extractTransactionRecordDao.update(record) != 1) {
            logger.error("更新提现状态失败, data: id={}, status={}, handlerId={}", id, status, handlerId);
            throw new ServiceException(2, "更新提现状态失败");
        }
        return ResultHelper.genResultWithSuccess();
    }

    @Override
    public Result<List<ExtractTransactionRecord>> findByFilter(ExtractTransactionFilter filter) {
        if(null == filter
                || (null == filter.getBizType()
                && null == filter.getStatus()
                && null == filter.getDate()
                && StringUtils.isBlank(filter.getTradeNo()))) {
            logger.error("参数不正确, data: {}", JsonUtil.getJsonFromObject(filter));
            throw new ServiceException(4, "参数不正确");
        }

        return ResultHelper.genResultWithSuccess(extractTransactionRecordDao.findByFilter(filter));
    }
}
