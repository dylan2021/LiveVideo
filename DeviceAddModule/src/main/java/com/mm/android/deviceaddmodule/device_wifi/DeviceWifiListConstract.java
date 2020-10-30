package com.mm.android.deviceaddmodule.device_wifi;

import com.mm.android.deviceaddmodule.mobilecommon.base.mvp.IBasePresenter;
import com.mm.android.deviceaddmodule.mobilecommon.base.mvp.IBaseView;
import com.mm.android.deviceaddmodule.mobilecommon.entity.device.DHDevice;

import java.util.List;

public interface DeviceWifiListConstract {
    interface Presenter extends IBasePresenter {
        void getDeviceWifiListAsync();
        WifiInfo getWifiInfo(int position);
        CurWifiInfo getCurWifiInfo();
        boolean isSupport5G(String wifiMode);
        DHDevice getDHDevice();
    }

    interface View extends IBaseView {
        void refreshListView(List<WifiInfo> wlanInfos);
        void onLoadSucceed(boolean isEmpty, boolean isError);
        void updateCurWifiLayout(CurWifiInfo curWifiInfo);
        void viewFinish();
    }
}
