/*
 * Copyright 2015 © 59store.com.
 *
 * RepoMapper.java
 *
 */
package com.store59.base.data.mapper;

import com.store59.base.common.filter.RepoFilter;
import com.store59.base.common.model.Repo;
import com.store59.kylin.common.model.Result;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shanren on 15/7/27.
 */
public interface RepoMapper {

    Repo findRepoByRid(int rid);

    List<Repo> findRepoListAll();

    List<Repo> findRepoListByIds(@Param("ridList") List<Integer> ridList);

    List<Repo> findByFilter(RepoFilter filter);
}
