package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface BaseSoftApTipConstract {
    interface Presenter extends IBasePresenter {
        boolean isLastTipPage();               //是否为最后一页引导页
        void dealWithUnknownSsid();
        boolean isWifiConnect();
        void verifyWifiOrLocationPermission();

    }

    interface View extends IBaseView<Presenter> {
        void updateTipImage(String imageUrl);
        void updateTipTxt(String tipInfo);
        void updateResetTxt(String resetTxt);
        void goErrorTipPage();
        void applyLocationPermission();
        void gotoSoftApTipConnectWifiPage();
    }
}
