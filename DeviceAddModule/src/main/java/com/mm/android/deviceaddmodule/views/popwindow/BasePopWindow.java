package com.mm.android.deviceaddmodule.views.popwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 弹出框基类
 */
public abstract class BasePopWindow extends PopupWindow {
    BasePopWindow(View view, int width, int height){
        super(view,width,height);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 绘制弹出框内容
     * @param activity
     */
    public abstract void drawContent(Activity activity);

    public void drawContent(Activity activity,boolean isPort){

    }


    /**
     * 更新弹出框
     * @param activity
     */
    public abstract void updateContent(Activity activity,boolean isPort);
}
