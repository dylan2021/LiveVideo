package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;


import com.mm.android.deviceaddmodule.provider.DeviceAddProvider;

/**
 * 功能接口管理类
 */

public class ProviderManager {

    public static IApp getAppProvider() {
        return AppProvider.newInstance();
    }
    public static IDeviceAdd getDeviceAddProvider() {
        return DeviceAddProvider.newInstance();
    }

    public static IDeviceAddCustom getDeviceAddCustomProvider() {
        return DeviceAddCustomImpl.newInstance();
    }
}
