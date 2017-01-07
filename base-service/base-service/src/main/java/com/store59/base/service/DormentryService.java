package com.store59.base.service;

import java.util.List;

import com.store59.kylin.common.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;

import com.store59.base.common.api.DormentryApi;
import com.store59.base.common.filter.DormentryFloorFilter;
import com.store59.base.common.model.Dormentry;
import com.store59.base.data.dao.DormentryDao;
import com.store59.kylin.common.model.Result;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = DormentryApi.class, exportPath = "/dormentry")
public class DormentryService implements DormentryApi {
    @Autowired
    private DormentryDao dormentryDao;

    public Result<List<Dormentry>> getDormentryListBySiteId(int siteId) {
        return ResultHelper.genResultWithSuccess(dormentryDao.getDormentryListBySiteId(siteId));
    }

    public Result<Dormentry> getDormentry(Integer dormentryId) {
        return ResultHelper.genResultWithSuccess(dormentryDao.getDormentry(dormentryId));
    }

    public Result<List<Dormentry>> getFloorListByFilter(
            DormentryFloorFilter filter) {
        return ResultHelper.genResultWithSuccess(dormentryDao.getFloorListByFilter(filter));
    }

}
