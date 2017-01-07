package com.store59.base.common.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * 学校
 * 
 * @author heqingpan
 *
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class Site implements Serializable {
    private Integer siteId;

    private Integer sid;

    private Integer dhid;

    private Integer zoneId;

    private Byte status;

    private Double freeshipAmount;

    private Double shipfee;

    private String siteName;

    private Integer serviceTimeStart;

    private Integer serviceTimeEnd;

    private Byte deliveryZone;

    private Byte deliveryPolicy;

    private String deliveryArea;

    private String statusRemark;

    private Byte dormOnly;

    private Integer dormDeliveryTime;

    private String dormDeliveryAddress;

    private Double longitude;

    private Double latitude;

    private String coordinate;

    private String keywords;

    private String address;

    private Integer guessTotalStudent;

    private Integer guessTotalEntry;

    private Integer guessTotalShop;

    private Integer stationId;
    
    private Byte localDormhouse;

    private String shortName;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getDhid() {
        return dhid;
    }

    public void setDhid(Integer dhid) {
        this.dhid = dhid;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Double getFreeshipAmount() {
        return freeshipAmount;
    }

    public void setFreeshipAmount(Double freeshipAmount) {
        this.freeshipAmount = freeshipAmount;
    }

    public Double getShipfee() {
        return shipfee;
    }

    public void setShipfee(Double shipfee) {
        this.shipfee = shipfee;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName == null ? null : siteName.trim();
    }

    public Integer getServiceTimeStart() {
        return serviceTimeStart;
    }

    public void setServiceTimeStart(Integer serviceTimeStart) {
        this.serviceTimeStart = serviceTimeStart;
    }

    public Integer getServiceTimeEnd() {
        return serviceTimeEnd;
    }

    public void setServiceTimeEnd(Integer serviceTimeEnd) {
        this.serviceTimeEnd = serviceTimeEnd;
    }

    public Byte getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(Byte deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    public Byte getDeliveryPolicy() {
        return deliveryPolicy;
    }

    public void setDeliveryPolicy(Byte deliveryPolicy) {
        this.deliveryPolicy = deliveryPolicy;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea == null ? null : deliveryArea.trim();
    }

    public String getStatusRemark() {
        return statusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark == null ? null : statusRemark.trim();
    }

    public Byte getDormOnly() {
        return dormOnly;
    }

    public void setDormOnly(Byte dormOnly) {
        this.dormOnly = dormOnly;
    }

    public Integer getDormDeliveryTime() {
        return dormDeliveryTime;
    }

    public void setDormDeliveryTime(Integer dormDeliveryTime) {
        this.dormDeliveryTime = dormDeliveryTime;
    }

    public String getDormDeliveryAddress() {
        return dormDeliveryAddress;
    }

    public void setDormDeliveryAddress(String dormDeliveryAddress) {
        this.dormDeliveryAddress = dormDeliveryAddress == null ? null : dormDeliveryAddress.trim();
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate == null ? null : coordinate.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getGuessTotalStudent() {
        return guessTotalStudent;
    }

    public void setGuessTotalStudent(Integer guessTotalStudent) {
        this.guessTotalStudent = guessTotalStudent;
    }

    public Integer getGuessTotalEntry() {
        return guessTotalEntry;
    }

    public void setGuessTotalEntry(Integer guessTotalEntry) {
        this.guessTotalEntry = guessTotalEntry;
    }

    public Integer getGuessTotalShop() {
        return guessTotalShop;
    }

    public void setGuessTotalShop(Integer guessTotalShop) {
        this.guessTotalShop = guessTotalShop;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Byte getLocalDormhouse() {
        return localDormhouse;
    }

    public void setLocalDormhouse(Byte localDormhouse) {
        this.localDormhouse = localDormhouse;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}

