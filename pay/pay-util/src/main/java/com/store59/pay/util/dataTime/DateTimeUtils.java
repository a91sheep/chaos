/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.dataTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/1/8
 * @since 1.0
 */
public class DateTimeUtils {

    /**
     * 将字符串转为时间戳
     *
     * @param user_time
     * @return
     */
    public static String ToTimestamp(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 返回时间格式的字符
     * 返回格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String ToSting(Long milliSecond){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(milliSecond);
    }
}
