package com.mm.android.deviceaddmodule.contract;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface InitContract {
    interface Presenter extends IBasePresenter {
        void setDeviceEX(DEVICE_NET_INFO_EX deviceEX);
        void playTipSound();               //播放提示音频
        void startDevInitByIp();          //设备单播初始化
        void startDevInit();              //设备组播初始化
        boolean isPwdValid();                 //密码是否有效
        void checkDevice();
        void recyle();
    }

    interface View extends IBaseView<Presenter> {
        int getMusicRes();                  //获取音频资源
        String getInitPwd();                //获取设备密码
        void goSoftAPWifiListPage();              //软AP添加wifi选择页
        void goConnectCloudPage();         //进入连接云平台页
        void goErrorTipPage();
    }
}
