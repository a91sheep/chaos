package com.store59.print.service;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.print.common.model.PrintIdRelation;
import com.store59.print.common.remoting.PrintIdRelationService;
import com.store59.print.dao.PrintIdRelationDao;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/8/2
 * @since 1.0
 */
@RemoteService(serviceInterface = PrintIdRelationService.class, serviceType = ServiceType.HESSIAN, exportPath = "/printidrelation")
public class PrintIdRelationServiceImpl implements PrintIdRelationService {
    @Autowired
    private PrintIdRelationDao printIdRelationDao;

    @Override
    public Result<PrintIdRelation> insert(PrintIdRelation printIdRelation) {
        return ResultHelper.genResultWithSuccess(printIdRelationDao.insert(printIdRelation) == 1 ? printIdRelation : null);
    }
}
