package com.store59.print.dao.mapper;

import com.store59.print.common.model.PrintOrderRecord;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/20
 * @since 1.0
 */
public interface PrintOrderRecordMapper {

    int insert(PrintOrderRecord printOrderRecord);

    List<String> getUncheckOrderIds();

    int updateRecordStatus(List<String> orderIdList);
}
