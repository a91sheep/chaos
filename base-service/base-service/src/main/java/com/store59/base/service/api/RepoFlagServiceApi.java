/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service.api;

import com.store59.base.common.api.RepoFlagApi;
import com.store59.base.common.filter.RepoFlagFilter;
import com.store59.base.common.model.RepoFlag;
import com.store59.base.service.RepoFlagService;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * RepoFlag RPC服务实现层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = RepoFlagApi.class, exportPath = "/repoflag")
public class RepoFlagServiceApi implements RepoFlagApi {

    @Autowired
    private RepoFlagService repoFlagService;

    @Override
    public Result<List<RepoFlag>> findByFlagIds(RepoFlagFilter RepoFlagFilter) {
        return ResultHelper.genResultWithSuccess(repoFlagService.findByFlagIds(RepoFlagFilter));
    }
}