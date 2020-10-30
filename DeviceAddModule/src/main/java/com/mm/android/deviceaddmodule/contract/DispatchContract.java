package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface DispatchContract {
    interface Presenter extends IBasePresenter {
        boolean isManualInputPage();                    //是否为手动输入设备序列号页面
        boolean isSnInValid(String sn);
        boolean isScCodeInValid(String scCode);
        void dispatchResult();
    }

    interface View extends IBaseView<ScanContract.Presenter> {
        void goTypeChoosePage();            //跳转到设备类型选择页
        void goNotSupportBindTipPage();     //跳转到不支持绑定的设备页面
        void goOtherUserBindTipPage();      //跳转至设备被其他用户绑定提示页
        void showAddBoxTip();               //盒子添加提示
        void goCloudConnectPage();         //跳转至云平台连接页
        void goDeviceLoginPage();            //跳转至设备登录页
        void goSecCodePage();               //跳转至安全码验证页
        void goDeviceBindPage();            //跳转至设备绑定页
        void goIMEIInputPage();            //跳转至输入imei页
    }
}
