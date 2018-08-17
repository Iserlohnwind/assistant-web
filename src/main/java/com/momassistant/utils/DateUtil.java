package com.momassistant.utils;

import jodd.datetime.JDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class DateUtil {

    private static final String DATE_FORMAT_TEMPLATE = "yyyy-MM-dd";
    public static Date addSeconds(Date from, int seconds) {
        JDateTime toConvert = new JDateTime(from);
        toConvert.addSecond(seconds);
        return toConvert.convertToDate();
    }

    public static Date addDays(Date from, int days) {
        JDateTime toConvert = new JDateTime(from);
        toConvert.addDay(days);
        return toConvert.convertToDate();
    }

    public static String format(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_TEMPLATE);
        return simpleDateFormat.format(date);
    }


    public static int getIntervalOfCalendarDay(Date from, Date to) {
        Calendar cFrom = Calendar.getInstance();
        Calendar cTo = Calendar.getInstance();
        cFrom.setTime(from);
        cTo.setTime(to);
        setTimeToMidnight(cFrom);
        setTimeToMidnight(cTo);
        long fromSecond = cFrom.getTimeInMillis() / 1000;
        long toSecond = cTo.getTimeInMillis() / 1000;
        long intervalSecond = fromSecond - toSecond;
        return (int)(intervalSecond/(3600*24));
    }

    private static void setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

    }
}
