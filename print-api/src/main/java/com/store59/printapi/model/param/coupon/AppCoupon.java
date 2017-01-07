package com.store59.printapi.model.param.coupon;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public class AppCoupon {
    private Integer       item_id;             //ID
    private Integer       type;               //优惠券类型（0:现金抵用券，2:商品抵用券）
    private Integer       scope;              //业务范围（0:通用，1:便利店，2:夜猫店，3:零食盒子）
    private Long          add_time;            //创建时间
    private Integer       use_time;            //使用时间
    private Long          use_uid;             //使用用户id
    private String        use_order_id;         //使用订单id
    private Integer       use_order_type;       //使用订单类型(1:便利店订单那，2:夜猫店订单，3:零食盒子订单)
    private String        code;               //优惠券编号
    private Double        discount;           //折扣抵用金额
    private Long          active_date;         //起始日期
    private Long          expire_date;         //过期日期
    private Long          uid;                //用户id
    private Double        threshold;          //优惠券使用金额门槛
    private List<Integer> discount_apply_rids;  //优惠券使用商品
    private String        text;               //优惠券显示的文字
    private String        phone;              //手机号码
    private String        source;             //优惠券来源
    private String        tips;
    private Integer       status;             //优惠券状态
    private Integer       new_status;          //优惠券状态-H5  0未到使用时间 1正常  2使用过 3过期
    private Long          active_time;          //起始日期兼容APP要求字段
    private Long          expire_time;          //过期日期兼容APP要求字段
    private String        tip;
    private String        extension;

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public Long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Long add_time) {
        this.add_time = add_time;
    }

    public Integer getUse_time() {
        return use_time;
    }

    public void setUse_time(Integer use_time) {
        this.use_time = use_time;
    }

    public Long getUse_uid() {
        return use_uid;
    }

    public void setUse_uid(Long use_uid) {
        this.use_uid = use_uid;
    }

    public String getUse_order_id() {
        return use_order_id;
    }

    public void setUse_order_id(String use_order_id) {
        this.use_order_id = use_order_id;
    }

    public Integer getUse_order_type() {
        return use_order_type;
    }

    public void setUse_order_type(Integer use_order_type) {
        this.use_order_type = use_order_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getActive_date() {
        return active_date;
    }

    public void setActive_date(Long active_date) {
        this.active_date = active_date;
    }

    public Long getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(Long expire_date) {
        this.expire_date = expire_date;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public List<Integer> getDiscount_apply_rids() {
        return discount_apply_rids;
    }

    public void setDiscount_apply_rids(List<Integer> discount_apply_rids) {
        this.discount_apply_rids = discount_apply_rids;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNew_status() {
        return new_status;
    }

    public void setNew_status(Integer new_status) {
        this.new_status = new_status;
    }

    public Long getActive_time() {
        return active_time;
    }

    public void setActive_time(Long active_time) {
        this.active_time = active_time;
    }

    public Long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Long expire_time) {
        this.expire_time = expire_time;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
