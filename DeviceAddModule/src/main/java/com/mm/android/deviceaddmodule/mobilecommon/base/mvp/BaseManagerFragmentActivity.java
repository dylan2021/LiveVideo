
package com.mm.android.deviceaddmodule.mobilecommon.base.mvp;

import android.view.View;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.dialog.LCAlertDialog;
import com.mm.android.deviceaddmodule.views.popwindow.BasePopWindow;
import com.mm.android.deviceaddmodule.views.popwindow.PopWindowFactory;

/**
 * 设备管理的FragmentActivity基类，主要重写全局等待框
 * 注：initView()需要手动实现
 */

public abstract class BaseManagerFragmentActivity<T extends IBasePresenter> extends BaseMvpFragmentActivity<T> {
    private BasePopWindow mLoadingPopWindow;
    private PopWindowFactory mPopWindowFactory;
    private View mTitle;

    protected abstract View initTitle();

    @Override
    protected void initView() {
        mTitle = initTitle();
    }

    /**
     * 修改时是否弹框提示未保存
     * @return
     */
    protected boolean showUnSaveAlertDialog(){
        return false;
    }

    private void showAlertDialog() {
        LCAlertDialog.Builder builder = new LCAlertDialog.Builder(this);
        LCAlertDialog alertDialog = builder
                .setTitle(R.string.device_manager_not_saved_tip)
                .setConfirmButton(R.string.device_manager_exit,
                        new LCAlertDialog.OnClickListener() {

                            @Override
                            public void onClick(LCAlertDialog dialog,
                                                int which, boolean isChecked) {
                                    finish();
                            }
                        }).setCancelButton(R.string.common_cancel, null)
                .create();
        alertDialog.show(getSupportFragmentManager(), "");

    }

    @Override
    public void showProgressDialog() {
        if (mTitle == null) {
            super.showProgressDialog();
        } else {
            if (mLoadingPopWindow != null) {
                mLoadingPopWindow.dismiss();
            }
            if (mPopWindowFactory == null)
                mPopWindowFactory = new PopWindowFactory();
            try{
                mLoadingPopWindow = mPopWindowFactory.createLoadingPopWindow(this, mTitle);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void cancelProgressDialog() {
        if (mLoadingPopWindow == null) {
            super.cancelProgressDialog();
        } else {
            mLoadingPopWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingPopWindow != null) {
            mLoadingPopWindow.dismiss();
            mLoadingPopWindow = null;
            mPopWindowFactory = null;
        }
    }

    @Override
    public void onBackPressed() {
        if(showUnSaveAlertDialog()){
            showAlertDialog();
        }else{
            super.onBackPressed();
        }
    }
}
