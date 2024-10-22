package com.xtree.base.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@SuppressLint("SimpleDateFormat")
public class TimeUtils {
    public final static String FORMAT_MM_DD = "MM/dd";
    public final static String FORMAT_YY_MM_DD = "yyyy-MM-dd";
    public final static String FORMAT_MM_DD_HH_MM = "MM月dd日 HH:mm";
    public final static String FORMAT_MM_DD_1 = "MM月dd日";
    public final static String FORMAT_HH_MM = "HH:mm";
    public final static String FORMAT_YY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_YY_MM_DD_HH_MM_SS_1 = "yyyy/MM/dd HH:mm:ss";
    public final static String FORMAT_YY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_YY_MM_DD_HH_MM_1 = "yyyy年MM月dd日 HH:mm";

    /**
     * 时间转换
     * <p>
     * 时间格式支持：<br>
     * Date: java.util.Date<br>
     * Long: 1417251375035 1417251375 <br>
     * String:<br>
     * 2014-11-29T16:56:15.035+0800 <br>
     * 2014-11-29 16:56:15.035 <br>
     * 2014-11-29 16:56:15
     *
     * @param time
     * @return MM-dd HH:mm / last year: yyyy-MM-dd HH:mm
     */
    public static String showTime(Object time) {
        if (time instanceof Date) {
            return parseTime((Date) time);
        } else if (time instanceof String) {
            return parseTime(utc2Date((String) time));
        } else if (time instanceof Long) {
            return parseTime(utc2Date((Long) time));
        }

        return null;
    }

    public static long Obj2UTC(Object time) {
        if (time instanceof Date) {
            return ((Date) time).getTime();
        } else if (time instanceof String) {
            return utc2Date((String) time).getTime();
        } else if (time instanceof Long) {
            return (utc2Date((Long) time)).getTime();
        }
        return 0;
    }

    /**
     * 时间格式支持:<br>
     * 2014-11-29T16:56:15.035+0800 <br>
     * 2014-11-29 16:56:15.035 <br>
     * 2014-11-29 16:56:15
     *
     * @param utc
     * @return Date
     */
    public static Date utc2Date(String utc) {
        if (null == utc || "".equals(utc)) {
            return null;
        }

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        if (utc.length() == 23) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        } else if (utc.length() == 19) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        Date date = null;
        try {
            date = formatter.parse(utc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 时间格式支持：<br>
     * 1417251375035 <br>
     * 1417251375
     *
     * @param utc
     * @return Date
     */
    public static Date utc2Date(long utc) {
        long milliseconds = (Long) utc;
        if (milliseconds < 10000000000L) {
            milliseconds = milliseconds * 1000;
        }
        return new Date(milliseconds);
    }

    /**
     * @param date
     * @return MM-dd HH:mm / last year: yyyy-MM-dd HH:mm
     */
    @SuppressWarnings("deprecation")
    public static String parseTime(Date date) {
        if (date == null) {
            date = new Date();
        }
        // yyyy-MM-dd HH:mm:ss
        DateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        if (date.getYear() != new Date().getYear()) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
        return format.format(date);
    }

    public static String getTime(Date date, String dateFormat) {
        if (date == null) {
            date = new Date();
        }
        DateFormat format = new SimpleDateFormat(dateFormat);

        return format.format(date);
    }

    public static int getDate() {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(format.format(new Date()));
    }

    public static String getCurDate() {
        return android.text.format.DateFormat.format("yyyyMMdd", new Date()).toString();
    }

    /**
     * 返回几号 两位数字
     *
     * @param date
     * @return dd
     */
    public static String parseDate(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat format = new SimpleDateFormat("dd");
        return format.format(date);
    }

    public static String parseTime(Date date, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * @param str
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formTime(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        try {
            String s = str.split("\\.")[0];
            String time = s.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "" + sdf.parse(time).getTime();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Function :getDate
     * <p>
     * <p>
     * Description :unix时间戳转换为Date Author :[hWX275113] 2015年5月4日
     *
     * @param unixDate
     * @return
     */
    public static Date unixTime2Date(String unixDate) {
        Date date = new Date(Long.parseLong(unixDate) * 1000);
        return date;
    }

    /**
     * 取带时区的时间
     *
     * @param date
     * @return
     * @author zWX232819
     * @date 2015年5月12日
     */
    public static String getTimeZ(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        return formatter.format(date);
    }

    public static Date strFormatDate(String time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String longFormatString(long time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static long longFormatDate(long time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(time);
        return strFormatDate(dateFormat.format(date), format).getTime();
    }

    /**
     * format格式转换成 format2 格式
     *
     * @param date    日期时间
     * @param format
     * @param format2
     * @return
     */
    public static String convert(String date, String format, String format2) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        SimpleDateFormat formatter2 = new SimpleDateFormat(format2);
        //formatter.format(date);
        try {
            return formatter2.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<String> calculateDates(LocalDate startDate, int daysToAdd) {
        List<String> dateList = new ArrayList<>();
        DateTimeFormatter formatter = null;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < daysToAdd; i++) {
            // 将日期格式化为字符串并添加到列表中
            dateList.add(startDate.format(formatter));

            // 计算下一天的日期
            startDate = startDate.plusDays(1);
        }

        return dateList;
    }

    public static List<Date> getNextDays(int daysToAdd) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (int i = 0; i < daysToAdd; i++) {
            dateList.add(strFormatDate(parseTime(calendar.getTime(), FORMAT_YY_MM_DD), FORMAT_YY_MM_DD));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateList;
    }

    public static List<Date> getLastDays(int daysToAdd) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (int i = 0; i < daysToAdd; i++) {
            dateList.add(strFormatDate(parseTime(calendar.getTime(), FORMAT_YY_MM_DD), FORMAT_YY_MM_DD));
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        return dateList;
    }

    /**
     * 获取指定时间N天后的时间
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addDays(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }

    /**
     * 秒数转换成分秒
     *
     * @param s
     * @return
     */
    public static String sToMs(int s) {
        long milliseconds = s * 1000;
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;

        String strMinutes = String.valueOf(minutes);
        String strSeconds = String.valueOf(seconds);

        if (minutes < 10) {
            strMinutes = "0" + minutes;
        }
        if (seconds < 10) {
            strSeconds = "0" + seconds;
        }
        return String.format("%s : %s", strMinutes, strSeconds);
    }

}
