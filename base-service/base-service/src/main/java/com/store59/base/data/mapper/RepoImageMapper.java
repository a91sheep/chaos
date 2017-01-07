package com.store59.base.data.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.store59.base.common.model.RepoImage;

public interface RepoImageMapper {
    RepoImage selectByPrimaryKey(Integer itemId);

    List<RepoImage> getRepoImageListByRid(Integer rid);

    List<RepoImage> getRepoImageListByRidList(@Param("ridList")List<Integer> ridList);
}
