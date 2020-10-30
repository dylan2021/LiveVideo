package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;

import android.text.TextUtils;

/**
 * 设备模块帮助类
 **/
public class DeviceHelper {
    public static boolean isH1G(String deviceModelName){
        return !TextUtils.isEmpty(deviceModelName) && "H1G".equalsIgnoreCase(deviceModelName);
    }
}
