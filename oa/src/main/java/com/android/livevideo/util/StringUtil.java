/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.livevideo.util;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Gool
 */
public class StringUtil {

    private StringUtil() {
        throw new IllegalArgumentException("工具类不能实例化");
    }

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isTextEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 清除掉指定特殊字符
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(CharSequence email) {
        if (isTextEmpty(email))
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isTextEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendlyTime(String sdate) {
        Date time = null;

        if (isInEasternEightZones()) {
            time = toDate(sdate);
        } else {
            time = transformTime(toDate(sdate), TimeZone.getTimeZone("GMT+08"),
                    TimeZone.getDefault());
        }

        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天 ";
        } else if (days > 2 && days < 31) {
            ftime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            ftime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            ftime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            ftime = "3个月前";
        } else {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
            defaultVaule = true;
        else
            defaultVaule = false;
        return defaultVaule;
    }

    /**
     * 根据不同时区，转换时间 2014年7月31日
     */
    public static Date transformTime(Date date, TimeZone oldZone,
                                     TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }

    public static boolean isNull(String str) {
        if (str == null || str.trim().length() == 0 || "".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String text) {
        if (TextUtils.isEmpty(text) || "null".equalsIgnoreCase(text)) {
            return true;
        }
        return false;
    }

    public static String getTransString(String orig) {
        if (isEmpty(orig)) {
            return "";
        }
        return orig;
    }

    public static String getText(String str) {
        if (str == null || str.trim().length() == 0 || "".equals(str) || "null".equals(str)) {

            return "";
        } else {
            return str;
        }
    }

    public static String getTextForReplace(String str, String replace) {
        if (str == null || str.trim().length() == 0 || "".equals(str) || "null".equals(str)) {

            return replace;
        } else {
            return str;
        }
    }


    /**
     * 判断字符串中是否存在空格
     *
     * @param tag 字符串参数
     * @return true 有空格 false 无空格
     */
    static boolean findSpace(String tag) {
        if (null == tag) {
            return false;
        }
        String c;
        for (int i = 0; i < tag.length(); i++) {
            c = tag.substring(i, i + 1);
            if (c.equals(" ")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤所有空格
     *
     * @param s
     * @return
     */
    public static String filterBlank(String s) {
        String s2 = "";
        if (s.indexOf(" ") != -1) {
            s2 = s.replaceAll(" ", "");
        } else {
            s2 = s;
        }
        return s2;
    }

    /**
     * 过滤符号"-"
     *
     * @param s
     * @return
     */
    public static String filterLine(String s) {
        String s2 = "";
        if (s.indexOf("-") != -1) {
            s2 = s.replaceAll("-", "");
        } else {
            s2 = s;
        }
        return s2;
    }

    /**
     * 删掉截取的部分
     *
     * @param inputSrc       字符串
     * @param startByteIndex 开始的字节索引
     * @param endByteIndex   结束的字节索引
     * @return
     */
    public static String deleteByteLength(String inputSrc, int startByteIndex, int endByteIndex) {
        StringBuilder sb = new StringBuilder(inputSrc);
        int inputByteLength = inputSrc.getBytes().length;
        if (startByteIndex < 0)
            startByteIndex = 0;
        if (endByteIndex > inputByteLength)
            endByteIndex = inputByteLength;
        char[] inputChars = inputSrc.toCharArray();
        int startByteLength = 0;
        int startIndex = 0;
        for (Character c : inputChars) {
            int charByteLength = c.toString().getBytes().length;
            startByteLength += charByteLength;
            if (startByteLength > startByteIndex)
                break;
            startIndex++;
        }
        int endByteLength = 0;
        int endIndex = 0;
        for (Character c : inputChars) {
            int charByteLength = c.toString().getBytes().length;
            endByteLength += charByteLength;
            if (endByteLength > endByteIndex)
                break;
            endIndex++;
        }
        return sb.delete(startIndex, endIndex).toString();
    }

    /**
     * 按字节长度截取字符串
     *
     * @param str     字符串参数
     * @param toCount 截取的字节长度
     * @param more    字符串末尾补上的字符串
     * @return 返回截取后的字符串
     */
    public static String substring(String str, int toCount, String more) {
        int reInt = 0;
        String reStr = "";
        if (str == null)
            return "";
        char[] tempChar = str.toCharArray();
        for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
            String s1 = str.valueOf(tempChar[kk]);
            byte[] b = s1.getBytes();
            reInt += b.length;
            reStr += tempChar[kk];
        }
        if (toCount == reInt || (toCount == reInt - 1))
            reStr += more;
        return reStr;
    }

    /**
     * 取字符串的前toCount个字符
     *
     * @param str     被处理字符串
     * @param toCount 截取长度
     * @param more    后缀字符串
     * @return String
     */
    public static String substringChar(String str, int toCount, String more) {
        int reInt = 0;
        String reStr = "";
        if (str == null)
            return "";
        char[] tempChar = str.toCharArray();
        for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
            String s1 = str.valueOf(tempChar[kk]);
            byte[] b = s1.getBytes();
            reInt += b.length;
            reStr += tempChar[kk];
        }
        if (toCount == reInt || (toCount == reInt - 1))
            reStr += more;
        return reStr;
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 判断数字
     *
     * @param str1
     * @return
     */
    public static boolean isNumber(String str1) {
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher matcher = pattern.matcher(str1);
        return matcher.matches();
    }


    /**
     * 判断数字+字母
     *
     * @param str1
     * @return
     */
    public static boolean isNumChars(String str1) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher = pattern.matcher(str1);
        return matcher.matches();
    }
    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */

    /**
     * 判断中文+字母+空格
     *
     * @param str1
     * @return
     */
    public static boolean isChineseZimus(String str1) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\u4e00-\u9fa5\u0020]+$");
        Matcher matcher = pattern.matcher(str1);
        return matcher.matches();
    }

    /**
     * 判断手机号格式(数字+长度)
     *
     * @param str1
     * @return
     */
    public static boolean idMobleNum(String str1) {
        Pattern pattern = Pattern.compile("^[0-9]{11}$");
        Matcher matcher = pattern.matcher(str1);
        return matcher.matches();
    }

    /**
     * 判断手机号格式是否正确(根据号段)
     *
     * @param str1
     * @return
     */
    public static boolean isMobile(String str1) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(str1);
        return matcher.matches();
    }

