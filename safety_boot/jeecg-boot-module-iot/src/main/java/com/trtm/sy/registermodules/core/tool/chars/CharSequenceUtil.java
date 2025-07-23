package com.trtm.sy.registermodules.core.tool.chars;


import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ArrayUtil;

public class CharSequenceUtil {
    public static final int INDEX_NOT_FOUND = -1;
    public static final String NULL = "null";
    public static final String EMPTY = "";
    public static final String SPACE = " ";

    public CharSequenceUtil() {
    }

    public static boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for (int i = 0; i < length; ++i) {
                if (!CharUtil.isBlankChar(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return "null";
        } else {
            return !ArrayUtil.isEmpty(params) && !isBlank(template) ? StrFormatter.format(template.toString(), params) : template.toString();
        }
    }
}
