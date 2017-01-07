package com.yunyin.print.common.model;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/14
 * @since 1.0
 */
public class PTerminal {
    private Long   id;
    private Long   provinceId;
    private Long   cityId;
    private Long   zoneId;
    private Long   siteId;
    private String terminalName;    //终端名称
    private String terminalCode;        //终端机型号
    private String terminalMachineCode;     //终端机编码
    private String address;     //终端地址
    private Byte   businessType;  //经营方式 1 、自营  2、加盟
    private Long   businessId;  //经营者id
    private Double longitude;   //经度
    private Double latitude;    //纬度
    private Byte   terminalStatus;    //终端机状态 1、使用中（正常） 2、维修中
    private String faultRemark;     // 故障描述
    private Long   warrantyStart;      //保修期开始时间
    private Long   warrantyEnd;      //保修期结束时间
    private String warrantyRemark;      //保修期备注
    private Byte   isActive;     //是否有效 0-无效 1-有效
    private String createUser;
    private Long   createTime;
    private String updateUser;
    private Long   updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getTerminalMachineCode() {
        return terminalMachineCode;
    }

    public void setTerminalMachineCode(String terminalMachineCode) {
        this.terminalMachineCode = terminalMachineCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Byte getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Byte businessType) {
        this.businessType = businessType;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
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

    public Byte getTerminalStatus() {
        return terminalStatus;
    }

    public void setTerminalStatus(Byte terminalStatus) {
        this.terminalStatus = terminalStatus;
    }

    public String getFaultRemark() {
        return faultRemark;
    }

    public void setFaultRemark(String faultRemark) {
        this.faultRemark = faultRemark;
    }

    public Long getWarrantyStart() {
        return warrantyStart;
    }

    public void setWarrantyStart(Long warrantyStart) {
        this.warrantyStart = warrantyStart;
    }

    public Long getWarrantyEnd() {
        return warrantyEnd;
    }

    public void setWarrantyEnd(Long warrantyEnd) {
        this.warrantyEnd = warrantyEnd;
    }

    public String getWarrantyRemark() {
        return warrantyRemark;
    }

    public void setWarrantyRemark(String warrantyRemark) {
        this.warrantyRemark = warrantyRemark;
    }

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
