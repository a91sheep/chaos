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
public class ZoneInfo {
    private String         zoneName;
    private List<SiteInfo> sites;

    /**
     * @return the zoneName
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * @param zoneName the zoneName to set
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    /**
     * @return the sites
     */
    public List<SiteInfo> getSites() {
        return sites;
    }

    /**
     * @param sites the sites to set
     */
    public void setSites(List<SiteInfo> sites) {
        this.sites = sites;
    }
}
