package com.lechange.demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceLocalCacheService;
import com.common.openapi.DeviceRecordService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.CloudRecordsData;
import com.common.openapi.entity.ControlMovePTZData;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceLocalCacheData;
import com.common.openapi.entity.LocalRecordsData;
import com.common.openapi.entity.RecordsData;
import com.lechange.demo.R;
import com.lechange.demo.adapter.MediaPlayRecordAdapter;
import com.lechange.demo.dialog.EncryptKeyInputDialog;
import com.lechange.demo.tools.DateHelper;
import com.lechange.demo.tools.MediaPlayHelper;
import com.lechange.demo.view.Direction;
import com.lechange.demo.view.LcCloudRudderView;
import com.lechange.opensdk.listener.LCOpenSDK_EventListener;
import com.lechange.opensdk.listener.LCOpenSDK_TalkerListener;
import com.lechange.opensdk.media.LCOpenSDK_ParamReal;
import com.lechange.opensdk.media.LCOpenSDK_ParamTalk;
import com.lechange.opensdk.media.LCOpenSDK_PlayWindow;
import com.lechange.opensdk.media.LCOpenSDK_Talk;
import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.device_wifi.DeviceConstant;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceOnlineMediaPlayActivity extends AppCompatActivity implements View.OnClickListener, IGetDeviceInfoCallBack.IDeviceCacheCallBack {
    private static final String TAG = DeviceOnlineMediaPlayActivity.class.getSimpleName();

    private LCOpenSDK_PlayWindow mPlayWin = new LCOpenSDK_PlayWindow();
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;
    private Bundle bundle;
    private FrameLayout frLiveWindow, frLiveWindowContent;
    private TextView tvNoVideo, tvDeviceName, tvCloudVideo, tvLocalVideo, tvLoadingMsg;
    private RecyclerView rcvVideoList;
    private LinearLayout llVideoContent, llVideo, llSpeak, llScreenShot, llCloudStage, llFullScreen, llSound, llPlayStyle, llPlayPause, llDetail, llBack, llVideo1, llSpeak1, llScreenShot1, llCloudStage1;
    private ImageView ivPalyPause, ivPlayStyle, ivSound, ivCloudStage, ivScreenShot, ivSpeak, ivVideo, ivCloudStage1, ivScreenShot1, ivSpeak1, ivVideo1;
    private ProgressBar pbLoading;
    private RelativeLayout rlLoading;
    private LcCloudRudderView rudderView;
    private boolean cloudvideo = true;
    private boolean isShwoRudder = false;
    private AudioTalkerListener audioTalkerListener = new AudioTalkerListener();

    private VideoMode videoMode = VideoMode.MODE_HD;
    private SoundStatus soundStatus = SoundStatus.PLAY;
    private SpeakStatus speakStatus = SpeakStatus.STOP;
    private RecordStatus recordStatus = RecordStatus.STOP;
    private PlayStatus playStatus = PlayStatus.ERROR;
    private LinearLayoutManager linearLayoutManager;
    private MediaPlayRecordAdapter mediaPlayRecordAdapter;
    private List<RecordsData> recordsDataList = new ArrayList<>();
    private List<RecordsData> cloudRecordsDataList;
    private List<RecordsData> localRecordsDataList;
    private String cloudRecordsDataTip = "";
    private String localRecordsDataTip = "";
    private DeviceRecordService deviceRecordService = ClassInstanceManager.newInstance().getDeviceRecordService();
    private Direction mPTZPreDirection = null;
    private int mCurrentOrientation;
    private LinearLayout llController;
    private FrameLayout frRecord;
    private RelativeLayout rlTitle;
    private ImageView ivChangeScreen;
    private EncryptKeyInputDialog encryptKeyInputDialog;
    private String encryptKey;
    private boolean supportPTZ;

    public enum PlayStatus {
        PLAY, PAUSE, ERROR
    }

    public enum LoadStatus {
        LOADING, LOAD_SUCCESS, LOAD_ERROR
    }

    public enum SoundStatus {
        PLAY, STOP, NO_SUPPORT
    }

    public enum SpeakStatus {
        PLAY, STOP, NO_SUPPORT,OPENING
    }

    public enum VideoMode {
        MODE_HD, MODE_SD
    }

    public enum RecordStatus {
        START, STOP
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentOrientation = Configuration.ORIENTATION_PORTRAIT;
        setContentView(R.layout.activity_device_online_media_play);
        initView();
        initData();
        operatePTZ();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switchScreenDirection();
    }

    private void operatePTZ() {
        rudderView.setRudderListener(new LcCloudRudderView.RudderListener() {
            @Override
            public void onSteeringWheelChangedBegin() {

            }

            @Override
            public void onSteeringWheelChangedContinue(Direction direction) {
                if (direction == null && mPTZPreDirection != null) {
                    controlPTZ(mPTZPreDirection, 200, true);
                    mPTZPreDirection = null;
                } else if (direction != mPTZPreDirection) {
                    // 方向不同才更新云台指示图标
                    mPTZPreDirection = direction;
                    controlPTZ(mPTZPreDirection, 30000, false);
                }
            }

            @Override
            public void onSteeringWheelChangedSingle(Direction direction) {
                controlPTZ(direction, 200, false);
            }

            @Override
            public void onSteeringWheelChangedEnd() {
            }
        });
    }

    private void controlPTZ(Direction em, long time, boolean stop) {
        String operation = "";
        if (em == Direction.Left) {
            operation = "2";
        } else if (em == Direction.Right) {
            operation = "3";
        } else if (em == Direction.Up) {
            operation = "0";
        } else if (em == Direction.Down) {
            operation = "1";
        }
        if (stop) {
            operation = "10";
        }
        ControlMovePTZData controlMovePTZData = new ControlMovePTZData();
        controlMovePTZData.data.deviceId = deviceListBean.deviceId;
        controlMovePTZData.data.channelId = deviceListBean.channels.get(deviceListBean.checkedChannel).channelId;
        controlMovePTZData.data.operation = operation;
        controlMovePTZData.data.duration = time;
        deviceRecordService.controlMovePTZ(controlMovePTZData);
    }

    private void initCloudRecord() {
        if ((cloudRecordsDataTip != null && !cloudRecordsDataTip.isEmpty()) || cloudRecordsDataList != null) {
            if (cloudRecordsDataList != null) {
                showRecordList(cloudRecordsDataList);
            } else {
                showRecordListTip(cloudRecordsDataTip);
            }
        } else {
            CloudRecordsData cloudRecordsData = new CloudRecordsData();
            cloudRecordsData.data.deviceId = deviceListBean.deviceId;
            cloudRecordsData.data.channelId = deviceListBean.channels.get(deviceListBean.checkedChannel).channelId;
            cloudRecordsData.data.beginTime = DateHelper.dateFormat(new Date(System.currentTimeMillis())) + " 00:00:00";
            cloudRecordsData.data.endTime = DateHelper.dateFormat(new Date(System.currentTimeMillis())) + " 23:59:59";
            cloudRecordsData.data.count = 6;
            deviceRecordService.getCloudRecords(cloudRecordsData, new IGetDeviceInfoCallBack.IDeviceCloudRecordCallBack() {
                @Override
                public void deviceCloudRecord(CloudRecordsData.Response result) {
                    List<CloudRecordsData.ResponseData.RecordsBean> cloudRecords = result.data.records;
                    if (cloudRecords != null && cloudRecords.size() > 0) {
                        cloudRecordsDataList = new ArrayList<>();
                        for (CloudRecordsData.ResponseData.RecordsBean recordsBean : cloudRecords) {
                            RecordsData recordsData = RecordsData.parseCloudData(recordsBean);
                            cloudRecordsDataList.add(recordsData);
                        }
                        showRecordList(cloudRecordsDataList);
                    } else {
                        cloudRecordsDataTip = getResources().getString(R.string.lc_demo_device_more_video);
                        showRecordListTip(cloudRecordsDataTip);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    cloudRecordsDataTip = throwable .getMessage();
                    showRecordListTip(cloudRecordsDataTip);
                }
            });
        }
    }

    private void showRecordList(List<RecordsData> list) {
        rcvVideoList.setVisibility(View.VISIBLE);
        tvNoVideo.setVisibility(View.GONE);
        recordsDataList.clear();
        for (RecordsData a : list) {
            recordsDataList.add(a);
        }
        if (mediaPlayRecordAdapter == null) {
            mediaPlayRecordAdapter = new MediaPlayRecordAdapter(recordsDataList, DeviceOnlineMediaPlayActivity.this);
            rcvVideoList.setAdapter(mediaPlayRecordAdapter);
        } else {
            mediaPlayRecordAdapter.notifyDataSetChanged();
        }
        //加载更多
        mediaPlayRecordAdapter.setLoadMoreClickListener(new MediaPlayRecordAdapter.LoadMoreClickListener() {
            @Override
            public void loadMore() {
                gotoRecordList();
            }
        });
        //进入视频片段播放页
        mediaPlayRecordAdapter.setOnItemClickListener(new MediaPlayRecordAdapter.OnItemClickListener() {
            @Override
            public void click(int recordType, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MethodConst.ParamConst.deviceDetail, deviceListBean);
                bundle.putSerializable(MethodConst.ParamConst.recordData, recordsDataList.get(position));
                bundle.putInt(MethodConst.ParamConst.recordType, recordsDataList.get(position).recordType == 0 ? MethodConst.ParamConst.recordTypeCloud : MethodConst.ParamConst.recordTypeLocal);
                Intent intent = new Intent(DeviceOnlineMediaPlayActivity.this, DeviceRecordPlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void gotoRecordList() {
        if (cloudvideo) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MethodConst.ParamConst.deviceDetail, deviceListBean);
            bundle.putInt(MethodConst.ParamConst.recordType, MethodConst.ParamConst.recordTypeCloud);
            Intent intent = new Intent(DeviceOnlineMediaPlayActivity.this, DeviceRecordListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MethodConst.ParamConst.deviceDetail, deviceListBean);
            bundle.putInt(MethodConst.ParamConst.recordType, MethodConst.ParamConst.recordTypeLocal);
            Intent intent = new Intent(DeviceOnlineMediaPlayActivity.this, DeviceRecordListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void showRecordListTip(String txt) {
        rcvVideoList.setVisibility(View.GONE);
        tvNoVideo.setVisibility(View.VISIBLE);
        tvNoVideo.setText(txt);
    }

    private void initLocalRecord() {
        if ((localRecordsDataTip != null && !localRecordsDataTip.isEmpty()) || localRecordsDataList != null) {
            if (localRecordsDataList != null) {
                showRecordList(localRecordsDataList);
            } else {
                showRecordListTip(localRecordsDataTip);
            }
        } else {
            LocalRecordsData localRecordsData = new LocalRecordsData();
            localRecordsData.data.deviceId = deviceListBean.deviceId;
            localRecordsData.data.channelId = deviceListBean.channels.get(deviceListBean.checkedChannel).channelId;
            localRecordsData.data.beginTime = DateHelper.dateFormat(new Date(System.currentTimeMillis())) + " 00:00:00";
            localRecordsData.data.endTime = DateHelper.dateFormat(new Date(System.currentTimeMillis())) + " 23:59:59";
            localRecordsData.data.type = "All";
            localRecordsData.data.queryRange = "1-6";
            deviceRecordService.queryLocalRecords(localRecordsData, new IGetDeviceInfoCallBack.IDeviceLocalRecordCallBack() {
                @Override
                public void deviceLocalRecord(LocalRecordsData.Response result) {
                    List<LocalRecordsData.ResponseData.RecordsBean> localRecords = result.data.records;
                    if (localRecords != null && localRecords.size() > 0) {
                        localRecordsDataList = new ArrayList<>();
                        for (LocalRecordsData.ResponseData.RecordsBean recordsBean : localRecords) {
                            RecordsData recordsData = RecordsData.parseLocalData(recordsBean);
                            localRecordsDataList.add(recordsData);
                        }
                        showRecordList(localRecordsDataList);
                    } else {
                        localRecordsDataTip = getResources().getString(R.string.lc_demo_device_more_video);
                        showRecordListTip(localRecordsDataTip);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    localRecordsDataTip = throwable.getMessage();
                    showRecordListTip(localRecordsDataTip);
                }
            });
        }
    }

    private void initView() {
        frLiveWindow = findViewById(R.id.fr_live_window);
        frLiveWindowContent = findViewById(R.id.fr_live_window_content);
        tvCloudVideo = findViewById(R.id.tv_cloud_video);
        tvLocalVideo = findViewById(R.id.tv_local_video);
        llBack = findViewById(R.id.ll_back);
        tvDeviceName = findViewById(R.id.tv_device_name);
        llDetail = findViewById(R.id.ll_detail);
        llPlayPause = findViewById(R.id.ll_paly_pause);
        llPlayStyle = findViewById(R.id.ll_play_style);
        llSound = findViewById(R.id.ll_sound);
        llFullScreen = findViewById(R.id.ll_fullscreen);
        llCloudStage = findViewById(R.id.ll_cloudstage);
        llScreenShot = findViewById(R.id.ll_screenshot);
        llSpeak = findViewById(R.id.ll_speak);
        llVideo = findViewById(R.id.ll_video);
        llVideoContent = findViewById(R.id.ll_video_content);
        rcvVideoList = findViewById(R.id.rcv_video_list);
        tvNoVideo = findViewById(R.id.tv_no_video);
        rudderView = findViewById(R.id.rudder);
        ivPalyPause = findViewById(R.id.iv_paly_pause);
        ivPlayStyle = findViewById(R.id.iv_play_style);
        ivSound = findViewById(R.id.iv_sound);
        ivCloudStage = findViewById(R.id.iv_cloudStage);
        ivScreenShot = findViewById(R.id.iv_screen_shot);
        ivSpeak = findViewById(R.id.iv_speak);
        ivVideo = findViewById(R.id.iv_video);

        llCloudStage1 = findViewById(R.id.ll_cloudstage1);
        llScreenShot1 = findViewById(R.id.ll_screenshot1);
        llSpeak1 = findViewById(R.id.ll_speak1);
        llVideo1 = findViewById(R.id.ll_video1);
        ivCloudStage1 = findViewById(R.id.iv_cloudStage1);
        ivScreenShot1 = findViewById(R.id.iv_screen_shot1);
        ivSpeak1 = findViewById(R.id.iv_speak1);
        ivVideo1 = findViewById(R.id.iv_video1);

        rlLoading = findViewById(R.id.rl_loading);
        pbLoading = findViewById(R.id.pb_loading);
        tvLoadingMsg = findViewById(R.id.tv_loading_msg);
        llController = findViewById(R.id.ll_controller);
        frRecord = findViewById(R.id.fr_record);
        rlTitle = findViewById(R.id.rl_title);
        ivChangeScreen = findViewById(R.id.iv_change_screen);
        linearLayoutManager = new LinearLayoutManager(DeviceOnlineMediaPlayActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvVideoList.setLayoutManager(linearLayoutManager);
        initCommonClickListener();
        // 初始化播放窗口
        switchScreenDirection();
        mPlayWin.initPlayWindow(this, frLiveWindowContent, 0, false);
        setWindowListener(mPlayWin);
        mPlayWin.openTouchListener();//开启收拾监听
    }

    private void switchScreenDirection() {
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(frLiveWindow.getLayoutParams());
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            mLayoutParams.width = metric.widthPixels; // 屏幕宽度（像素）
            mLayoutParams.height = metric.widthPixels * 9 / 16;
            mLayoutParams.setMargins(0, 0, 0, 0);
            mLayoutParams.addRule(RelativeLayout.BELOW, R.id.rl_title);
            frLiveWindow.setLayoutParams(mLayoutParams);
            MediaPlayHelper.quitFullScreen(DeviceOnlineMediaPlayActivity.this);
            llController.setVisibility(View.VISIBLE);
            rlTitle.setVisibility(View.VISIBLE);
            llSpeak1.setVisibility(View.GONE);
            llCloudStage1.setVisibility(View.GONE);
            llVideo1.setVisibility(View.GONE);
            llScreenShot1.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(frRecord
                    .getLayoutParams());
            layoutParams.addRule(RelativeLayout.BELOW, R.id.ll_controller);
            frRecord.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(rudderView.getLayoutParams());
            layoutParams3.gravity = Gravity.CENTER;
            rudderView.setLayoutParams(layoutParams3);
            switchCloudRudder(false);
        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(metric.widthPixels, metric.heightPixels);
            mLayoutParams.setMargins(0, 0, 0, 0);
            mLayoutParams.removeRule(RelativeLayout.BELOW);
            frLiveWindow.setLayoutParams(mLayoutParams);
            MediaPlayHelper.setFullScreen(DeviceOnlineMediaPlayActivity.this);
            llController.setVisibility(View.GONE);
            rlTitle.setVisibility(View.GONE);
            llSpeak1.setVisibility(View.VISIBLE);
            llCloudStage1.setVisibility(View.VISIBLE);
            llVideo1.setVisibility(View.VISIBLE);
            llScreenShot1.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(frRecord
                    .getLayoutParams());
            layoutParams.removeRule(RelativeLayout.BELOW);
            frRecord.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(rudderView.getLayoutParams());
            layoutParams3.gravity = Gravity.CENTER_VERTICAL;
            rudderView.setLayoutParams(layoutParams3);
            switchCloudRudder(false);
        }
    }

    private void initData() {
        bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        deviceListBean = (DeviceDetailListData.ResponseData.DeviceListBean) bundle.getSerializable(MethodConst.ParamConst.deviceDetail);
        switchVideoList(true);
        getDeviceLocalCache();
        tvDeviceName.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
    }

    /**
     * 获取设备缓存信息
     */
    private void getDeviceLocalCache() {
        DeviceLocalCacheData deviceLocalCacheData = new DeviceLocalCacheData();
        deviceLocalCacheData.setDeviceId(deviceListBean.deviceId);
        if (deviceListBean.channels != null && deviceListBean.channels.size() > 0) {
            deviceLocalCacheData.setChannelId(deviceListBean.channels.get(deviceListBean.checkedChannel).channelId);
        }
        DeviceLocalCacheService deviceLocalCacheService = ClassInstanceManager.newInstance().getDeviceLocalCacheService();
        deviceLocalCacheService.findLocalCache(deviceLocalCacheData, this);
    }

    @Override
    public void deviceCache(DeviceLocalCacheData deviceLocalCacheData) {
        BitmapDrawable bitmapDrawable = MediaPlayHelper.picDrawable(deviceLocalCacheData.getPicPath());
        if (bitmapDrawable != null) {
            rlLoading.setBackground(bitmapDrawable);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingStatus(LoadStatus.LOADING, getResources().getString(R.string.lc_demo_device_video_play_loading), deviceListBean.deviceId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
        recordStatus = RecordStatus.STOP;
    }

    private void initAbility(boolean loadSuccess) {
        String deviceAbility = deviceListBean.ability;
        String channelAbility = deviceListBean.channels.get(deviceListBean.checkedChannel).ability;
        //云台
        supportPTZ = (deviceListBean.checkedChannel == 0 ? (deviceListBean.channels.size() > 1 ? channelAbility.contains("PT") : deviceAbility.contains("PT")) : channelAbility.contains("PT")) && loadSuccess;
        cloudStageClickListener(supportPTZ);
        //对讲
        speakClickListener((channelAbility.contains("AudioTalkV1") || deviceAbility.contains("AudioTalk")) && loadSuccess);
    }

    private void switchVideoList(boolean b) {
        this.cloudvideo = b;
        tvCloudVideo.setSelected(cloudvideo);
        tvLocalVideo.setSelected(!cloudvideo);
        if (cloudvideo) {
            initCloudRecord();
        } else {
            initLocalRecord();
        }
    }

    private void initCommonClickListener() {
        llBack.setOnClickListener(this);
        llDetail.setOnClickListener(this);
        tvCloudVideo.setOnClickListener(this);
        tvLocalVideo.setOnClickListener(this);
        tvNoVideo.setOnClickListener(this);
        llFullScreen.setOnClickListener(this);
    }

    private void featuresClickListener(boolean loadSuccess) {
        llPlayPause.setOnClickListener(loadSuccess ? this : null);
        llPlayStyle.setOnClickListener(loadSuccess ? this : null);

        llScreenShot.setOnClickListener(loadSuccess ? this : null);
        llScreenShot1.setOnClickListener(loadSuccess ? this : null);
        llVideo.setOnClickListener(loadSuccess ? this : null);
        llVideo1.setOnClickListener(loadSuccess ? this : null);
        llSound.setOnClickListener(loadSuccess ? this : null);
        ivPalyPause.setImageDrawable(loadSuccess ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_pause) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_pause_disable));
        ivPlayStyle.setImageDrawable(videoMode == VideoMode.MODE_HD ?
                (loadSuccess ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_hd) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_hd_disable)) :
                (loadSuccess ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_sd) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_hd_disable)));
        ivScreenShot.setImageDrawable(loadSuccess
                ? getDrawable(R.drawable.lc_demo_photo_capture_selector)
                : getDrawable(R.mipmap.lc_demo_livepreview_icon_screenshot_disable));
        ivVideo.setImageDrawable(loadSuccess
                ? getDrawable(R.mipmap.lc_demo_livepreview_icon_video)
                : getDrawable(R.mipmap.lc_demo_livepreview_icon_video_disable));

        ivScreenShot1.setImageDrawable(loadSuccess
                ? getDrawable(R.mipmap.live_video_icon_h_screenshot)
                : getDrawable(R.mipmap.live_video_icon_h_screenshot_disable));
        ivVideo1.setImageDrawable(loadSuccess
                ? getDrawable(R.mipmap.live_video_icon_h_video_off)
                : getDrawable(R.mipmap.live_video_icon_h_video_off_disable));
        ivSound.setImageDrawable(loadSuccess ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_off) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_off_disable));
        //媒体声
        if (soundStatus != SoundStatus.PLAY) {
            return;
        }
        if (openAudio()) {
            soundStatus = SoundStatus.PLAY;
            ivSound.setImageDrawable(getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_on));
        }
    }

    private void cloudStageClickListener(boolean isSupport) {
        llCloudStage.setOnClickListener(isSupport ? this : null);
        ivCloudStage.setImageDrawable(isSupport
                ? getDrawable(R.mipmap.lc_demo_livepreview_icon_cloudstage)
                : getDrawable(R.mipmap.lc_demo_livepreview_icon_cloudstage_disable));
        llCloudStage1.setOnClickListener(isSupport ? this : null);
        ivCloudStage1.setImageDrawable(isSupport
                ? getDrawable(R.mipmap.live_video_icon_h_cloudterrace_off)
                : getDrawable(R.mipmap.live_video_icon_h_cloudterrace_off_disable));
    }

    private void speakClickListener(boolean isSupport) {
        ivSpeak.setOnClickListener(isSupport ? this : null);
        ivSpeak.setImageDrawable(isSupport
                ? getDrawable(R.mipmap.lc_demo_livepreview_icon_speak)
                : getDrawable(R.mipmap.lc_demo_livepreview_icon_speak_disable));
        ivSpeak1.setOnClickListener(isSupport ? this : null);
        ivSpeak1.setImageDrawable(isSupport
                ? getDrawable(R.mipmap.live_video_icon_h_talk_off)
                : getDrawable(R.mipmap.live_video_icon_h_talk_off_disable));
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
                } else {
                    if (resultSource == 5 && (!(code.equals("1000") || code.equals("0")))) {
                        // code 1000-开启播放成功  0-开始拉流
                        failed = true;
                        if (code.equals("1000005")) {
                            inputEncryptKey();
                        }
                    } else if (resultSource == 0 && (code.equals("0") || code.equals("1") || code.equals("3") || code.equals("7"))) {
                        // code
                        // 0-组帧失败，错误状态
                        // 1-内部要求关闭,如连接断开等，错误状态
                        // 3-RTSP鉴权失败，错误状态
                        // 7-秘钥错误
                        failed = true;
                        if (code.equals("7")) {
                            inputEncryptKey();
                        }
                    }
                }
                if (failed) {
                    loadingStatus(LoadStatus.LOAD_ERROR, getResources().getString(R.string.lc_demo_device_video_play_error) + ":" + code + "." + resultSource, "");
                    playStatus = PlayStatus.ERROR;
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
                playStatus = PlayStatus.PLAY;
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
            }

            //播放时间信息回调
            @Override
            public void onPlayerTime(int index, long time) {
                super.onPlayerTime(index, time);
                LogUtil.debugLog(TAG, "onPlayerTime: index= " + index + " , time= " + time);
            }
        });
    }

    /**
     * 输入秘钥
     */
    private void inputEncryptKey() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (encryptKeyInputDialog == null) {
                    encryptKeyInputDialog = new EncryptKeyInputDialog(DeviceOnlineMediaPlayActivity.this);
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
                    play(psk);
                    rlLoading.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.VISIBLE);
                    tvLoadingMsg.setText(msg);
                } else if (loadStatus == LoadStatus.LOAD_SUCCESS) {
                    //播放成功
                    rlLoading.setVisibility(View.GONE);
                    rudderView.enable(true);
                    initAbility(true);
                    featuresClickListener(true);
                } else {
                    //播放失败
                    stop();
                    rlLoading.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.GONE);
                    tvLoadingMsg.setText(msg);
                    initAbility(false);
                    featuresClickListener(false);
                }
            }
        });
    }

    /**
     * 开始播放
     */
    public void play(String psk) {
        LCOpenSDK_ParamReal paramReal = new LCOpenSDK_ParamReal(
                LCDeviceEngine.newInstance().accessToken,
                deviceListBean.deviceId,
                Integer.parseInt(deviceListBean.channels.get(deviceListBean.checkedChannel).channelId),
                psk,
                deviceListBean.playToken,
                videoMode == VideoMode.MODE_HD ? 0 : 1,
                true
        );
        mPlayWin.playRtspReal(paramReal);
    }

    /**
     * 停止播放
     */
    public void stop() {
        captureLastPic();
        stopRecord();// 关闭录像
        closeAudio();// 关闭音频
        stopTalk();//关闭对讲
        rudderView.enable(false);
        mPlayWin.stopRtspReal(true);// 关闭视频
    }

    /**
     * 保存最后一帧做封面
     */
    private void captureLastPic() {
        if (playStatus == PlayStatus.ERROR) {
            return;
        }
        String capturePath;
        try {
            capturePath = capture(false);
        } catch (Throwable e) {
            capturePath = null;
        }
        if (capturePath == null) {
            return;
        }
        DeviceLocalCacheService deviceLocalCacheService = ClassInstanceManager.newInstance().getDeviceLocalCacheService();
        DeviceLocalCacheData deviceLocalCacheData = new DeviceLocalCacheData();
        deviceLocalCacheData.setPicPath(capturePath);
        deviceLocalCacheData.setDeviceName(deviceListBean.name);
        deviceLocalCacheData.setDeviceId(deviceListBean.deviceId);
        if (deviceListBean.channels != null && deviceListBean.channels.size() > 0) {
            deviceLocalCacheData.setChannelId(deviceListBean.channels.get(deviceListBean.checkedChannel).channelId);
            deviceLocalCacheData.setChannelName(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
        }
        deviceLocalCacheService.addLocalCache(deviceLocalCacheData);
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
    public String capture(boolean notify) {
        String captureFilePath = null;
        String channelName = null;
        if (deviceListBean.channels != null && deviceListBean.channels.size() > 0) {
            channelName = deviceListBean.channels.get(deviceListBean.checkedChannel).channelName;
        } else {
            channelName = deviceListBean.name;
        }
        // 去除通道中在目录中的非法字符
        channelName = channelName.replace("-", "");
        captureFilePath = MediaPlayHelper.getCaptureAndVideoPath(notify ? MediaPlayHelper.DHFilesType.DHImage : MediaPlayHelper.DHFilesType.DHImageCache, channelName);
        int ret = mPlayWin.snapShot(captureFilePath);
        if (ret == 0) {
            if (notify) {
                // 扫描到相册中
                MediaScannerConnection.scanFile(this, new String[]{captureFilePath}, null, null);
            }
        } else {
            captureFilePath = null;
        }
        return captureFilePath;
    }

    /**
     * 打开声音
     */
    public boolean openAudio() {
        return mPlayWin.playAudio() == 0;
    }

    /**
     * 关闭声音
     */
    public boolean closeAudio() {
        return mPlayWin.stopAudio() == 0;
    }

    /**
     * 开始对讲 多通道通道号参数传入对应的通道号，单通道传-1
     */
    public void startTalk() {
        closeAudio();
        soundStatus = SoundStatus.STOP;
        speakStatus = SpeakStatus.OPENING;
        ivSound.setImageDrawable(getDrawable(R.mipmap.lc_demo_live_video_icon_h_sound_off));
        LCOpenSDK_Talk.setListener(audioTalkerListener);//对讲前先设备监听
        LCOpenSDK_ParamTalk paramTalk = new LCOpenSDK_ParamTalk(
                LCDeviceEngine.newInstance().accessToken,
                deviceListBean.deviceId,
                deviceListBean.channels.size() > 1 ? Integer.parseInt(deviceListBean.channels.get(deviceListBean.checkedChannel).channelId) : -1,
                TextUtils.isEmpty(encryptKey) ? deviceListBean.deviceId : encryptKey,
                deviceListBean.playToken,
                true
        );
        LCOpenSDK_Talk.playTalk(paramTalk);
    }

    /**
     * 停止对讲
     */
    public void stopTalk() {
        speakStatus = SpeakStatus.STOP;
        ivSpeak.setImageDrawable(getDrawable(R.mipmap.lc_demo_livepreview_icon_speak));
        ivSpeak1.setImageDrawable(getDrawable(R.mipmap.live_video_icon_h_talk_off));
        LCOpenSDK_Talk.stopTalk();
        LCOpenSDK_Talk.setListener(null);//停止对讲后对讲监听置为空
    }

    class AudioTalkerListener extends LCOpenSDK_TalkerListener {
        public AudioTalkerListener() {
            super();
        }

        @Override
        public void onTalkResult(String error, int type) {
            super.onTalkResult(error, type);
            boolean talkResult = false;
            if (type == 99 || error.equals("-1000") || error.equals("0") || error.equals("1") || error.equals("3")) {
                talkResult = false;
            } else if (error.equals("4")) {
                talkResult = true;
            }
            final boolean finalTalkResult = talkResult;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!finalTalkResult) {
                        stopTalk();
                        // 提示对讲打开失败
                        Toast.makeText(DeviceOnlineMediaPlayActivity.this, R.string.lc_demo_device_talk_open_failed, Toast.LENGTH_SHORT).show();
                        speakStatus = SpeakStatus.STOP;
                        ivSpeak.setImageDrawable(getDrawable(R.mipmap.lc_demo_livepreview_icon_speak));
                        ivSpeak1.setImageDrawable(getDrawable(R.mipmap.live_video_icon_h_talk_off));
                    } else {
                        // 提示对讲打开成功
                        Toast.makeText(DeviceOnlineMediaPlayActivity.this, R.string.lc_demo_device_talk_open_success, Toast.LENGTH_SHORT).show();
                        speakStatus = SpeakStatus.PLAY;
                        ivSpeak.setImageDrawable(getDrawable(R.mipmap.lc_demo_livepreview_icon_speak_ing));
                        ivSpeak1.setImageDrawable(getDrawable(R.mipmap.live_video_icon_h_talk_on));
                    }
                }
            });
        }

        @Override
        public void onTalkPlayReady() {
            super.onTalkPlayReady();
        }

        @Override
        public void onAudioRecord(byte[] bytes, int i, int i1, int i2, int i3) {
            super.onAudioRecord(bytes, i, i1, i2, i3);
        }

        @Override
        public void onAudioReceive(byte[] bytes, int i, int i1, int i2, int i3) {
            super.onAudioReceive(bytes, i, i1, i2, i3);
        }

        @Override
        public void onDataLength(int i) {
            super.onDataLength(i);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_back) {
            //返回
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
        } else if (id == R.id.ll_detail) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MethodConst.ParamConst.deviceDetail, deviceListBean);
            Intent intent = new Intent(DeviceOnlineMediaPlayActivity.this, DeviceDetailActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        } else if (id == R.id.ll_paly_pause) {
            //播放暂停
            if (playStatus == PlayStatus.PLAY) {
                stop();
                initAbility(false);
                featuresClickListener(false);
                llPlayPause.setOnClickListener(this);
            } else {
                getDeviceLocalCache();
                loadingStatus(LoadStatus.LOADING, getResources().getString(R.string.lc_demo_device_video_play_loading), TextUtils.isEmpty(encryptKey) ? deviceListBean.deviceId : encryptKey);
            }
            playStatus = (playStatus == PlayStatus.PLAY) ? PlayStatus.PAUSE : PlayStatus.PLAY;
            ivPalyPause.setImageDrawable(playStatus == PlayStatus.PLAY ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_pause) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_play));
        } else if (id == R.id.ll_play_style) {
            //视频清晰度切换
            videoMode = (videoMode == VideoMode.MODE_HD) ? VideoMode.MODE_SD : VideoMode.MODE_HD;
            loadingStatus(LoadStatus.LOADING, getResources().getString(R.string.lc_demo_device_video_play_change), TextUtils.isEmpty(encryptKey) ? deviceListBean.deviceId : encryptKey);
            ivPlayStyle.setImageDrawable(videoMode == VideoMode.MODE_HD ? getDrawable(R.mipmap.lc_demo_live_video_icon_h_hd) : getDrawable(R.mipmap.lc_demo_live_video_icon_h_sd));
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
        } else if (id == R.id.ll_cloudstage || id == R.id.ll_cloudstage1) {
            //云台和录像列表切换
            switchCloudRudder(!isShwoRudder);
        } else if (id == R.id.ll_screenshot || id == R.id.ll_screenshot1) {
            //截图
            if (capture(true) != null) {
                Toast.makeText(this, getResources().getString(R.string.lc_demo_device_capture_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.lc_demo_device_capture_failed), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.iv_speak || id == R.id.iv_speak1) {
            //对讲 如果是打开状态去关闭,反之
            if (speakStatus == SpeakStatus.NO_SUPPORT || speakStatus==SpeakStatus.OPENING) {
                return;
            }
            if (speakStatus == SpeakStatus.STOP) {
                startTalk();
            } else {
                stopTalk();
            }
        } else if (id == R.id.ll_video || id == R.id.ll_video1) {
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
            ivVideo.setImageDrawable(recordStatus == RecordStatus.START
                    ? getDrawable(R.mipmap.lc_demo_livepreview_icon_video_ing)
                    : getDrawable(R.mipmap.lc_demo_livepreview_icon_video));
            ivVideo1.setImageDrawable(recordStatus == RecordStatus.START
                    ? getDrawable(R.mipmap.live_video_icon_h_video_on)
                    : getDrawable(R.mipmap.live_video_icon_h_video_off));
        } else if (id == R.id.tv_cloud_video) {
            //切换至云录像
            switchVideoList(true);
        } else if (id == R.id.tv_local_video) {
            //切换至设备录像
            switchVideoList(false);
        } else if (id == R.id.tv_no_video) {
            //暂无录像切换
            gotoRecordList();
        }
    }

    private void switchCloudRudder(boolean isShow) {
        this.isShwoRudder = isShow;
        if (isShow) {
            ivCloudStage.setImageDrawable(getDrawable(R.mipmap.lc_demo_livepreview_icon_cloudstage_click));
            ivCloudStage1.setImageDrawable(getDrawable(R.mipmap.live_video_icon_h_cloudterrace_on));
            llVideoContent.setVisibility(View.GONE);
            rudderView.setVisibility(View.VISIBLE);
        } else {
            ivCloudStage.setImageDrawable(supportPTZ
                    ? getDrawable(R.mipmap.lc_demo_livepreview_icon_cloudstage)
                    : getDrawable(R.mipmap.lc_demo_livepreview_icon_cloudstage_disable));
            ivCloudStage1.setImageDrawable(supportPTZ
                    ? getDrawable(R.mipmap.live_video_icon_h_cloudterrace_off)
                    : getDrawable(R.mipmap.live_video_icon_h_cloudterrace_off_disable));
            llVideoContent.setVisibility(mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ? View.VISIBLE : View.GONE);
            rudderView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayWin.uninitPlayWindow();// 销毁底层资源
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            boolean unBind = data.getBooleanExtra(DeviceConstant.IntentKey.DHDEVICE_UNBIND, false);
            if (unBind) {
                finish();
            }
        }
        if (resultCode == 100 && data != null) {
            String name = data.getStringExtra(DeviceConstant.IntentKey.DHDEVICE_NEW_NAME);
            tvDeviceName.setText(name);
            deviceListBean.channels.get(deviceListBean.checkedChannel).channelName = name;
        }
    }
}
