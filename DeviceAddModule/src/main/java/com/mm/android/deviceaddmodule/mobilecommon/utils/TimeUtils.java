package com.mm.android.deviceaddmodule.mobilecommon.utils;

import android.content.Context;
import android.text.TextUtils;

import com.company.NetSDK.NET_TIME;
import com.mm.android.deviceaddmodule.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    /**
     * 一天的总时间
     */
    public static final int TOTAL_SECONDS = 24 * 3600;

    public static final String LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static final String BLE_MESSAGE_FORMAT = "yyyy年MM月dd天HH:mm:ss";

    public static final String SMB_REQUEST_FORMAT = "yyyyMMdd'T'HHmmss";
    public static final String REQUEST_FORMAT = "yyyyMMddHHmmss";
    public static final String REQUEST_DATE_FORMAT = "yyyyMMdd";

    public static final String COMMON_TIME_FORMAT = "%04d%02d%02d%02d%02d%02d";
    public static final String DH_TIME_FORMAT = "%04d%02d%02dT%02d%02d%02d";

    public static final String SHORT_FORMAT = "HH:mm:ss";
    public static final String SHORT_FORMAT2 = "HHmmss";

    public static final String SIMPLE_FORMAT = "HH:mm";

    public static String displayTime(Context context,long inputTime, String todayFormatStr, String yesterdayFormatStr,
                                     String otherFormatStr) {
        // 日期格式化
        SimpleDateFormat todayFormat = todayFormatStr != null ? TimeUtils.getDateFormatWithUS(todayFormatStr) : null;
        SimpleDateFormat yesterdayFormat = yesterdayFormatStr != null ? TimeUtils.getDateFormatWithUS(yesterdayFormatStr) : null;
        SimpleDateFormat otherFormat = otherFormatStr != null ? TimeUtils.getDateFormatWithUS(otherFormatStr) : null;
        String timeStr = null;

        // 获取当前凌晨时间
        Calendar c = Calendar.getInstance();
        String getYear = String.valueOf(c.get(Calendar.YEAR));
        String getMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String getDayOfMonth = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if (getMonth.length() == 1) {
            getMonth = "0" + getMonth;
        }
        if (getDayOfMonth.length() == 1) {
            getDayOfMonth = "0" + getDayOfMonth;
        }
        String currentStartTimeStr = getYear + "-" + getMonth + "-" + getDayOfMonth + " 00:00:00";

        // 当前凌晨时间格式转换
        java.sql.Timestamp currentStartTime = java.sql.Timestamp.valueOf(currentStartTimeStr);
        long currentStart = currentStartTime.getTime();

        // 与当前凌晨时间相差秒数
        long timeGap = (currentStart - inputTime) / 1000;

        // 输入时间：年
        if (timeGap <= 0) {
            timeStr = todayFormat != null ? todayFormat.format(inputTime) :context.getResources().getString(R.string.common_today); // 今天格式：10:00
        } else if (timeGap > 0 && timeGap <= 24 * 60 * 60) {
            timeStr = yesterdayFormat != null ? yesterdayFormat.format(inputTime) :context.getResources().getString(R.string.common_yesterday); // 昨天格式：昨天
        } else {
            timeStr = otherFormat != null ? otherFormat.format(inputTime) : String.valueOf(inputTime); // 其他格式：15/09/03
        }

        return timeStr;
    }


    public static NET_TIME Date2NetTime(Date date)
    {
        NET_TIME time 	= new NET_TIME();
        time.dwYear 	= date.getYear() + 1900;
        time.dwMonth	= date.getMonth() + 1;
        time.dwDay		= date.getDate();
        time.dwHour		= date.getHours();
        time.dwMinute	= date.getMinutes();
        time.dwSecond	= date.getSeconds();
        return time;
    }

    public static Date NetTimeToData(NET_TIME netTime)
    {
        Date date = new Date((int)netTime.dwYear - 1900,
                (int)netTime.dwMonth - 1,
                (int)netTime.dwDay,
                (int)netTime.dwHour,
                (int)netTime.dwMinute,
                (int)netTime.dwSecond);
        return date;
    }

    /**
     * 比较两个NET_TIME的大小
     * @param time1
     * @param time2
     * @return	an int < 0 if time1 is less than time2, 0 if they are equal, and an int > 0 if time1 is greater.
     */
    public static int compareNetTime(NET_TIME time1, NET_TIME time2)
    {
        Date date1 = new Date((int)time1.dwYear - 1900,
                (int)time1.dwMonth - 1,
                (int)time1.dwDay,
                (int)time1.dwHour,
                (int)time1.dwMinute,
                (int)time1.dwSecond);

        Date date2 = new Date((int)time2.dwYear - 1900,
                (int)time2.dwMonth - 1,
                (int)time2.dwDay,
                (int)time2.dwHour,
                (int)time2.dwMinute,
                (int)time2.dwSecond);
        return date1.compareTo(date2);

    }
    public static boolean isToday(Calendar calendar) {
        if(calendar == null){
            return false;
        }
        Calendar now = Calendar.getInstance();
        boolean isToday = now.get(Calendar.YEAR) == calendar
                .get(Calendar.YEAR)
                && (now.get(Calendar.MONTH) + 1) == (calendar
                .get(Calendar.MONTH) + 1)
                && now.get(Calendar.DATE) == calendar.get(Calendar.DATE)
                ;
        return isToday;
    }

    public static boolean isTodayOrBefore(Calendar calendar) {
        if(calendar == null){
            return false;
        }
        Calendar now = Calendar.getInstance();

        boolean isTodayOrBefore = now.get(Calendar.YEAR) == calendar
                .get(Calendar.YEAR)
                && (now.get(Calendar.MONTH) + 1) == (calendar
                .get(Calendar.MONTH) + 1)
                && now.get(Calendar.DATE) == calendar.get(Calendar.DATE)
                || calendar.before(now);
        return isTodayOrBefore;
    }

    public static boolean isBeforeToday(Calendar calendar) {
        if(calendar == null){
            return false;
        }
        Calendar now = Calendar.getInstance();

        boolean isTodayOrAfter = now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && (now.get(Calendar.MONTH) + 1) == (calendar.get(Calendar.MONTH) + 1)
                && now.get(Calendar.DATE) == calendar.get(Calendar.DATE)
                || !calendar.before(now);
        return !isTodayOrAfter;
    }
    public static boolean isCurrentMonthOrBefore(Calendar calendar) {
        if(calendar == null){
            return false;
        }
        Calendar now = Calendar.getInstance();

        boolean isCurrentMonthOrBefore = now.get(Calendar.YEAR) == calendar
                .get(Calendar.YEAR)
                && (now.get(Calendar.MONTH) + 1) == (calendar
                .get(Calendar.MONTH) + 1)
                || !calendar.before(now);
        return !isCurrentMonthOrBefore;
    }

    /**
     * String2Date
     * <p>
     * </p>
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date stringToDate(String dateStr, String format) {
        SimpleDateFormat formatter = TimeUtils.getDateFormatWithUS(format);
        Date strtodate = null;
        try {
            strtodate = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
        return strtodate;
    }

    /**
     * 服务器0时区字符串转换才本地时间
     * @param dateStr
     * @param format
     * @return
     */
    public static long getLocalTimeByString(String dateStr, String format){
        if(TextUtils.isEmpty(dateStr)){
            return 0;
        }

        long time = TimeUtils.stringToDate(dateStr, format).getTime();
        return time = time + getTimeZone() * 1000; //加偏移量
    }


    /**
     * long转String
     * <p>
     * </p>
     * @param milliseconds
     *            the number of milliseconds since Jan. 1, 1970 GMT.
     * @param formatStr
     *            要转化成的时间格式
     * @return
     */
    public static String long2String(long milliseconds, String formatStr) {
        Date date = new Date(milliseconds);
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS(formatStr);
        return format.format(date);
    }

    /**
     * string2String
     * <p>
     * </p>
     * @param dateStr
     *            被转换的时间字符串
     * @param formatFrom
     *            转化前的时间格式
     * @param formatTo
     *            转化后的时间格式
     * @return
     */
    public static String string2String(String dateStr, String formatFrom, String formatTo) {
        SimpleDateFormat formatF = TimeUtils.getDateFormatWithUS(formatFrom);
        try {
            Date date = formatF.parse(dateStr);
            SimpleDateFormat formatT = TimeUtils.getDateFormatWithUS(formatTo);
            return formatT.format(date);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return dateStr;
        }
    }

    public static String string2StringForReport(String dateStr, String formatFrom, String formatTo) {
        SimpleDateFormat formatF = TimeUtils.getDateFormatWithUS(formatFrom);
        try {
            Date date = formatF.parse(dateStr);
            SimpleDateFormat formatT = TimeUtils.getDateFormatWithUS(formatTo);
            return formatT.format(date);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return "--";
        }
    }

    /**
     * date2Str
     * <p>
     * </p>
     * @param d
     *            被转化的Date
     * @param format
     *            转化格式
     * @return
     */
    public static String date2String(Date d, String format) {
        if(d == null) {
            return "";
        }
        SimpleDateFormat formatter = TimeUtils.getDateFormatWithUS(format);
        return formatter.format(d);
    }

    public static Date string2hhmm(String strDate) {
        Date mDate = stringToDate2(strDate, LONG_FORMAT);
        if (mDate == null) {
            mDate = stringToDate2(strDate, SHORT_FORMAT);
        }
        if (mDate == null) {
            mDate = stringToDate2(strDate, SIMPLE_FORMAT);
        }
        return mDate;
    }

    private static Date stringToDate2(String dateStr, String format) {
        SimpleDateFormat formatter = TimeUtils.getDateFormatWithUS(format);
        Date strtodate = null;
        try {
            strtodate = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strtodate;
    }



    public static String getNewRequestTime(long time){
        Date d = new Date(time);
        return date2String(d,REQUEST_DATE_FORMAT)+"T"+date2String(d,SHORT_FORMAT2);
    }

    public static long getResponseTime(String dateString){
        if(dateString==null)
            return 0;
        if(dateString.contains("T")){
            dateString = dateString.replace("T" ,"");
        }
        Date date = stringToDate(dateString,REQUEST_FORMAT);
        if (date == null)
            return 0;
        return date.getTime();
    }

    /**
     * 获取时间戳
     * @param dateString   0时区的时间
     * @return
     */
    public static long getResponseStamp(String dateString){
        if(dateString==null)
            return 0;
        if(dateString.contains("T")){
            dateString = dateString.replace("T" ,"");
        }

        SimpleDateFormat formatter = TimeUtils.getDateFormatWithUS(REQUEST_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date strtodate = null;
        try {
            strtodate = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (strtodate == null)
            return 0;
        return strtodate.getTime();
    }
    public static String setOnceTime(String time) {
        Date d = string2hhmm(time);
        Date now = new Date();
        if (d!=null && d.before(now)) {
            // 加一天
            long tomorro = d.getTime() + 24 * 60 * 60 * 1000;
            Date tomorr = new Date(tomorro);
            return date2String(tomorr, LONG_FORMAT);
        }
        return date2String(d, LONG_FORMAT);
    }

    /**
     * 格式化显示时间
     *
     * @param inputTime
     *            毫秒
     * @param inputTime 输入时间（UNIX时间戳毫秒）
     * @return
     */
    public static String displayTime(long inputTime, String todayFormatStr, String yesterdayFormatStr,
                                     String otherFormatStr) {
        // 日期格式化
        SimpleDateFormat todayFormat = todayFormatStr != null ? TimeUtils.getDateFormatWithUS(todayFormatStr) : null;
        SimpleDateFormat yesterdayFormat = yesterdayFormatStr != null ? TimeUtils.getDateFormatWithUS(yesterdayFormatStr) : null;
        SimpleDateFormat otherFormat = otherFormatStr != null ? TimeUtils.getDateFormatWithUS(otherFormatStr) : null;
        String timeStr = null;

        // 获取当前凌晨时间
        Calendar c = Calendar.getInstance();
        String getYear = String.valueOf(c.get(Calendar.YEAR));
        String getMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String getDayOfMonth = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if (getMonth.length() == 1) {
            getMonth = "0" + getMonth;
        }
        if (getDayOfMonth.length() == 1) {
            getDayOfMonth = "0" + getDayOfMonth;
        }
        String currentStartTimeStr = getYear + "-" + getMonth + "-" + getDayOfMonth + " 00:00:00";

        // 当前凌晨时间格式转换
        java.sql.Timestamp currentStartTime = java.sql.Timestamp.valueOf(currentStartTimeStr);
        long currentStart = currentStartTime.getTime();

        // 与当前凌晨时间相差秒数
        long timeGap = (currentStart - inputTime) / 1000;

        // 输入时间：年
        if (timeGap <= 0) {
            timeStr = todayFormat != null ? todayFormat.format(inputTime) : "今天  "; // 今天格式：10:00
        } else if (timeGap > 0 && timeGap <= 24 * 60 * 60) {
            timeStr = yesterdayFormat != null ? yesterdayFormat.format(inputTime) : "昨天  "; // 昨天格式：昨天
        } else {
            timeStr = otherFormat != null ? otherFormat.format(inputTime) : String.valueOf(inputTime); // 其他格式：15/09/03
        }

        return timeStr;
    }

    /**
     * 我的文件、报警消息列表悬停头的文案 今天 、昨天、 05/07、15/12/19
     *
     * @param strTimestamp
     *            输入时间（格式："yyyy-MM-dd HH:mm:ss"）
     * @return
     */
    public static String getStickxinzaieader(String strTimestamp) {
        // 日期格式化
        SimpleDateFormat xinzai = TimeUtils.getDateFormatWithUS("yy/MM/dd");
        SimpleDateFormat mh = TimeUtils.getDateFormatWithUS("MM/dd");
        SimpleDateFormat y = TimeUtils.getDateFormatWithUS("yyyy");
        String timeStr = null;

        // 获取当前凌晨时间
        Calendar c = Calendar.getInstance();
        String getYear = String.valueOf(c.get(Calendar.YEAR));
        String getMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String getDayOfMonth = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if (getMonth.length() == 1) {
            getMonth = "0" + getMonth;
        }
        if (getDayOfMonth.length() == 1) {
            getDayOfMonth = "0" + getDayOfMonth;
        }
        String currentStartTimeStr = getYear + "-" + getMonth + "-" + getDayOfMonth + " 00:00:00";

        // 当前凌晨时间格式转换
        java.sql.Timestamp currentStartTime = java.sql.Timestamp.valueOf(currentStartTimeStr);
        long currentStart = currentStartTime.getTime();

        // 输入时间格式转换
        java.sql.Timestamp time = java.sql.Timestamp.valueOf(strTimestamp);
        long timestamp = time.getTime();

        // 与当前凌晨时间相差秒数
        long timeGap = (currentStart - timestamp) / 1000;

        // 输入时间：年
        String year = y.format(timestamp);

        if (timeGap <= 0) {
            timeStr = "今天  "; // 格式：今天
        } else if (timeGap > 0 && timeGap <= 24 * 60 * 60) {
            timeStr = "昨天  "; // 格式：昨天
        } else if (year.equals(String.valueOf(getYear))) {
            timeStr = mh.format(timestamp); // （年内）MM月dd日
        } else {
            timeStr = xinzai.format(timestamp); // （年前）格式：yyyy年MM月dd日
        }

        return timeStr;
    }

    /**
     * 我的文件、报警消息列表悬停头的文案 今天 、昨天、 05/07、15/12/19
     *
     * @param timestamp 输入时间UNIX时间戳毫秒
     * @return
     */
    public static String getStickxinzaieader(long timestamp) {
        // 日期格式化
        SimpleDateFormat xinzai = TimeUtils.getDateFormatWithUS("yy/MM/dd");
        SimpleDateFormat mh = TimeUtils.getDateFormatWithUS("MM/dd");
        SimpleDateFormat y = TimeUtils.getDateFormatWithUS("yyyy");
        String timeStr = null;

        // 获取当前凌晨时间
        Calendar c = Calendar.getInstance();
        String getYear = String.valueOf(c.get(Calendar.YEAR));
        String getMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String getDayOfMonth = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if (getMonth.length() == 1) {
            getMonth = "0" + getMonth;
        }
        if (getDayOfMonth.length() == 1) {
            getDayOfMonth = "0" + getDayOfMonth;
        }
        String currentStartTimeStr = getYear + "-" + getMonth + "-" + getDayOfMonth + " 00:00:00";

        // 当前凌晨时间格式转换
        java.sql.Timestamp currentStartTime = java.sql.Timestamp.valueOf(currentStartTimeStr);
        long currentStart = currentStartTime.getTime();

        // 与当前凌晨时间相差秒数
        long timeGap = (currentStart - timestamp) / 1000;

        // 输入时间：年
        String year = y.format(timestamp);

        if (timeGap <= 0) {
            timeStr = "今天  "; // 格式：今天
        } else if (timeGap > 0 && timeGap <= 24 * 60 * 60) {
            timeStr = "昨天  "; // 格式：昨天
        } else if (year.equals(String.valueOf(getYear))) {
            timeStr = mh.format(timestamp); // （年内）MM月dd日
        } else {
            timeStr = xinzai.format(timestamp); // （年前）格式：yyyy年MM月dd日
        }

        return timeStr;
    }

    /**
     * 获取当前年的第一天
     * <p>
     * </p>
     *
     * @return
     */
    public static Date getCurrentYearFirst() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return getYearFirst(year);
    }
    /**
     * 获取某一年的第一天
     * <p>
     * </p>
     * @param year
     * @return
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date firstDay = calendar.getTime();
        return firstDay;
    }

    /**
     * 获取当前年的最后一天
     * <p>
     * </p>
     * @return
     */
    public static Date getCurrentYearLast() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return getYearLast(year);
    }
    /**
     * 获取某一年的最后一天
     * <p>
     * </p>
     * @param year
     * @return
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date lastDay = calendar.getTime();
        return lastDay;
    }

    /**
     * 在输入Date的基础上加/减某段时间，返回一个新的Date
     * <p>
     * </p>
     * @param date
     * @param field
     * @param value
     * @return
     */
    public static Date add(Date date, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.add(field, value);
        return calendar.getTime();
    }


    /**
     * 直播分享时间格式
     */
    public static  String getTime(long time) {
        time = time +System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("MM月dd日 HH:mm");
        return  format.format(date);

    }

    public static String getTimeSec() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("yyyy-MM-dd HH:mm:ss");
        return  format.format(date);

    }

    /*用户融合添加*/

    /*获取手机时区----UTC格式*/
    public static int getTimeZone(){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int zone = zoneOffset/1000;

        return zone;
    }

    /*获取时间字符串:从yyyyMMddTHHmmssZ时间格式到yyyy-MM-dd HH:mm*/
    public static String getTimeFromUTC(String timeStr) {
        if(TextUtils.isEmpty(timeStr)) {
            return "";
        }
        Date timeDate = TimeUtils.stringToDate(timeStr, "yyyyMMdd'T'HHmmss'Z'");
        if(timeDate == null) {
            return "";
        }
        long time = timeDate.getTime();
//        time = time + getTimeZone() * 1000; //加偏移量
        Date date = new Date(time);
        SimpleDateFormat sdf = TimeUtils.getDateFormatWithUS("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    /**
     * 我的文件、报警消息列表悬停头的文案 今天 、昨天、 05/07、15/12/19
     *
     * @param context 上下文
     * @param timestamp 输入时间UNIX时间戳毫秒
     * @return
     */
    public static String getStickxinzaieader(Context context,long timestamp) {
        // 日期格式化
        SimpleDateFormat xinzai = TimeUtils.getDateFormatWithUS("yy/MM/dd");
        SimpleDateFormat mh = TimeUtils.getDateFormatWithUS("MM/dd");
        SimpleDateFormat y = TimeUtils.getDateFormatWithUS("yyyy");
        String timeStr = null;

        // 获取当前凌晨时间
        Calendar c = Calendar.getInstance();
        String getYear = String.valueOf(c.get(Calendar.YEAR));
        String getMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String getDayOfMonth = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if (getMonth.length() == 1) {
            getMonth = "0" + getMonth;
        }
        if (getDayOfMonth.length() == 1) {
            getDayOfMonth = "0" + getDayOfMonth;
        }
        String currentStartTimeStr = getYear + "-" + getMonth + "-" + getDayOfMonth + " 00:00:00";

        // 当前凌晨时间格式转换
        java.sql.Timestamp currentStartTime = java.sql.Timestamp.valueOf(currentStartTimeStr);
        long currentStart = currentStartTime.getTime();

        // 与当前凌晨时间相差秒数
        long timeGap = (currentStart - timestamp) / 1000;

        // 输入时间：年
        String year = y.format(timestamp);

        if (timeGap <= 0) {
            timeStr = context.getResources().getString(R.string.common_today); // 格式：今天
        } else if (timeGap > 0 && timeGap <= 24 * 60 * 60) {
            timeStr = context.getResources().getString(R.string.common_yesterday); // 格式：昨天
        } else if (year.equals(String.valueOf(getYear))) {
            timeStr = mh.format(timestamp); // （年内）MM月dd日
        } else {
            timeStr = xinzai.format(timestamp); // （年前）格式：yyyy年MM月dd日
        }

        return timeStr;
    }

    //获取当前时区与0时区差值
    public static int getTimeOffset(){
        Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
        int offset=calendar.get(Calendar.ZONE_OFFSET);
        return offset/1000;
    }

    /**
     *
     * @param time 带时区的时间戳（时间单位为mS）
     * @return
     */
    public static long change2UTC(long time) {
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeStr=format.format(new Date(time));
        Date date = string2hhmm(timeStr);
        return date != null ? date.getTime() : 0;
    }

    /**
     *
     * @param time UTC时间戳（时间单位为mS）
     * @return
     */
    public static long change2Local(long time){
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getDefault());
        String timeStr=format.format(new Date(time + getTimeOffset() * 1000));
        Date date = string2hhmm(timeStr);
        return date != null ? date.getTime() : 0;
    }

    /**
     *将服务返回的时间字串格式转换为yyyy-MM-dd HH:mm:ss格式
     * @param time 服务返回设备时间
     * @return
     */
    public static String changeTimeFormat2Standard(String time){
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("yyyyMMdd'T'HHmmss");
        Date date=null;
        try {
            date=format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat standardFormat = TimeUtils.getDateFormatWithUS("yyyy-MM-dd HH:mm:ss");
        String standardTimeStr=date==null?"":standardFormat.format(date);
        return standardTimeStr;
    }

    /**
     *将服务返回的时间字串格式转换为MM-dd HH:mm格式
     * @param time 服务返回设备时间
     * @return
     */
    public static String changeTimeFormat2StandardNoYear(String time){
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("yyyyMMdd'T'HHmmss");
        Date date=null;
        try {
            date=format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat standardFormat = TimeUtils.getDateFormatWithUS("MM-dd HH:mm:ss");
        String standardTimeStr=date==null?"":standardFormat.format(date);
        return standardTimeStr;
    }

    /**
     *将服务返回的时间字串格式转换为yyyy-MM-dd HH:mm格式
     * @param time 服务返回设备时间
     * @return
     */
    public static String changeTimeFormat2StandardMin(String time){
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("yyyyMMdd'T'HHmmss");
        Date date=null;
        try {
            date=format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat standardFormat = TimeUtils.getDateFormatWithUS("yyyy-MM-dd HH:mm");
        String standardTimeStr=date==null?"":standardFormat.format(date);
        return standardTimeStr;
    }

    /**
     *将服务返回的时间字串格式转换为长整型
     * @param time 服务返回设备时间
     * @return
     */
    public static long changeTimeStrToStamp(String time){
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("yyyyMMdd'T'HHmmss");
        Date date=null;
        try {
            date=format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeStamp=date==null?0:date.getTime();
        return timeStamp;
    }

    /**
     *将服务返回的时间字串格式转换为outTimeformat格式
     * @param time 服务返回设备时间
     *             @param outTimeformat 自定义时间格式 , 0时区时间转换
     * @return
     */
    public static String changeTimeFormat2StandardMinByDateFormat(String time,String outTimeformat){
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS("yyyyMMdd'T'HHmmss'Z'");
        Date date=null;
        try {
            date=format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        SimpleDateFormat standardFormat = TimeUtils.getDateFormatWithUS(outTimeformat);
        if(date == null)return "";
        date.setTime(date.getTime()+ TimeUtils.getTimeZone() * 1000);//加偏移量
        String standardTimeStr= standardFormat.format(date);
        return standardTimeStr;
    }

    //返回 0时区 毫秒值
    public static long changeTime2UTCStamp(String time, String dateFormat) {
        SimpleDateFormat format = TimeUtils.getDateFormatWithUS(dateFormat);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date == null ? 0 : date.getTime();
    }

    /**
     * 不带年份的时间转时间戳
     * @param time
     * @return
     */
    public static long changeDateToUnix(String time) {
        SimpleDateFormat sdf = TimeUtils.getDateFormatWithUS("MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    /**
     * 转成大华标准时间 yyyyMMddTHHmmss
     * @return
     */
    public static String changeTimeFormat(String timeFormat) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            return String.format(timeFormat,
                    calendar.get(Calendar.YEAR),
                    (calendar.get(Calendar.MONTH) + 1),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 按周设置夏令时，保存时将
     * Mar 2nd Sun 00:00
     * 格式转换成
     * 3-2-0 00:00:00 格式
     * 3--1-1 代表三月最后一个周一
     * 月份 "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"
     第几个 "1st","2nd","3rd","4th","5th"
     周几 "Mon","Tue","Wed","Thu","Fri","Sat","Sun"

     月，是从1开始，1~12
     周是从1开始，1~4，以及-1，-1表示最后一周，或第四周是最后一周，也可以用4表示
     从0~6，0表示周日

     * @param time
     * @return
     */
    private static final String[] months = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private static final String[] numweeks = new String[]{"1st","2nd","3rd","4th","last"};
    private static final String[] weekDays = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

    public static String changeEngStrToNumStr(String time)
    {
        StringBuffer buffer = new StringBuffer();
        if(!TextUtils.isEmpty(time))
        {
            String[] array = time.split(" ");
            if(array!=null && array.length==4)
            {
                String month = array[0];
                String numWeek = array[1];
                String weekDay = array[2];
                String date = array[3];

                for(int i=0; i<months.length; i++)
                {
                    if(months[i].equalsIgnoreCase(month))
                    {
                        buffer.append(i+1).append("-");
                        break;
                    }
                }

                for(int i=0; i<numweeks.length; i++)
                {
                    if(numweeks[i].equalsIgnoreCase(numWeek) && !"last".equalsIgnoreCase(numWeek))
                    {
                        buffer.append(i+1).append("-");
                        break;
                    }else if("last".equalsIgnoreCase(numWeek)) //最后一周用-1表示
                    {
                        buffer.append("-1").append("-");
                        break;
                    }
                }

                for(int i=0; i<weekDays.length; i++)
                {
                    if(weekDays[i].equalsIgnoreCase(weekDay) && !"Sun".equalsIgnoreCase(weekDay))
                    {
                        buffer.append(i+1).append(" ");
                        break;
                    }else if("Sun".equalsIgnoreCase(weekDay))
                    {
                        //周日对应到0
                        buffer.append("0").append(" ");
                        break;
                    }
                }

                buffer.append(date).append(":00"); //最后增加 “秒”的两位
            }else
            {
                return "";
            }
        }
        return buffer.toString();
    }

    /**
     * 将数字格式字符转换为英文格式
     * 03-02-00 00:00:00
     * 03--1-00 00:00:00
     * 格式转换成
     * Mar 2nd Sun 00:00
     * @param time
     * @return
     */
    public static String changeNumStrToEngStr(String time)
    {
        StringBuffer buffer = new StringBuffer();
        boolean hasLastFlag = false; //是否包含最后一个的标识 -1

        if(time!=null)
        {
            if(time.contains("-1"))
            {
                time = time.replace("-1","last");
                hasLastFlag = true;
            }
            String[] array = time.split(" ");
            String[] dateArray = null;
            String[] timeArray = null;

            if(array!=null && array.length==2)
            {
                dateArray = array[0].split("-");
                timeArray = array[1].split(":");
            }else
            {
                return "";
            }

            String month = null;
            String numWeek = null;
            String weekDay = null;
            String hour = null;
            String minute = null;

            if(dateArray!=null && dateArray.length==3 && timeArray!=null && timeArray.length==3)
            {
                month = dateArray[0];
                numWeek = dateArray[1];
                weekDay = dateArray[2];
                hour = timeArray[0];
                minute = timeArray[1];
            }else
            {
                return "";
            }

            if(month!=null && month.length()==2)
            {
                if("0".equals(month.substring(0,1)))
                {
                    int index = Integer.parseInt(month.substring(1))-1;
                    if(index<0 || index>11)
                    {
                        return "";
                    }else
                    {
                        buffer.append(months[index]).append(" ");
                    }
                }else
                {
                    int index = Integer.parseInt(month)-1;
                    if(index<0 || index>11)
                    {
                        return "";
                    }else
                    {
                        buffer.append(months[index]).append(" ");
                    }
                }
            }

            if(hasLastFlag)
            {
                buffer.append("last").append(" ");

            }else if(numWeek!=null && !numWeek.equalsIgnoreCase("last"))
            {
                int index = Integer.parseInt(numWeek.substring(1))-1;
                if(index<0 || index >4)
                {
                    return "";
                }else
                {
                    buffer.append(numweeks[index]).append(" ");
                }
            }

            if(weekDay!=null  && weekDay.equals("00"))		//00 是周日，不能按常规判断
            {
                buffer.append(weekDays[6]).append(" ");
            }
            else if(weekDay!=null  && weekDay.length()==2 && !weekDay.equals("00"))
            {
                int index = Integer.parseInt(weekDay.substring(1))-1;
                if(index<0 || index >6)
                {
                    return "";
                }else
                {
                    buffer.append(weekDays[index]).append(" ");
                }
            }
            buffer.append(hour).append(":").append(minute);
        }
        return buffer.toString();
    }

    /**
     * 使用默认英语格式化时间，避免多语言环境下（如阿拉伯语）格式化的时间服务器无法识别
     * @param formate
     * @return
     */
    public static SimpleDateFormat getDateFormatWithUS(String formate){
        return  new SimpleDateFormat(formate, Locale.US);
    }

    /**
     * 计算两时间戳相差天数
     * @param startTime
     * @param endTime
     * @return
     */
    public static long dateDiff(long startTime, long endTime) {
        long nd = 24 * 60 * 60;// 一天的秒数
        long nh = 60 * 60;// 一小时的秒数
        long nm = 60;// 一分钟的秒数
        long ns = 1;// 一秒钟的秒数
        long diff;
        long day = 0;
        // 获得两个时间的毫秒时间差异
        diff = endTime - startTime;
        day = diff / nd;// 计算差多少天
        long hour = diff % nd / nh;// 计算差多少小时
        long min = diff % nd % nh / nm;// 计算差多少分钟
        long sec = diff % nd % nh % nm / ns;// 计算差多少秒
        if (day >= 1) {
            return day;
        } else {
            if (day == 0) {
                return 0;
            } else {
                return 0;
            }

        }
    }

    public static long getDateAddDays(int days){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTimeInMillis();

    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
}
