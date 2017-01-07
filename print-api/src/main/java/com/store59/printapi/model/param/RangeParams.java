/**
 * 
 */
package com.store59.printapi.model.param;

import org.hibernate.validator.constraints.NotBlank;

import com.store59.printapi.common.constant.MessageConstant;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月15日
 * @since 1.1
 */
public class RangeParams {
    @NotBlank(message = MessageConstant.Province_Id)
    private String provinceId;
    @NotBlank(message = MessageConstant.City_Id)
    private String cityId;
    @NotBlank(message = MessageConstant.Zone_Id)
    private String zoneId;
    @NotBlank(message = MessageConstant.Site_Id)
    private String siteId;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
