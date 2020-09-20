package com.android.livevideo.dialogfragment;

import android.app.Dialog;
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
import android.widget.TextView;

import com.android.livevideo.R;

/**
 * Fragment类型的对话框
 * Created by zeng on 2016/5/23.
 */
public class OneBtDialogFragment extends DialogFragment {

    private static OneBtDialogFragment fragment = null;
    private TextView title_tv, negative_tv;
    private String title;
    private String negativeButtonText = null;
    private int titleId = 0;
    private int negativeButtonTextId = 0;

    private int dialogWidth = 0;

    private boolean isShow = false;

    private View.OnClickListener negativeButtonClickListener;

    public static OneBtDialogFragment newInstance() {
        if (fragment == null) {
            fragment = new OneBtDialogFragment();
        }
        return fragment;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
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

        if (title == null && titleId > 0) {
            title = (String) context.getText(titleId);
        }
        if (negativeButtonText == null && negativeButtonTextId > 0) {
            negativeButtonText = (String) context.getText(negativeButtonTextId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.layout_dialog_one_bt, container);
        negative_tv = (TextView) view.findViewById(R.id.right_tv);
        title_tv = (TextView) view.findViewById(R.id.title);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (title != null) {
            title_tv.setText(title);
        } else {
            title_tv.setVisibility(View.GONE);
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
    @Override
    public void onResume() {
        super.onResume();
        //设置对话框宽度
        if(dialogWidth > 0){
            getDialog().getWindow().setLayout(dialogWidth,WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
    /**
     * 设置对话框的宽度 单位dp
     *
     * @param width
     */
    public void setDialogWidth(int width) {
        this.dialogWidth = width;
    }

    public OneBtDialogFragment setTitle(int titleId) {
        this.titleId = titleId;
        return this;
    }

    public OneBtDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }


    public OneBtDialogFragment setNegativeButton(int negativeButtonTextId, View.OnClickListener listener) {
        this.negativeButtonTextId = negativeButtonTextId;
        this.negativeButtonClickListener = listener;
        return this;
    }

    public OneBtDialogFragment setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
        this.negativeButtonText = negativeButtonText;
        this.negativeButtonClickListener = listener;
        return this;
    }
}
