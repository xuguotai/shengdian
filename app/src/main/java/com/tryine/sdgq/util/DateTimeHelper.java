package com.tryine.sdgq.util;

/**
 * @ClassName DateTimeHelper
 * @Description TODO
 * @Author 熊鹏
 * @Email 104092980@qq.com
 * @Date 2019/9/5 14:14
 * @Version 1.0
 */

import android.annotation.SuppressLint;
import android.util.Log;

import com.tryine.sdgq.common.mine.bean.DateBean;
import com.tryine.sdgq.common.user.bean.UserBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Used 实现日期和字符串之间的转换以及日期的相关操作
 *
 * @注意 new SimpleDateFormat的构造函数中必须含有Locale.CHINA或者Locale.getDefault()
 * SimpleDateFormat format = new SimpleDateFormat(parse,Locale.CHINA);
 * SimpleDateFormat format = new SimpleDateFormat(parse,Locale.getDefault());
 * 解决黄色感叹号：http://www.blogchen.com/archives/392.html
 */
public class DateTimeHelper {

    /**
     * 将未指定格式的字符串转换成日期类型
     *
     * @param date - 20151123
     * @return Mon Nov 23 00:00:00 CST 2015
     */
    public static Date parseStringToDate(String date) throws ParseException {
        Date result = null;
        String parse = date;
        parse = parse.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");
        parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
        parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");
        SimpleDateFormat format = new SimpleDateFormat(parse, Locale.CHINA);
        result = format.parse(date);
        return result;
    }

