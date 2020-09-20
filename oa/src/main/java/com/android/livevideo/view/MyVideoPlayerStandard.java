package com.android.livevideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.livevideo.R;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * 这里可以监听到视频播放的生命周期和播放状态
 * 所有关于视频的逻辑都应该写在这里
 * Created by Nathen on 2017/7/2.
 */
public class MyVideoPlayerStandard extends JZVideoPlayerStandard {
    public MyVideoPlayerStandard(Context context) {
        super(context);
    }

    public MyVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ngame_jz_layout_standard;
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.fullscreen) {
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //click quit fullscreen
            } else {
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        fullscreenButton.setImageResource(R.drawable.ic_exit_full);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            //click quit fullscreen
            fullscreenButton.setImageResource(R.drawable.ic_exit_full);
        } else {
            fullscreenButton.setImageResource(R.drawable.ic_exit_full);
        }
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        fullscreenButton.setImageResource(R.drawable.ic_exit_full);
    }

    @Override
    public void startWindowTiny() {
        super.startWindowTiny();
    }

}
