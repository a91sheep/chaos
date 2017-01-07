/**
 * 
 */
package com.store59.printapi.model.result;

import java.util.List;

import com.store59.dorm.common.model.DormEntryShop;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月15日
 * @since 1.1
 */
public class DormentryShopInfo {
    private List<DormEntryShop> contents;

    public List<DormEntryShop> getContents() {
        return contents;
    }

    public void setContents(List<DormEntryShop> contents) {
        this.contents = contents;
    }
}
