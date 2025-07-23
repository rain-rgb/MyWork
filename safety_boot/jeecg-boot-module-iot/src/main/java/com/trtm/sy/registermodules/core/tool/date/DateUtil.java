package com.trtm.sy.registermodules.core.tool.date;

import cn.hutool.core.date.DatePattern;

import java.util.Date;

public class DateUtil {

    public static String formatTime(Date date) {
        return null == date ? null : DatePattern.NORM_TIME_FORMAT.format(date);
    }

    public static String formatDateTime(Date date) {
        return null == date ? null : DatePattern.NORM_DATETIME_FORMAT.format(date);
    }
}
