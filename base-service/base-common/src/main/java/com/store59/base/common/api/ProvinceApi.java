package com.store59.base.common.api;

import java.util.List;

import com.store59.base.common.model.Province;
import com.store59.kylin.common.model.Result;

/**
 * 省份接口
 * 
 * @author heqingpan
 *
 */
public interface ProvinceApi {
    /**
     * 获取所以省份列表
     * 
     * @return
     */
    Result<List<Province>> getProvinceList();

    /**
     * 获取省份信息
     * 
     * @param provinceId
     * @return
     */
    Result<Province> getProvince(Integer provinceId);
}
