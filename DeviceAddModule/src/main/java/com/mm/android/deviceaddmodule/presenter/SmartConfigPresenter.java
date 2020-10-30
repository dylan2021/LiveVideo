package com.mm.android.deviceaddmodule.presenter;

import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.dahua.mobile.utility.network.DHWifiUtil;
import com.lechange.common.configwifi.LCSmartConfig;
import com.lechange.opensdk.configwifi.LCOpenSDK_ConfigWifi;
import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.SearchDeviceManager;
import com.mm.android.deviceaddmodule.contract.SmartConfigConstract;
import com.mm.android.deviceaddmodule.event.DeviceAddEvent;
import com.mm.android.deviceaddmodule.helper.DeviceAddHelper;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.ProviderManager;
import com.mm.android.deviceaddmodule.mobilecommon.base.LCBusinessHandler;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;
import com.mm.android.deviceaddmodule.mobilecommon.common.LCConfiguration;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.mobilecommon.utils.PreferencesHelper;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;

public class SmartConfigPresenter implements SmartConfigConstract.Presenter, SearchDeviceManager.ISearchDeviceListener {
    private int TIMER_TIMEOUT = 120 * 1000;// 120秒 连接路由
    private static final int TIME_OUT = 60 * 1000;                            //接收超时时间
    private static final int SEARCH_SUCCESS = 1;                            //搜索成功
    private static final int SEARCH_FAILED = 2;                            //搜索不到设备
    private static final int NOT_INIT = 3;                            //不需要初始化
    private static final int NEED_INIT = 4;                            //需要初始化

    private static final String VOICE_CONFIG_FILE_NAME = "Audiopair.wav";            // 声波配对的音频文件

    WeakReference<SmartConfigConstract.View> mView;
    DHWifiUtil mDHWifiUtil;
    DEVICE_NET_INFO_EX mDeviceNetInfoEx;
    String mDeviceSnCode;
    boolean mIsSupportSoundWave;
    boolean mIsSupportSoundWaveV2;    // 是否支持声波新方案
    private boolean mIsRunningAudio = false;
    private MediaPlayer mMediaPlayer = null;
    private String tempDirectory;   //smartconfig、声波文件临时存放路径

    public SmartConfigPresenter(SmartConfigConstract.View view/*, boolean isQRCodeConfig*/) {
        mView = new WeakReference<>(view);
        mDHWifiUtil = new DHWifiUtil(mView.get().getContextInfo().getApplicationContext());
        mDeviceSnCode = DeviceAddModel.newInstance().getDeviceInfoCache().getDeviceSn();

        initInfo();
    }

