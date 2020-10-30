package com.mm.android.deviceaddmodule.mobilecommon.base.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.base.BaseFragmentActivity;
import com.mm.android.deviceaddmodule.mobilecommon.eventbus.event.BaseEvent;

/**
 * MVP模式FragmentActivity基类，继承自common模块基类FragmentAcitivity，实现BaseView中通用接口，所有MVP模式的FragmentActivity需继承自此类。
 **/
public abstract class BaseMvpFragmentActivity<T extends IBasePresenter> extends BaseFragmentActivity implements IBaseView {

    protected abstract void initLayout();         //初始化布局
    protected abstract void initView();           //初始化控件
    protected abstract void initData();           //初始化数据
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        initData();
        initLayout();
        initView();
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null) mPresenter.unInit();
        super.onDestroy();
    }

    @Override
    public Context getContextInfo() {
        return this;
    }

    @Override
    public boolean isViewActive() {
        return !isActivityDestory();
    }

    @Override
    public void showToastInfo(String msg) {
        toast(msg);
    }

    @Override
    public void showToastInfo(int msgId) {
        toast(msgId);
    }

    @Override
    public void showToastInfo(int msgId, String errorDesc) {
        if (!TextUtils.isEmpty(errorDesc)) {
            toast(errorDesc);
        } else {
            toast(msgId);
        }
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(R.layout.mobile_common_progressdialog_layout);
    }

    @Override
    public void onMessageEvent(BaseEvent event) {

    }

    @Override
    public void unInit() {

    }

    @Override
    public void initPresenter() {

    }

    public void cancelProgressDialog() {
        dissmissProgressDialog();
    }
}
