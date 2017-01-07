package com.store59.base.service;

import java.util.List;

import com.store59.base.common.model.CityStatisticsInfo;
import com.store59.kylin.common.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;

import com.store59.base.common.api.CityApi;
import com.store59.base.common.model.City;
import com.store59.base.data.dao.CityDao;
import com.store59.kylin.common.model.Result;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = CityApi.class, exportPath = "/city")
public class CityService implements CityApi {
    @Autowired
    private CityDao cityDao;

    public Result<City> getCity(Integer cityId) {
        return ResultHelper.genResultWithSuccess(cityDao.getCity(cityId));
    }

    public Result<List<City>> getCityList(Integer provinceId) {
        return ResultHelper.genResultWithSuccess(cityDao.getCityList(provinceId));
    }

    @Override
    public Result<List<City>> findByName(String name) {
        return ResultHelper.genResultWithSuccess(cityDao.findByName(name.trim()));
    }

    @Override
    public Result<List<CityStatisticsInfo>> statisticsCityDormentryCountList(Byte status){
        return ResultHelper.genResultWithSuccess(cityDao.statisticsCityDormentryCountList(status));
    }

}
