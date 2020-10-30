package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface TypeChooseConstract {
    interface Presenter extends IBasePresenter {
        void getDeviceInfoSync(String deviceModelName);
        void checkDevIntroductionInfo(String deviceModelName);
        void resetDevPwdCache();                        //回到设备选择页，清空设备密码缓存
    }

    interface View extends IBaseView<Presenter> {
        void showSearchError();
    }
}
