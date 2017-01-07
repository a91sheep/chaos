package com.store59.base.service;

import com.store59.base.common.api.AreaJoinApi;
import com.store59.base.common.model.AreaJoin;
import com.store59.base.data.dao.AreaJoinDao;
import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by solder on 15/9/10.
 */

@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = AreaJoinApi.class, exportPath = "/areajoin")
public class AreaJoinService implements AreaJoinApi{

    @Autowired
    private AreaJoinDao areaJoinDao;

    public Result getBySiteId(int siteId){
        return ResultHelper.genResultWithSuccess(areaJoinDao.getBySiteId(siteId));
    }

    @Override
    public Result<List<AreaJoin>> findBySiteIds(List<Integer> siteIds) {
        if(CollectionUtils.isEmpty(siteIds))
            throw new ServiceException(8, "集合参数为空");

        return ResultHelper.genResultWithSuccess(areaJoinDao.findBySiteIds(siteIds));
    }
}
