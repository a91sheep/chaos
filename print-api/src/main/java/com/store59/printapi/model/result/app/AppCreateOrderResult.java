package com.store59.printapi.model.result.app;

import com.store59.printapi.model.param.createOrder.AppCreateOrderDetailParam;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/29
 * @since 1.0
 */
public class AppCreateOrderResult {
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
    private List<AppCreateOrderDetailParam> items;
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
    private Integer							doc_type;
    private Integer							coupon_had;//是否有优惠券：0没有，1有
    private Integer							ad_page_num;//福利纸张数
    private Double							free_amount;//福利纸优惠价格
    private Double							doc_coupon_amount;//文档价格减去免费打印后的价格，用这个价格来算优惠券
    
    public Double getDoc_coupon_amount() {
		return doc_coupon_amount;
	}

	public void setDoc_coupon_amount(Double doc_coupon_amount) {
		this.doc_coupon_amount = doc_coupon_amount;
	}

	public Integer getAd_page_num() {
		return ad_page_num;
	}

	public void setAd_page_num(Integer ad_page_num) {
		this.ad_page_num = ad_page_num;
	}

	public Double getFree_amount() {
		return free_amount;
	}

	public void setFree_amount(Double free_amount) {
		this.free_amount = free_amount;
	}

	public Integer getCoupon_had() {
		return coupon_had;
	}

	public void setCoupon_had(Integer coupon_had) {
		this.coupon_had = coupon_had;
	}

	public Integer getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(Integer doc_type) {
		this.doc_type = doc_type;
	}

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

    public List<AppCreateOrderDetailParam> getItems() {
        return items;
    }

    public void setItems(List<AppCreateOrderDetailParam> items) {
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
