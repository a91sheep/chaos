/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.data.dao;

import com.store59.base.common.filter.ExtractTransactionFilter;
import com.store59.base.common.model.ExtractTransactionRecord;
import com.store59.base.data.mapper.ExtractTransactionRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author <a href="mailto:jiangzq@59store.com">刑天</a>
 * @version 1.0 16/4/27
 * @since 1.0
 */
@Repository
public class ExtractTransactionRecordDao {
    @Autowired
    public ExtractTransactionRecordMapper masterExtractTransactionRecordMapper;
    @Autowired
    public ExtractTransactionRecordMapper slaveExtractTransactionRecordMapper;

    public int insert(ExtractTransactionRecord record){
        return masterExtractTransactionRecordMapper.insert(record);
    }

    public int update(ExtractTransactionRecord record){
        return masterExtractTransactionRecordMapper.update(record);
    }

    public List<ExtractTransactionFilter> findByFilter(ExtractTransactionFilter filter) {
        return slaveExtractTransactionRecordMapper.findByFilter(filter);
    }
}
