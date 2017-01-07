/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service.api;

import com.store59.base.common.api.RepoBarcodeApi;
import com.store59.base.common.model.RepoBarcode;
import com.store59.base.service.RepoBarcodeService;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * RepoBarcode RPC服务实现层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/10
 * @since 1.0
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = RepoBarcodeApi.class, exportPath = "/repobarcode")
public class RepoBarcodeServiceApi implements RepoBarcodeApi {

    @Autowired
    private RepoBarcodeService repoBarcodeService;

    @Override
    public Result<RepoBarcode> getByBarcode(String barcode){
        return ResultHelper.genResultWithSuccess(repoBarcodeService.getByBarcode(barcode));
    }

    @Override
    public Result<List<RepoBarcode>> findByRids(List<Integer> rids) {
        if(CollectionUtils.isEmpty(rids))
            return ResultHelper.genResult(8, "集合参数为空");

        return ResultHelper.genResultWithSuccess(repoBarcodeService.findByRids(rids));
    }

    @Override
    public Result<RepoBarcode> insert(RepoBarcode repoBarcode){
        return ResultHelper.genResultWithSuccess(repoBarcodeService.insert(repoBarcode) == 1 ? repoBarcode : null);
    }

}