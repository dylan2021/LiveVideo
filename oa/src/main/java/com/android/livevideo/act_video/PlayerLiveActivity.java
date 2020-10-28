package com.android.livevideo.act_video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.core.utils.KeyConst;
import com.dou361.ijkplayer.bean.LiveBean;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;


/**
 * 描 述：直播全屏竖屏场景
 * <p/>
 */
public class PlayerLiveActivity extends BaseFgActivity {

    private Context mContext;
    private View rootView;
    private List<LiveBean> list;
    private String title = "";
    private PowerManager.WakeLock wakeLock;
    private String url;
    private NgameJZVideoPlayerStandard playerStandard;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        Intent i = getIntent();
        initStatusBar();
        setContentView(R.layout.player_view_player);
        url = i.getStringExtra(KeyConst.url);
        Log.d("播放", "播放" + url);

        title = i.getStringExtra(KeyConst.title);
        ((TextView) findViewById(R.id.app_title_tv)).setText(title);
        playerStandard = findViewById(R.id.video_view);
       // playerStandard.topContainer.setVisibility(View.GONE);
        playerStandard.setUp(url, JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "视频");
        playerStandard.startVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override

    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

}