    /**
     * 邮箱链接跳转
     *
     * @param email
     * @return
     */
    public static String getEmailUrl(String email) {
        String result = "";
        String[] str = email.split("@");
        if (str != null && str.length > 0) {
            result = "http://mail." + str[1];
        }
        return result;
    }


    //去掉字符串末尾的空格和Tab符
    public static String strEndTrim(String str) {
        char[] chars = str.toCharArray();
        int n;
        for (n = chars.length - 1; n >= 0; n--) {
            if (chars[n] != ' ' && chars[n] != '\t' && chars[n] != '\n') {
                break;
            }
        }
        return str.substring(0, n + 1);
    }

    /**
     * 字符截取某部分  把截取的位置以某字符显示
     *
     * @param text       字符串
     * @param index      截取开始点
     * @param end        截取结束点
     * @param replaceStr 截取部分显示的字符
     * @return
     */
    public static String getReplaceText(String text, int index, int end, String replaceStr) {
        String mString = "";
        for (int i = 0; i < end - index; i++) {
            mString += replaceStr;
        }
        mString = text.substring(0, index) + mString + text.substring(end, text.length());
        return mString;
    }

    /**
     * 计算listview高度的方法
     *
     * @param listView lv
     * @param height   额外高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, int height) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + height;
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 计算listview count个高度
     *
     * @param listView
     * @param count    指定个数
     * @param height   额外高度
     */
    public static void setListViewHeightCount(ListView listView, int count, int height) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = count; i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (count - 1)) + height;
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 动态加载GridView 高度
     *
     * @param myGridView
     * @param numColumns 列数
     */
    public static void setGridViewHeightBasedOnChildren(GridView myGridView, int numColumns) {
        ListAdapter listAdapter = myGridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int count = 0;
        int size = listAdapter.getCount();
        int totalHeight = 0;
        if (size % numColumns == 0) {
            count = size / numColumns;
        } else {
            count = size / numColumns + 1;
        }
        for (int i = 0; i < count; i++) {
            View listItem = listAdapter.getView(i, null, myGridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = myGridView.getLayoutParams();
        params.height = totalHeight + myGridView.getVerticalSpacing() * (count - 1) + 0;
        myGridView.setLayoutParams(params);
    }

}