    private void initInfo() {
        String configMode = DeviceAddModel.newInstance().getDeviceInfoCache().getConfigMode();
        mIsSupportSoundWave = configMode != null && configMode.contains(DeviceAddInfo.ConfigMode.SoundWave.name());
        mIsSupportSoundWaveV2 = DeviceAddHelper.isSupportSoundWaveV2(DeviceAddModel.newInstance().getDeviceInfoCache());

        mView.get().updateTip2Txt(mIsSupportSoundWave, mIsSupportSoundWaveV2);

        mView.get().hideTipWifiPwdErrorTxt(ProviderManager.getAppProvider().getAppType() == LCConfiguration.APP_LECHANGE_OVERSEA);

        String userId = LCDeviceEngine.newInstance().userId;
        tempDirectory = mView.get().getContextInfo().getFilesDir() + File.separator + userId + File.separator;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mView.get() != null && mView.get().isViewActive()) {
                switch (msg.what) {
                    case SEARCH_SUCCESS:        //搜索设备成功
                        stopAudio();
                        recyle();
                        isDeviceNeedInit();
                        break;
                    case NOT_INIT:              //设备不需初始化
                        mView.get().goConnectCloudPage();
                        break;
                    case NEED_INIT:             //设备未初始化
                        mView.get().goDevInitPage(mDeviceNetInfoEx);
                        break;
                }

            }
        }
    };

    private void isDeviceNeedInit() {
        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
        if(DeviceAddHelper.isSupportAddBySc(deviceAddInfo) || !DeviceAddHelper.isDeviceNeedInit(mDeviceNetInfoEx)) {
            mHandler.obtainMessage(NOT_INIT).sendToTarget();
        } else {
            mHandler.obtainMessage(NEED_INIT).sendToTarget();
        }
    }

    @Override
    public void recyle() {
        //关闭
        LCOpenSDK_ConfigWifi.configWifiStop();
        mView.get().stopCountDown();
        DeviceAddModel.newInstance().setLoop(false);
    }

    @Override
    public String getConfigMode() {
        return DeviceAddModel.newInstance().getDeviceInfoCache().getConfigMode();
    }

    //开始smartconfig配对
    @Override
    public void startSmartConfig() {
        startConfig();
        startSearchDevices();
    }

    /**
     * SmartConfig
     */
    private void startConfig() {
        ScanResult scanResult = mDHWifiUtil.getScanResult();
        String encryptionCap = "";
        if (scanResult != null) {
            encryptionCap = scanResult.capabilities == null ? "" : scanResult.capabilities;
        }
        DeviceAddInfo.WifiInfo wifiInfo = DeviceAddModel.newInstance().getDeviceInfoCache().getWifiInfo();
        String ssid = wifiInfo.getSsid() == null ? "" : wifiInfo.getSsid();
        String ssid_pwd = wifiInfo.getPwd() == null ? "" : wifiInfo.getPwd();

        /**
         * cfgType：配网方式，1--组播，2--广播，4--声波，可按位或运算组合使用；
         * voiceConfigFilePath：声波配对的音频文件的生成路径，由上层调用者传入；
         * enableBgMusic：声波配对是否使用背景声，默认是布谷鸟叫声；
         * freq：声波配对收发信号的基带频率；
         * fsk_tx_mode：fsk发送方式，0--新的fsk发送方式，1--老的fsk发送方式，2--新的和老的fsk波形发送方式；
        */
        int fskTxMode = 1;
        boolean enableBgMusic = true;
        LCOpenSDK_ConfigWifi.configWifiStart(mDeviceSnCode, ssid, ssid_pwd, encryptionCap, LCSmartConfig.ConfigType.LCConfigWifi_Type_ALL, enableBgMusic,11000, fskTxMode);

    }

    /**
     * 播放新声波方案的音频文件
     */
    @Override
    public void playAudio() {

    }

    @Override
    public void pauseAudio() {

    }

    /**
     * 停止播放新声波方案的音频文件
     */
    @Override
    public void stopAudio() {

    }

    @Override
    public void releaseAudio() {
    }

    @Override
    public void wifiPwdErrorClick() {
        DeviceAddInfo.WifiInfo info =  DeviceAddModel.newInstance().getDeviceInfoCache().getWifiInfo();
        info.setPwd("");
        DHWifiUtil mDHWifiUtil=new DHWifiUtil(mView.get().getContextInfo().getApplicationContext());
        String curWifiName = mDHWifiUtil.getCurrentWifiInfo().getSSID().replaceAll("\"", "");
        String WIFI_SAVE_PREFIX = LCDeviceEngine.newInstance().userId + "_WIFI_ADD_";
        PreferencesHelper.getInstance(mView.get().getContextInfo()).set(WIFI_SAVE_PREFIX + curWifiName,"");
        mView.get().goWfiPwdPage();
    }

    /**
     * 搜索设备
     */
    private void startSearchDevices() {
        final SearchDeviceManager manager = SearchDeviceManager.getInstance();
        mDeviceNetInfoEx = manager.getDeviceNetInfo(mDeviceSnCode);
        if (mDeviceNetInfoEx != null) {
            // 已经搜到
            mHandler.obtainMessage(SEARCH_SUCCESS).sendToTarget();
        } else {
            manager.registerListener(this);
            manager.startSearch();

            getDeviceInfo();
        }
    }

    private void getDeviceInfo() {
        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
        // 第一次添加时使用二维码或用输入的sc码来判断，离线配网或者设备已在平台上线过则可以通过能力集进行判断
        if(DeviceAddHelper.isSupportAddBySc(deviceAddInfo)) {
            String deviceSn = DeviceAddModel.newInstance().getDeviceInfoCache().getDeviceSn();
            String model = DeviceAddModel.newInstance().getDeviceInfoCache().getModelName();
            LCBusinessHandler getDeviceHandler = new LCBusinessHandler() {
                @Override
                public void handleBusiness(Message msg) {
                    if (mView.get() == null
                            || (mView.get() != null && !mView.get().isViewActive())) {
                        return;
                    }
                    if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                        dispatchResult();
                    } else {//发生异常且倒计时未完成，重新查询
                        getDeviceInfo();
                    }

                }
            };
            DeviceAddModel.newInstance().getDeviceInfoLoop(deviceSn, model, "", TIME_OUT, getDeviceHandler);
        }
    }

    private void dispatchResult() {
        stopAudio();
        recyle();

        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
        String status=deviceAddInfo.getStatus();
        if(TextUtils.isEmpty(status)){
            status=DeviceAddInfo.Status.offline.name();
        }
        if (DeviceAddInfo.Status.online.name().equals(status)) {
            if (isWifiOfflineConfiMode()) {//wifi离线配置
                EventBus.getDefault().post(new DeviceAddEvent(DeviceAddEvent.DESTROY_ACTION));
                ProviderManager.getDeviceAddCustomProvider().goHomePage(mView.get().getContextInfo());
                mView.get().showToastInfo(R.string.add_device_wifi_config_success);
            } else {
                mView.get().goBindDevicePage();
            }
        } else {
            if (isWifiOfflineConfiMode()) {//wifi离线配置
                mView.get().showToastInfo(R.string.add_device_config_failed);
                mView.get().completeAction();
            } else {
                mView.get().goConfigTimeoutPage();
            }
        }
    }

    public boolean isWifiOfflineConfiMode() {
        return DeviceAddModel.newInstance().getDeviceInfoCache().isWifiOfflineMode();
    }

    @Override
    public void onDeviceSearched(String sncode, DEVICE_NET_INFO_EX info) {
        if (info != null && mDeviceSnCode.equalsIgnoreCase(sncode)) {
            mDeviceNetInfoEx = info;
            mHandler.obtainMessage(SEARCH_SUCCESS).sendToTarget();
            SearchDeviceManager.getInstance().unRegisterListener(this);
        }
    }
}
