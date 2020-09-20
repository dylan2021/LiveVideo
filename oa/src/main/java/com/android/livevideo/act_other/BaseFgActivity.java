package com.android.livevideo.act_other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.base.service.ConnectionChangeReceiver;
import com.android.livevideo.util.StatusBarUtil;

/**
 * @author Gool Lee
 * @Date
 */
@SuppressLint("WrongConstant")
public class BaseFgActivity extends FragmentActivity {

    public ConnectionChangeReceiver myReceiver;
    protected final String TAG = "777";
    protected TextView emptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        registerReceiver();
    }

    protected void initTitleBackBt(String title) {
        TextView titleTv = (TextView) findViewById(R.id.center_tv);
        if (titleTv != null) {
            titleTv.setText(title);
        }
        View finishBt = findViewById(R.id.left_bt);
        if (finishBt != null) {
            finishBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    protected void initTitleBackBtSetText(String title, String backText) {
        TextView titleTv = (TextView) findViewById(R.id.center_tv);
        if (titleTv != null) {
            titleTv.setText(title);
        }
        Button finishBt = findViewById(R.id.left_bt);
        finishBt.setText(backText);
        if (finishBt != null) {
            finishBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    protected void setTv(int id, String text) {
        ((TextView) findViewById(id)).setText(text);

    }

    protected Button getTitleRightBt(String rightText) {
        Button rightBt = (Button) findViewById(R.id.title_right_bt);
        rightBt.setText(rightText);
        rightBt.setVisibility(View.VISIBLE);
        return rightBt;
    }

    /* 注册广播，监听网络异常 */
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);

    }

    /* 取消网络监听 */
    public void unregisterReceiver() {
        if (myReceiver != null) {
            this.unregisterReceiver(myReceiver);
        }
    }

    /**
     * 点击空白处，关闭软键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    //透明状态栏
    protected void initStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
    }
}
