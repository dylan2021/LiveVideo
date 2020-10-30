package com.mm.android.deviceaddmodule.contract;

import android.content.Intent;
import android.os.Bundle;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface DeviceAddConstract {
    interface Presenter extends IBasePresenter {
        String getCurTitleMode();

        void setCurTitleMode(String titleMode);

        void dispatchIntentData(Intent intent);

        void getGPSLocation();              //获取gps信息

        void dispatchPageNavigation();      //添加流程页跳转

        void uninit();                      //释放相关资源

        void getDeviceShareInfo();

        boolean canBeShare();

        void changeToWireless();
        void changeToWired();
        void changeToSoftAp();
        void changeToNB();
    }

    interface View extends IBaseView<Presenter> {
        void setTitle(int titleId);        //设置标题

        void goScanPage();              //扫描页

        void goDispatchPage();              //分发页

        void goHubPairPage(String sn, String hubType);           //hub电池相机引导页

        void goApConfigPage(boolean hasSelecteGateway);              //跳转至配件添加页

        void goWiredwirelessPage(boolean isWifi);  //跳转至有线/无线添加

        void goWiredwirelessPageNoAnim(boolean isWifi);  //跳转至有线/无线添加

        void goSoftApPage();                //跳转至软AP添加

        void goSoftApPageNoAnim();          //跳转至软AP添加

        void goNBPage();               //跳转至NB添加

        void goOfflineConfigPage(String sn, String devModelName, String imei);         //跳转到离线配网页面

        void gotoDeviceSharePage(String sn); //跳转设备分享页面

        void gotoAddBleLockPage(Bundle bundle);

        void goInitPage(DEVICE_NET_INFO_EX device_net_info_ex);

        void goCloudConnetPage();

        void goTypeChoosePage();

        void completeAction(boolean isAp);

        void goLocationPage();              //跳转至设备本地配网添加

        void goIMEIInputPage();            //跳转至输入imei页

        void goNotSupportBindTipPage();     //跳转到不支持绑定的设备页面
    }
}
