/*
 * Copyright 2015 © 59store.com.
 *
 * RepoService.java
 *
 */
package com.store59.base.service;

import com.store59.base.common.api.RepoApi;
import com.store59.base.common.filter.RepoFilter;
import com.store59.base.common.model.Repo;
import com.store59.base.common.model.RepoImage;
import com.store59.base.data.dao.RepoDao;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by shanren on 15/7/27.
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = RepoApi.class, exportPath = "/repo")
public class RepoService implements RepoApi {
    @Autowired
    private RepoDao repoDao;
    @Autowired
    private RepoImageService repoImageService;

    @Override
    public Result<Repo> findRepoByRid(int rid) {
        return ResultHelper.genResultWithSuccess(repoDao.findRepoByRid(rid));
    }

    @Override
    public Result<List<Repo>> findRepoListAll() {
        return ResultHelper.genResultWithSuccess(repoDao.findRepoListAll());
    }

    @Override
    public Result<List<Repo>> findRepoListByIds(List<Integer> ridList) {
        if(CollectionUtils.isEmpty(ridList))
            return ResultHelper.genResult(8, "集合参数为空");

        return ResultHelper.genResultWithSuccess(repoDao.findRepoListByIds(ridList));
    }

    public Result<List<RepoImage>> getRepoImageListByRidList(
            List<Integer> ridList) {
        return ResultHelper.genResultWithSuccess(repoImageService.getRepoImageListByRidList(ridList));
    }

    @Override
    public Result<List<Repo>> findByFilter(RepoFilter filter) {
        return ResultHelper.genResultWithSuccess(repoDao.findByFilter(filter));
    }
}
