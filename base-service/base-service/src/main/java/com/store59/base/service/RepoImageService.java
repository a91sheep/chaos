package com.store59.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store59.base.common.model.RepoImage;
import com.store59.base.data.dao.RepoImageDao;

@Service
public class RepoImageService {
    @Autowired
    private RepoImageDao repoImageDao;

    public List<RepoImage> getRepoImageListByRid(Integer rid) {
        return repoImageDao.getRepoImageListByRid(rid);
    }

    public List<RepoImage> getRepoImageListByRidList(List<Integer> ridList) {
        return repoImageDao.getRepoImageListByRidList(ridList);
    }

}
