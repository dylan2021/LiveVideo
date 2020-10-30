package com.mm.android.deviceaddmodule.device_wifi;

import com.mm.android.deviceaddmodule.mobilecommon.base.mvp.IBasePresenter;
import com.mm.android.deviceaddmodule.mobilecommon.base.mvp.IBaseView;

public interface HiddenWifiConstract {
    interface Presenter extends IBasePresenter {
        void wifiOperate();
    }

    interface View extends IBaseView {
        String getWifiSSID();
        String getWifiPassword();
        void onWifiOperateSucceed(CurWifiInfo curWifiInfo);
    }
}
