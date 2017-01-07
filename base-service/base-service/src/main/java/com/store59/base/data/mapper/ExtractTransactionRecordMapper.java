package com.store59.base.data.mapper;

import com.store59.base.common.filter.ExtractTransactionFilter;
import com.store59.base.common.model.ExtractTransactionRecord;

import java.util.List;

public interface ExtractTransactionRecordMapper {

    int insert(ExtractTransactionRecord extractTransactionRecord);

    int update(ExtractTransactionRecord record);

    List<ExtractTransactionFilter> findByFilter(ExtractTransactionFilter filter);
}