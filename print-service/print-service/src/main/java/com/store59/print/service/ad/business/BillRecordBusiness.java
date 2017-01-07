package com.store59.print.service.ad.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store59.print.common.filter.BillRecordFilter;
import com.store59.print.common.model.ad.BillRecord;
import com.store59.print.dao.ad.BillRecordDao;

/**
 * Created on 2016-08-17.
 */
@Service
public class BillRecordBusiness {
    @Autowired
    private BillRecordDao billRecordDao;

    public BillRecord findBillRecord(Long id){
        return billRecordDao.findBillRecord(id);
    }
    
    public List<BillRecord> findBillRecordList(BillRecordFilter filter){
        return billRecordDao.findBillRecordList(filter);
    }

    public BillRecord addBillRecord(BillRecord record){
        return billRecordDao.addBillRecord(record);
    }

    public Boolean updateBillRecord(BillRecord record){
        return billRecordDao.updateBillRecord(record);
    }

    public Boolean deleteBillRecord(Long id){
        return billRecordDao.deleteBillRecord(id);
    }
}

