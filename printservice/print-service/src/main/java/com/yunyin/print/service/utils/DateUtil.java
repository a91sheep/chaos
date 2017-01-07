package com.yunyin.print.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/14
 * @since 1.0
 */
public class DateUtil {

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
