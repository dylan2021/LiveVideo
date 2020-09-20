package com.android.livevideo.act_other;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.livevideo.adapter.QuickConsultationAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Gool Lee
 */

public class CommonBaseActivity extends BaseFgActivity implements QuickConsultationAdapter
        .OnGridViewItemClickListener {

    public GridView gridView;
    public ImageView iv_upload;
    public TextView tv_info;
    public QuickConsultationAdapter adapter;
    public List<PictBean> pictures = new ArrayList<PictBean>();//图片文件
    public Bundle bundle;
    public Intent intent;
    protected SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    public void setGridView() {
        if (gridView == null) {
            return;
        }
        int size = pictures != null ? pictures.size() : 0;
        int length = 115;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int itemWidth = (int) (length * density) - 2;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.width = itemWidth * size + (size == 1 ? 25 : 0);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(0); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        //gridView.setNumColumns(size); // 设置列数量=列表集合数
        adapter = new QuickConsultationAdapter(this, pictures, this);
        gridView.setAdapter(adapter);
        //reSetLvHeight(gridView);
    }

    //设置界面传递的参数
    public Bundle setBundle() {
        bundle = new Bundle();
        bundle.putSerializable("pictures", (Serializable) pictures);
        return bundle;
    }

    //获得界面传递的参数
    public void getBundle() {
        if (getIntent() != null) {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                pictures = (List<PictBean>) bundle.getSerializable("pictures") != null ?
                        (List<PictBean>) bundle.getSerializable("pictures") : new
                        ArrayList<PictBean>();
            }
        }
    }

    @Override
    public void onGridViewItemClick(int position, Boolean isDelete) {
        PictBean bean = pictures.get(position);
        if (isDelete == true) { //删除图片
            for (int j = 0; j < pictures.size(); j++) {
                if (bean.getLocalURL().equals(pictures.get(j).getLocalURL())) {
                    pictures.remove(j);
                    break;
                }
            }
            if (iv_upload != null) {
                iv_upload.setVisibility(View.VISIBLE);
            }
            if (tv_info != null) {
                if (pictures.size() == 0) {
                    tv_info.setVisibility(View.VISIBLE);
                } else {
                    tv_info.setVisibility(View.GONE);
                }
            }
            setGridView();
        }
    }

    public void removePicture(PictBean pictBean) {
        for (int i = 0; i < pictures.size(); i++) {
            if (pictBean.getLocalURL().equals(pictures.get(i).getLocalURL())) {
                pictures.remove(i);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDatas();
    }

    protected void clearDatas() {
        adapter = null;
        pictures = null;
        bundle = null;
        intent = null;
    }
}

