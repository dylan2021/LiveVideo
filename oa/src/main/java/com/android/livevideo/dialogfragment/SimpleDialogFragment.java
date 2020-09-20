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

import com.android.livevideo.R;
import com.android.livevideo.util.Utils;

/**
 * Dylan
 */
public class SimpleDialogFragment extends DialogFragment {

    private static SimpleDialogFragment fragment = null;
    private TextView title_tv,positive_tv,negative_tv;
    private LinearLayout contentWrapper;
    private View contentView;
    private String title;
    private String positiveButtonText = null;
    private String negativeButtonText = null;
    private int titleId = 0;
    private int positiveButtonTextId = 0;
    private int negativeButtonTextId = 0;

    private int dialogWidth = 0;

    private boolean isShow = false;

    private View.OnClickListener positiveButtonClickListener;
    private View.OnClickListener negativeButtonClickListener;

    public static SimpleDialogFragment newInstance(){
        if(fragment == null){
            fragment = new SimpleDialogFragment();
        }
        return fragment;
    }

    public boolean isShow(){
        return isShow;
    }

    public void setIsShow(boolean isShow){
        this.isShow = isShow;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity();

        if(title == null && titleId > 0){
            title = (String) context.getText(titleId);
        }
        if(positiveButtonText == null && positiveButtonTextId > 0){
            positiveButtonText = (String) context.getText(positiveButtonTextId);
        }
        if(negativeButtonText == null && negativeButtonTextId > 0){
            negativeButtonText = (String) context.getText(negativeButtonTextId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.layout_dialog_simple,container);
        positive_tv = (TextView) view.findViewById(R.id.left_tv);
        negative_tv = (TextView) view.findViewById(R.id.right_tv);
        title_tv = (TextView) view.findViewById(R.id.title);
        contentWrapper = (LinearLayout) view.findViewById(R.id.content_wrapper);

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
            title_tv.setText(title);
            title_tv.setVisibility(View.VISIBLE);
        }else {
            title_tv.setVisibility(View.GONE);
        }

        //设置左侧监听器
        if(positiveButtonText != null){

            positive_tv.setText(positiveButtonText);
            positive_tv.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    positiveButtonClickListener.onClick(v);
               }
            });
        }else {
            positive_tv.setVisibility(View.GONE);
        }

        //设置右侧监听器
        if (negativeButtonText != null) {
            negative_tv.setText(negativeButtonText);
            negative_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    negativeButtonClickListener.onClick(v);
                }
            });
        } else {
            negative_tv.findViewById(R.id.right_tv).setVisibility(View.GONE);
        }

    }

    /**
     * 设置对话框中间的内容
     * @param v
     * @return
     */
    public SimpleDialogFragment setContentView(View v) {
        this.contentView = v;
        return this;
    }

    /**
     * 设置对话框的宽度 单位dp
     * @param width
     */
    public void setDialogWidth(int width){
        this.dialogWidth = width;
    }

    public SimpleDialogFragment setTitle(int titleId) {
        this.titleId = titleId;
        return this;
    }

    public SimpleDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public SimpleDialogFragment setPositiveButton(int positiveButtonTextId,View.OnClickListener listener){
        this.positiveButtonTextId = positiveButtonTextId;
        this.positiveButtonClickListener = listener;
        return this;
    }

    public SimpleDialogFragment setPositiveButton(String positiveButtonText,View.OnClickListener listener) {
        this.positiveButtonText = positiveButtonText;
        this.positiveButtonClickListener = listener;
        return this;
    }

    public SimpleDialogFragment setNegativeButton(int negativeButtonTextId,View.OnClickListener listener) {
        this.negativeButtonTextId = negativeButtonTextId;
        this.negativeButtonClickListener = listener;
        return this;
    }

    public SimpleDialogFragment setNegativeButton(String negativeButtonText,View.OnClickListener listener) {
        this.negativeButtonText = negativeButtonText;
        this.negativeButtonClickListener = listener;
        return this;
    }
}
