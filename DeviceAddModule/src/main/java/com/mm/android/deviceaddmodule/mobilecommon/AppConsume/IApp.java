package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.mm.android.deviceaddmodule.mobilecommon.base.IBaseProvider;

/**
 * App通用接口类
 */

public interface IApp extends IBaseProvider {
    public int getAppType();              //APP类型(0:乐橙/1:Easy4ip)

    public Context getAppContext();              //获取App上下文


    public void goDeviceSharePage(Activity activity , Bundle bundle);        //进入设备分享页

    void goBuyCloudPage(Activity activity , Bundle bundle);

    //跳转修改设备密码引导页
    void goModifyDevicePwdGuidePage(Activity activity);

    String getAppLanguage();
}
