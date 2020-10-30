package com.mm.android.deviceaddmodule.contract;

import android.support.v4.app.Fragment;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;

public interface ErrorTipConstract {
    interface Presenter extends IBasePresenter {
        void dispatchError(int errorCode);
        boolean isResetPage();          //通用错误页，及没有按钮
        boolean isUserBindTipPage();    //是否为设备绑定提示页
        boolean isUserBindTipPageByBind();//绑定时提示设备被其他用户绑定
    }

    interface View extends IBaseView<Presenter> {
        Fragment getParent();
        void updateInfo(String info,String img,boolean isNeedMatch);
        void updateInfo(int infoId,int tip2Id,String img,boolean isNeedMatch);
        void updateInfo(int infoId,String img,boolean isNeedMatch);
        void hideTipTxt();
        void hideHelp();

    }
}
