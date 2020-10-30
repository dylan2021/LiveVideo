package com.mm.android.deviceaddmodule.device_wifi;

/**
 * 设备管理常量类
 */

public class DeviceConstant {

    /**
     * Intent跳转的key常量或者一些常量key值
     */
    public interface IntentKey {
        String DEVICE_CURRENT_WIFI_INFO = "DEVICE_CURRENT_WIFI_INFO";
        String DEVICE_WIFI_CONFIG_INFO = "DEVICE_WIFI_CONFIG_INFO";
        String DHDEVICE_INFO = "DHDEVICE_INFO";
        String DHDEVICE_UNBIND = "DHDEVICE_UNBIND";
        String DHDEVICE_NEW_NAME = "DHDEVICE_NEW_NAME";

    }

    /**
     * Intent跳转请求码返回码常量
     */
    public interface IntentCode {
        int DEVICE_SETTING_WIFI_OPERATE = 208;
        int DEVICE_SETTING_WIFI_LIST = 209;
    }

}
