package com.momassistant.utils;

import jodd.datetime.JDateTime;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class DateUtil {

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
}
