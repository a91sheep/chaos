/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service.api;

import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import com.store59.base.service.RepoFlagRelationService;

import com.store59.kylin.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.store59.base.common.api.RepoFlagRelationApi;
import com.store59.base.common.model.RepoFlagRelation;

import java.util.List;

/**
 * RepoFlagRelation RPC服务实现层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/18
 * @since 1.0
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = RepoFlagRelationApi.class, exportPath = "/repoflagrelation")
public class RepoFlagRelationServiceApi implements RepoFlagRelationApi {

    @Autowired
    private RepoFlagRelationService repoFlagRelationService;

    @Override
    public Result<List<RepoFlagRelation>> findByObjectIdAndType(int objectId, int type) {
        return ResultHelper.genResultWithSuccess(repoFlagRelationService.findByObjectIdAndType(objectId, type));
    }
}