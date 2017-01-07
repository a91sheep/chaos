package com.store59.print.service;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.print.common.model.PrintOrderRecord;
import com.store59.print.common.remoting.PrintOrderRecordService;
import com.store59.print.dao.PrintOrderRecordDao;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/20
 * @since 1.0
 */
@RemoteService(serviceInterface = PrintOrderRecordService.class, serviceType = ServiceType.HESSIAN, exportPath = "/printorderrecord")
public class PrintOrderRecordServiceImpl implements PrintOrderRecordService {
    @Autowired
    private PrintOrderRecordDao printOrderRecordDao;

    @Override
    public Result<PrintOrderRecord> insert(PrintOrderRecord printOrderRecord) {
        return ResultHelper.genResultWithSuccess(printOrderRecordDao.insert(printOrderRecord) == 1 ? printOrderRecord : null);
    }

    @Override
    public Result<List<String>> getUncheckOrderIds() {
        return ResultHelper.genResultWithSuccess(printOrderRecordDao.getUncheckOrderIds());
    }

    @Override
    public Result<Boolean> updateRecordStatus(List<String> orderIdList) {
        return ResultHelper.genResultWithSuccess(printOrderRecordDao.updateRecordStatus(orderIdList) == 1);
    }
}
