/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.print.tools;

import java.util.Random;

/**
 * 订单id生成工具类
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
public class OrderIdTool {

    //订单id业务前缀，表示业务类型，2表示打印店
    private final static char PREFIX = '2';

    //2016-01-01的时间戳
    private final static long INIT_DATA = 1451577600000l;

    //随机数的范围
    private final static int RANDOM_MAX = 1000000;

    private static Random RANDOM = new Random();

    /**
     * 获取打印订单的订单号
     *
     * @return
     */
    public static Long getPrintOderId() {
        long timestamp = System.currentTimeMillis() - INIT_DATA;
        String orderId = String.format("%s%s%06d", PREFIX, String.valueOf(timestamp), RANDOM.nextInt(RANDOM_MAX));
        return new Long(orderId);
    }
}
