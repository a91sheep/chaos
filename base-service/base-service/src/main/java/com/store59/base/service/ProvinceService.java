package com.store59.base.service;

import java.util.List;

import com.store59.kylin.common.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;

import com.store59.base.common.api.ProvinceApi;
import com.store59.base.common.model.Province;
import com.store59.base.data.dao.ProvinceDao;
import com.store59.kylin.common.model.Result;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = ProvinceApi.class, exportPath = "/province")
public class ProvinceService implements ProvinceApi {
    @Autowired
    private ProvinceDao provinceDao;

    public Result<List<Province>> getProvinceList() {
        return ResultHelper.genResultWithSuccess(provinceDao.getProvinceList());
    }

    public Result<Province> getProvince(Integer provinceId) {
        return ResultHelper.genResultWithSuccess(provinceDao.getProvince(provinceId));
    }

}
