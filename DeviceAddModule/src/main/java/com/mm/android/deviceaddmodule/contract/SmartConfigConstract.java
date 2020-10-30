package com.mm.android.deviceaddmodule.contract;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface SmartConfigConstract {
    interface Presenter extends IBasePresenter {
        void startSmartConfig();         //开始smartconfig配对线程，和声波配对
        void recyle();              //回收资源

        String getConfigMode();

        void stopAudio();

        void playAudio();

        void pauseAudio();

        void releaseAudio();

        void wifiPwdErrorClick();
    }

    interface View extends IBaseView<Presenter> {
        void goDevInitPage(DEVICE_NET_INFO_EX device_net_info_ex);               //进入设备初始化页
        void goConnectCloudPage();         //进入连接云平台页
        void goDevLoginPage();         //P2P设备进入设备登录页
        void goConfigTimeoutPage();         //配置超时页
        void goWfiPwdPage();
        void stopCountDown();
        void updateTip2Txt(boolean isSupportSoundWave, boolean isSupportSoundWaveV2);

        void hideTipWifiPwdErrorTxt(boolean isOversea);
        void completeAction();
        void goBindDevicePage();
    }
}