    /**
     * 将日期以指定格式输出
     *
     * @param date   - new Date()
     * @param format - "yyyy-MM-dd"
     * @return 2015-11-23
     */
    public static String formatToString(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,
                    Locale.CHINA);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int formatToInt(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,
                    Locale.CHINA);
            return Integer.parseInt(sdf.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将日期格式的字符串以指定格式输出
     *
     * @param date   - "2015/11/23"
     * @param format - "yyyy-MM-dd"
     * @return 2015-11-23
     */
    public static String formatToString(String date, String format) {
        try {
            Date dt = DateTimeHelper.parseStringToDate(date);
            SimpleDateFormat sdf = new SimpleDateFormat(format,
                    Locale.CHINA);
            return sdf.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将时间戳转化为固定格式的日期 （yyyy-MM-dd HH:mm）
     *
     * @param time - new Date().getTime()+""
     * @return 2015-11-23 14:05
     */
    public static String getStrTime(String time) {
        if (time.trim().equals("") || time == null)
            return "";
        String strTime = null;
        time = time.substring(0, 10);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.CHINA);
        long ltime = Long.valueOf(time);
        strTime = mFormat.format(new Date(ltime * 1000L));
        return strTime;
    }

    /**
     * 将时间戳转化为指定格式日期
     *
     * @param time   - new Date().getTime()+""
     * @param format - "yyyy/MM/dd hh:mm:ss"
     * @return 2015/11/23 02:05:36
     */
    public static String getStrTime(String time, String format) {
        if (time.trim().equals("") || time == null || time.equals("null"))
            return "";
        String strTime = null;
        time = time.substring(0, 10);
        SimpleDateFormat mFormat = new SimpleDateFormat(format, Locale.CHINA);
        long ltime = Long.valueOf(time);
        strTime = mFormat.format(new Date(ltime * 1000L));
        return strTime;
    }


    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @param startDate - new Date()
     * @param endDate   - DateTimeHelper.parseStringToDate("2015-12-20")
     * @return 3周前
     */
    public static String twoDateDistance(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000l)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000l) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 24 * 60 * 60 * 1000l) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 7 * 24 * 60 * 60 * 1000l) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 4 * 7 * 24 * 60 * 60 * 1000l) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }

    public static String twoDateDistancet(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000l)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000l) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 24 * 60 * 60 * 1000l) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 7 * 24 * 60 * 60 * 1000l) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 判断两个日期的大小
     *
     * @param DATE1 -- "2015-11-23
     * @param DATE2 --"2015-12-20"
     * @return true 默认第一个比第二个时间小，所以如果第一个大于第二个，返回false
     */
    public static boolean compare_date(String DATE1, String DATE2) {
        //DateFormat df = new SimpleDateFormat();

        //getDateInstance方法——获取日期格式器 2015-11-23
        //getDateTimeInstance方法——获取日期/时间格式器  2015-11-23 09:37:50
        //getInstance方法用于获取为日期和时间使用SHORT风格的默认日期/时间格式器

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = sdf.parse(DATE1);
            Date dt2 = sdf.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 将时间time字符串转化为Date对象
     *
     * @param strTime - 1462772155198
     * @return Date
     */
    public static Date parseFormatTimeToDate(String strTime) {

        Date date = new Date();
        try {
            date.setTime(Long.parseLong(strTime));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        return date;
    }

    /**
     * 获取格式化后Date的Time值
     *
     * @param dateStr 2015-11-23
     * @return 1448208000000
     */
    public static long getParseDateTime(String dateStr) {
        long daterTime = 0;
        DateFormat df = DateFormat.getDateInstance();
        try {
            Date dt1 = df.parse(dateStr);
            daterTime = dt1.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return daterTime;
    }

    /**
     * 当前时间延后一个星期
     *
     * @param startDate 2016-03-16
     * @return 2015-03-23
     */
    public static String getLastWeekDate(String startDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String endDate = "";
        try {
            Date date = df.parse(startDate);
            long startTime = date.getTime();
            long endTime = startTime + 7 * 24 * 60 * 60 * 1000;
            endDate = getStrTime(endTime + "", "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
        }
        return endDate;
    }

    /**
     * 判断是否同一天
     *
     * @param date
     * @param sameDate
     * @return boolean
     */
    public static boolean isSameDay(Date date, Date sameDate) {
        if (null == date || null == sameDate) {
            return false;
        }
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(sameDate);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        if (nowCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
                && nowCalendar.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)
                && nowCalendar.get(Calendar.DATE) == dateCalendar.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    /**
     * 计算剩余支付时间
     */
    public static int getRemainingTime(String creatTime, String systemTime, String autoCancelTime) {
        creatTime = creatTime.replace("T", " ");
        systemTime = systemTime.replace("T", " ");
        DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        long remainingTime = 0;
        try {
            long creat = df.parse(creatTime).getTime();
            creat = creat + Integer.parseInt(autoCancelTime) * 60 * 1000;//创建时间+15分钟
            long system = df.parse(systemTime).getTime();
            remainingTime = new Long(creat) - new Long(system);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt((remainingTime / 1000) + "");
    }

    /**
     * 计算剩余支付时间
     */
    public static int getRemainingTime(String creatTime, String systemTime, int minute) {
        creatTime = creatTime.replace("T", " ");
        systemTime = systemTime.replace("T", " ");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long remainingTime = 0;
        try {
            long creat = df.parse(creatTime).getTime();
            creat = creat + minute * 60 * 1000;//创建时间+15分钟
            long system = df.parse(systemTime).getTime();
            remainingTime = new Long(creat) - new Long(system);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt((remainingTime / 1000) + "");
    }


    /**
     * 当前时间
     *
     * @return 2015-10-23
     */
    public static String getNowDate() {
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//格式化对象
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);                       //设置当前日期
        return sdf.format(calendar.getTime());
    }

    /**
     * 当前时间 加或减多少天
     */
    public static String countDay(int amount) {
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//格式化对象
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);                     //设置当前日期
        calendar.add(Calendar.DATE, amount);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取是周几
     */
    public static String getWeek(String time) {
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//格式化对象
        Calendar calendar = Calendar.getInstance();//日历对象
        try {
            calendar.setTime(sdf.parse(time));    //设置当前日期
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String week = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                week = "周日";
                break;
            case Calendar.MONDAY:
                week = "周一";
                break;
            case Calendar.TUESDAY:
                week = "周二";
                break;
            case Calendar.WEDNESDAY:
                week = "周三";
                break;
            case Calendar.THURSDAY:
                week = "周四";
                break;
            case Calendar.FRIDAY:
                week = "周五";
                break;
            case Calendar.SATURDAY:
                week = "周六";
                break;
        }
        return week;
    }


    /**
     * 转换消息时间
     **/
    public static String getMessageTime(String time) {
        if ("".equals(replaceStrNULL(time))) {
            return "";
        }
        if (getNowDate().equals(formatToString(time, "yyyy-MM-dd"))) {
            //是当天
            return formatToString(time, "HH:mm");
        } else {
            return formatToString(time, "MM-dd");
        }
    }


    protected static String replaceStrNULL(String str) {
        return null == str || "null".equals(str) ? "" : str;
    }

    /**
     * 获取当前系统时间
     */
    public static String getTimeStamp() {
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(day);
    }

    /**
     * 获取当前系统时间
     */
    public static String getTimeStampa() {
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(day);
    }


    /**
     * 获取当前系统时间戳
     *
     * @return
     */
    public static String getDateToStamp() {
        long timeStamp = System.currentTimeMillis();
        String time = stampToDate(timeStamp);
        return time;
    }


    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    /**
     * 计算时间差（单位：天）
     *
     * @param startDate - "2015-11-23"
     * @param endDate   - "2015-12-20"
     * @return 27
     */
    public static String getTimeDifference(String startDate, String endDate) {
        if ("".equals(startDate) || "".equals(endDate)) {
            return "0";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date d1 = df.parse(endDate);
            Date d2 = df.parse(startDate);
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long second = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;

            return hours + ":" + minutes + ":" + second;
        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }

    public static String caseData(String dt) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String now = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分").format(date);
        return now.split(" ")[0];//年月日
    }

    public static String caseTime(String dt) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String now = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分").format(date);
        return now.split(" ")[1];//时分
    }

    /**
     * 判断时间是否在时间段内 开始时间大于结束时间返回true
     *
     * @param beginTime
     * @param endTime
     * @return true
     * Date1.after(Date2),当Date1大于Date2时，返回TRUE，当小于等于时，返回false；
     * Date1.before(Date2)，当Date1小于Date2时，返回TRUE，当大于等于时，返回false；
     * 如果业务数据存在相等的时候，而且相等时也需要做相应的业务判断或处理时，请注意。
     */
    public static boolean belongCalendar(Date beginTime, Date endTime) {

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (begin.after(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * String转Date
     *
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(str, pos);
        return strtodate;
    }

    public static Date stringToDate1(String str) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(str, pos);
        return strtodate;
    }

    /*
     *  判断当前时间是否在设置的dark mode时间段内
     *  @param date1: 开始时间（hh:mm）
     *  @param date2: 结束时间（hh:mm）
     */
    public static int getTimeCompareSize(String startTime, String endTime) {
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //正常情况下的逻辑操作.
                i = 3;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            long a = sdf.parse(date_str).getTime() / 1000;
            return a;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static List<Long> getWeekDayList(String date, String formatSrt) {
        // 存放每一天时间的集合
        List<Long> weekMillisList = new ArrayList<>();
        long dateMill = 0;
        try {
            // 获取date的毫秒值
            dateMill = getMillis(date, formatSrt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMill);
        // 本周的第几天
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        Log.e("本周第几天", weekNumber + "");
        // 获取本周一的毫秒值
        long mondayMill = dateMill - 86400000 * (weekNumber - 2);

        for (int i = 0; i < 7; i++) {
            weekMillisList.add(mondayMill + 86400000 * i);
        }
        return weekMillisList;
    }

    /**
     * 把格式化过的时间转换毫秒值
     *
     * @param time      时间
     * @param formatSrt 时间格式 如 yyyy-MM-dd
     * @return 当前日期的毫秒值
     */
    public static long getMillis(String time, String formatSrt) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(formatSrt);
        return format.parse(time).getTime();
    }

    /**
     * 将毫秒值格转换为时间 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param date
     * @param format 你要的时间格式 yyyy-MM-dd HH:mm:ss或者yyyy-MM-dd
     * @return 返回转换后的值
     */
    public static String formatDate(Long date, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }


    public static List<DateBean> getDateList() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("d");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        List<DateBean> dateBeans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            try {
                DateBean dateBean = new DateBean();
                if (i == 0) {
                    calendar.add(calendar.DATE, 0);//把日期往后增加一天.整数往后推,负数往前移动
                } else {
                    calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                }
                date = calendar.getTime(); //这个时间就是日期往后推一天的结果
                dateBean.setDate(formatter.format(date));
                dateBean.setDay(formatter1.format(date));
                dateBean.setWeek(getWeek(date));
                dateBeans.add(dateBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateBeans;
    }

    public static boolean currentTimebelongTheRange(String sysDate, String startTimeStringFormat, String endTimeStringFormat) throws Exception {
        String dateFormatString = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString);

        //first get now time calendar
        String nowTimeFormat = simpleDateFormat.format(stringToDate(sysDate));
        Date nowTimeData = simpleDateFormat.parse(nowTimeFormat);
        Calendar nowTimeCalendar = Calendar.getInstance();
        nowTimeCalendar.setTime(Objects.requireNonNull(nowTimeData));

        //second get start time calendar
        Date startTimeData = simpleDateFormat.parse(startTimeStringFormat);
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(Objects.requireNonNull(startTimeData));

        //third get end time calendar
        Date endTimeData = simpleDateFormat.parse(endTimeStringFormat);
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(Objects.requireNonNull(endTimeData));

        return nowTimeCalendar.after(startTimeCalendar) && nowTimeCalendar.before(endTimeCalendar);
    }


    /**
     * @param startDate
     * @param sysDate
     * @param userBean
     * @return
     */
    public static boolean isShowCourse(String startDate, String sysDate, UserBean userBean) {
        try {
            Date date = stringToDate(sysDate);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);// 获取小时
            int minute = calendar.get(Calendar.MINUTE);// 获取分钟

            if (hour > 21) {
                return true;
            } else if (hour == 21 && minute > 29) {
                return true;
            } else {
                if (hour == 21 && minute > 04 && minute < 30) {
                    if (userBean.getIsVip() == 1) {
                        if (getDateAToDD(startDate) == DateTimeHelper.getDateAdd4(sysDate)) {//查询数据日期是否服务器日期+4  防止用户修改手机系统日期
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            return false;
        }

    }


    public static int getDateAdd4(String sysDate) {
        Date date = stringToDate(sysDate);
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 4);//把日期往后增加4天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        return Integer.parseInt(formatter.format(date));
    }

    public static int getDateAToDD(String sysDate) {
        Date date = stringToDate1(sysDate);
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return Integer.parseInt(formatter.format(date));
    }


    public static String getWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String week = "";
        if ("周一".equals(sdf.format(date))) {
            week = "星期一";
        } else if ("周二".equals(sdf.format(date))) {
            week = "星期二";
        } else if ("周三".equals(sdf.format(date))) {
            week = "星期三";
        } else if ("周四".equals(sdf.format(date))) {
            week = "星期四";
        } else if ("周五".equals(sdf.format(date))) {
            week = "星期五";
        } else if ("周六".equals(sdf.format(date))) {
            week = "星期六";
        } else if ("周日".equals(sdf.format(date))) {
            week = "星期天";
        }
        return week;
    }


    public static long getSurplusTime(String endTime) {
        try {
            long between = 0;
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date end = dfs.parse(endTime);
            between = (end.getTime() - new java.util.Date().getTime());//除以1000是为了转换成秒
            return between;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }
    }

}
