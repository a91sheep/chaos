package com.store59.print.dao;

import com.store59.print.common.model.PrintOrderRecord;
import com.store59.print.dao.mapper.PrintOrderRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/20
 * @since 1.0
 */
@Repository
public class PrintOrderRecordDao {

    @Autowired
    private PrintOrderRecordMapper masterPrintOrderRecordMapper;
    @Autowired
    private PrintOrderRecordMapper slavePrintOrderRecordMapper;

    public int insert(PrintOrderRecord printOrderRecord) {
        return masterPrintOrderRecordMapper.insert(printOrderRecord);
    }

    public List<String> getUncheckOrderIds() {
        return slavePrintOrderRecordMapper.getUncheckOrderIds();
    }

    public int updateRecordStatus(List<String> orderIdList) {
        return masterPrintOrderRecordMapper.updateRecordStatus(orderIdList);
    }
}
