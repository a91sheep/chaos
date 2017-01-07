/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.yunyin.print.main.api.controller.common.location;

import com.yunyin.base.common.dto.Result;
import com.yunyin.base.common.model.City;
import com.yunyin.base.common.model.Province;
import com.yunyin.base.common.model.Site;
import com.yunyin.base.common.model.Zone;
import com.yunyin.print.main.api.service.common.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author <a href="mailto:guopf@59store.com">任之</a>
 * @version 1.0 2016/12/21
 * @since 1.0
 */
@RestController
@RequestMapping("/print/location/*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @RequestMapping(method = RequestMethod.GET, value = "/province")
    public Result<List<Province>> getProvince() {
        return locationService.getProvince();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/city")
    public Result<List<City>> getCityByProvinceId(@RequestParam("provinceId") Long provinceId) {
        return locationService.getCityByProvinceId(provinceId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/zone")
    public Result<List<Zone>> getZoneByCityId(@RequestParam("cityId") Long cityId) {
        return locationService.getZoneByCityId(cityId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/site")
    public Result<List<Site>> getSiteByZoneId(@RequestParam("zoneId") Long zoneId) {
        return locationService.getSiteByZoneId(zoneId);
    }
}
