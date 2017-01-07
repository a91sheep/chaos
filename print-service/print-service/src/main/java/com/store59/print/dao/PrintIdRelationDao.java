package com.store59.print.dao;

import com.store59.print.common.model.PrintIdRelation;
import com.store59.print.dao.mapper.PrintIdRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/8/2
 * @since 1.0
 */
@Repository
public class PrintIdRelationDao {
    @Autowired
    private PrintIdRelationMapper masterPrintIdRelationMapper;

    @Autowired
    private PrintIdRelationMapper slavePrintIdRelationMapper;

    public int insert(PrintIdRelation printIdRelation) {
        return masterPrintIdRelationMapper.insert(printIdRelation);
    }
}
