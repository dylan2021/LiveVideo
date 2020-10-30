package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface DevSecCodeConstract {
    interface Presenter extends IBasePresenter {
        void validate();
    }

    interface View extends IBaseView<Presenter> {
        String getDeviceSecCode();     //从输入框中获取设备安全码

        void goErrorTipPage(int errorCode);              //错误提示页

        void goBindSuceesPage();            //进入绑定成功页

        void goOtherUserBindTipPage();

        void completeAction();              //完成退出

        void goDevLoginPage();       //设备登录页

        void goDevSecCodePage();    //设备安全码页
    }
}
