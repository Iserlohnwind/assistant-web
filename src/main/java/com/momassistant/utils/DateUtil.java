package com.momassistant.utils;

import jodd.datetime.JDateTime;

import java.text.SimpleDateFormat;
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
}
