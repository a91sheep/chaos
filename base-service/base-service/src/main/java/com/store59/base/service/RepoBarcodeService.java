/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.base.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.store59.base.data.dao.RepoBarcodeDao;
import com.store59.base.common.model.RepoBarcode;

import java.util.List;

/**
 * RepoBarcode 服务层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 15/11/10
 * @since 1.0
 */
@Service
public class RepoBarcodeService {

    @Autowired
    private RepoBarcodeDao repoBarcodeDao;

    public RepoBarcode getByBarcode(String barcode){
        return repoBarcodeDao.getByBarcode(barcode);
    }

    public int insert(RepoBarcode repoBarcode){
        return repoBarcodeDao.insert(repoBarcode);
    }

    public List<RepoBarcode> findByRids(List<Integer> rids) {
        return repoBarcodeDao.findByRids(rids);
    }
}