/**
 * 
 */
package com.store59.printapi.model.result.common;

import java.util.List;


/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月15日
 * @since 1.1
 */
public class ApartInfo {
    
    private String apartName;
    
    private List<DormentryInfo> dormentryList;

    public String getApartName() {
        return apartName;
    }

    public void setApartName(String apartName) {
        this.apartName = apartName;
    }

    public List<DormentryInfo> getDormentryList() {
        return dormentryList;
    }

    public void setDormentryList(List<DormentryInfo> dormentryList) {
        this.dormentryList = dormentryList;
    }
}
