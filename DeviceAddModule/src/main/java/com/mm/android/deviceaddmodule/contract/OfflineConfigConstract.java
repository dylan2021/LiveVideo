package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface OfflineConfigConstract {
    interface Presenter extends IBasePresenter {
        void resetCache();
        void getDeviceInfo(String deviceSn, String deviceModelName, String imeiCode);   //从服务获取设备信息
    }

    interface View extends IBaseView<Presenter> {
        void onGetDeviceInfoError();
    }
}
