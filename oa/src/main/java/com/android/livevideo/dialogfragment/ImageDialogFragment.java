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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.livevideo.R;

/**
 * 图片放大缩小对话框框
 * Created by zeng on 2016/5/30.
 */
public class ImageDialogFragment extends DialogFragment {

    private View.OnClickListener listener;

    private static ImageDialogFragment fragment = null;

    public static ImageDialogFragment newInstance(){
        if(fragment == null){
            fragment = new ImageDialogFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        ImageView view = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_def_logo_412_200));
        //view.setBackground(getActivity().getResources().getDrawable(R.drawable.temp_game_1));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogFragment.this.dismiss();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity();


    }

    @Override
    public void onResume() {
        super.onResume();
        //设置对话框宽度
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}
