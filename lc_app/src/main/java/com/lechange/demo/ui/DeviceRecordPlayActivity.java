package com.lechange.demo.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceRecordService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeleteCloudRecordsData;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.RecordsData;
import com.lechange.demo.R;
import com.lechange.demo.dialog.EncryptKeyInputDialog;
import com.lechange.demo.tools.DateHelper;
import com.lechange.demo.tools.DialogUtils;
import com.lechange.demo.tools.MediaPlayHelper;
import com.lechange.demo.view.LcProgressBar;
import com.lechange.demo.view.RecoderSeekBar;
import com.lechange.opensdk.listener.LCOpenSDK_DownloadListener;
import com.lechange.opensdk.listener.LCOpenSDK_EventListener;
import com.lechange.opensdk.media.LCOpenSDK_Download;
import com.lechange.opensdk.media.LCOpenSDK_ParamCloudRecord;
import com.lechange.opensdk.media.LCOpenSDK_ParamDeviceRecord;
import com.lechange.opensdk.media.LCOpenSDK_PlayWindow;
import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.mobilecommon.dialog.LCAlertDialog;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DeviceRecordPlayActivity extends AppCompatActivity implements View.OnClickListener, IGetDeviceInfoCallBack.IDeviceDeleteRecordCallBack {
    private static final String TAG = DeviceRecordPlayActivity.class.getSimpleName();
    private FrameLayout frLiveWindow, frLiveWindowContent;
    private TextView tvDeviceName, tvLoadingMsg, recordStartTime, recordEndTime;
    private LinearLayout llFullScreen, llSound, llPlayStyle, llPlayPause, llDelete, llBack, llScreenshot, llVideo;
    private ImageView ivPalyPause, ivPlayStyle, ivSound, ivScreenShot, ivVideo;
    private ProgressBar pbLoading;
    private RelativeLayout rlLoading;
    private LcProgressBar pgDownload;
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;
    private Bundle bundle;
    private LCOpenSDK_PlayWindow mPlayWin = new LCOpenSDK_PlayWindow();
    private RecordsData recordsData;
    //1 云录像 2 设备录像
    private int recordType;
    private RecoderSeekBar recordSeekbar;
    private int progress;
    private String beginTime;

    private SoundStatus soundStatus = SoundStatus.PLAY;
    private PlayStatus playStatus = PlayStatus.PAUSE;
    private RecordStatus recordStatus = RecordStatus.STOP;
    private DownloadStatus downloadStatus = DownloadStatus.UNBEGIN;
    //倍速位置
    private int speedPosition = 0;
    //倍速数组
    private int[] speed = {1, 4, 8, 16};
    //倍速图片
    private Drawable[] speedImage = new Drawable[4];
    private DeviceRecordService deviceRecordService = ClassInstanceManager.newInstance().getDeviceRecordService();
    private String totalMb;
    private ImageView ivChangeScreen;
    private LinearLayout llOperate;
    private RelativeLayout rlTitle;
    private EncryptKeyInputDialog encryptKeyInputDialog;
    private String encryptKey;
    private String path;
    private LCAlertDialog mLCAlertDialog;

    public enum LoadStatus {
        LOADING, LOAD_SUCCESS, LOAD_ERROR
    }

    public enum SoundStatus {
        PLAY, STOP, NO_SUPPORT
    }

    public enum PlayStatus {
        PLAY, PAUSE, ERROR ,STOP
    }

    public enum RecordStatus {
        START, STOP
    }

    public enum DownloadStatus {
        UNBEGIN, ING, FINISH, ERROR
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentOrientation = getResources().getConfiguration().orientation;
        setContentView(R.layout.activity_device_record_play);
        initView();
        initData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(frLiveWindow.getLayoutParams());
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            mLayoutParams.width = metric.widthPixels; // 屏幕宽度（像素）
            mLayoutParams.height = metric.widthPixels * 9 / 16;
            mLayoutParams.setMargins(0, 0, 0, 0);
            frLiveWindow.setLayoutParams(mLayoutParams);
            MediaPlayHelper.quitFullScreen(DeviceRecordPlayActivity.this);
            llOperate.setVisibility(View.VISIBLE);
            if (recordType == MethodConst.ParamConst.recordTypeLocal) {
                pgDownload.setVisibility(View.GONE);
            }else {
                pgDownload.setVisibility(View.VISIBLE);
            }
            rlTitle.setVisibility(View.VISIBLE);
        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(metric.widthPixels, metric.heightPixels);
            mLayoutParams.setMargins(0, 0, 0, 0);
            frLiveWindow.setLayoutParams(mLayoutParams);
            MediaPlayHelper.setFullScreen(DeviceRecordPlayActivity.this);
            llOperate.setVisibility(View.GONE);
            pgDownload.setVisibility(View.GONE);
            rlTitle.setVisibility(View.GONE);
        }
    }

    private void initData() {
        speedImage[0] = getResources().getDrawable(R.mipmap.play_module_video_1x);
        speedImage[1] = getResources().getDrawable(R.mipmap.play_module_video_4x);
        speedImage[2] = getResources().getDrawable(R.mipmap.play_module_video_8x);
        speedImage[3] = getResources().getDrawable(R.mipmap.play_module_video_16x);
        bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        deviceListBean = (DeviceDetailListData.ResponseData.DeviceListBean) bundle.getSerializable(MethodConst.ParamConst.deviceDetail);
        recordsData = (RecordsData) bundle.getSerializable(MethodConst.ParamConst.recordData);
        recordType = bundle.getInt(MethodConst.ParamConst.recordType);
        tvDeviceName.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
        if (recordType == MethodConst.ParamConst.recordTypeLocal) {
            llDelete.setVisibility(View.GONE);
            pgDownload.setVisibility(View.GONE);
            totalMb = byte2mb(Long.parseLong(recordsData.fileLength + ""));
        } else {
            totalMb = byte2mb(Long.parseLong(recordsData.size));
        }
        //初始化时间
        initSeekBarAndTime();
        //初始化控件
        initCommonClickListener();
        //播放视频
        loadingStatus(LoadStatus.LOADING, getResources().getString(R.string.lc_demo_device_video_play_loading), deviceListBean.deviceId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseAsync();
        playStatus = PlayStatus.PAUSE;
        ivPalyPause.setImageDrawable(getDrawable(R.mipmap.lc_demo_live_video_icon_h_play));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
        mPlayWin.uninitPlayWindow();// 销毁底层资源
    }

    private void playVideo(String psk) {
        if (recordType == MethodConst.ParamConst.recordTypeCloud) {
            //云录像
            LCOpenSDK_ParamCloudRecord paramCloudRecord = new LCOpenSDK_ParamCloudRecord(
                    LCDeviceEngine.newInstance().accessToken,
                    deviceListBean.deviceId,
                    Integer.parseInt(deviceListBean.channels.get(deviceListBean.checkedChannel).channelId),
                    psk,
                    deviceListBean.playToken,
                    recordsData.recordRegionId,
                    1000,
                    0,
                    24 * 3600
            );
            mPlayWin.playCloud(paramCloudRecord);
        } else if (recordType == MethodConst.ParamConst.recordTypeLocal) {
            //设备录像
            LCOpenSDK_ParamDeviceRecord paramDeviceRecord = new LCOpenSDK_ParamDeviceRecord(
                    LCDeviceEngine.newInstance().accessToken,
                    deviceListBean.deviceId,
                    Integer.parseInt(deviceListBean.channels.get(deviceListBean.checkedChannel).channelId),
                    psk,
                    deviceListBean.playToken,
                    recordsData.recordId,
                    DateHelper.parseMills(recordsData.beginTime),
                    DateHelper.parseMills(recordsData.endTime),
                    0,
                    0,
                    true
            );
            mPlayWin.playRtspPlayback(paramDeviceRecord);
        }
    }

    private void stop() {
        stopPlayWindow();
        //禁止拖动
        setCanSeekChanged(false);
    }

    /**
     * 单独关闭播放  注意：组件要求必须要主线程调用
     */
    private void stopPlayWindow() {
        closeAudio();// 关闭音频
        if (recordType == MethodConst.ParamConst.recordTypeCloud) {
            mPlayWin.stopCloud(true);
        } else {
            mPlayWin.stopRtspPlayback(true);// 关闭视频
        }
    }

    /**
     * 设置拖动进度条是否能使用
     */
    public void setCanSeekChanged(boolean canSeek) {
        recordSeekbar.setCanTouchAble(canSeek);
    }

    /**
     * 开始录像
     */
    public boolean startRecord() {
        // 录像的路径
        String videoPath = null;
        String channelName = null;
        if (deviceListBean.channels != null && deviceListBean.channels.size() > 0) {
            channelName = deviceListBean.channels.get(deviceListBean.checkedChannel).channelName;
        } else {
            channelName = deviceListBean.name;
        }
        // 去除通道中在目录中的非法字符
        channelName = channelName.replace("-", "");
        videoPath = MediaPlayHelper.getCaptureAndVideoPath(MediaPlayHelper.DHFilesType.DHVideo, channelName);
        MediaScannerConnection.scanFile(this, new String[]{videoPath}, null, null);
        // 开始录制 1
        int ret = mPlayWin.startRecord(videoPath, 1, 0x7FFFFFFF);
        return ret == 0;
    }

    /**
     * 关闭录像
     */
    public boolean stopRecord() {
        return mPlayWin.stopRecord() == 0;
    }

    /**
     * 截图
     */
    public String capture() {
        String captureFilePath = null;
        String channelName = null;
        if (deviceListBean.channels != null && deviceListBean.channels.size() > 0) {
            channelName = deviceListBean.channels.get(deviceListBean.checkedChannel).channelName;
        } else {
            channelName = deviceListBean.name;
        }
        // 去除通道中在目录中的非法字符
        channelName = channelName.replace("-", "");
        captureFilePath = MediaPlayHelper.getCaptureAndVideoPath(MediaPlayHelper.DHFilesType.DHImage, channelName);
        int ret = mPlayWin.snapShot(captureFilePath);
        if (ret == 0) {
            // 扫描到相册中
            MediaScannerConnection.scanFile(this, new String[]{captureFilePath}, null, null);
        } else {
            captureFilePath = null;
        }
        return captureFilePath;
    }

    private void initView() {
        frLiveWindow = findViewById(R.id.fr_live_window);
        frLiveWindowContent = findViewById(R.id.fr_live_window_content);
        llBack = findViewById(R.id.ll_back);
        tvDeviceName = findViewById(R.id.tv_device_name);
        llDelete = findViewById(R.id.ll_delete);
        llPlayPause = findViewById(R.id.ll_paly_pause);
        llPlayStyle = findViewById(R.id.ll_play_style);
        llSound = findViewById(R.id.ll_sound);
        llFullScreen = findViewById(R.id.ll_fullscreen);
        ivPalyPause = findViewById(R.id.iv_paly_pause);
        ivPlayStyle = findViewById(R.id.iv_play_style);
        ivSound = findViewById(R.id.iv_sound);
        rlLoading = findViewById(R.id.rl_loading);
        pbLoading = findViewById(R.id.pb_loading);
        tvLoadingMsg = findViewById(R.id.tv_loading_msg);
        recordStartTime = findViewById(R.id.record_startTime);
        recordSeekbar = findViewById(R.id.record_seekbar);
        recordEndTime = findViewById(R.id.record_endTime);
        llScreenshot = findViewById(R.id.ll_screenshot);
        llVideo = findViewById(R.id.ll_video);
        ivScreenShot = findViewById(R.id.iv_screen_shot);
        ivVideo = findViewById(R.id.iv_video);
        pgDownload = findViewById(R.id.pg_download);
        ivChangeScreen = findViewById(R.id.iv_change_screen);
        llOperate = findViewById(R.id.ll_operate);
        rlTitle = findViewById(R.id.rl_title);
        pgDownload.setText(getResources().getString(R.string.lc_demo_device_record_download));
        // 初始化播放窗口
        LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) frLiveWindow
                .getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mLayoutParams.width = metric.widthPixels; // 屏幕宽度（像素）
        mLayoutParams.height = metric.widthPixels * 9 / 16;
        mLayoutParams.setMargins(0, 0, 0, 0);
        frLiveWindow.setLayoutParams(mLayoutParams);
        mPlayWin.initPlayWindow(this, frLiveWindowContent, 0, false);
        setWindowListener(mPlayWin);
        mPlayWin.openTouchListener();//开启收拾监听
    }

    private void initCommonClickListener() {
        llBack.setOnClickListener(this);
        llFullScreen.setOnClickListener(this);
        if (recordType == MethodConst.ParamConst.recordTypeCloud) {
            llDelete.setOnClickListener( this);
            pgDownload.setOnClickListener(this );
            LCOpenSDK_Download.setListener(new CloudDownloadListener());
        }
    }

    private void featuresClickListener(boolean loadSuccess) {
        llPlayStyle.setOnClickListener(loadSuccess ? this : null);
        llPlayPause.setOnClickListener(loadSuccess ? this : null);
        llSound.setOnClickListener(loadSuccess ? this : null);
        ivScreenShot.setOnClickListener(loadSuccess ? this : null);
        ivVideo.setOnClickListener(loadSuccess ? this : null);
        ivPalyPause.setImageDrawable(loadSuccess ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_pause) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_pause_disable));
        ivSound.setImageDrawable(loadSuccess ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_off) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_off_disable));
        ivScreenShot.setImageDrawable(loadSuccess ? getDrawable(R.drawable.lc_demo_photo_capture_selector) : getDrawable(R.mipmap.lc_demo_livepreview_icon_screenshot_disable));
        ivVideo.setImageDrawable(loadSuccess ? getDrawable(R.mipmap.lc_demo_livepreview_icon_video) : getDrawable(R.mipmap.lc_demo_livepreview_icon_video_disable));
        //媒体声
        if (soundStatus != SoundStatus.PLAY) {
            return;
        }
        if (openAudio()) {
            soundStatus = SoundStatus.PLAY;
            ivSound.setImageDrawable(getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_on));
        }
    }

    private void initSeekBarAndTime() {
        String startTime = recordsData.beginTime.substring(11);
        String endTime = recordsData.endTime.substring(11);
        recordSeekbar.setMax((int) ((DateHelper.parseMills(recordsData.endTime) - DateHelper.parseMills(recordsData.beginTime)) / 1000));
        recordSeekbar.setProgress(0);
        recordStartTime.setText(startTime);
        recordEndTime.setText(endTime);
    }

    private void setSeekBarListener() {
        recordSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (recordSeekbar.getMax() - DeviceRecordPlayActivity.this.progress <= 2) {
                    seek(recordSeekbar.getMax() >= 2 ? recordSeekbar.getMax() - 2 : 0);
                } else {
                    seek(DeviceRecordPlayActivity.this.progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean byUser) {
                if (byUser) {
                    DeviceRecordPlayActivity.this.progress = progress;
                }
            }
        });
    }

    /**
     * 继续播放(异步)
     */
    public void resumeAsync() {
        mPlayWin.resumeAsync();
    }

    /**
     * 暂停播放(异步)
     */
    public void pauseAsync() {
        mPlayWin.pauseAsync();
    }

    public boolean openAudio() {
        return mPlayWin.playAudio() == 0;
    }

    public boolean closeAudio() {
        return mPlayWin.stopAudio() == 0;
    }

    public void seek(int index) {
        long seekTime = DateHelper.parseMills(recordsData.beginTime) / 1000 + index;
        //先暂存时间记录
        beginTime = DateHelper.getTimeHMS(seekTime * 1000);
        this.progress = index;
        recordSeekbar.setProgress(index);
        recordStartTime.setText(this.beginTime);
        mPlayWin.seek(index);
    }

    private int mCurrentOrientation;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_back) {
            if (downloadStatus == DownloadStatus.ING) {
                LCAlertDialog.Builder builder = new LCAlertDialog.Builder(this);
                builder.setTitle(R.string.lc_demo_device_delete_exit);
                builder.setMessage(R.string.lc_demo_device_delete_exit_tip);
                builder.setCancelButton(com.mm.android.deviceaddmodule.R.string.common_cancel, null);
                builder.setConfirmButton(com.mm.android.deviceaddmodule.R.string.common_confirm,
                        new LCAlertDialog.OnClickListener() {
                            @Override
                            public void onClick(LCAlertDialog dialog, int which, boolean isChecked) {
                                stopDownLoad();
                                finish();
                            }
                        });
                mLCAlertDialog = builder.create();
                mLCAlertDialog.show(getSupportFragmentManager(), "exit");
                return;
            }
            finish();
        } else if (id == R.id.ll_fullscreen) {
            //横竖屏切换
            if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mCurrentOrientation = Configuration.ORIENTATION_LANDSCAPE;
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mCurrentOrientation = Configuration.ORIENTATION_PORTRAIT;
            }
            ivChangeScreen.setImageDrawable(mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE ? getResources().getDrawable(R.mipmap.live_btn_smallscreen) : getResources().getDrawable(R.mipmap.video_fullscreen));
        } else if (id == R.id.pg_download) {
            //下载
            if (downloadStatus == DownloadStatus.ING || downloadStatus == DownloadStatus.FINISH) {
                return;
            }
            pgDownloadProgress(
                    getResources().getString(R.string.lc_demo_device_record_download_begin),
                    0,
                    100,
                    getResources().getColor(R.color.lc_demo_color_ffffff));
            downloadStatus = DownloadStatus.ING;
            startDownload();
        } else if (id == R.id.ll_play_style) {
            if (speedPosition == 3) {
                speedPosition = 0;
            } else {
                speedPosition = speedPosition + 1;
            }
            ivPlayStyle.setImageDrawable(speedImage[speedPosition]);
            mPlayWin.setPlaySpeed(speed[speedPosition]);
        } else if (id == R.id.ll_delete) {
            LCAlertDialog.Builder builder = new LCAlertDialog.Builder(this);
            builder.setTitle(R.string.lc_demo_device_delete_sure);
            builder.setMessage("");
            builder.setCancelButton(com.mm.android.deviceaddmodule.R.string.common_cancel, null);
            builder.setConfirmButton(com.mm.android.deviceaddmodule.R.string.common_confirm,
                    new LCAlertDialog.OnClickListener() {
                        @Override
                        public void onClick(LCAlertDialog dialog, int which, boolean isChecked) {
                            //删除
                            DialogUtils.show(DeviceRecordPlayActivity.this);
                            DeleteCloudRecordsData deleteCloudRecordsData = new DeleteCloudRecordsData();
                            List<String> recordRegionIds = new ArrayList<>();
                            recordRegionIds.add(recordsData.recordRegionId);
                            deleteCloudRecordsData.data.recordRegionIds = recordRegionIds;
                            deviceRecordService.deleteCloudRecords(deleteCloudRecordsData, DeviceRecordPlayActivity.this);
                        }
                    });
            mLCAlertDialog = builder.create();
            mLCAlertDialog.show(getSupportFragmentManager(), "delete");
        } else if (id == R.id.ll_paly_pause) {
            //播放暂停 重新播放
            if (playStatus == PlayStatus.PLAY) {
                pauseAsync();
                featuresClickListener(false);
                llPlayPause.setOnClickListener(this);
                playStatus = (playStatus == PlayStatus.PLAY) ? PlayStatus.PAUSE : PlayStatus.PLAY;
                ivPalyPause.setImageDrawable(playStatus == PlayStatus.PLAY ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_pause) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_play));
            } else if (playStatus == PlayStatus.PAUSE){
                resumeAsync();
                featuresClickListener(true);
                llPlayPause.setOnClickListener(this);
                playStatus = (playStatus == PlayStatus.PLAY) ? PlayStatus.PAUSE : PlayStatus.PLAY;
                ivPalyPause.setImageDrawable(playStatus == PlayStatus.PLAY ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_pause) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_play));
            }else {
                loadingStatus(LoadStatus.LOADING, getResources().getString(R.string.lc_demo_device_video_play_change), TextUtils.isEmpty(encryptKey)?deviceListBean.deviceId:encryptKey);
            }
        } else if (id == R.id.ll_sound) {
            //媒体声 如果是开启去关闭，反之
            if (soundStatus == SoundStatus.NO_SUPPORT) {
                return;
            }
            boolean result = false;
            if (soundStatus == SoundStatus.PLAY) {
                result = closeAudio();
            } else {
                result = openAudio();
            }
            if (!result) {
                return;
            }
            soundStatus = (soundStatus == SoundStatus.PLAY) ? SoundStatus.STOP : SoundStatus.PLAY;
            ivSound.setImageDrawable(soundStatus == SoundStatus.PLAY ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_on) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_off));
        } else if (id == R.id.iv_screen_shot) {
            //截图
            if (capture() != null) {
                Toast.makeText(this, getResources().getString(R.string.lc_demo_device_capture_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.lc_demo_device_capture_failed), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.iv_video) {
            if (playStatus != PlayStatus.PLAY) {
                return;
            }
            //录像 如果是关闭状态去打开，反之
            if (recordStatus == RecordStatus.STOP) {
                if (startRecord()) {
                    Toast.makeText(this, getResources().getString(R.string.lc_demo_device_record_begin), Toast.LENGTH_SHORT).show();
                } else {
                    return;
                }
            } else {
                if (stopRecord()) {
                    Toast.makeText(this, getResources().getString(R.string.lc_demo_device_record_stop), Toast.LENGTH_SHORT).show();
                } else {
                    return;
                }
            }
            recordStatus = (recordStatus == RecordStatus.START) ? RecordStatus.STOP : RecordStatus.START;
            ivVideo.setImageDrawable(recordStatus == RecordStatus.START ? getDrawable(R.mipmap.lc_demo_livepreview_icon_video_ing) : getDrawable(R.mipmap.lc_demo_livepreview_icon_video));
        }
    }

    private void setWindowListener(LCOpenSDK_PlayWindow playWin) {
        playWin.setWindowListener(new LCOpenSDK_EventListener() {
            //手势缩放开始事件
            @Override
            public void onZoomBegin(int index) {
                super.onZoomBegin(index);
                LogUtil.debugLog(TAG, "onZoomBegin: index= " + index);
            }

            //手势缩放中事件
            @Override
            public void onZooming(int index, float dScale) {
                super.onZooming(index, dScale);
                LogUtil.debugLog(TAG, "onZooming: index= " + index + " , dScale= " + dScale);
                mPlayWin.doScale(dScale);
            }

            //缩放结束事件
            @Override
            public void onZoomEnd(int index, ZoomType zoomType) {
                super.onZoomEnd(index, zoomType);
                LogUtil.debugLog(TAG, "onZoomEnd: index= " + index + " , zoomType= " + zoomType);
            }

            //窗口单击事件
            @Override
            public void onControlClick(int index, float dx, float dy) {
                super.onControlClick(index, dx, dy);
                LogUtil.debugLog(TAG, "onControlClick: index= " + index + " , dx= " + dx + " , dy= " + dy);
            }

            //窗口双击事件
            @Override
            public void onWindowDBClick(int index, float dx, float dy) {
                super.onWindowDBClick(index, dx, dy);
                LogUtil.debugLog(TAG, "onWindowDBClick: index= " + index + " , dx= " + dx + " , dy= " + dy);
            }

            //滑动开始事件
            @Override
            public boolean onSlipBegin(int index, Direction direction, float dx, float dy) {
                LogUtil.debugLog(TAG, "onSlipBegin: index= " + index + " , direction= " + direction + " , dx= " + dx + " , dy= " + dy);
                return super.onSlipBegin(index, direction, dx, dy);
            }

            //滑动中事件
            @Override
            public void onSlipping(int index, Direction direction, float prex, float prey, float dx, float dy) {
                super.onSlipping(index, direction, prex, prey, dx, dy);
                LogUtil.debugLog(TAG, "onSlipping: index= " + index + " , direction= " + direction + " , prex= " + prex + " , prey= " + prey + " , dx= " + dx + " , dy= " + dy);
            }

            //滑动结束事件
            @Override
            public void onSlipEnd(int index, Direction direction, float dx, float dy) {
                super.onSlipEnd(index, direction, dx, dy);
                LogUtil.debugLog(TAG, "onSlipEnd: index= " + index + " , direction= " + direction + " , dx= " + dx + " , dy= " + dy);
            }

            //长按开始回调
            @Override
            public void onWindowLongPressBegin(int index, Direction direction, float dx, float dy) {
                super.onWindowLongPressBegin(index, direction, dx, dy);
                LogUtil.debugLog(TAG, "onWindowLongPressBegin: index= " + index + " , direction= " + direction + " , dx= " + dx + " , dy= " + dy);
            }

            //长按事件结束
            @Override
            public void onWindowLongPressEnd(int index) {
                super.onWindowLongPressEnd(index);
                LogUtil.debugLog(TAG, "onWindowLongPressEnd: index= " + index);
            }

            /**
             * 播放事件回调
             * resultSource:  0--RTSP  1--HLS  5--DHHTTP  99--OPENAPI
             */
            @Override
            public void onPlayerResult(int index, String code, int resultSource) {
                super.onPlayerResult(index, code, resultSource);
                LogUtil.debugLog(TAG, "onPlayerResult: index= " + index + " , code= " + code + " , resultSource= " + resultSource);
                boolean failed = false;
                if (resultSource == 99) {
                    //code  -1000 HTTP交互出错或超时
                    failed = true;
                } else if (resultSource == 1) {
                    //云录像
                    if (code.equals("0") || code.equals("4") || code.equals("7") || code.equals("11")) {
                        failed = true;
                    }
                    if (code.equals("11")) {
                        showInputKey();
                    }
                } else if (resultSource == 0) {
                    //设备录像
                    if (code.equals("0") || code.equals("1") || code.equals("3") || code.equals("7")) {
                        failed = true;
                    }
                    if (code.equals("7")) {
                        showInputKey();
                    }
                } else if (resultSource == 5) {
                    //设备录像
                    if (!(code.equals("1000") || code.equals("0") || code.equals("2000") || code.equals("4000"))) {
                        failed = true;
                    }
                    if (code.equals("1000005")) {
                        showInputKey();
                    }
                }
                if (failed) {
                    loadingStatus(LoadStatus.LOAD_ERROR, getResources().getString(R.string.lc_demo_device_video_play_error) + ":" + code + "." + resultSource, "");
                }
            }

            //分辨率改变事件
            @Override
            public void onResolutionChanged(int index, int width, int height) {
                super.onResolutionChanged(index, width, height);
                LogUtil.debugLog(TAG, "onResolutionChanged: index= " + index + " , width= " + width + " , height= " + height);
            }

            //播放开始回调
            @Override
            public void onPlayBegan(int index) {
                super.onPlayBegan(index);
                LogUtil.debugLog(TAG, "onPlayBegan: index= " + index);
                loadingStatus(LoadStatus.LOAD_SUCCESS, "", "");
            }

            //接收数据回调
            @Override
            public void onReceiveData(int index, int len) {
                super.onReceiveData(index, len);
                LogUtil.debugLog(TAG, "onReceiveData: index= " + index + " , len= " + len);
            }

            //接收帧流回调
            @Override
            public void onStreamCallback(int index, byte[] bytes, int len) {
                super.onStreamCallback(index, bytes, len);
                LogUtil.debugLog(TAG, "onStreamCallback: index= " + index + " , len= " + len);
            }

            //播放结束事件
            @Override
            public void onPlayFinished(int index) {
                super.onPlayFinished(index);
                LogUtil.debugLog(TAG, "onPlayFinished: index= " + index);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stop();
                        playStatus = PlayStatus.STOP;
                        featuresClickListener(false);
                        speedPosition = 0;
                        ivPlayStyle.setImageDrawable(speedImage[0]);
                        llPlayPause.setOnClickListener( DeviceRecordPlayActivity.this );
                        ivPalyPause.setImageDrawable(getDrawable(R.mipmap.lc_demo_live_video_icon_h_play));
                    }
                });
            }

            //播放时间信息回调
            @Override
            public void onPlayerTime(int index, final long current) {
                super.onPlayerTime(index, current);
                LogUtil.debugLog(TAG, "onPlayerTime: index= " + index + " , time= " + current);
                long startTime = DateHelper.parseMills(recordsData.beginTime) / 1000;
                DeviceRecordPlayActivity.this.progress = (int) (current - startTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordSeekbar.setProgress(DeviceRecordPlayActivity.this.progress);
                        DeviceRecordPlayActivity.this.beginTime = DateHelper.getTimeHMS(current * 1000);
                        recordStartTime.setText(DeviceRecordPlayActivity.this.beginTime);
                    }
                });
            }
        });
    }

    private void showInputKey() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (encryptKeyInputDialog == null) {
                    encryptKeyInputDialog = new EncryptKeyInputDialog(DeviceRecordPlayActivity.this);
                }
                encryptKeyInputDialog.show();
                encryptKeyInputDialog.setOnClick(new EncryptKeyInputDialog.OnClick() {
                    @Override
                    public void onSure(String txt) {
                        encryptKey = txt;
                        loadingStatus(LoadStatus.LOADING, getResources().getString(R.string.lc_demo_device_video_play_change), txt);
                    }
                });
            }
        });
    }

    /**
     * 播放状态
     *
     * @param loadStatus 播放状态
     * @param msg
     */
    private void loadingStatus(final LoadStatus loadStatus, final String msg, final String psk) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadStatus == LoadStatus.LOADING) {
                    //先关闭
                    stop();
                    //开始播放
                    playVideo(psk);
                    rlLoading.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.VISIBLE);
                    tvLoadingMsg.setText(msg);
                    //禁止拖动
                    setCanSeekChanged(false);
                } else if (loadStatus == LoadStatus.LOAD_SUCCESS) {
                    //播放成功
                    rlLoading.setVisibility(View.GONE);
                    //允许拖动
                    setCanSeekChanged(true);
                    //SeekBar监听
                    setSeekBarListener();
                    playStatus = PlayStatus.PLAY;
                    featuresClickListener(true);
                } else {
                    //播放失败
                    stop();
                    rlLoading.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.GONE);
                    tvLoadingMsg.setText(msg);
                    //禁止拖动
                    setCanSeekChanged(false);
                    playStatus = PlayStatus.ERROR;
                    featuresClickListener(false);
                }
            }
        });
    }

    private void startDownload() {
        path = MediaPlayHelper.getDownloadVideoPath(0, recordsData.recordId, DateHelper.parseMills(recordsData.beginTime));
        LCOpenSDK_Download.startDownload(0,
                path,
                LCDeviceEngine.newInstance().accessToken,
                recordsData.recordRegionId,
                recordsData.deviceId,
                String.valueOf(0),
                recordsData.deviceId,
                1000,
                5000);
    }

    private void stopDownLoad() {
        LCOpenSDK_Download.stopDownload(0);
    }

    private int downloadProgress;

    private class CloudDownloadListener extends LCOpenSDK_DownloadListener {
        @Override
        public void onDownloadReceiveData(int index, final int dataLen) {
            LogUtil.debugLog(TAG, "CloudDownloadListener----" + "index=" + index + ", dataLen=" + dataLen);
            if (downloadStatus == DownloadStatus.ING) {
                downloadProgress = downloadProgress + dataLen;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pgDownloadProgress(
                                getResources().getString(R.string.lc_demo_device_record_download_begin) + byte2mb(downloadProgress) + "MB/" + totalMb + "M",
                                (int) ((float) downloadProgress / (float) Long.parseLong(recordsData.size) * 100),
                                100,
                                getResources().getColor(R.color.lc_demo_color_ffffff));

                    }
                });
            }
        }

        @Override
        public void onDownloadState(int index, String code, int type) {
            LogUtil.debugLog(TAG, "CloudDownloadListener----" + "index=" + index + ", code=" + code + ", type=" + type);
            if (type == 99 || code.equals("0") || code.equals("4") || code.equals("7") || code.equals("8") || code.equals("11")) {
                //下载出错
                downloadStatus = DownloadStatus.ERROR;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pgDownloadProgress(getResources().getString(R.string.lc_demo_device_record_download_error),
                                0,
                                0,
                                getResources().getColor(R.color.lc_demo_color_FF4F4F));
                        downloadProgress = 0;
                    }
                });
            }

            if (type == 1 && code.equals("1")) {
                //开始下载
                downloadStatus = DownloadStatus.ING;
            }

            if (type == 1 && code.equals("2")) {
                //下载完成
                downloadStatus = DownloadStatus.FINISH;
                MediaScannerConnection.scanFile(DeviceRecordPlayActivity.this, new String[]{path}, null, null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pgDownloadProgress(getResources().getString(R.string.lc_demo_device_record_download_finish),
                                100,
                                100,
                                getResources().getColor(R.color.lc_demo_color_ffffff));
                        downloadProgress = 0;
                    }
                });
            }
        }
    }

    private void pgDownloadProgress(String tip, int progress, int secondProgress, int textColor) {
        pgDownload.setText(tip);
        pgDownload.setProgress(progress);
        pgDownload.setSecondaryProgress(secondProgress);
        pgDownload.setTextColor(textColor);
    }

    @Override
    public void deleteDeviceRecord() {
        DialogUtils.dismiss();
        Toast.makeText(this, getResources().getString(R.string.lc_demo_device_record_delete_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("data", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onError(Throwable throwable) {
        DialogUtils.dismiss();
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private String byte2mb(long b) {
        double mb = (double) b / 1024 / 1024;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(mb);
    }
}
