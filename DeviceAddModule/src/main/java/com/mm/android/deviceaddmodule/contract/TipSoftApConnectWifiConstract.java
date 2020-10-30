package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface TipSoftApConnectWifiConstract {
    interface Presenter extends IBasePresenter {
        void copyWifiPwd();
        void connectWifiAction(boolean isFirst);                //开始连接设备热点
        void dispatchHotConnected();            //处理热点连接，判断当前wifi是否已连接至热点
        String getHotSSID();                    //获取热点ssid
    }

    interface View extends IBaseView<Presenter> {
        void updateWifiName(String wifiName);
        void updateConnectFailedTipText(String wifiName, String wifiPwd, boolean isSupportAddBySc, boolean isManualInput);
        void goSecurityCheckPage();
    }
}
