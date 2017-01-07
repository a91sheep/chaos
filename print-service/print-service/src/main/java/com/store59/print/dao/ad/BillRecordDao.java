package com.store59.print.dao.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.filter.BillRecordFilter;
import com.store59.print.common.model.ad.BillRecord;
import com.store59.print.dao.mapper.BillRecordMapper;

/**
 * Created on 2016-08-17.
 */
@Repository
public class BillRecordDao {
    @Autowired
    private BillRecordMapper masterBillRecordMapper;
    @Autowired
    private BillRecordMapper slaveBillRecordMapper;

    public BillRecord findBillRecord(Long id){
        return slaveBillRecordMapper.select(id);
    }
    
    public List<BillRecord> findBillRecordList(BillRecordFilter filter){
        return slaveBillRecordMapper.findListByFilter(filter);
    }

    public BillRecord addBillRecord(BillRecord record){
        return masterBillRecordMapper.insert(record) == 0 ? null : record;
    }

    public Boolean updateBillRecord(BillRecord record){
        return masterBillRecordMapper.update(record) == 0 ? false : true;
    }

    public Boolean deleteBillRecord(Long id){
        return masterBillRecordMapper.delete(id) == 0 ? false : true;
    }
}

