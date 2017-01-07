package com.store59.printapi.model.result.app;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class ShopDelivery {
    private Byte               send_type;  //1店长配送  2.上门自取
    private Double             delivery_amount;  //配送费
    private Double             free_delivery_amount;  //免配送费门槛
    private String             description;  //配送方式的说明
    private List<ShopTimeInfo> delivery_times;  //配送时间处理
    private String             pick_address;//取货地址
    private String             pick_time_string;//取货时间

    public Byte getSend_type() {
        return send_type;
    }

    public void setSend_type(Byte send_type) {
        this.send_type = send_type;
    }

    public Double getDelivery_amount() {
        return delivery_amount;
    }

    public void setDelivery_amount(Double delivery_amount) {
        this.delivery_amount = delivery_amount;
    }

    public Double getFree_delivery_amount() {
        return free_delivery_amount;
    }

    public void setFree_delivery_amount(Double free_delivery_amount) {
        this.free_delivery_amount = free_delivery_amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ShopTimeInfo> getDelivery_times() {
        return delivery_times;
    }

    public void setDelivery_times(List<ShopTimeInfo> delivery_times) {
        this.delivery_times = delivery_times;
    }

    public String getPick_address() {
        return pick_address;
    }

    public void setPick_address(String pick_address) {
        this.pick_address = pick_address;
    }

    public String getPick_time_string() {
        return pick_time_string;
    }

    public void setPick_time_string(String pick_time_string) {
        this.pick_time_string = pick_time_string;
    }
}
