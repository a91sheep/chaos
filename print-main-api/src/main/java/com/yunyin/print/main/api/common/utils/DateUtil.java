package com.yunyin.print.main.api.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/19
 * @since 1.0
 */
public class DateUtil {

    public static int getCurrentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static Date timestampToDate(Integer timeStamp) throws ParseException {
        if (timeStamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = timeStamp + "000";
        long t = Long.parseLong(str);
        return sdf.parse(sdf.format(t));
    }

    /**
     * 获取当前日期
     *
     * @return Date eg:2015-10-25 11:11:11
     */
    public static String timestampToDateStr(Integer timeStamp) {
        if (timeStamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = timeStamp + "000";
        long t = Long.parseLong(str);
        return sdf.format(t);
    }
}
