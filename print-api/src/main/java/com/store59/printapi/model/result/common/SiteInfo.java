/**
 * 
 */
package com.store59.printapi.model.result.common;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月19日
 * @since 1.1
 */
public class SiteInfo {
    private Integer siteId;
    private String  siteName;

    /**
     * @return the siteId
     */
    public Integer getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @param siteName the siteName to set
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
