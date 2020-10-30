package com.mm.android.deviceaddmodule.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.common.LCConfiguration;
import com.mm.android.deviceaddmodule.mobilecommon.dialog.LCAlertDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设备添加相关工具类
 *
 */
public class Utils4AddDevice {
    public static final int NETWORK_NONE = -1; //  无网络
    public static final int NETWORK_WIFI = 0;  //  wifi
    public static final int NETWORK_MOBILE = 1;  //  数据网络

    public static String strRegCodeFilter(String str) {

        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String strEx = "[0-9A-Za-z]";
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (!temp.matches(strEx)) {
                str = str.replace(temp, "");
                return strRegCodeFilter(str);
            }
        }
        return str;
    }

    public static String wifiPwdFilter(String str) {

        if (TextUtils.isEmpty(str)) {
            return str;
        }
        // TD:20780
        String chinese1 = "[\u2E80-\uA4CF]";
        String chinese2 = "[\uF900-\uFAFF]";
        String chinese3 = "[\uFE30-\uFE4F]";

        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(chinese1) || temp.matches(chinese2) || temp.matches(chinese3)) {
                str = str.replace(temp, "");
                return wifiPwdFilter(str);
            }
        }
        return str;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE); // 获取系统网络连接管理�?
        if (connectivity == null) { // 如果网络管理器为null
            return false; // 返回false表明网络无法连接
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo(); // 获取�?��的网络连接对�?
            if (info != null) { // 网络信息不为null
                for (NetworkInfo anInfo : info) { // 遍历网路连接对象
                    if (anInfo.isConnected()) { // 当有�?��网络连接对象连接上网络时
                        return true; // 返回true表明网络连接正常
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo curNetwork = connectivity.getActiveNetworkInfo();
            if (curNetwork != null && curNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static int getNetWorkState(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null) {
            NetworkInfo curNetwork = connectivity.getActiveNetworkInfo();
            if (curNetwork != null && curNetwork.isConnected()) {
                if (curNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    return NETWORK_WIFI;
                } else if (curNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return NETWORK_MOBILE;
                }
            }
        }
        return NETWORK_NONE;
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        boolean wifi= (wifiInfo.getSSID()==null) || wifiInfo.getSSID().equalsIgnoreCase("");
        return !wifi;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager == null){
                return false;
            }
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断是否只包含数字或大小写字母
     *
     * @param str
     * @return
     */
    public static boolean checkString(String str) {
        String regEx = "[0-9A-Za-z]*"; // 只能是数字以及个别字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static String filterInvalidString(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String numberAndAbc = "[a-zA-Z0-9]";
        StringBuilder buffer = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(numberAndAbc)) {
                buffer.append(temp);
            }
        }
        return buffer.toString();
    }

    public static String filterInvalidString4Type(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String numberAndAbc = "[a-zA-Z0-9-/\\\\]";
        StringBuilder buffer = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(numberAndAbc)) {
                buffer.append(temp);
            }
        }
        return buffer.toString();
    }

    /**
     * 是否是乐盒设备
     *
     * @param deviceMode
     * @return
     */
    public static boolean isDeviceTypeBox(String deviceMode) {
        if (deviceMode == null || TextUtils.isEmpty(deviceMode)) {
            return false;
        }
        if (deviceMode.equalsIgnoreCase("G10")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置下划线字体
     */
    public static SpannableString setSpannableString(Context context, int showResId, int showUnderLineResId) {

        String showResourceTip = context.getResources().getString(showResId);
        String showUnderLineResTip = context.getResources().getString(showUnderLineResId);
        String tip = showResourceTip + showUnderLineResTip;
        SpannableString ss = new SpannableString(tip);
        int start = showResourceTip.length();
        int end = showUnderLineResTip.length() + start;
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        MyURLSpan mus = new MyURLSpan(context.getResources().getString(R.string.assetfont_html));// 字体
        ForegroundColorSpan fcs = new ForegroundColorSpan(context.getResources()
                .getColor(R.color.lc_color_4ea7f2));
        ss.setSpan(mus, start, end, flag);
        ss.setSpan(fcs, start, end, flag);
        return ss;
    }

    /**
     * <p>
     * 设置下划线字体及点击事件
     * </p>
     */
    public static void setSpannableString(int showResId, int showUnderLineResId, OnClickListener listener, TextView view) {
        String showResourceTip = "";
        if (showResId != 0) {
            showResourceTip = view.getContext().getResources().getString(showResId);
        }
        String showUnderLineResTip = view.getContext().getResources().getString(showUnderLineResId);
        String tip = showResourceTip + showUnderLineResTip;
        SpannableString ss = new SpannableString(tip);
        int start = showResourceTip.length();
        int end = showUnderLineResTip.length() + start;
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        MyURLSpan mus = new MyURLSpan(view.getContext().getResources().getString(R.string.assetfont_html));// 字体
        mus.setOnClickListener(listener);
        ForegroundColorSpan fcs = new ForegroundColorSpan(view.getContext().getResources()
                .getColor(R.color.lc_color_4ea7f2));
        ss.setSpan(mus, start, end, flag);
        ss.setSpan(fcs, start, end, flag);
        view.setText(ss);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * <p>
     * 设置下划线字体及点击事件
     * </p>
     */
    public static void setSpannableString(int showResId, String showUnderLineResTip, OnClickListener listener, TextView view) {
        String showResourceTip = "";
        if (showResId != 0) {
            showResourceTip = view.getContext().getResources().getString(showResId);
        }
        if (showUnderLineResTip == null) {
            showUnderLineResTip = "";
        }
        String tip = showResourceTip + showUnderLineResTip;
        SpannableString ss = new SpannableString(tip);
        int start = showResourceTip.length();
        int end = showUnderLineResTip.length() + start;
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        MyURLSpan mus = new MyURLSpan(view.getContext().getResources().getString(R.string.assetfont_html));// 字体
        mus.setOnClickListener(listener);
        ForegroundColorSpan fcs = new ForegroundColorSpan(view.getContext().getResources()
                .getColor(R.color.lc_color_4ea7f2));
        ss.setSpan(mus, start, end, flag);
        ss.setSpan(fcs, start, end, flag);
        view.setText(ss);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static DisplayImageOptions mDeviceModeImageOptions;


    /**
     * 是否为TP1等设备，包括TP1 TC1 TK1 TC3 TK3 TC4 TC5 TC5S TP6、TP6C、TC6、TC6C、TP7
     */
    public static boolean isTp1And(String deviceModelName) {
        return LCConfiguration.TYPE_TC1.equals(deviceModelName) || LCConfiguration.TYPE_TK1.equals(deviceModelName)
                || LCConfiguration.TYPE_TC3.equals(deviceModelName) || LCConfiguration.TYPE_TK3.equals(deviceModelName)
                || LCConfiguration.TYPE_TC4.equals(deviceModelName) || LCConfiguration.TYPE_TC5.equals(deviceModelName)
                || LCConfiguration.TYPE_TC5S.equals(deviceModelName) || LCConfiguration.TYPE_TP1.equals(deviceModelName)
                || LCConfiguration.TYPE_TP6.equals(deviceModelName) || LCConfiguration.TYPE_TP6C.equals(deviceModelName)
                || LCConfiguration.TYPE_TC6.equals(deviceModelName) || LCConfiguration.TYPE_TC6C.equals(deviceModelName)
                || LCConfiguration.TYPE_TP7.equals(deviceModelName);
    }


    //根据域名获取
    public static String getAddDeviceHelpUrl(String host) {
        if (host.contains(":443")) {
            host = host.split(":")[0];
        }
        return "http://" + host + "/bindhelp.html";
    }


    /**
     *  * 检测设备新老版本， ture 表示新版本并且DHCP打开走单播流程， false 老版本/DHCP关闭走组播加单播流程。
     *  * @return
     *  
     */
    public static boolean checkDeviceVersion(DEVICE_NET_INFO_EX deviceInfo) {
        if(deviceInfo==null)
            return false;
        int flag = (deviceInfo.bySpecialAbility >> 2) & 0x03;
        return 0x00 != flag && 0x03 != flag;
    }


    /**
     *  * 检测设备获取的IP是否有效， ture 有效
     *  * @param deviceInfo
     *  * @return
     *  
     */
    public static boolean checkEffectiveIP(DEVICE_NET_INFO_EX deviceInfo) {
        if(deviceInfo==null)
            return false;
        int flag = (deviceInfo.bySpecialAbility >> 2) & 0x03;
        return 0x02 == flag;
    }

    /**
     * 生成二维码
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static Bitmap creatQRImage(String url, final int width, final int height) {
        try {
            if(TextUtils.isEmpty(url)) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, "0");  // 不要边距
            // 图像数据变换,使用矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片
            // 两个for循环是图片横列扫描的结果
            for(int y = 0; y < height; y++) {
                for(int x = 0; x <width; x++) {
                    if(bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片格式,使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
