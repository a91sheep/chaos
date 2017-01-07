package com.store59.printapi.model.result.order;

import com.store59.print.common.model.PrintOrder;

import java.util.Map;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/12
 * @since 1.0
 */
public class OrderCenterDTO {
    private PrintOrder          printOrder;
    private String              sellerId; //卖家id
    private String              sellerName;   //卖家名字
    private String              sellerPhone;  //卖家手机
    private String              sellerAddress;    //卖家地址
    private Integer             sellerSiteId; //卖家校区id
    private Integer             sellerDormentryId;    //卖家楼栋id
    private String              sellerShopId; //卖家店铺id
    private Map<String, String> extension;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public Integer getSellerSiteId() {
        return sellerSiteId;
    }

    public void setSellerSiteId(Integer sellerSiteId) {
        this.sellerSiteId = sellerSiteId;
    }

    public Integer getSellerDormentryId() {
        return sellerDormentryId;
    }

    public void setSellerDormentryId(Integer sellerDormentryId) {
        this.sellerDormentryId = sellerDormentryId;
    }

    public String getSellerShopId() {
        return sellerShopId;
    }

    public void setSellerShopId(String sellerShopId) {
        this.sellerShopId = sellerShopId;
    }

    public PrintOrder getPrintOrder() {
        return printOrder;
    }

    public void setPrintOrder(PrintOrder printOrder) {
        this.printOrder = printOrder;
    }

    public Map<String, String> getExtension() {
        return extension;
    }

    public void setExtension(Map<String, String> extension) {
        this.extension = extension;
    }
}
