package com.mm.android.deviceaddmodule.mobilecommon.base.api;

import android.content.DialogInterface;
import android.support.annotation.LayoutRes;

/**
 * 控制显示等待框的接口
 */

public interface IProgressDialogControlView {

    /**
     * 根据 id 显示 等待
     * @param layoutId
     */
    void showProgressDialog(@LayoutRes int layoutId);
    /**
    *  隐藏等待
    */
    void dissmissProgressDialog();

    /**
     * 取消弹框
     */
    void cancleProgressDialog();

    /**
     * 设置是否可以取消
     * @param flag
     */
   void setProgressDialogCancelable(boolean flag);

   void setProgressDialogCancelListener(DialogInterface.OnCancelListener cancelListener);

}
