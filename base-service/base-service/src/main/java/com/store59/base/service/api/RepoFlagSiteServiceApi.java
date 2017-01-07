/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service.api;

import com.store59.base.common.api.RepoFlagSiteApi;
import com.store59.base.common.model.RepoFlagSite;
import com.store59.base.service.RepoFlagSiteService;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * repoFlagSite RPC服务实现层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/17
 * @since 1.0
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = RepoFlagSiteApi.class, exportPath = "/repoflagsite")
public class RepoFlagSiteServiceApi implements RepoFlagSiteApi {

    @Autowired
    private RepoFlagSiteService repoFlagSiteService;

    @Override
    public Result<List<RepoFlagSite>> findBySiteId(int siteId) {
        return ResultHelper.genResultWithSuccess(repoFlagSiteService.findBySiteId(siteId));
    }
}