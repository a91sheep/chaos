/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.service;

import com.store59.base.common.api.SchoolApi;
import com.store59.base.common.model.School;
import com.store59.base.data.dao.SchoolDao;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * @author <a href="mailto:baixf@59store.com">白白</a>
 * @version 1.0 16/5/9
 * @since 1.0
 */
@RemoteService(serviceType= ServiceType.HESSIAN, serviceInterface = SchoolApi.class, exportPath = "/school")
public class SchoolService implements SchoolApi {
    @Autowired
    private SchoolDao schoolDao;

    @Override
    public Result<School> getSchool(Integer id){
        if(id == null) {
            return ResultHelper.genResult(4, "参数不正确");
        }
        return ResultHelper.genResultWithSuccess(schoolDao.getSchool(id));
    }

    @Override
    public Result<List<School>> findByName(String name){
        if(name == null) {
            return ResultHelper.genResult(4, "参数不正确");
        }
        return ResultHelper.genResultWithSuccess(schoolDao.findByNmae(name));
    }

    @Override
    public Result insert(School school) {
        if(school == null || school.getName() == null) {
            return ResultHelper.genResult(4, "参数不正确");
        }
        if(getCountByName(school.getName()) > 0){
            return ResultHelper.genResult(4, "参数不正确");
        }
        schoolDao.insert(school);
        return ResultHelper.genResultWithSuccess();
    }

    @Override
    public Result update(School school) {
        if(school == null || school.getId() == null || school.getName() == null) {
            return ResultHelper.genResult(4, "参数不正确");
        }
        School oldSchool = schoolDao.getSchool(school.getId());
        if(!oldSchool.getName().equals(school.getName()) && getCountByName(school.getName()) > 0) {
            return ResultHelper.genResult(4, "参数不正确");
        }
        schoolDao.update(school);
        return ResultHelper.genResultWithSuccess();
    }

    @Override
    public Result delete(Integer id) {
        if(id == null) {
            return ResultHelper.genResult(4, "参数不正确");
        }
        schoolDao.delete(id);
        return ResultHelper.genResultWithSuccess();
    }

    private Integer getCountByName(String name) {
        return schoolDao.getCountByName(name);
    }
}
