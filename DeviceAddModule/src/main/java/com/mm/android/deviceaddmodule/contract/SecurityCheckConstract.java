package com.mm.android.deviceaddmodule.contract;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface SecurityCheckConstract {
    interface Presenter extends IBasePresenter {
        void checkDevice();
        void recyle();
    }

    interface View extends IBaseView<Presenter> {
        void goInitPage(DEVICE_NET_INFO_EX device_net_info_ex);
        void goErrorTipPage();
        void goDevLoginPage();                    //设备登录页
        void goSoftApWifiListPage(boolean isNotNeedLogin);
        void goCloudConnetPage();
    }
}
