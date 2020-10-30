package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface DevLoginConstract {
    interface Presenter extends IBasePresenter {
        void devLogin();
    }

    interface View extends IBaseView<Presenter> {
        String getDevicePassword();     //从输入框中获取设备密码

        void goSoftAPWifiListPage();              //软AP添加wifi选择页

        void goDeviceBindPage();              //进入设备绑定页

        void goBindSuceesPage();            //进入绑定成功页

        void goOtherUserBindTipPage();      //进入其他用户绑定提示页

        void goErrorTipPage(int errorCode);              //错误提示页

        void completeAction();              //完成退出

        void goDevLoginPage();       //设备登录页

        void goDevSecCodePage();    //设备安全码页


    }
}
