package com.mm.android.deviceaddmodule.mobilecommon.base.mvp;

import android.content.Context;

import com.mm.android.deviceaddmodule.mobilecommon.eventbus.event.BaseEvent;

/**
 * MVP模式V层基类接口
 **/
public interface IBaseView {
    Context getContextInfo();           //获取上下文信息

    boolean isViewActive();            //View层是否处于活动状态，是否已销毁

    void showToastInfo(String msg);    //Toast

    void showToastInfo(int msgId);    //Toast

    void showToastInfo(int msgId, String errorDesc);    //Toast

    void showProgressDialog();        //显示加载框
    void cancelProgressDialog();     //隐藏加载框

    void onMessageEvent(BaseEvent event);//eventBus 消息通知

    void unInit();                   //反初始化，以释放相关资源

    void initPresenter(); // 初始化presenter

    void finish();                  //退出当前页
}
