package com.android.livevideo.dialogfragment;

import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.util.Utils;

/**
 * 加载等待对话框
 * Created by zeng on 2016/8/3.
 */
public class WaitingDialogFragment extends DialogFragment {

    private static WaitingDialogFragment fragment;
    private ImageView img_hand;
    private TextView tv_summary;

    private String msg;

    private int dialogWidth = 0;

    private Context context;

    public static WaitingDialogFragment newInstance(){

        if(fragment == null){
            fragment = new WaitingDialogFragment();
        }
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle bundle = getArguments();
        if(bundle != null){
            msg = bundle.getString("msg","加载中...");
        }else {
            msg = "加载中...";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.layout_dialog_waiting,container);
        img_hand = (ImageView) view.findViewById(R.id.img_1);
        tv_summary = (TextView) view.findViewById(R.id.tv_summary);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //设置对话框宽度
        if(dialogWidth > 0){
            getDialog().getWindow().setLayout(Utils.dip2px(getActivity(),dialogWidth), WindowManager.LayoutParams.WRAP_CONTENT);
        }else {
            //getDialog().getWindow().setLayout(CommonUtil.dip2px(getActivity(),250), WindowManager.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setLayout(Utils.dip2px(getActivity(),80),
                    Utils.dip2px(getActivity(),80));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_summary.setText(msg);

        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        img_hand.startAnimation(operatingAnim);
    }

    /**
     * 设置对话框的宽度
     * @param width 单位dp
     */
    public void setDialogWidth(int width){
        this.dialogWidth = width;
    }

}
