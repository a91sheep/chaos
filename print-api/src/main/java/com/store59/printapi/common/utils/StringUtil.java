package com.store59.printapi.common.utils;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
public class StringUtil {
    /**
     * 检查对象字符串是否为null 或者 为空
     * 说明：
     * null -> true
     * "" -> true
     * "  " -> false
     * 其他情况 -> false
     *
     * @param val
     * @return 检查结果
     */
    public static boolean isBlank(String val) {
        if (val == null || "".equals(val)) {
            return true;
        }

        return false;
    }

    /**
     * 源字符串中是否包含目标字符串的检查
     *
     * @param src
     * @param dest
     * @return true：包含，false：不包含
     */
    public static boolean contains(String src, String dest) {
        if (isBlank(src) || isBlank(dest)) {
            return false;
        }

        if (src.indexOf(dest) == -1) {
            return false;
        }

        return true;
    }

    /**
     * 源字符串是否以目标字符串结尾的检查
     *
     * @param src
     * @param dest
     * @return true：是，false：不是
     */
    public static boolean endWith(String src, String dest) {
        if (isBlank(src) || isBlank(dest)) {
            return false;
        }

        if (src.endsWith(dest)) {
            return true;
        }

        return false;
    }

    /**
     * 判断字符串是否匹配给定的正则表达式
     *
     * @param src
     * @param regex
     * @return 匹配结果（true：匹配成功，false：匹配失败）
     */
    public static boolean matches(String src, String regex) {
        if (isBlank(src) || isBlank(regex)) {
            return false;
        }

        return src.matches(regex);
    }

    /**
     * 把srcStr字符串中的 startReplaceChar ＋ 数字 ＋ endReplaceChar 形式的字符串替换成replaceStr[数字]字符串。
     * 例如：srcStr为 我要替换的字符串是{0}，我还想替换{1}，replaceStr为"你好"、“我很好”，startReplaceChar为‘{’，endReplaceChar为‘}’的情况
     *     替换后的字符串为 我要替换的字符串是你好，我还想替换我很好
     * 注意：本方法目前只能替换数字为 0-9 的10个字符串
     *
     * @param srcStr
     * @param replaceStr
     * @param startReplaceChar
     * @param endReplaceChar
     * @return 替换后的字符串
     */
    public static String replace(String srcStr, String[] replaceStr,
                                 char startReplaceChar, char endReplaceChar) {
        if (srcStr == null || replaceStr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char nowC;
        char nextC;
        int len = srcStr.length();
        for (int i = 0; i < len; i++) {
            nowC = srcStr.charAt(i);
            if (nowC == startReplaceChar) {
                if ((i + 2) < len) {
                    if (srcStr.charAt((i + 2)) == endReplaceChar) {
                        nextC = srcStr.charAt(i + 1);
                        if (Character.isDigit(nextC)) {
                            int nextI = Integer.parseInt("" + nextC);
                            if (nextI < replaceStr.length) {
                                i += 2;
                                sb.append(replaceStr[nextI]);
                                continue;
                            }
                        }
                    }
                }
            }

            sb.append(nowC);
        }

        return sb.toString();
    }

    public static String replaceAll(String src, String regex, String replaceStr) {
        if (isBlank(src)) {
            return "";
        }

        return src.replaceAll(regex, replaceStr);
    }

    public static String replace(String srcStr, String ... replaceStr) {
        return replace(srcStr, replaceStr, '{', '}');
    }

    public static String substring(String srcStr, int sublen) {
        if (isBlank(srcStr)) {
            return "";
        }

        if (srcStr.length() <= sublen) {
            return srcStr;
        }

        return srcStr.substring(0, sublen);
    }

    public static String trimToEmpty(String val) {
        if (isBlank(val)) {
            return "";
        }

        return val.trim();
    }

    public static String notBlankAddSpace(String val) {
        if (isTrimBlank(val)) {
            return "";
        }

        return val + " ";
    }

    public static boolean isTrimBlank(String val) {
        if (val == null || "".equals(val.trim())) {
            return true;
        }

        return false;
    }
}