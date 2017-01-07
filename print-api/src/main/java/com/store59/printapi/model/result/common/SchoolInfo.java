/**
 * 
 */
package com.store59.printapi.model.result.common;

import java.util.List;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月19日
 * @since 1.1
 */
public class SchoolInfo {
    private List<ZoneInfo> schools;

    /**
     * @return the schools
     */
    public List<ZoneInfo> getSchools() {
        return schools;
    }

    /**
     * @param schools the schools to set
     */
    public void setSchools(List<ZoneInfo> schools) {
        this.schools = schools;
    }
}
