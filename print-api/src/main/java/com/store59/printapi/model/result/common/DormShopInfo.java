/**
 * 
 */
package com.store59.printapi.model.result.common;

import com.store59.dorm.common.model.DormShop;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @version 1.1 2016年1月20日
 * @since 1.1
 */
public class DormShopInfo {

    private List<Integer> dormIdList;

    private Map<Integer, DormShop> dormShopMap;

    public List<Integer> getDormIdList() {
        return dormIdList;
    }

    public void setDormIdList(List<Integer> dormIdList) {
        this.dormIdList = dormIdList;
    }

    public Map<Integer, DormShop> getDormShopMap() {
        return dormShopMap;
    }

    public void setDormShopMap(Map<Integer, DormShop> dormShopMap) {
        this.dormShopMap = dormShopMap;
    }
}
