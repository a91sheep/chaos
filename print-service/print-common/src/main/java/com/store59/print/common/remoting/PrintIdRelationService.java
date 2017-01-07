package com.store59.print.common.remoting;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.model.PrintIdRelation;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/8/2
 * @since 1.0
 */
public interface PrintIdRelationService {

    Result<PrintIdRelation> insert(PrintIdRelation printIdRelation);
}
