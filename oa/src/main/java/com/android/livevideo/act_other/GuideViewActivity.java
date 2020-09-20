package com.android.livevideo.act_other;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.adapter.GuideViewAdapter;
import com.android.livevideo.core.utils.FileUtil;

import java.util.ArrayList;
/*
   Gool Lee
 */
public class GuideViewActivity extends BaseFgActivity {
    private ViewPager viewPage;
    private int[] imgs = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
    private ArrayList<ImageView> list;
    private LinearLayout llPoint;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐藏状态栏
        initview();
        addView();
        addPoint();
    }


    private void initview() {
        viewPage = (ViewPager) findViewById(R.id.viewpage);
        llPoint = (LinearLayout) findViewById(R.id.llPoint);
        textView = (TextView) findViewById(R.id.guideTv);
    }

    /**
     * 添加图片到view
     */
    private void addView() {
        list = new ArrayList<ImageView>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            iv.setImageResource(imgs[i]);
            FileUtil.scaleImage(this, iv, imgs[i]);
            list.add(iv);
        }
        // 加入适配器
        viewPage.setAdapter(new GuideViewAdapter(list));

    }

    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < imgs.length; i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(20, 0, 0, 0);
            }
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.shape_radius_eee_10px);
            llPoint.addView(iv);
        }
        llPoint.getChildAt(0).setBackgroundResource(R.drawable.shape_radius_white_10px);

    }

    /**
     * 判断小圆点
     *
     * @param position
     */
    private void monitorPoint(int position) {
        for (int i = 0; i < imgs.length; i++) {
            if (i == position) {
                llPoint.getChildAt(position).setBackgroundResource(
                        R.drawable.shape_radius_white_10px);
            } else {
                llPoint.getChildAt(i).setBackgroundResource(
                        R.drawable.shape_radius_eee_10px);
            }
        }
        // 3.当滑动到最后一个添加按钮点击进入，
        if (position == imgs.length - 1) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}