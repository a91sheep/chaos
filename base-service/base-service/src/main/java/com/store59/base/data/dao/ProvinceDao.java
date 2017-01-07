package com.store59.base.data.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.store59.base.common.model.Province;
import com.store59.base.data.mapper.ProvinceMapper;

@Repository
public class ProvinceDao {
    @Autowired
    private ProvinceMapper masterProvinceMapper;
    @Autowired
    private ProvinceMapper slaveProvinceMapper;

//    @Cacheable(value = "province", key = "allpro")
    public List<Province> getProvinceList() {
        return slaveProvinceMapper.getProvinceList();
    }
    
    @Cacheable(value = "province", key = "'provinceId_'+#provinceId")
    public Province getProvince(Integer provinceId){
        return slaveProvinceMapper.getProvince(provinceId);
    }

}