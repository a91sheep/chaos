package com.store59.base.service;

import com.store59.base.common.api.SiteApi;
import com.store59.base.common.model.Site;
import com.store59.base.common.model.SiteView;
import com.store59.base.data.dao.SiteDao;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = SiteApi.class, exportPath = "/site")
public class SiteService implements SiteApi {
    @Autowired
    private SiteDao siteDao;

    public Result<Site> getSite(Integer siteId) {
        return ResultHelper.genResultWithSuccess(siteDao.getSite(siteId));
    }

    public Result<List<Site>> getSiteByZoneId(Integer zoneId) {
        return ResultHelper.genResultWithSuccess(siteDao.getSiteByZoneId(zoneId));
    }

    public Result<List<Site>> searchSiteList(String name){
        return ResultHelper.genResultWithSuccess(siteDao.searchSiteList(name));
    }

    @Override
    public Result<List<SiteView>> locateSiteList(double longitude, double latitude, int limit) {
        List<SiteView> srcList = siteDao.getByPosition(longitude, latitude);
        Collections.sort(srcList,
                        (s1, s2) -> calDistance(longitude, latitude, s1.longitude,
                                s1.latitude).compareTo(
                                calDistance(longitude, latitude, s2.longitude,
                                        s2.latitude)));

        if (limit > 0 && limit < srcList.size()) {
                List<SiteView> dstList = new ArrayList<>(limit);
                for (int i = 0; i < limit; i++) {
                    dstList.add(srcList.get(i));
                }
                return ResultHelper.genResultWithSuccess(dstList);
            }
        return ResultHelper.genResultWithSuccess(srcList);
    }

    /**
     * 经纬度 距离计算
     *
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    private Double calDistance(double longitude1, double latitude1,
                               double longitude2, double latitude2) {
        return Math.sqrt(Math.pow(longitude1 - longitude2, 2)
                + Math.pow(latitude1 - latitude2, 2));
    }

}
