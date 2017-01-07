package com.store59.base.common.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 楼栋
 * 
 * @author heqingpan
 *
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class Dormentry implements java.io.Serializable {
    private Integer dormentryId;

    private Integer dormId;

    private Integer drinkDormId;

    private Byte drinkStatus;

    private Integer siteId;

    private Integer sid;

    private Byte status;

    private Integer sort;

    private String address;

    private String address1;

    private String address2;

    private String address3;

    private String notice;

    private Integer dormentryType;

    private Integer dormentryFloors;

    private Integer roomNumber;

    private Integer studentNumber;

    private Float longitude;

    private Float latitude;

    private Integer maxReward;

    public Integer getDormentryId() {
        return dormentryId;
    }

    public void setDormentryId(Integer dormentryId) {
        this.dormentryId = dormentryId;
    }

    public Integer getDormId() {
        return dormId;
    }

    public void setDormId(Integer dormId) {
        this.dormId = dormId;
    }

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1 == null ? null : address1.trim();
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2 == null ? null : address2.trim();
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
    }

    public Integer getDormentryType() {
        return dormentryType;
    }

    public void setDormentryType(Integer dormentryType) {
        this.dormentryType = dormentryType;
    }

    public Integer getDormentryFloors() {
        return dormentryFloors;
    }

    public void setDormentryFloors(Integer dormentryFloors) {
        this.dormentryFloors = dormentryFloors;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Integer getMaxReward() {
        return maxReward;
    }

    public void setMaxReward(Integer maxReward) {
        this.maxReward = maxReward;
    }

    public Integer getDrinkDormId() {
        return drinkDormId;
    }

    public void setDrinkDormId(Integer drinkDormId) {
        this.drinkDormId = drinkDormId;
    }

    public Byte getDrinkStatus() {
        return drinkStatus;
    }

    public void setDrinkStatus(Byte drinkStatus) {
        this.drinkStatus = drinkStatus;
    }
}

