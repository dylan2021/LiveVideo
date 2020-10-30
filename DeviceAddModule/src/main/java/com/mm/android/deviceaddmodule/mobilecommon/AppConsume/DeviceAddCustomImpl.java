package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;

import android.app.Activity;
import android.content.Context;

import com.mm.android.deviceaddmodule.helper.ActivityHelper;


public class DeviceAddCustomImpl implements IDeviceAddCustom {

    private volatile static DeviceAddCustomImpl deviceAddCustom;

    public static DeviceAddCustomImpl newInstance() {
        if (deviceAddCustom == null) {
            synchronized (DeviceAddCustomImpl.class) {
                if (deviceAddCustom == null) {
                    deviceAddCustom = new DeviceAddCustomImpl();
                }
            }
        }
        return deviceAddCustom;
    }

    @Override
    public void goFAQWebview(Context context) {

    }

    @Override
    public void goHomePage(Context context) {
        Activity activity=ActivityHelper.getCurrentActivity();
        if (activity!=null){
            activity.finish();
        }
    }

    @Override
    public void uninit() {

    }
}
