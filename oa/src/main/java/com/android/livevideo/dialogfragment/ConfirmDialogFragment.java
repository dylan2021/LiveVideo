package com.android.livevideo.dialogfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.util.Utils;
import com.android.livevideo.R;

/**
 * 自定义的确认类型的对话框
 * @author zeng
 * @since 2016/12/08
 */
public class ConfirmDialogFragment extends DialogFragment {

    private TextView tv_title,tv_left, tv_right;
    private LinearLayout contentWrapper;
    private View contentView;
    private View titleLine,bottomLine;

    private String title;
    private String leftButText = null;
    private String rightButText = null;

    private int titleId = 0;
    private int LeftButTextId = 0;
    private int RightButTextId = 0;

    private int dialogWidth = 0;

    private View.OnClickListener leftButClickListener;
    private View.OnClickListener rightButClickListener;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity();

        if(title == null && titleId > 0){
            title = (String) context.getText(titleId);
        }
        if(leftButText == null && LeftButTextId > 0){
            leftButText = (String) context.getText(LeftButTextId);
        }
        if(rightButText == null && RightButTextId > 0){
            rightButText = (String) context.getText(RightButTextId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.layout_dialog_confirm,container);

        tv_title = (TextView) view.findViewById(R.id.title);
        tv_left = (TextView) view.findViewById(R.id.left_tv);
        tv_right = (TextView) view.findViewById(R.id.right_tv);
        contentWrapper = (LinearLayout) view.findViewById(R.id.content_wrapper);
        titleLine = view.findViewById(R.id.line1);
        bottomLine = view.findViewById(R.id.line3);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //设置对话框宽度
        if(dialogWidth > 0){
            getDialog().getWindow().setLayout(Utils.dip2px(getActivity(),dialogWidth),WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //将内容布局加入到对话框中
        if(contentView != null){
            ViewGroup  parent = (ViewGroup) contentView.getParent();
            if(parent != null)
                parent.removeAllViewsInLayout();
            contentWrapper.addView(contentView);
        }

        if(title != null){
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
            titleLine.setVisibility(View.VISIBLE);
        }else {
            tv_title.setVisibility(View.GONE);
            titleLine.setVisibility(View.GONE);
        }

        //设置左侧监听器
        if(leftButText != null){

            tv_left.setText(leftButText);
            tv_left.setVisibility(View.VISIBLE);
            bottomLine.setVisibility(View.VISIBLE);
            tv_left.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    leftButClickListener.onClick(v);
               }
            });
        }else {
            tv_left.setVisibility(View.GONE);
            bottomLine.setVisibility(View.GONE);
        }

        //设置右侧监听器
        if (rightButText != null) {
            tv_right.setText(rightButText);
            tv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rightButClickListener.onClick(v);
                }
            });
        } else {
            tv_right.findViewById(R.id.right_tv).setVisibility(View.GONE);
        }

    }

    /**
     * 设置对话框中间的内容
     * @param v 中间内容的布局文件
     */
    public ConfirmDialogFragment setContentView(View v) {
        this.contentView = v;
        return this;
    }

    /**
     * 设置左按钮的文字
     * @param text 按钮文字
     */
    public void setLeftButText(String text){
        this.leftButText = text;
    }

    /**
     * 设置右按钮的文字
     * @param text  按钮文字
     */
    public void setRightButText(String text){
        this.rightButText = text;
    }

    /**
     * 设置对话框的宽度 单位dp
     * @param width 对话框的宽度 单位dp
     */
    public void setDialogWidth(int width){
        this.dialogWidth = width;
    }

    public ConfirmDialogFragment setTitle(int titleId) {
        this.titleId = titleId;
        return this;
    }

    public ConfirmDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public ConfirmDialogFragment setPositiveButton(int positiveButtonTextId, View.OnClickListener listener){
        this.LeftButTextId = positiveButtonTextId;
        this.leftButClickListener = listener;
        return this;
    }

    public ConfirmDialogFragment setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
        this.leftButText = positiveButtonText;
        this.leftButClickListener = listener;
        return this;
    }

    public ConfirmDialogFragment setNegativeButton(int negativeButtonTextId, View.OnClickListener listener) {
        this.RightButTextId = negativeButtonTextId;
        this.rightButClickListener = listener;
        return this;
    }

    public ConfirmDialogFragment setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
        this.rightButText = negativeButtonText;
        this.rightButClickListener = listener;
        return this;
    }
}
