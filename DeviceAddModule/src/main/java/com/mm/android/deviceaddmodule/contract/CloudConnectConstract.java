package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface CloudConnectConstract {
    interface Presenter extends IBasePresenter {
        void bindDevice();
        void getDeviceInfo();
        void recyle();
        boolean isWifiOfflineConfiMode();       //是否为离线配置模式
        void notifyMiddleTimeUp();
        void startConnectTiming();              //开始连接云
        void stopConnectTiming();               //结束连接
    }


    interface View extends IBaseView<DeviceAddConstract.Presenter> {
        void goBindSuceesPage();     //绑定成功页
        void goDevLoginPage();       //设备登录页
        void goDevSecCodePage();    //设备安全码页
        void goErrorTipPage();      //错误提示页
        void goErrorTipPage(int errorCode);      //错误提示页
        void goBindDevicePage();    //绑定设备页
        void completeAction();      //流程结束
        void setCountDownTime(int time);    //设置倒计时时间
        void setMiddleTime(int time);    //设置中间时间
        void goOtherUserBindTipPage();      //设备被他人绑定提示页
        void goNotSupportBuindTipPage();    //设备不支持被绑定
    }
}
