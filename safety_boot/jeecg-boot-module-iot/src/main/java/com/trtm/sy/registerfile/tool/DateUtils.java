package com.trtm.sy.registerfile.tool;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jeecg.common.exception.JeecgBootException;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间工具类
 *
 * @author zww
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public static SimpleDateFormat yyyMMdd = new SimpleDateFormat(YYYY_MM_DD);
    public static SimpleDateFormat yyyMMddhhmmss = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    public static SimpleDateFormat yyyy = new SimpleDateFormat(YYYY);
    public static SimpleDateFormat yyyMM = new SimpleDateFormat(YYYY_MM);

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个时间差
     */
    public static String getTimeBetween(String beginDate, String endDate)
    {
        SimpleDateFormat timeSf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        SimpleDateFormat dateSf = new SimpleDateFormat(YYYY_MM_DD);
        long beginTime;
        long endTime;
        try {
            if (beginDate.length() == 19) {
                beginTime = timeSf.parse(beginDate).getTime();
            }else{
                beginTime = dateSf.parse(beginDate).getTime();
            }
            if (endDate.length() == 19) {
                endTime = timeSf.parse(endDate).getTime();
            }else {
                endTime = dateSf.parse(endDate).getTime();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long sm = 1000;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endTime - beginTime;
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒
        long sec = diff % nd % nh % nm / sm;
        // 计算差多少秒//输出结果
        StringBuffer stringBuffer = new StringBuffer();
        if (day > 0){
            stringBuffer.append(day).append("天");
        }
        if (hour > 0){
            stringBuffer.append(hour).append("小时");
        }
        if (min > 0){
            stringBuffer.append(min).append("分钟");
        }
        if (sec > 0){
            stringBuffer.append(sec).append("秒");
        }
        return stringBuffer.toString();
    }

    public static int getTimeDifference(String beginDate, String endDate)
    {
        long beginTime;
        long endTime;
        try {
            beginTime = yyyMMddhhmmss.parse(beginDate).getTime();
            endTime = yyyMMddhhmmss.parse(endDate).getTime();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        // 获得两个时间的毫秒时间差异
        long diff = endTime - beginTime;
        if (diff > 0){
            return 1;
        }else if (diff < 0){
            return -1;
        }else {
            return 0;
        }
    }

    public static String getFirstDayPreMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String firstDay = format.format(cal_1.getTime()) + " 00:00:00";
        return firstDay;
    }

    public static String getEndDayPreMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天
        String lastDay = format.format(cale.getTime()) + " 23:59:59";
        return lastDay;
    }


    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - past);
        Date today = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String result = sdf.format(today);
        return result;
    }


    /**
     * 获取某个日期是一年中的第几周
     * @param dateStr
     * @return
     */
    public static int getWeekNum(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.FRIDAY);
        calendar.setTime(date);
        int i = calendar.get(Calendar.WEEK_OF_YEAR);
        return i;
    }

    /**
     * 获取指定周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置周
        cal.set(Calendar.WEEK_OF_YEAR, week);
        // 设置该周第一天为星期五
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    /**
     * 获取指定周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置周
        cal.set(Calendar.WEEK_OF_YEAR, week);
        // 设置该周第一天为星期五
        cal.setFirstDayOfWeek(Calendar.FRIDAY);
        // 设置最后一天是星期日
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() - 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }

    public static List<Integer> getWeekNums(String date){
        List<Integer> list = new ArrayList<>();
        if (StringUtils.isEmpty(date) || date.length() < 7 || date.indexOf("-") != 4){
            return list;
        }
        String beginDate = date.substring(0,7) + "-07";
        String[] split = date.substring(0, 7).split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        String endDate = lastDayOfMonth(beginDate);
        int beginWeek = 0;
        int endWeek = 0;

        beginWeek = getWeekNum(beginDate) - 1;
        endWeek = getWeekNum(endDate) - 1;
        if (month == 12){
            endWeek = getMaxWeek(year);
        }
        if (beginWeek < 1 || endWeek < 1 || beginWeek >= endWeek){
            throw new JeecgBootException("时间计算有误！");
        }
        for (int i = beginWeek; i <= endWeek ; i++) {
            list.add(i);
        }
        return list;
    }


    public static int getMaxWeek(int year){
        int day = 31;
        int weekNum = DateUtils.getWeekNum(year + "-12-" + day);
        while(weekNum == 1){
            day --;
            weekNum = DateUtils.getWeekNum(year + "-12-" + day);
        }
        return weekNum;
    }

    public static String lastDayOfMonth(String dateStr) {
        dateStr = dateStr.substring(0,7) + "-01";
        Date date = null;
        try {
            date = yyyMMdd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return yyyMMdd.format(cal.getTime());
    }

    public static String getNextMonth(String dateStr) {
        dateStr = dateStr.substring(0,7) + "-01";
        Date date = null;
        try {
            date = yyyMMdd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        return yyyMMdd.format(cal.getTime());
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    /**
     *
     * @param date
     * @param num
     * @return
     * @throws ParseException
     */
    public static Date DeforeOneHourToNowDate(Date date,int timeType,int num) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.setTime(date);
        calendar.add(timeType, num);
        return calendar.getTime();
    }

    /**
     * 获取本周的第一天开始时间
     * @return
     * @throws ParseException
     */
    public static String DeforeOneWeekStartTime()  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 指定日期
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        //计算当前时间在本周的开始时间
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //start of the week
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR,-1);
        }
        calendar.add(Calendar.DAY_OF_WEEK, -(calendar.get(Calendar.DAY_OF_WEEK) - 2));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String startTime = sdf.format(calendar.getTime());
        return startTime;
    }

    /**
     *获取本周的最后一天结束时间
     * @return
     * @throws ParseException
     */
    public static String DeforeOneWeekEndTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 指定日期
        Calendar calendar = Calendar.getInstance();
        //计算当前时间在本周的结束时间
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }

    /**
     *获取本月的第一天开始时间
     * @return
     * @throws ParseException
     */
    public static String firstDayOfMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 创建Calendar实例并设置日期为当前日期
        Calendar calen = Calendar.getInstance();
        Date date = calen.getTime();
        calen.setTime(date);
        // 将日历的日期设置为本月的第一天
        calen.set(Calendar.DAY_OF_MONTH, 1);
        // 将时分秒毫秒设置为0，即开始时间为当天0点
        calen.set(Calendar.HOUR_OF_DAY, 0);
        calen.set(Calendar.MINUTE, 0);
        calen.set(Calendar.SECOND, 0);
        calen.set(Calendar.MILLISECOND, 0);
        // 获取本月开始时间
        String firstDayOfMonth = sdf.format(calen.getTime());
        return firstDayOfMonth;
    }

    /**
     *获取本月的第一天开始时间
     * @return
     * @throws ParseException
     */
    public static String lastDayOfMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 创建Calendar实例并设置日期为当前日期
        Calendar calen = Calendar.getInstance();
        Date date = calen.getTime();
        calen.setTime(date);
        // 将日历的日期设置为下个月的第一天
        calen.set(Calendar.MONTH, calen.get(Calendar.MONTH) + 1);
        calen.set(Calendar.DAY_OF_MONTH, 1);
        // 将时分秒毫秒设置为0，即结束时间为当天0点
        calen.set(Calendar.HOUR_OF_DAY, 0);
        calen.set(Calendar.MINUTE, 0);
        calen.set(Calendar.SECOND, 0);
        calen.set(Calendar.MILLISECOND, 0);
        // 减去1毫秒，获取本月结束时间
        String lastDayOfMonth = sdf.format(new Date(calen.getTimeInMillis() - 1));
        return lastDayOfMonth;
    }

    public static void main(String[] args) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat w = new SimpleDateFormat("EEEE");
            Calendar instance = Calendar.getInstance();
            String dateStr = "2022-09-29";
            instance.setTime(f.parse(dateStr));
            int i = instance.get(Calendar.DAY_OF_WEEK)-1;
            System.out.println(dateStr+" 是周"+i);
            System.out.println(dateStr+" 是 "+w.format(instance.getTime()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
