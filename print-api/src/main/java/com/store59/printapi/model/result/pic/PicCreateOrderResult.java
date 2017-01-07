package com.store59.printapi.model.result.pic;

import java.util.List;

import com.store59.printapi.model.param.createOrder.PicCreateOrderDetailParam;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月12日 下午2:13:59 
 * 类说明
 */
public class PicCreateOrderResult {
    private String                          coupon_code;
    private Byte                            send_type;  //1 店长配送  2 上门自取
    private Integer                         delivery_type;  // 2 立即配送  3 上门自取
    private Long                            expect_start_time;  //配送开始时间
    private Long                            expect_end_time;  //配送结束时间
    private String                          expect_time_name;  //配送时间显示的字符串(包括立即送出)
    private Byte                            open_ad;  //是否免费打印  0不开启  1开启免费打印
    private String                          phone;
    private String                          address;
    private String                          remark;
    private String                          shop_id;
    private List<PicCreateOrderDetailParam> items;
    private Double                          delivery_amount;
    private Double                          document_amount;
    private Double                          total_amount;
    private Double                          coupon_discount;
    private String                          pick_address;
    private String                          pick_time_string;
    private Integer                         dormentry_id;
    private Integer                         source;
    private Integer                         print_num;  //打印文档总份数
    private Integer                         print_pages;  //打印文档总页数


    public Integer getPrint_num() {
        return print_num;
    }

    public void setPrint_num(Integer print_num) {
        this.print_num = print_num;
    }

    public Integer getPrint_pages() {
        return print_pages;
    }

    public void setPrint_pages(Integer print_pages) {
        this.print_pages = print_pages;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public Byte getSend_type() {
        return send_type;
    }

    public void setSend_type(Byte send_type) {
        this.send_type = send_type;
    }

    public Integer getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(Integer delivery_type) {
        this.delivery_type = delivery_type;
    }

    public Long getExpect_start_time() {
        return expect_start_time;
    }

    public void setExpect_start_time(Long expect_start_time) {
        this.expect_start_time = expect_start_time;
    }

    public Long getExpect_end_time() {
        return expect_end_time;
    }

    public void setExpect_end_time(Long expect_end_time) {
        this.expect_end_time = expect_end_time;
    }

    public String getExpect_time_name() {
        return expect_time_name;
    }

    public void setExpect_time_name(String expect_time_name) {
        this.expect_time_name = expect_time_name;
    }

    public Byte getOpen_ad() {
        return open_ad;
    }

    public void setOpen_ad(Byte open_ad) {
        this.open_ad = open_ad;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public List<PicCreateOrderDetailParam> getItems() {
        return items;
    }

    public void setItems(List<PicCreateOrderDetailParam> items) {
        this.items = items;
    }

    public Double getDelivery_amount() {
        return delivery_amount;
    }

    public void setDelivery_amount(Double delivery_amount) {
        this.delivery_amount = delivery_amount;
    }

    public Double getDocument_amount() {
        return document_amount;
    }

    public void setDocument_amount(Double document_amount) {
        this.document_amount = document_amount;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public Double getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(Double coupon_discount) {
        this.coupon_discount = coupon_discount;
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

    public Integer getDormentry_id() {
        return dormentry_id;
    }

    public void setDormentry_id(Integer dormentry_id) {
        this.dormentry_id = dormentry_id;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}
