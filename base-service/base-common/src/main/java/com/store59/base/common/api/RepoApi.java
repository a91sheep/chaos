package com.store59.base.common.api;

import com.store59.base.common.filter.RepoFilter;
import com.store59.base.common.model.Repo;
import com.store59.base.common.model.RepoImage;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * Created by shanren on 15/7/24.
 */
public interface RepoApi {

    Result<Repo> findRepoByRid(int rid);

    Result<List<Repo>> findRepoListAll();

    Result<List<Repo>> findRepoListByIds(List<Integer> ridList);

    Result<List<RepoImage>> getRepoImageListByRidList(List<Integer> ridList);

    /**
     * 组合条件查询商品列表
     *
     * @param filter 组合条件对象
     * @return
     */
    Result<List<Repo>> findByFilter(RepoFilter filter);
}
