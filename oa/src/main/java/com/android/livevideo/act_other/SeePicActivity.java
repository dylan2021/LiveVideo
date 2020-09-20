package com.android.livevideo.act_other;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.livevideo.R;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.widget.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Gool Lee
 */
public class SeePicActivity extends BaseFgActivity {
    private ArrayList<TouchImageView> imgViews = new ArrayList<>();
    private View layoutInflater;
    private ViewPager viewPager;
    private ViewGroup pointGroup;
    private ImageView[] pointViews;
    private int selectPosition = 0; // viewpager初始位置
    private ArrayList<String> urlList = new ArrayList<>();
    private TouchImageView iv;
    private SeePicActivity context;
    private String permissionUrl;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        urlList = getIntent().getStringArrayListExtra(KeyConst.LIST_STR);

        layoutInflater = getLayoutInflater().inflate(R.layout.activity_show_view, null);
        viewPager = (ViewPager) layoutInflater.findViewById(R.id.imagePages);
        pointGroup = (ViewGroup) layoutInflater.findViewById(R.id.pointGroup);
        if (urlList == null || urlList.size() == 0) {
            ImageView imageView = (ImageView) layoutInflater.findViewById(R.id.show_view_iv);
            imageView.setVisibility(View.VISIBLE);
            layoutInflater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.finish();
                }
            });
            setContentView(layoutInflater);
            return;
        }
        selectPosition = getIntent().getIntExtra(KeyConst.selectPosition, 0);
        setContentView(layoutInflater);
        setView();
    }

    private void setView() {
        for (int i = 0; i < urlList.size(); i++) {
            iv = new TouchImageView(context);
            final String url = urlList.get(i);
            if (TextUtil.isEmpty(url)) {
                iv.setImageResource(R.drawable.ic_error_img);
            } else if (url.startsWith("data:image/")) {//base64方式
                Bitmap bitmap = ImageUtil.base64UrlStrToBitmap(url);
                if (bitmap == null) {
                    iv.setImageResource(R.drawable.ic_error_img);
                } else {
                    iv.setImageBitmap(bitmap);
                }
            } else {  //url方式
                //bitmap = ImageUtil.getHttpBitmap(url);
                //iv.setImageBitmap(bitmap);
                Picasso.with(context).load(url)
                        .error(R.drawable.ic_error_img)
                        .placeholder(R.drawable.ic_def_logo_720_288)
                        .into(iv);
            }
            imgViews.add(iv);

            iv.setOnImageClickListener(new TouchImageView.OnClickListener() {
                @Override
                public void onClick() {
                    permissionUrl = null;
                    context.finish();
                }
            });
            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showSaveDialog(url);
                    return false;
                }
            });
        }
        pointViews = new ImageView[urlList.size()];

        if (context == null && context.isFinishing()) {
            return;
        }
        for (int i = 0; i < imgViews.size(); i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(10, 0, 0, 0);
            }
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(pointParams);
            pointViews[i] = iv;
            if (i == 0) {
                //选中原点颜色
                pointViews[i].setBackgroundResource(R.drawable.shape_radius_white_10px);
            } else {
                //未选中
                pointViews[i].setBackgroundResource(R.drawable.shape_radius_eee_10px);
            }
            pointGroup.addView(pointViews[i]);
        }
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new PointChangeListener());
        viewPager.setCurrentItem(selectPosition);

    }

    //保存图片
    private void showSaveDialog(final String finalUrl) {
        permissionUrl = finalUrl;
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_save_pic, null);

        inflate.findViewById(R.id.logout_yes_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //没有权限则申请权限
                        ToastUtil.show(context, R.string.no_store_permission);
                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        //有权限直接执行,docode()不用做处理
                        ImageUtil.savePicUrl(context, finalUrl);
                    }
                } else {
                    //小于6.0，不用申请权限，直接执行
                    ImageUtil.savePicUrl(context, finalUrl);
                }

                dialog.cancel();
            }
        });
        inflate.findViewById(R.id.logout_cancel_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setContentView(inflate);

        DialogUtils.setDialogWindow(context, dialog, Gravity.BOTTOM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //已经申请权限成功
                    ImageUtil.savePicUrl(context, permissionUrl);
                } else {
                    //ToastUtil.show(context, "请开启文件存储权限");
                }
                break;
        }
    }

    // 指引页面数据适配器
    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // 通过集合的size告诉Adapter共有多少张图片
            return imgViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // 移除旧的View
            ((ViewPager) arg0).removeView(imgViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // 获取新的view
            ((ViewPager) arg0).addView(imgViews.get(arg1));
            return imgViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }

    // 监听器内部类
    // 指引页面更改事件监听器
    class PointChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            pointViews[position].setBackgroundResource(R.drawable.shape_radius_white_10px);
            for (int i = 0; i < pointViews.length; i++) {
                if (position != i) {
                    pointViews[i].setBackgroundResource(R.drawable.shape_radius_eee_10px);
                }
            }
        }
    }


}
