package com.common.openapi;

import android.content.Context;

public class ClassInstanceManager {
    private volatile static ClassInstanceManager classInstanceManager;
    private DeviceLocalCacheManager deviceLocalCacheManager;
    private DeviceDetailService deviceDetailService;
    private DeviceListService deviceListService;
    private DeviceRecordService deviceRecordService;
    private DeviceLocalCacheService deviceLocalCacheService;

    public static ClassInstanceManager newInstance() {
        if (classInstanceManager == null) {
            synchronized (ClassInstanceManager.class) {
                if (classInstanceManager == null) {
                    classInstanceManager = new ClassInstanceManager();
                }
            }
        }
        return classInstanceManager;
    }

    public void init(Context context) {
        deviceLocalCacheManager = new DeviceLocalCacheManager();
        deviceLocalCacheManager.init(context);
        deviceDetailService = new DeviceDetailService();
        deviceListService = new DeviceListService();
        deviceRecordService = new DeviceRecordService();
        deviceLocalCacheService = new DeviceLocalCacheService();
    }

    public DeviceLocalCacheManager getDeviceLocalCacheManager() {
        return deviceLocalCacheManager;
    }

    public DeviceDetailService getDeviceDetailService() {
        return deviceDetailService;
    }

    public DeviceListService getDeviceListService() {
        return deviceListService;
    }

    public DeviceRecordService getDeviceRecordService() {
        return deviceRecordService;
    }

    public DeviceLocalCacheService getDeviceLocalCacheService() {
        return deviceLocalCacheService;
    }

}
