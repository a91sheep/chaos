/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.model.enums;

/**
 * 阿里交易状态枚举.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public enum AlipayTradeStatusEnum {

    /** 交易创建，等待买家付款。 */
    WAIT_BUYER_PAY,

    /**
     * <pre>
     * 在指定时间段内未支付时关闭的交易；
     * 在交易完成全额退款成功时关闭的交易。
     * </pre>
     */
    TRADE_CLOSED,

    /** 交易成功，且可对该交易做操作，如：多级分润、退款等。 */
    TRADE_SUCCESS,

    /** 等待卖家收款（买家付款后，如果卖家账号被冻结）。 */
    TRADE_PENDING,

    /** 交易成功且结束，即不可再做任何操作。 */
    TRADE_FINISHED,

    ;

}
