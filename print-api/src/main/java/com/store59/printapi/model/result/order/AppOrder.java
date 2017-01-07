package com.store59.printapi.model.result.order;

import com.store59.printapi.model.param.createOrder.AppCreateOrderDetailParam;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/15
 * @since 1.0
 */
public class AppOrder {
    private String                          order_sn;
    private Byte                            status;
    private Byte                            type;    // 8打印店订单
    private Byte                            source;
    private Byte                            paytype;
    private Long                            paytime;
    private String                          pay_trade_no;
    private Byte                            refund_status_code;  //0为退款中，1为已退款
    private String                          refund_status_msg;
    private Integer                         print_num;  //打印文档份数
    private Double                          print_amount;    //打印文档总金额
    private Integer                         print_pages;    //打印文档总页数
    private Double                          delivery_amount; //配送费
    private Double                          discount;    //合计折扣金额
    private Double                          coupon_discount; //优惠券金额
    private Double                          order_amount;    // print_amount + delivery_fee - discount，最后的订单金额
    private Long                            add_time;   //下单时间
    private Long                            confirm_time;   //店长确认订单时间
    private Long                            send_time;  //送达时间
    private String                          buyer_name;  //收货人
    private String                          address; //用户地址
    private String                          phone;   //用户电话
    private String                          dorm_contact;  //楼主手机号
    private String                          dorm_address;  //楼主地址
    private Byte                            send_type;  // 1店长配送 2上门自取
    private Byte                            delivery_type;  //2立即送出  3预订  (send_type = 1时才有)
    private Integer                         expect_start_time;
    private Integer                         expect_end_time;
    private String                          delivery_desc;   //配送时间和配送方式的描述(店长配送  19:00-19:15)
    private String                          remark;
    private List<AppCreateOrderDetailParam> items;
    private Integer                         dorm_id;
    private String                          attach;
    private Integer                         doc_type;
    private String                          cancel_reason;
    private Double                          item_amount;//订单金额
    private Double                          doc_amount;
    private Integer                         ad_page_num;//福利纸张数
    private Double                          free_amount;//福利纸优惠价格
    private Boolean                         is_consistent;//购物车免费张数和实际是否一致

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public Byte getPaytype() {
        return paytype;
    }

    public void setPaytype(Byte paytype) {
        this.paytype = paytype;
    }

    public Long getPaytime() {
        return paytime;
    }

    public void setPaytime(Long paytime) {
        this.paytime = paytime;
    }

    public String getPay_trade_no() {
        return pay_trade_no;
    }

    public void setPay_trade_no(String pay_trade_no) {
        this.pay_trade_no = pay_trade_no;
    }

    public Byte getRefund_status_code() {
        return refund_status_code;
    }

    public void setRefund_status_code(Byte refund_status_code) {
        this.refund_status_code = refund_status_code;
    }

    public String getRefund_status_msg() {
        return refund_status_msg;
    }

    public void setRefund_status_msg(String refund_status_msg) {
        this.refund_status_msg = refund_status_msg;
    }

    public Integer getPrint_num() {
        return print_num;
    }

    public void setPrint_num(Integer print_num) {
        this.print_num = print_num;
    }

    public Double getPrint_amount() {
        return print_amount;
    }

    public void setPrint_amount(Double print_amount) {
        this.print_amount = print_amount;
    }

    public Integer getPrint_pages() {
        return print_pages;
    }

    public void setPrint_pages(Integer print_pages) {
        this.print_pages = print_pages;
    }

    public Double getDelivery_amount() {
        return delivery_amount;
    }

    public void setDelivery_amount(Double delivery_amount) {
        this.delivery_amount = delivery_amount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(Double coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public Double getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(Double order_amount) {
        this.order_amount = order_amount;
    }

    public Long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Long add_time) {
        this.add_time = add_time;
    }

    public Long getConfirm_time() {
        return confirm_time;
    }

    public void setConfirm_time(Long confirm_time) {
        this.confirm_time = confirm_time;
    }

    public Long getSend_time() {
        return send_time;
    }

    public void setSend_time(Long send_time) {
        this.send_time = send_time;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDorm_contact() {
        return dorm_contact;
    }

    public void setDorm_contact(String dorm_contact) {
        this.dorm_contact = dorm_contact;
    }

    public String getDorm_address() {
        return dorm_address;
    }

    public void setDorm_address(String dorm_address) {
        this.dorm_address = dorm_address;
    }

    public Byte getSend_type() {
        return send_type;
    }

    public void setSend_type(Byte send_type) {
        this.send_type = send_type;
    }

    public Byte getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(Byte delivery_type) {
        this.delivery_type = delivery_type;
    }

    public Integer getExpect_start_time() {
        return expect_start_time;
    }

    public void setExpect_start_time(Integer expect_start_time) {
        this.expect_start_time = expect_start_time;
    }

    public Integer getExpect_end_time() {
        return expect_end_time;
    }

    public void setExpect_end_time(Integer expect_end_time) {
        this.expect_end_time = expect_end_time;
    }

    public String getDelivery_desc() {
        return delivery_desc;
    }

    public void setDelivery_desc(String delivery_desc) {
        this.delivery_desc = delivery_desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AppCreateOrderDetailParam> getItems() {
        return items;
    }

    public void setItems(List<AppCreateOrderDetailParam> items) {
        this.items = items;
    }

    public Integer getDorm_id() {
        return dorm_id;
    }

    public void setDorm_id(Integer dorm_id) {
        this.dorm_id = dorm_id;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Integer getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(Integer doc_type) {
        this.doc_type = doc_type;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public Double getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(Double item_amount) {
        this.item_amount = item_amount;
    }

    public Double getDoc_amount() {
        return doc_amount;
    }

    public void setDoc_amount(Double doc_amount) {
        this.doc_amount = doc_amount;
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

    public Boolean getIs_consistent() {
        return is_consistent;
    }

    public void setIs_consistent(Boolean is_consistent) {
        this.is_consistent = is_consistent;
    }
}
