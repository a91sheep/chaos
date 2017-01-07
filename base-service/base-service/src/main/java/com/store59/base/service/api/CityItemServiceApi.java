/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service.api;

import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import com.store59.base.service.CityItemService;

import com.store59.kylin.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.store59.base.common.api.CityItemApi;
import com.store59.base.common.model.CityItem;

import java.util.List;

/**
 * CityItem RPC服务实现层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/13
 * @since 1.0
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = CityItemApi.class, exportPath = "/cityitem")
public class CityItemServiceApi implements CityItemApi {

    @Autowired
    private CityItemService cityItemService;

    @Override
    public Result<List<CityItem>> findByCityId(Integer cityId){
        return ResultHelper.genResultWithSuccess(cityItemService.findByCityId(cityId));
    }

    @Override
    public Result<List<CityItem>> findByCityIdAndRid(Integer cityId, Integer rid) {
        return ResultHelper.genResultWithSuccess(cityItemService.findByCityIdAndRid(cityId, rid));
    }

}