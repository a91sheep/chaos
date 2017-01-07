package com.store59.printapi.common.utils;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 15/12/11
 * @since 1.0
 */
public class DateUtil {
    private static final String PATTERN_DATE = "yyyy-MM-dd";

    public static int getCurrentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static long getDiff(String lastModifyTime) {
        long diff = System.currentTimeMillis() - TypeConverter.str2Long(lastModifyTime);
        return (diff / 1000);
    }

    /**
     * 获取今日零点时间戳（单位：秒）
     *
     * @return
     */
    public static Integer getTodayStartTimeSeconds() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * 获取某年某月第一天零点时间戳（单位：秒）
     *
     * @param year
     * @param month
     * @return
     */
    public static Integer getFistDayTime(Short year, Byte month) {
        if (year == null || month == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, cal.getMinimum(Calendar.DATE), 0, 0, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * 获取某年某月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static Integer getDays(Short year, Byte month) {
        if (year == null || month == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONDAY, month - 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public static String timestampToDate(int timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = timeStamp + "000";
        long t = Long.parseLong(str);
        return sdf.format(t);
    }

    public static String timestampToDate(int timeStamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = timeStamp + "000";
        long t = Long.parseLong(str);
        return sdf.format(t);
    }

    public static int dateToTimestamp(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(strDate);
        return (int) (date.getTime() / 1000);
    }

    public static String dateCalculate(String strDate, int year, int month,
                                       int day) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(strDate));

        if (day != 0) {
            cal.add(cal.DATE, day);
        }
        if (month != 0) {
            cal.add(cal.MONTH, month);
        }
        if (year != 0) {
            cal.add(cal.YEAR, year);

        }
        return sdf.format(cal.getTime());
    }

    /**
     * 获取两个时间戳间隔多少天
     */
    public static int getIntervalDays(int timestamp1, int timestamp2) {
        return (timestamp1 - timestamp2) / (24 * 60 * 60);
    }

    /**
     * 获取当前日期
     *
     * @return Date eg:2015-10-25
     */
    public static Date getCurrentDate() {
        Date d = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(PATTERN_DATE);
            String dateStr = df.format(new Date());
            d = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
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

    /**
     * 获取当前日期
     *
     * @return Date eg:2015-10-25 11:11:11
     */
    public static Date timestampToDate(Integer timeStamp) throws ParseException {
        if (timeStamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = timeStamp + "000";
        long t = Long.parseLong(str);
        return sdf.parse(sdf.format(t));
    }
}