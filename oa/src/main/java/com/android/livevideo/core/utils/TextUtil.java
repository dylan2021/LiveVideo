package com.android.livevideo.core.utils;

import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.volley.VolleyError;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具类
 * Gool
 */
public class TextUtil {

    public static boolean setInputOneDot;

    /**
     * @param str 被判的字符串
     * @return 如果任何一个字符串为null, 则返回true
     */
    public static boolean isAnyEmpty(String... str) {

        for (String s : str) {
            if (s == null || s.length() <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     *
     * @return 如果为空则返回 true
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() <= 0) {
            return true;
        }
        return false;
    }


    /**
     * 是否是合法字符串
     *
     * @param str 被校验的字符串
     * @param reg 正则表达式
     * @return
     */
    public static boolean isLegal(String str, String reg) {

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    /**
     * 检测是否是合法的手机号
     *
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone) {

        return isLegal(phone, "^1[3|4|5|7|8]\\d{9}$");
    }

    //把String转化为double
    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    /**
     * 格式化下载数值
     *
     * @param count 数值
     * @return 格式化后的字符串
     */
    public static String formatCount(long count) {

        String countStr;

        if (count > 1000) {
            countStr = Math.round(count / 1000) + "千";
        } else if (count > 10000) {
            countStr = Math.round(count / 10000) + "万";
        } else if (count > 100000) {
            countStr = Math.round(count / 100000) + "十万";
        } else if (count > 1000000) {
            countStr = Math.round(count / 1000000) + "百万";
        } else {
            countStr = count + "";
        }
        return countStr;
    }

    /**
     * 描述：是否是邮箱.
     *
     * @param str 指定的字符串
     * @return 是否是邮箱:是为true，否则false
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }


    /**
     * double 除法
     *
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div2(double d1, double d2, int scale) {
        //  当然在此之前，你要判断分母是否为0，
        //  为0你可以根据实际需求做相应的处理
        if (d2 == 0 || d1 == 0) {
            return 0;
        }
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String subTimeYmd(String time) {
        if (time == null || time.length() < 10) {
            return "";
        } else {
            return time.substring(0, 10);
        }

    }

    public static String subTimeYmdHm(String time) {
        if (time == null || time.length() < 16) {
            return "";
        } else {
            return time.substring(0, 16);
        }

    }

    public static String subTimeMDHm(String time) {
        if (time == null || time.length() < 16) {
            return "";
        } else {
            return time.substring(5, 16);
        }

    }

    /**
     * 某时间距离现在
     *
     * @return
     */
    public static int differentDaysByMillisecond(Date date2) {
        Date date = new Date();
        int days = (int) ((date2.getTime() - date.getTime()) / (1000 * 3600 * 24));
        return days + 1;
    }

    /**
     * 两个时间的间隔
     *
     * @return
     */
    public static int differentDaysByMillisecond2(Date endDate, Date startDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    public static String differentDayOfTime(Date endDate, Date startDate) {
        if (startDate == null || endDate == null) {
            return "";
        }

        BigDecimal b1 = new BigDecimal(Double.toString(endDate.getTime() - startDate.getTime()));
        BigDecimal b2 = new BigDecimal(Double.toString(1000 * 3600 * 24));
        String dayStr = b1.divide(b2, 1, BigDecimal.ROUND_HALF_UP).doubleValue() + "";

        return dayStr.replace(".0", "");
    }

    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    public static String toAllSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    public static String remove_N(String str) {
        return str == null ? "" : str;
    }

    public static String remove_0(String str) {
        if (str == null) {
            return "";
        }
        if (str.endsWith(".0") || str.endsWith(".00")) {
            return str.replace(".00", "").replace(".0", "");
        } else {
            return str;
        }
    }

    /**
     * 身份证号校验
     */
    public static boolean isIdCardNum(String idCard) {
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!idCard.matches(reg)) {
            return false;
        }
        return true;
    }

    public static Integer getAgeFromIDCard(String idCardNo) {

        int length = idCardNo.length();

        String dates = "";

        if (length > 9) {
            dates = idCardNo.substring(6, 10);

            SimpleDateFormat df = new SimpleDateFormat("yyyy");

            String year = df.format(new Date());

            int u = Integer.parseInt(year) - Integer.parseInt(dates);

            return u > 150 ? 0 : u < 0 ? 0 : u;

        } else {
            return 0;
        }

    }

    public static String parseArrayToString(CharSequence[] arr) {
        if (arr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int offset = arr.length - 1;
        for (int i = 0; i < offset; i++) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[offset]);

        return sb.toString();
    }

    public static String parseArrayToString(String[] arr) {
        if (arr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int offset = arr.length - 1;
        for (int i = 0; i < offset; i++) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[offset]).append("]");

        return sb.toString();
    }

    public static void initEmptyTv(BaseFgActivity context, TextView emptyTv) {
        emptyTv.setText(!NetUtil.isNetworkConnected(context) ? context.getString(R.string.no_network) : "");
        emptyTv.setVisibility(!NetUtil.isNetworkConnected(context) ? View.VISIBLE : View.GONE);
        Drawable noNetWork = context.getResources().getDrawable(!NetUtil.isNetworkConnected(context) ?
                R.drawable.ic_bg_no_network : R.drawable.ic_bg_no_data);
        emptyTv.setCompoundDrawablesWithIntrinsicBounds(null, noNetWork, null, null);
    }

    public static String getLast2(String text) {
        if (isEmpty(text)) {
            return "";
        }
        int length = text.length();
        return length > 2 ? text.substring(length - 2) : text;
    }

    //设置EditText禁止输入
    public static void setEtNoFocusable(EditText hourTv) {
        hourTv.setEnabled(false);
        hourTv.setFocusable(false);
        hourTv.setKeyListener(null);
    }

    public static int getArrIndex(String[] arr, String value) {
        if (arr == null) {
            return 0;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) {
                return i;
            }
        }
        return 0;
    }

    //接口返回的内容
    public static String getErrorMsg(VolleyError error) {
        if (null != error && error.networkResponse != null && error.networkResponse.data != null) {
            byte[] htmlBodyBytes = error.networkResponse.data;
            return new String(htmlBodyBytes);
        } else {
            return null;
        }

    }

    /**
     * 设置只能输入小数点后两位
     */
    public static void setInput2Dot(final EditText editText) {
        editText.setFilters(new InputFilter[]{new MyInputFilter(Constant.INPUT_DOT_LENGTH), new InputFilter.LengthFilter(Constant.INPUT_MAX_LENGTH)});
    }

    //------------只能输入1位小数--------------
    public static boolean setInput1Dot(EditText et, CharSequence s) {
        String numStr = s.toString();
        if (numStr.contains(".")) {
            if (s.length() - 1 - numStr.indexOf(".") > 1) {
                s = numStr.subSequence(0, numStr.indexOf(".") + 2);
                et.setText(s);
                et.setSelection(s.length());
            }
        }
        if (numStr.trim().substring(0).equals(".")) {
            s = "0" + s;
            et.setText(s);
            et.setSelection(2);
        }
        if (numStr.startsWith("0") && numStr.trim().length() > 1) {
            if (!numStr.substring(1, 2).equals(".")) {
                et.setText(s.subSequence(0, 1));
                et.setSelection(1);
                return true;
            }
        }
        return false;
    }

    //是否并池
    public static String[] getObjectPoolArr() {
        return new String[]{"不并池", "并池"};
    }

    //是否并池
    public static String getObjectPoolStr(int type) {
        if (type > getObjectPoolArr().length) {
            return "";
        }
        return getObjectPoolArr()[type - 1];
    }

    public static String getPYIndexStr(String strChinese) {
        try {
            StringBuffer buffer = new StringBuffer();
            strChinese = strChinese.replace("（", "").replace("）", "");// 特殊符号处理
            byte b[] = strChinese.getBytes("GBK");// 把中文转化成byte数组
            for (int i = 0; i < b.length; i++) {
                if ((b[i] & 255) > 128) {
                    int char1 = b[i++] & 255;
                    char1 <<= 8;// 左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方
                    int chart = char1 + (b[i] & 255);
                    buffer.append(getPYIndexChar((char) chart));
                    continue;
                }
                char c = (char) b[i];
                if (!Character.isJavaIdentifierPart(c))// 确定指定字符是否可以是 Java
                    // 标识符中首字符以外的部分。
                    c = 'A';
                buffer.append(c);
            }
            return buffer.toString().toUpperCase();
        } catch (Exception e) {
            System.out.println((new StringBuilder()).append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519")
                    .append(e.getMessage()).toString());
        }
        return null;
    }

    /**
     * 得到首字母
     */
    private static char getPYIndexChar(char strChinese) {

        int charGBK = strChinese;

        char result;

        if (charGBK >= 45217 && charGBK <= 45252)

            result = 'A';

        else if (charGBK >= 45253 && charGBK <= 45760)

            result = 'B';

        else if (charGBK >= 45761 && charGBK <= 46317)

            result = 'C';

        else if (charGBK >= 46318 && charGBK <= 46825)

            result = 'D';

        else if (charGBK >= 46826 && charGBK <= 47009)

            result = 'E';

        else if (charGBK >= 47010 && charGBK <= 47296)

            result = 'F';

        else if (charGBK >= 47297 && charGBK <= 47613)

            result = 'G';

        else if (charGBK >= 47614 && charGBK <= 48118)

            result = 'H';

        else if (charGBK >= 48119 && charGBK <= 49061)

            result = 'J';

        else if (charGBK >= 49062 && charGBK <= 49323)

            result = 'K';

        else if (charGBK >= 49324 && charGBK <= 49895)

            result = 'L';

        else if (charGBK >= 49896 && charGBK <= 50370)

            result = 'M';

        else if (charGBK >= 50371 && charGBK <= 50613)

            result = 'N';

        else if (charGBK >= 50614 && charGBK <= 50621)

            result = 'O';

        else if (charGBK >= 50622 && charGBK <= 50905)

            result = 'P';

        else if (charGBK >= 50906 && charGBK <= 51386)

            result = 'Q';

        else if (charGBK >= 51387 && charGBK <= 51445)

            result = 'R';

        else if (charGBK >= 51446 && charGBK <= 52217)

            result = 'S';

        else if (charGBK >= 52218 && charGBK <= 52697)

            result = 'T';

        else if (charGBK >= 52698 && charGBK <= 52979)

            result = 'W';

        else if (charGBK >= 52980 && charGBK <= 53688)

            result = 'X';

        else if (charGBK >= 53689 && charGBK <= 54480)

            result = 'Y';

        else if (charGBK >= 54481 && charGBK <= 55289)

            result = 'Z';

        else

            result = (char) (65 + (new Random()).nextInt(25));


        return result;

    }

    public static String getCameraStatus(int onlineStatus) {
        switch (onlineStatus) {
            case 1:
                return "在线";
            case 2:
                return "离线";
            default:
                return "未知";
        }

    }
}
