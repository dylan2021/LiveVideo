package com.android.livevideo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.livevideo.R;
import com.android.livevideo.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Gool Lee
 */
public class BannerView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private Context context;
    private ViewPager viewPager;
    private BannerAdapter adapter;
    private LinearLayout dotLayout;
    private ArrayList<View> dots;

    private HorizontalScrollView parentScrollView;

    private Timer timer = new Timer();
    List<ImageView> initImg = new ArrayList<>();

    private boolean autoScroll = false;  //是否自动轮播
    private int scrollPeriod = 2;       //滚动周期
    private int currentItem = 0;        //ViewPager当前页面
    private int oldItem = 0;            //相对于currentItem的旧索引

    private int maxShow = 10;           //最多显示的轮播数据

    private Handler handler = new Handler();
    private TimerTask task;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //获取控件中自定义属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);

        autoScroll = typedArray.getBoolean(R.styleable.BannerView_auto_scroll, false);
        scrollPeriod = typedArray.getInt(R.styleable.BannerView_scroll_period, 1);
        currentItem = typedArray.getInt(R.styleable.BannerView_current_item, 0);

        typedArray.recycle();   //回收typedArray

        inflate(context, R.layout.layout_banner_view, this);
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        dotLayout = (LinearLayout) this.findViewById(R.id.lay_dot);

    }

    public void setParentScrollView(HorizontalScrollView scrollView) {
        this.parentScrollView = scrollView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (parentScrollView != null) {
            parentScrollView.requestDisallowInterceptTouchEvent(true);
        }

        return super.onTouchEvent(event);
    }

    /**
     * 设置轮播的数据
     *
     * @param views
     */
    public void setData(List<ImageView> views) {
        this.initImg = views;
        currentItem = 0; //activity刷新banner时，防止小点显示错误
        if (views == null || views.size() <= 0) {
            return;
        }

        if (views.size() > maxShow)
            views = views.subList(0, maxShow);

        adapter = new BannerAdapter(views);
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        dots = new ArrayList<>();
        if (dotLayout != null) {
            dotLayout.removeAllViews();
        }
        for (int i = 0; i < views.size(); i++) {
            ImageView img = new ImageView(context);
            img.setImageResource(R.drawable.selector_ic_next);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dip2px(context, 4), Utils.dip2px
                    (context, 4));
            if (i > 0) {
                lp.setMargins(Utils.dip2px(context, 6), 0, 0, 0);
                img.setSelected(false);
            } else {
                img.setSelected(true);
            }
            img.setLayoutParams(lp);
            dots.add(img);
            dotLayout.addView(img);
        }
        try {
            startScroll(views);
        } catch (Exception e) {
            e.toString();
        }
       /* WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        final List<ImageView> tempViews = views;
        for (final ImageView img : tempViews) {
            if (!TextUtil.isEmpty((String) img.getTag())) {
                Picasso.with(context)
                        .load((String) img.getTag())
                        .placeholder(R.drawable.ic_def_logo_720_288)
                        .error(R.drawable.ic_def_logo_720_288)
                       *//* .resize(width, CommonUtil.dip2px(context, 158))
                        .centerInside()*//*
                        .fit()
                        .tag(context)
                        .into(img);
            }

        }*/
    }

    /**
     * 停止自动播放
     */
    public void stopScroll() {
//        if (timer != null)
//            timer.cancel();
    }

    /**
     * 启动自动滚动播放
     *
     * @param views
     */
    private void startScroll(final List<ImageView> views) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (autoScroll && views != null && views.size() > 0) {
            task = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int i = 0; i < views.size(); i++) {
                                    dots.get(i).setSelected(false);
                                }
                                currentItem += 1;
                            } catch (Exception e) {
                                currentItem = 0; //后台添加或删除图片时，跳动混乱
                            }
                            if (currentItem >= views.size()) {
                                currentItem = 0;
                            }
                            dots.get(currentItem).setSelected(true);
                            viewPager.setCurrentItem(currentItem, true);
                            oldItem = currentItem;
                        }
                    });
                }
            };
            timer = new Timer();
            timer.schedule(task, 5000, 4000);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        dots.get(position).setEnabled(true);
        dots.get(currentItem).setEnabled(false);

        dots.get(oldItem).setSelected(false);
        oldItem = currentItem = position;
        dots.get(currentItem).setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按钮按下逻辑
                if (task != null) {
                    task.cancel();
                    task = null;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startScroll(initImg);
                break;
        }
        Log.i("=======================", ev.getAction() + "");
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 首页轮播图ViewPager 适配器
     *
     * @author flan
     * @since 2016年5月9日
     */
    protected class BannerAdapter extends PagerAdapter {

        private List<ImageView> views;

        public void setData(List<ImageView> views) {
            this.views = views;
        }

        public BannerAdapter(List<ImageView> views) {
            this.views = views;
        }

        @Override
        public int getCount() {

            if (views != null) {
                return views.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

    }
}
