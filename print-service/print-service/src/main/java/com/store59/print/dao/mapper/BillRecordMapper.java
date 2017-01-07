package com.store59.print.dao.mapper;

import java.util.List;

import com.store59.print.common.filter.BillRecordFilter;
import com.store59.print.common.model.ad.BillRecord;

/**
 * BillRecord mybatis mapper
 * 
 * Created on 2016-08-17.
 */
public interface BillRecordMapper {

    BillRecord select(Long id);

    int insert(BillRecord record);

    int update(BillRecord record);

    int delete(Long id);

    List<BillRecord> findListByFilter(BillRecordFilter filter);
}
