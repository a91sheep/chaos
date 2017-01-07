package com.store59.printapi.common.utils;

import java.math.BigDecimal;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
public class TypeConverter {

    public static Integer str2Integer(String src) {
        if (StringUtil.isBlank(src)) {
            return null;
        }

        return Integer.valueOf(src);
    }

    public static Long str2Long(String src) {
        if (StringUtil.isBlank(src)) {
            return null;
        }

        return Long.valueOf(src);
    }

    public static Byte str2Byte(String src) {
        if (StringUtil.isBlank(src)) {
            return null;
        }

        return Byte.valueOf(src);
    }

    public static Short str2Short(String src) {
        if (StringUtil.isBlank(src)) {
            return null;
        }

        return Short.valueOf(src);
    }

    public static Double str2Double(String src) {
        if (StringUtil.isBlank(src)) {
            return null;
        }

        return Double.valueOf(src);
    }

    public static Float str2Float(String src) {
        if (StringUtil.isBlank(src)) {
            return null;
        }

        return Float.valueOf(src);
    }

    public static BigDecimal str2BigDecimal(String src) {
        if (StringUtil.isBlank(src)) {
            return null;
        }

        return new BigDecimal(src);
    }

    public static Byte bol2Byte(Boolean src) {
        if (src == null) {
            return null;
        }

        Byte ret = 0;

        if (src.equals(Boolean.TRUE)) {
            ret = 1;
        }

        return ret;
    }

    public static Boolean str2Boolean(String src) {
        if (StringUtil.isBlank(src)) {
            return null;
        }

        Boolean ret = Boolean.FALSE;
        if ("1".equals(src)) {
            ret = Boolean.TRUE;
        }

        return ret;
    }

    public static Boolean byte2Boolean(Byte src) {
        if (src == null) {
            return null;
        }

        Boolean ret = Boolean.FALSE;
        if (src == 1) {
            ret = Boolean.TRUE;
        }

        return ret;
    }

    public static String long2String(Long src) {
        if (src == null) {
            return null;
        }

        return String.valueOf(src);
    }

    public static String int2String(Integer src) {
        if (src == null) {
            return null;
        }

        return String.valueOf(src);
    }

    public static String short2String(Short src) {
        if (src == null) {
            return null;
        }

        return String.valueOf(src);
    }

    public static String byte2String(Byte src) {
        if (src == null) {
            return null;
        }

        return String.valueOf(src);
    }
}