package com.momassistant.utils;

import jodd.datetime.JDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class DateUtil {

    private static final String DATE_FORMAT_TEMPLATE = "yyyy年MM月dd日";
    private static final String DATE_ZERO = "yyyy-MM-dd 00:00:00";


    public static Date parse(String date, String pattern) {
        try {
            return (new SimpleDateFormat(pattern)).parse(date);
        } catch (Exception e) {
            return null;
        }
    }
    public static Date addSeconds(Date from, int seconds) {
        JDateTime toConvert = new JDateTime(from);
        toConvert.addSecond(seconds);
        return toConvert.convertToDate();
    }

    public static Date getTomorrow() {
        JDateTime convert = new JDateTime(addDays(new Date(), 1));
        convert.set(convert.getYear(), convert.getMonth(), convert.getDay());
        return convert.convertToDate();
    }

    public static Date addDays(Date from, int days) {
        JDateTime toConvert = new JDateTime(from);
        toConvert.addDay(days);
        return toConvert.convertToDate();
    }

    public static Date addMonths(Date from, int days) {
        JDateTime toConvert = new JDateTime(from);
        toConvert.addMonth(days);
        return toConvert.convertToDate();
    }


    public static int getBabyMonthDiff(Date birthDay) {
        int diff = 0;
        JDateTime birthJDateTime = new JDateTime(birthDay);
        JDateTime nowJDateTime = new JDateTime(new Date());
        diff += (nowJDateTime.getYear() - birthJDateTime.getYear()) * 12;
        diff += nowJDateTime.getMonth() - birthJDateTime.getMonth();
        if (nowJDateTime.getDay() <= birthJDateTime.getDay()) {
            diff -= 1;
        }
        return diff;
    }

    public static String format(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_TEMPLATE);
        return simpleDateFormat.format(date);
    }

    public static boolean lessThan24Hour(Date date) {
        return new Date().getTime() - date.getTime() < 24 * 3600 * 1000;
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
