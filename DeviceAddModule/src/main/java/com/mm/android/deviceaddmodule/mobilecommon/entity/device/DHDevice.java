package com.mm.android.deviceaddmodule.mobilecommon.entity.device;

import com.mm.android.deviceaddmodule.mobilecommon.annotation.DeviceState;
import com.mm.android.deviceaddmodule.mobilecommon.entity.DataInfo;

/**
 * 设备结构体
 */
public class DHDevice extends DataInfo implements Cloneable {
    private String deviceId;                            // 设备序列号
    private String name;                                // 设备名称
    private String status;                              // online-在线 offline-在线 upgrading-升级中 sleep-休眠
    private String wifiTransferMode;

    public String getWifiTransferMode() {
        return wifiTransferMode;
    }

    public void setWifiTransferMode(String wifiTransferMode) {
        this.wifiTransferMode = wifiTransferMode;
    }

    public DHDevice() {
        status = DeviceState.OFFLINE;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return !DeviceState.OFFLINE.equalsIgnoreCase(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(@DeviceState String status) {
        this.status = status;
    }

    // 设备接入类型
    public enum AccessType {
        PaaS,           // 表示Paas程序接入
        Lechange,       // 表示乐橙非PaaS设备
        Easy4IP,        // 表示Easy4IP程序设备
        P2P             // 表示P2P程序设备
    }

    @Override
    public DHDevice clone() {
        DHDevice dhDevice = null;

        try {
            dhDevice = (DHDevice) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return dhDevice;
    }


}
