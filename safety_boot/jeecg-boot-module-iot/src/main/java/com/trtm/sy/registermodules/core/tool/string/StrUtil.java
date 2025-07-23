package com.trtm.sy.registermodules.core.tool.string;



import com.trtm.sy.registermodules.core.tool.chars.CharSequenceUtil;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil extends CharSequenceUtil implements StrPool {

    public StrUtil() {
    }

    /**
     * 截取俩个字符串之间的字符串
     *
     * @param str      被截取的字符
     * @param strStart 第一个字符
     * @param strEnd   第二个字符
     * @return 被截取后的字符
     */
    public static String cutString(String str, String strStart, String strEnd) {
        String reg = strStart + "(.*)" + strEnd;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            str = matcher.group(1);
        }
        return str;
    }
}
