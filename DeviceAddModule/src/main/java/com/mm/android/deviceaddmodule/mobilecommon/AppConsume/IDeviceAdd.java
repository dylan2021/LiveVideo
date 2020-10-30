package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;

import com.mm.android.deviceaddmodule.mobilecommon.base.BaseHandler;
import com.mm.android.deviceaddmodule.mobilecommon.base.IBaseProvider;

/**
 * 设备添加对外接口，外部模块可调用的功能
 */
public interface IDeviceAdd extends IBaseProvider {
    void stopSearchDevicesAsync(long ret, BaseHandler handler);      //异步停止设备搜索
    boolean stopSearchDevices(long ret, String requestId);          //同步停止设备搜索
}
