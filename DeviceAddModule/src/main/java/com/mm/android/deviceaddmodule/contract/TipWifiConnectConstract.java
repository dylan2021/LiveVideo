package com.mm.android.deviceaddmodule.contract;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface TipWifiConnectConstract {
    interface Presenter extends IBasePresenter {
        void searchDevice();
        void stopSearchDevice();
        String getConfigMode();
    }

    interface View extends IBaseView<Presenter> {
        void goDevInitPage(DEVICE_NET_INFO_EX device_net_info_ex);               //进入设备初始化页
        void goWifiConfigPage();            //进入配网页
        void goCloudConnectPage();          //进入云连接页
    }
}
