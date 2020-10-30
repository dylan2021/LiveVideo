package com.lechange.demo.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {
    public static Date string2Date(String time) {
        try {
            String str = time;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = (Date) sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String dateFormatTiming(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String getTimeHMS(long time) {
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(time);
        String date1 = format1.format(date);
        return date1;
    }

    public static long parseMills(String dateTime) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            return 0;
        }
    }
}
