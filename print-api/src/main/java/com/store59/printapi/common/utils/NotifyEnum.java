/**
 * 
 */
package com.store59.printapi.common.utils;

import com.store59.kylin.utils.EnumCode;

/**
 * 
 * @author <a href="mailto:zhangjk@59store.com">山雀</a>
 * @version 1.0 2016年3月21日
 * @since 1.0
 */
public enum NotifyEnum implements EnumCode<Integer> {
    NORMAL(0, ""), // 普通订单
    NEW(1, "您有新的订单,请及时处理"), // 新订单
    CANCEL(2, "您有一个订单被取消"), // 取消订单
    PAY_FINISH(3, "您有一个订单支付完成"), // 支付完成
    ERR_AFTER_PAY(10, "支付成功，但未生成订单！金额将于3个工作日内退至付款账户。对您造成不便，我们深感抱歉！"), // 支付完成 订单未完成
    ORDER_ERR_AFTER_PAY(10, "支付成功，订单被取消！金额将于3个工作日内退至付款账户。对您造成不便，我们深感抱歉！"), // 支付完成 订单未完成
    ERR_AFTER_ORDER(11, "收款成功，但该单未生成返利！请尽快联系客服，不要让银子溜走≥﹏≤ "),// 支付完成 订单余额冻结金额相关未完成
    ;
    private int    code;
    private String msg;

    private NotifyEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
