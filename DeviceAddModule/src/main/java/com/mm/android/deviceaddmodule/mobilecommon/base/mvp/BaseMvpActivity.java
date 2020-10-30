package com.mm.android.deviceaddmodule.mobilecommon.base.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.base.BaseActivity;
import com.mm.android.deviceaddmodule.mobilecommon.eventbus.event.BaseEvent;

/**
 * 暂无需求。
 * MVP模式Activity基类，继承自common模块基类Acitivity，实现BaseView中通用接口，所有MVP模式的Activity需继承自此类。
 */
public abstract class BaseMvpActivity<T extends IBasePresenter> extends BaseActivity implements IBaseView  {
    protected abstract void initLayout();         //初始化布局
    protected abstract void initView();           //初始化控件
    protected abstract void initData();           //初始化数据
    protected T mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initPresenter();
        initData();
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null) mPresenter.unInit();
        super.onDestroy();
    }

    public Context getContextInfo() {
        return this;
    }


    public boolean isViewActive() {
        return !isActivityDestory();
    }

    public void showToastInfo(String msg) {
        toast(msg);
    }

    public void showToastInfo(int msgId) {
        toast(msgId);
    }

    public void showToastInfo(int msgId, String errorDesc) {
        if (!TextUtils.isEmpty(errorDesc)) {
            toast(errorDesc);
        } else {
            toast(msgId);
        }
    }

    public void showProgressDialog() {
        showProgressDialog(R.layout.mobile_common_progressdialog_layout);
    }

    public void cancelProgressDialog() {
        dissmissProgressDialog();
    }

    @Override
    public void unInit() {

    }

    @Override
    public void onMessageEvent(BaseEvent event) {

    }

    @Override
    public void initPresenter() {

    }


}
