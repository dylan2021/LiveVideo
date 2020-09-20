package com.android.livevideo.util;

import android.util.Log;

import com.android.livevideo.App;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间类型的转换
 */
public class TimeUtils {

    public static String getTimeYM(long time) {
        if (time == 0) {
            return "";
        }
        sdf_Ym.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_Ym.format(new Date(time));
    }


    private static SimpleDateFormat sdf_YmdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf_YmdHm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat sdf_Ymd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat sdf_Ym = new SimpleDateFormat("yyyy-MM");


    public static String getTimeYYYYMMDD(long time) {
        if (time == 0) {
            return "";
        }
        sdf_YYYYMMDD.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_YYYYMMDD.format(new Date(time));
    }

    public static String getTimeYmdHm(long time) {
        if (time == 0) {
            return "";
        }
        sdf_YmdHm.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_YmdHm.format(new Date(time));
    }

    public static String getTimeYmdHms(long time) {
        if (time == 0) {
            return "";
        }
        sdf_YmdHms.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_YmdHms.format(new Date(time));
    }

    public static double getTripInfoDay(long startTime, long endTime) {
        double diff = getAttendTotalPeroid(startTime, endTime);//出差时长
        Log.d("", "出差时长:"+diff);

        double attendHours = getAttendHours8(startTime);//设置的上下班时长

        int days = (int) (diff / attendHours);
        Log.d("", "出差天数一:"+days);

        double hours = diff - days * attendHours;
        Log.d("", "出差天数一剩余小时:"+hours);
        double remainderDay = (hours > 0 & hours <= attendHours / 2) ? 0.5 : hours > attendHours / 2 ? 1 : 0;
        double day = days + remainderDay;
        return day;
    }

    /**
     * 获取上班时长   8小时
     */
    public static double getAttendHours8(long startTime) {
        if (startTime == 0 ) {
            return 0;
        }
        Date startData = new Date(startTime);

        long startAmTime = getAmPmTime(startData, App.amTime);
        long startPmTime = getAmPmTime(startData, App.pmTime);

        double attendHours8 = getDiffHours8(startAmTime,startPmTime) - 1.5;//午休
        return attendHours8;
    }

