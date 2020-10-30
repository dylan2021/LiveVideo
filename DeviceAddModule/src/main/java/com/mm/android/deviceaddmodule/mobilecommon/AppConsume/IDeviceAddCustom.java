package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;

import android.content.Context;

import com.mm.android.deviceaddmodule.mobilecommon.base.IBaseProvider;


/**
 * 设备添加模块，App需不同实现的接口
 **/
public interface IDeviceAddCustom extends IBaseProvider {
    void goFAQWebview(Context context);                                         //调整至FAQ
    void goHomePage(Context context);                                                            //回到设备列表页
}
