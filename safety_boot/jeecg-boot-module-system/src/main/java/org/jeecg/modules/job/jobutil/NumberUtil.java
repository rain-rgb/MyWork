package org.jeecg.modules.job.jobutil;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转换工具类
 */
public class NumberUtil {

    public static float strToFloat(String str) {
        if (StrUtil.isBlank(str)) {
            return 0f;
        } else {
            return Convert.toFloat(str, 0f);
        }
    }

    /**
     * date 时间 String格式化
     * @param date
     * @return
     */
    public static String Stringnyr(Date date){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String format = ft.format(date);
        return format;
    }

    /**
     * string 时间 data格式化
     * @param date
     * @return
     */
    public static Date datanyr(String date){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date parse=null;
        try {
            parse = ft.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }
}
