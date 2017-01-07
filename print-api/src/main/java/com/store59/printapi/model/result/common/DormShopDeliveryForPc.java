package com.store59.printapi.model.result.common;

import com.store59.printapi.model.result.app.ShopTimeInfo;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/28
 * @since 1.0
 */
public class DormShopDeliveryForPc {
    private Integer            id;
    private Integer            shopId;
    private Byte               method;
    private Byte               status;
    private String             content;
    private Double             charge;
    private Integer            thresholdSwitch;
    private Double             threshold;
    private List<Integer>      area;
    private String             address;
    private Integer            autoConfirmSwitch;
    private Integer            autoConfirm;
    private List<ShopTimeInfo> delivery_times;
    private String             pickTimeStr;  //上门自取时间段

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Byte getMethod() {
        return method;
    }

    public void setMethod(Byte method) {
        this.method = method;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Integer getThresholdSwitch() {
        return thresholdSwitch;
    }

    public void setThresholdSwitch(Integer thresholdSwitch) {
        this.thresholdSwitch = thresholdSwitch;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public List<Integer> getArea() {
        return area;
    }

    public void setArea(List<Integer> area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAutoConfirmSwitch() {
        return autoConfirmSwitch;
    }

    public void setAutoConfirmSwitch(Integer autoConfirmSwitch) {
        this.autoConfirmSwitch = autoConfirmSwitch;
    }

    public Integer getAutoConfirm() {
        return autoConfirm;
    }

    public void setAutoConfirm(Integer autoConfirm) {
        this.autoConfirm = autoConfirm;
    }

    public List<ShopTimeInfo> getDelivery_times() {
        return delivery_times;
    }

    public void setDelivery_times(List<ShopTimeInfo> delivery_times) {
        this.delivery_times = delivery_times;
    }

    public String getPickTimeStr() {
        return pickTimeStr;
    }

    public void setPickTimeStr(String pickTimeStr) {
        this.pickTimeStr = pickTimeStr;
    }
}
