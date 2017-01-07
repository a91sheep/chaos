package com.store59.printapi.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年6月16日 下午12:02:42 
 * 类说明
 */
@ConfigurationProperties(prefix = "callService.urls")
@Component
public class RemoteServiceUrlCfg {
    private String userService;
    private String baseService;
    private String dormService;
    private String printService;
    private String couponService;
    private String adService;
    private String orderService;

    public String getUserService() {
        return userService;
    }

    public void setUserService(String userService) {
        this.userService = userService;
    }

    public String getBaseService() {
        return baseService;
    }

    public void setBaseService(String baseService) {
        this.baseService = baseService;
    }

    public String getDormService() {
        return dormService;
    }

    public void setDormService(String dormService) {
        this.dormService = dormService;
    }

    public String getPrintService() {
        return printService;
    }

    public void setPrintService(String printService) {
        this.printService = printService;
    }

    public String getCouponService() {
        return couponService;
    }

    public void setCouponService(String couponService) {
        this.couponService = couponService;
    }

    public String getAdService() {
        return adService;
    }

    public void setAdService(String adService) {
        this.adService = adService;
    }

    public String getOrderService() {
        return orderService;
    }

    public void setOrderService(String orderService) {
        this.orderService = orderService;
    }
}
