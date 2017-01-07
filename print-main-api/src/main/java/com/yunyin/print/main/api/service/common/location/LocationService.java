/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.yunyin.print.main.api.service.common.location;

import com.yunyin.base.common.api.CityApi;
import com.yunyin.base.common.api.ProvinceApi;
import com.yunyin.base.common.api.SiteApi;
import com.yunyin.base.common.api.ZoneApi;
import com.yunyin.base.common.dto.Result;
import com.yunyin.base.common.model.City;
import com.yunyin.base.common.model.Province;
import com.yunyin.base.common.model.Site;
import com.yunyin.base.common.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:guopf@59store.com">任之</a>
 * @version 1.0 2016/12/21
 * @since 1.0
 */
@Service
public class LocationService {

    @Autowired
    private ProvinceApi provinceApi;

    @Autowired
    private CityApi cityApi;

    @Autowired
    private ZoneApi zoneApi;

    @Autowired
    private SiteApi siteApi;

    public Result<List<Province>> getProvince() {
        return provinceApi.getProvince();
    }

    public Result<List<City>> getCityByProvinceId(Long provinceId) {
        return cityApi.getCityByProvinceId(provinceId);
    }

    public Result<List<Zone>> getZoneByCityId(Long cityId) {
        return zoneApi.getZone(cityId);
    }

    public Result<List<Site>> getSiteByZoneId(Long zoneId) {
        return siteApi.getSite(zoneId);
    }
}
