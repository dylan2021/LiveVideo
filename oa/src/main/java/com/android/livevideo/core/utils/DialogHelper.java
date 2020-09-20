package com.android.livevideo.core.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.dialogfragment.WaitingDialogFragment;
import com.android.livevideo.R;
import com.android.livevideo.dialogfragment.LoginDialogFragment;
import com.android.livevideo.dialogfragment.ConfirmDialogFragment;
import com.android.livevideo.util.Utils;

/**
 * 通用对话框工具类
 * Created by zeng on 2016/7/19.
 */
public class DialogHelper {

    private static final String CONFIRM_DIALOG = "confirm_dialog";

    private FragmentManager fm;
    private Context context;
    public DialogHelper(FragmentManager fm,Context context){
        this.fm = fm;
        this.context = context;

        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(CONFIRM_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 显示确认对话框
     */
    public void showConfirm(String title, String content,String leftText,String rightText,
                            final View.OnClickListener leftButOnClickListener,
                            final View.OnClickListener rightButOnClickListener){

        final ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.setTitle(title);
        dialogFragment.setDialogWidth(250);

        TextView contentView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(Utils.dip2px(context,10),0, Utils.dip2px(context,10),0);
        contentView.setLayoutParams(params);
        contentView.setText(content);
        contentView.setTextColor(context.getResources().getColor(R.color.color212121));

        dialogFragment.setLeftButText(leftText);
        dialogFragment.setRightButText(rightText);
        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton(R.string.update_later, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                leftButOnClickListener.onClick(v);
            }
        });
        dialogFragment.setNegativeButton(R.string.update_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                rightButOnClickListener.onClick(v);

            }
        });
        dialogFragment.show(fm,CONFIRM_DIALOG);
    }

    /**
     * 显示确认对话框
     */
    public void showConfirm(String title, View contentView,String leftText,String rightText,
                            final View.OnClickListener leftButOnClickListener,
                            final View.OnClickListener rightButOnClickListener){

        final ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.setTitle(title);
        dialogFragment.setDialogWidth(250);

        dialogFragment.setLeftButText(leftText);
        dialogFragment.setRightButText(rightText);
        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton(R.string.update_later, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                leftButOnClickListener.onClick(v);
            }
        });
        dialogFragment.setNegativeButton(R.string.update_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                rightButOnClickListener.onClick(v);

            }
        });
        dialogFragment.show(fm,CONFIRM_DIALOG);
    }

    /**
     * 显示加载等待对话框
     * @param msg           显示的等待消息
     * @param cancelable    是否允许点击其他地方取消等待框
     */
    public void showAlert(String msg,boolean cancelable){

        DialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.setCancelable(cancelable);
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm,CONFIRM_DIALOG);

    }

    /**
     * 隐藏等待框
     */
    public void hideAlert(){

        FragmentTransaction ft = fm.beginTransaction();
        DialogFragment prev = (DialogFragment) fm.findFragmentByTag(CONFIRM_DIALOG);
        if (prev != null) {
            prev.dismiss();
            ft.remove(prev);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 显示等待对话框
     * @param fm
     * @param msg
     */
    public static void showWaiting(FragmentManager fm, String msg){

        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("waitingDialog");
        /*if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);*/
        if(prev == null || !prev.isAdded()){
            DialogFragment newFragment = WaitingDialogFragment.newInstance();
            newFragment.setCancelable(true);
            Bundle bundle = new Bundle();
            bundle.putString("msg",msg);
            newFragment.setArguments(bundle);
            //newFragment.show(fm, "waitingDialog");
            ft.add(newFragment,"waitingDialog");
            ft.commitAllowingStateLoss();
        }else {

        }


    }

    /**
     * 取消等待对话框
     */
    public static void hideWaiting(FragmentManager fm){

        FragmentTransaction ft = fm.beginTransaction();
        DialogFragment prev = (DialogFragment) fm.findFragmentByTag("waitingDialog");
        if (prev != null) {
            prev.dismiss();
            ft.remove(prev);
        }
        //ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

}
