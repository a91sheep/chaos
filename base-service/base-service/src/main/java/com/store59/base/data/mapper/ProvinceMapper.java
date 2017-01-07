package com.store59.base.data.mapper;

import java.util.List;

import com.store59.base.common.model.Province;

public interface ProvinceMapper {
    List<Province> getProvinceList();
    
    Province getProvince(Integer provinceId);
}
