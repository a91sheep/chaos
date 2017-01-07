package com.store59.base.service.api;

import com.store59.base.common.api.BanWordApi;
import com.store59.base.common.model.BanMatchResult;
import com.store59.base.service.BanWordService;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on 2016-04-22.
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = BanWordApi.class, exportPath = "/banword")
public class BanWordServiceApi implements BanWordApi {
    @Autowired
    private BanWordService banWordService;

    @Override
    public Result<BanMatchResult> matchBan(String content) {
        return ResultHelper.genResultWithSuccess(banWordService.matchBan(content));
    }
}

