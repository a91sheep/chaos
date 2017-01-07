/**
 * 
 */
package com.store59.printapi.model.trans;

import java.util.ArrayList;
import java.util.List;

import com.store59.base.common.model.City;
import com.store59.base.common.model.Dormentry;
import com.store59.base.common.model.Province;
import com.store59.base.common.model.Site;
import com.store59.base.common.model.Zone;
import com.store59.dorm.common.model.DormEntryShop;
import com.store59.dorm.common.model.DormShop;
import com.store59.printapi.model.result.DormentrySelect;
import com.store59.printapi.model.result.DormentryShopInfo;
import com.store59.printapi.model.result.SelectBox;
import com.store59.printapi.model.result.SelectBoxInfo;
import com.store59.printapi.model.result.common.ShopInfo;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月14日
 * @since 1.1
 */
public class ResultTrans {
    public static SelectBox transProvinceSelectBox(List<Province> source) {
        SelectBox target = new SelectBox();
        if (source == null) {
            return target;
        }
        List<SelectBoxInfo> targetList = new ArrayList<>(source.size());
        for (Province sourceItem : source) {
            SelectBoxInfo targetItem = new SelectBoxInfo();
            targetItem.setKey(sourceItem.getProvinceId().toString());
            targetItem.setValue(sourceItem.getName());
            targetList.add(targetItem);
        }
        target.setContents(targetList);
        return target;
    }

    public static SelectBox transCitySelectBox(List<City> source) {
        SelectBox target = new SelectBox();
        if (source == null) {
            return target;
        }
        List<SelectBoxInfo> targetList = new ArrayList<>(source.size());
        for (City sourceItem : source) {
            SelectBoxInfo targetItem = new SelectBoxInfo();
            targetItem.setKey(sourceItem.getCityId().toString());
            targetItem.setValue(sourceItem.getName());
            targetList.add(targetItem);
        }
        target.setContents(targetList);
        return target;
    }

    public static SelectBox transZoneSelectBox(List<Zone> source) {
        SelectBox target = new SelectBox();
        if (source == null) {
            return target;
        }
        List<SelectBoxInfo> targetList = new ArrayList<>(source.size());
        for (Zone sourceItem : source) {
            SelectBoxInfo targetItem = new SelectBoxInfo();
            targetItem.setKey(sourceItem.getZoneId().toString());
            targetItem.setValue(sourceItem.getName());
            targetList.add(targetItem);
        }
        target.setContents(targetList);
        return target;
    }

    public static SelectBox transSiteSelectBox(List<Site> source) {
        SelectBox target = new SelectBox();
        if (source == null) {
            return target;
        }
        List<SelectBoxInfo> targetList = new ArrayList<>(source.size());
        for (Site sourceItem : source) {
            SelectBoxInfo targetItem = new SelectBoxInfo();
            targetItem.setKey(sourceItem.getSiteId().toString());
            targetItem.setValue(sourceItem.getSiteName());
            targetList.add(targetItem);
        }
        target.setContents(targetList);
        return target;
    }

    public static DormentrySelect transDormentryList(List<Dormentry> source) {
        DormentrySelect target = new DormentrySelect();
        if (source == null) {
            return target;
        }
        List<Dormentry> targetList = new ArrayList<>(source.size());
        for (Dormentry sourceItem : source) {
            Dormentry targetItem = new Dormentry();
            targetItem.setDormentryId(sourceItem.getDormentryId());
            targetItem.setSiteId(sourceItem.getSiteId());
            targetItem.setStatus(sourceItem.getStatus());
            targetItem.setAddress1(sourceItem.getAddress1());
            targetItem.setAddress2(sourceItem.getAddress2());
            targetList.add(targetItem);
        }
        target.setContents(targetList);
        return target;
    }

    public static Dormentry transDormentry(Dormentry source) {
        Dormentry target = new Dormentry();
        if (source == null) {
            return target;
        }

        Dormentry targetItem = new Dormentry();
        targetItem.setDormentryId(source.getDormentryId());
        targetItem.setSiteId(source.getSiteId());
        targetItem.setStatus(source.getStatus());
        targetItem.setAddress1(source.getAddress1());
        targetItem.setAddress2(source.getAddress2());

        return targetItem;
    }

    public static DormentryShopInfo transDormentryShopList(List<DormEntryShop> source) {
        DormentryShopInfo target = new DormentryShopInfo();
        if (source == null) {
            return target;
        }
        List<DormEntryShop> targetList = new ArrayList<>(source.size());
        for (DormEntryShop sourceItem : source) {
            DormEntryShop targetItem = new DormEntryShop();
            targetItem.setDormentryId(sourceItem.getDormentryId());
            targetItem.setShopId(sourceItem.getShopId());
            targetList.add(targetItem);
        }
        target.setContents(targetList);
        return target;
    }

    public static ShopInfo transDormShopList(List<DormShop> source) {
        ShopInfo target = new ShopInfo();
        if (source == null) {
            return target;
        }
        List<DormShop> targetList = new ArrayList<>(source.size());
        for (DormShop sourceItem : source) {
            DormShop targetItem = new DormShop();
            targetItem.setName(sourceItem.getName());
            targetItem.setDormShopDeliveries(sourceItem.getDormShopDeliveries());
            targetItem.setShopId(sourceItem.getShopId());
            targetList.add(targetItem);
        }
//        target.setContents(targetList);
        return target;
    }

    public static DormShop transDormShop(DormShop source) {
        DormShop target = new DormShop();
        if (source == null) {
            return target;
        }

        DormShop targetItem = new DormShop();
        targetItem.setShopId(source.getShopId());
        targetItem.setStatus(source.getStatus());
        targetItem.setName(source.getName());
        targetItem.setLogo(source.getLogo());
        targetItem.setNotice(source.getNotice());
        targetItem.setFreeshipAmount(source.getFreeshipAmount());
        targetItem.setShipfee(source.getShipfee());
        targetItem.setDormShopPrices(source.getDormShopPrices());
        targetItem.setShopId(source.getShopId());
        targetItem.setStatus(source.getStatus());
        targetItem.setDormShopDeliveries(source.getDormShopDeliveries());

        return targetItem;
    }
}
