package com.store59.base.service;

import com.store59.base.common.api.ZoneApi;
import com.store59.base.common.model.Site;
import com.store59.base.common.model.Zone;
import com.store59.base.data.dao.SiteDao;
import com.store59.base.data.dao.ZoneDao;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = ZoneApi.class, exportPath = "/zone")
public class ZoneService implements ZoneApi {
    @Autowired
    private ZoneDao zoneDao;

    @Autowired
    private SiteDao siteDao;

    public Result<Zone> getZone(Integer zoneId) {
        return ResultHelper.genResultWithSuccess(zoneDao.getZone(zoneId));
    }

    public Result<List<Zone>> getZoneList(Integer cityId) {
        return ResultHelper.genResultWithSuccess(zoneDao.getZoneList(cityId));
    }

    @Override
    public Result<List<Zone>> findByCityId(Integer cityId, boolean isSite) {
        List<Zone> zones = zoneDao.getZoneList(cityId);

        if(CollectionUtils.isNotEmpty(zones) && isSite) {
            List<Integer> zoneIds = zones.stream().map(Zone::getZoneId).collect(Collectors.toList());
            List<Site> sites = siteDao.findByZoneIds(zoneIds);
            if(CollectionUtils.isNotEmpty(sites)) {
                for(Zone zone : zones) {
                    zone.setSites(isMatch(sites, zone.getZoneId()));
                }
            }
        }

        return ResultHelper.genResultWithSuccess(zones);
    }

    /**
     * 数据筛选
     *
     * @param sites
     * @param zoneId
     * @return
     */
    private List<Site> isMatch(List<Site> sites, Integer zoneId) {
        List<Site> resultSite = new ArrayList<>();
        for(Site site : sites) {
            if(site.getZoneId().intValue() == zoneId.intValue()) {
                resultSite.add(site);
            }
        }
        return resultSite;
    }

}