    /**
     * 考勤时长
     */
    public static double getAttendTotalPeroid(long startTime, long endTime) {
        String noon_start = "12:00:00";
        String noon_end = "13:30:00";
        double TOTAL_HOURS = 0;
        if (startTime == 0 || endTime == 0) {
            return 0;
        }
        long ONE_DAY_MS = 24 * 60 * 60 * 1000;
        Date date_start = new Date(startTime);
        Date date_end = new Date(endTime);
        //计算日期从开始时间于结束时间的0时计算
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(date_start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(date_end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        int dayNum = (int) ((toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (ONE_DAY_MS));
        Date startData = new Date(startTime);
        Date endDate = new Date(endTime);
        long startAmTime = getAmPmTime(startData, App.amTime);
        long start12Time = getAmPmTime(startData, noon_start);//午休开始
        long start14Time = getAmPmTime(startData, noon_end);//午休结束
        long startPmTime = getAmPmTime(startData, App.pmTime);

        long endAmTime = getAmPmTime(endDate, App.amTime);
        long end12Time = getAmPmTime(endDate, noon_start);
        long end14Time = getAmPmTime(endDate, noon_end);
        long endPmTime = getAmPmTime(endDate, App.pmTime);

        double amAttend = betweenHours(startAmTime, start12Time);
        double pmAttend = betweenHours(start14Time, startPmTime);
        if (dayNum > 0) {
            for (int i = 0; i <= dayNum; i++) {
                if (i == 0) {//开始第一天
                    if (startTime >= startPmTime) {
                    } else if (startTime > startAmTime) {
                        if (startTime >= start12Time && startTime <= start14Time) {//12:00-14:00
                            TOTAL_HOURS = TOTAL_HOURS + pmAttend;

                        } else if (startTime < start12Time) {//9:00-12:00
                            double amPeriod = betweenHours(startTime, start12Time);
                            TOTAL_HOURS = TOTAL_HOURS + amPeriod + pmAttend;
                        } else {//14:00-17:00
                            double pmPeriod = betweenHours(startTime, startPmTime);
                            TOTAL_HOURS = TOTAL_HOURS + pmPeriod;
                        }
                    } else {
                        TOTAL_HOURS = TOTAL_HOURS + getAttendHours8(startTime);
                    }
                } else if (i == dayNum) {//结束时间
                    if (endTime >= endPmTime) {
                        TOTAL_HOURS = TOTAL_HOURS + getAttendHours8(startTime);
                    } else if (endTime > endAmTime) {
                        if (endTime >= end12Time && endTime <= end14Time) {//12:00-14:00
                            TOTAL_HOURS = TOTAL_HOURS + amAttend;
                        } else if (endTime < end12Time) {//9:00-12:00
                            double amPeriod = betweenHours(endAmTime, endTime);
                            TOTAL_HOURS = TOTAL_HOURS + amPeriod;
                        } else {//13:30-17:00
                            double pmPeriod = betweenHours(end14Time, endTime);
                            TOTAL_HOURS = TOTAL_HOURS + amAttend + pmPeriod;
                        }
                    } else {

                    }

                } else {//工作时间
                    TOTAL_HOURS = TOTAL_HOURS + getAttendHours8(startTime);
                }
            }
        } else {//此时在同一天之内

            if (startTime <= startAmTime) {//9.00之前
                if (endTime < start12Time) {
                    TOTAL_HOURS = TOTAL_HOURS + betweenHours(startAmTime, endTime);
                } else if (endTime <= start14Time) {//12-14
                    TOTAL_HOURS = TOTAL_HOURS + amAttend;
                } else if (endTime > start14Time && endTime < startPmTime) {//14-17
                    double pmPeriod = betweenHours(end14Time, endTime);
                    TOTAL_HOURS = TOTAL_HOURS + amAttend + pmPeriod;
                } else {
                    TOTAL_HOURS = TOTAL_HOURS + getAttendHours8(startTime);
                }
                //开始时间在9:00-12:00
            } else if (startTime >= startAmTime && startTime <= start12Time) {
                if (endTime < start12Time) {
                    TOTAL_HOURS = TOTAL_HOURS + betweenHours(startTime, endTime);
                } else if (endTime <= start14Time) {//12-14
                    TOTAL_HOURS = TOTAL_HOURS + betweenHours(startTime, start12Time);
                } else if (endTime > start14Time && endTime < startPmTime) {//14-17
                    double amPeriod = betweenHours(startTime, start12Time);
                    double pmPeriod = betweenHours(end14Time, endTime);
                    TOTAL_HOURS = TOTAL_HOURS + amPeriod + pmPeriod;
                } else {
                    double amPeriod = betweenHours(startTime, start12Time);
                    TOTAL_HOURS = TOTAL_HOURS + amPeriod + pmAttend;
                }
                //开始时间在 12:00-14:00
            } else if (startTime >= start12Time && startTime <= start14Time) {
                if (endTime <= start14Time) {
                } else if (endTime >= startPmTime) {
                    TOTAL_HOURS = TOTAL_HOURS + pmAttend;
                } else {
                    double pmPeriod = betweenHours(end14Time, endTime);
                    TOTAL_HOURS = TOTAL_HOURS + pmPeriod;
                }
                //开始时间在 14:00-17:00
            } else if (startTime >= start14Time && startTime <= startPmTime) {
                if (endTime <= startPmTime) {
                    double pmPeriod = betweenHours(startTime, endTime);
                    TOTAL_HOURS = TOTAL_HOURS + pmPeriod;
                } else {
                    double pmPeriod = betweenHours(startTime, startPmTime);
                    TOTAL_HOURS = TOTAL_HOURS + pmPeriod;
                }
            } else {//不用管
            }

        }
        return TOTAL_HOURS<0?0:TOTAL_HOURS;
    }
    /* */

    /**
     * 加班时长
     */
    public static double getAttendHourOvertime(long startTime, long endTime) {
        String noon_start = "12:00:00";
        double ATTEND_HOURS_22 = 22.5;
        String noon_end = "13:30:00";
        double TOTAL_HOURS = 0;
        if (startTime == 0 || endTime == 0) {
            return 0;
        }
        long ONE_DAY_MS = 24 * 60 * 60 * 1000;
        Date date_start = new Date(startTime);
        Date date_end = new Date(endTime);
        //计算日期从开始时间于结束时间的0时计算
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(date_start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(date_end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        int dayNum = (int) ((toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (ONE_DAY_MS));
        Date startData = new Date(startTime);
        Date endDate = new Date(endTime);
        long startAmTime = getAmPmTime(startData, "00:00");
        long start12Time = getAmPmTime(startData, noon_start);//午休开始
        long start14Time = getAmPmTime(startData, noon_end);//午休结束
        long startPmTime = getAmPmTime(startData, "24:00");

        long endAmTime = getAmPmTime(endDate, "00:00");
        long end12Time = getAmPmTime(endDate, noon_start);
        long end14Time = getAmPmTime(endDate, noon_end);
        long endPmTime = getAmPmTime(endDate, "24:00");

        double amAttend = betweenHours(startAmTime, start12Time);
        double pmAttend = betweenHours(start14Time, startPmTime);
        if (dayNum > 0) {
            for (int i = 0; i <= dayNum; i++) {
                if (i == 0) {//开始第一天
                    if (startTime >= startPmTime) {
                    } else if (startTime > startAmTime) {
                        if (startTime >= start12Time && startTime <= start14Time) {//12:00-14:00
                            TOTAL_HOURS = TOTAL_HOURS + pmAttend;

                        } else if (startTime < start12Time) {//9:00-12:00
                            double amPeriod = betweenHours(startTime, start12Time);
                            TOTAL_HOURS = TOTAL_HOURS + amPeriod + pmAttend;
                        } else {//14:00-17:00
                            double pmPeriod = betweenHours(startTime, startPmTime);
                            TOTAL_HOURS = TOTAL_HOURS + pmPeriod;
                        }
                    } else {
                        TOTAL_HOURS = TOTAL_HOURS + ATTEND_HOURS_22;
                    }
                } else if (i == dayNum) {//结束时间
                    if (endTime >= endPmTime) {
                        TOTAL_HOURS = TOTAL_HOURS +ATTEND_HOURS_22;
                    } else if (endTime > endAmTime) {
                        if (endTime >= end12Time && endTime <= end14Time) {//12:00-14:00
                            TOTAL_HOURS = TOTAL_HOURS + amAttend;
                        } else if (endTime < end12Time) {//9:00-12:00
                            double amPeriod = betweenHours(endAmTime, endTime);
                            TOTAL_HOURS = TOTAL_HOURS + amPeriod;
                        } else {//13:30-17:00
                            double pmPeriod = betweenHours(end14Time, endTime);
                            TOTAL_HOURS = TOTAL_HOURS + amAttend + pmPeriod;
                        }
                    } else {

                    }

                } else {//工作时间
                    //TOTAL_HOURS = TOTAL_HOURS + getAttendHours8(startTime);
                    TOTAL_HOURS = TOTAL_HOURS + ATTEND_HOURS_22;
                }
            }
        } else {//此时在同一天之内

            if (startTime <= startAmTime) {//9.00之前
                if (endTime < start12Time) {
                    TOTAL_HOURS = TOTAL_HOURS + betweenHours(startAmTime, endTime);
                } else if (endTime <= start14Time) {//12-14
                    TOTAL_HOURS = TOTAL_HOURS + amAttend;
                } else if (endTime > start14Time && endTime < startPmTime) {//14-17
                    double pmPeriod = betweenHours(end14Time, endTime);
                    TOTAL_HOURS = TOTAL_HOURS + amAttend + pmPeriod;
                } else {
                    TOTAL_HOURS = TOTAL_HOURS + ATTEND_HOURS_22;
                }
                //开始时间在9:00-12:00
            } else if (startTime >= startAmTime && startTime <= start12Time) {
                if (endTime < start12Time) {
                    TOTAL_HOURS = TOTAL_HOURS + betweenHours(startTime, endTime);
                } else if (endTime <= start14Time) {//12-14
                    TOTAL_HOURS = TOTAL_HOURS + betweenHours(startTime, start12Time);
                } else if (endTime > start14Time && endTime < startPmTime) {//14-17
                    double amPeriod = betweenHours(startTime, start12Time);
                    double pmPeriod = betweenHours(end14Time, endTime);
                    TOTAL_HOURS = TOTAL_HOURS + amPeriod + pmPeriod;
                } else {
                    double amPeriod = betweenHours(startTime, start12Time);
                    TOTAL_HOURS = TOTAL_HOURS + amPeriod + pmAttend;
                }
                //开始时间在 12:00-14:00
            } else if (startTime >= start12Time && startTime <= start14Time) {
                if (endTime <= start14Time) {
                } else if (endTime >= startPmTime) {
                    TOTAL_HOURS = TOTAL_HOURS + pmAttend;
                } else {
                    double pmPeriod = betweenHours(end14Time, endTime);
                    TOTAL_HOURS = TOTAL_HOURS + pmPeriod;
                }
                //开始时间在 14:00-17:00
            } else if (startTime >= start14Time && startTime <= startPmTime) {
                if (endTime <= startPmTime) {
                    double pmPeriod = betweenHours(startTime, endTime);
                    TOTAL_HOURS = TOTAL_HOURS + pmPeriod;
                } else {
                    double pmPeriod = betweenHours(startTime, startPmTime);
                    TOTAL_HOURS = TOTAL_HOURS + pmPeriod;
                }
            } else {//不用管
            }

        }
        return TOTAL_HOURS<0?0:TOTAL_HOURS;
    }

    /*两个时间距离多少小时*/
    public static double getDiffHours8(long startTime, long endTime ) {
        if (startTime == 0 || endTime == 0) {
            return 0;
        }
        try {
            long diff = endTime - startTime;//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            double minHour = (minutes > 0 & minutes <= 30) ? 0.5 : minutes > 30 ? 1 : 0;
            return days * 24 + hours + minHour;
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getDiffHours1(long endTime, long startTime) {
        long oneHour = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long totalMillis = endTime - startTime;
        long hour = totalMillis / oneHour + (totalMillis % oneHour > 0 ? 1 : 0);
        return hour;
    }

    public static long getDiffHours(long startTime, long endTime) {
        long oneHour = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long totalMillis = endTime - startTime;
        long hour = totalMillis / oneHour;
        return hour;
    }

    private static long getAmPmTime(Date amPmDate, String amPm) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(amPmDate);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(amPm.substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.valueOf(amPm.substring(3, 5)));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    public static int getDifferentDays(long startTime, long endTime) {
        int days = (int) ((endTime - startTime) / (1000 * 3600 * 24));
        return days;
    }

    public static double betweenHours(long startTime, long endTime) {
        if (startTime == 0 || endTime == 0) {
            return 0;
        }
        try {
            long diff = endTime - startTime;//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            double minHour = (minutes > 0 & minutes <= 30) ? 0.5 : minutes > 30 ? 1 : 0;
            return days * 24 + hours + minHour;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String betweenHoursAttend(long startTime, long endTime) {
        if (startTime == 0 || endTime == 0) {
            return "";
        }
        try {
            long diff = endTime - startTime;//这样得到的差值是微秒级别

            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            double minHour = (minutes > 0 & minutes <= 30) ? 0.5 : minutes > 30 ? 1 : 0;
            return days * 24 + hours + minHour + "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * local时间转换成UTC时间
     *
     * @return
     */
    public static String millonsToUTC(long millons) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return format.format(new Date(millons));
    }

    public static SimpleDateFormat getFormatYmdHm() {
        return sdf_YmdHm;
    }

    public static SimpleDateFormat getFormatYmdHms() {
        return sdf_YmdHms;
    }

    public static SimpleDateFormat getFormatYmd() {
        return sdf_Ymd;
    }

    public static long getTodayZeroTime() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }

    public static long getTodayZeroTime(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long date = calendar.getTime().getTime();
        return date;
    }

    public static String getTimeYmd(long time) {
        if (time == 0) {
            return "";
        }
        sdf_Ymd.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_Ymd.format(new Date(time));
    }
}
