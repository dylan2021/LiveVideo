package com.mm.android.deviceaddmodule.mobilecommon.base.mvp;

import android.content.Intent;

/**
 * MVP模式P层基类接口
 */
public interface IBasePresenter {
    void dispatchIntentData(Intent intent);         //处理intent数据
    void unInit();                                  //反初始化，以释放相关资源
}
