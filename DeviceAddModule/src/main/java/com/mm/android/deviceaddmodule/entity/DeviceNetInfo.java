package com.mm.android.deviceaddmodule.entity;

import com.company.NetSDK.DEVICE_NET_INFO_EX;

/**
 * 封装DEVICE_NET_INFO_EX
 */

public class DeviceNetInfo {
    private DEVICE_NET_INFO_EX mDevNetInfoEx;

    private boolean mIsValid;

    public DeviceNetInfo(DEVICE_NET_INFO_EX devNetInfoEx) {
        mDevNetInfoEx = devNetInfoEx;
        mIsValid = true;
    }

    public DEVICE_NET_INFO_EX getDevNetInfoEx() {
        return mDevNetInfoEx;
    }

    public void setDevNetInfoEx(DEVICE_NET_INFO_EX devNetInfoEx) {
        this.mDevNetInfoEx = devNetInfoEx;
    }

    public boolean isValid() {
        return mIsValid;
    }

    public void setValid(boolean isValid) {
        this.mIsValid = isValid;
    }

    @Override
    public String toString() {
        return "byInitStatus:" + mDevNetInfoEx.byInitStatus + "bySpecialAbility:" + mDevNetInfoEx.bySpecialAbility;/*.append(":").append(mDevNetInfoEx)*/
    }
}
