package com.store59.base.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 城市
 * 
 * @author heqingpan
 *
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class City implements Serializable {
    private Integer cityId;

    private Integer provinceId;

    private String name;

    private String spellAll;

    private String spellShort;

    private String spellFirst;

    private Boolean isHot;

    private Short sort;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSpellAll() {
        return spellAll;
    }

    public void setSpellAll(String spellAll) {
        this.spellAll = spellAll == null ? null : spellAll.trim();
    }

    public String getSpellShort() {
        return spellShort;
    }

    public void setSpellShort(String spellShort) {
        this.spellShort = spellShort == null ? null : spellShort.trim();
    }

    public String getSpellFirst() {
        return spellFirst;
    }

    public void setSpellFirst(String spellFirst) {
        this.spellFirst = spellFirst == null ? null : spellFirst.trim();
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Short getSort() {
        return sort;
    }

    public void setSort(Short sort) {
        this.sort = sort;
    }

}
