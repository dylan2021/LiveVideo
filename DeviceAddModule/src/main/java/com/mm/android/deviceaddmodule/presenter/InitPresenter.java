package com.mm.android.deviceaddmodule.presenter;

import android.os.Handler;
import android.os.Message;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.dahua.mobile.utility.music.DHMusicPlayer;
import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.SearchDeviceManager;
import com.mm.android.deviceaddmodule.contract.InitContract;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.ProviderManager;
import com.mm.android.deviceaddmodule.mobilecommon.base.LCBusinessHandler;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;
import com.mm.android.deviceaddmodule.mobilecommon.common.LCConfiguration;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;
import com.mm.android.deviceaddmodule.mobilecommon.utils.PasswordCheckRules;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;

import java.lang.ref.WeakReference;

import static com.mm.android.deviceaddmodule.helper.Utils4AddDevice.checkDeviceVersion;
import static com.mm.android.deviceaddmodule.helper.Utils4AddDevice.checkEffectiveIP;

public class InitPresenter implements InitContract.Presenter, SearchDeviceManager.ISearchDeviceListener {
    private static final String TAG = "InitPresenter";
    private int SEARCH_DEVICE_WAIT_TIME = 5 * 1000;                          // 搜索设备时间
    private int NEW_V_SEARCH_DEVICE_WAIT_TIME = 10 * 1000;                          // 搜索设备时间
    private static final int SEARCH_SUCCESS = 1;                            //搜索成功
    private static final int SEARCH_FAILED = 2;                            //搜索不到设备

    WeakReference<InitContract.View> mView;
    DHMusicPlayer mDHMusicPlayer;
    DEVICE_NET_INFO_EX mDeviceNetInfoEx;
    String mDeviceSn;
    boolean isBeginInit;                                        //是否开始初始化，即是否点击初始化按钮
    private boolean isDeviceSearching = false;              //是否在开始搜索设备
    boolean mIsDeviceNewVersion;                              //新程序设备，直接走单播
    boolean mIsHasInitbyMulicast = false;                    //是否已经尝试过组播
    boolean isSoftApAddType = false;                          //当前软AP添加设备（门铃、铃铛等）版本，不管新老设备，IP会一直无效，故软AP添加初始化走组播+单播流程，待后续设备程序更改后再进行优化。
    boolean mIsHasInitbyIp = false;                         //是否已经尝试过单播

    public InitPresenter(InitContract.View view) {
        mView = new WeakReference<>(view);
        mDHMusicPlayer = new DHMusicPlayer(mView.get().getContextInfo(), true, mView.get().getMusicRes());
        mDeviceSn = DeviceAddModel.newInstance().getDeviceInfoCache().getDeviceSn();
        if (DeviceAddInfo.DeviceAddType.SOFTAP.equals(DeviceAddModel.newInstance().getDeviceInfoCache().getCurDeviceAddType())) {
            isSoftApAddType = true;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mView.get() != null &&
                    mView.get().isViewActive()) {
                switch (msg.what) {
                    case SEARCH_SUCCESS:
                        searchDevicesSuccess();
                        break;
                    case SEARCH_FAILED:
                        stopSearchDevice();
                        if (isBeginInit) {
                            errorAction();
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    };

    private void searchDevicesSuccess() {
        stopSearchDevice();
        if (mDeviceNetInfoEx == null) {
            return;
        }
        if (isOnlyNeedUnicast() || mIsHasInitbyMulicast) {
            if (isBeginInit) {
                LogUtil.debugLog(TAG, "searched device: initDevByIp");
                initDevAccountByIp(mDeviceNetInfoEx, mView.get().getInitPwd());
            }
        } else {
            LogUtil.debugLog(TAG, "searched device: initDev");
            startDevInit();
        }
    }

    private Runnable mTimeOutRunnable = new Runnable() {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(SEARCH_FAILED);
        }
    };

    @Override
    public void checkDevice() {
        if (isOnlyNeedUnicast()) {//新设备，需检测ip是否有效
            //检测设备Ip是否有效，无效需要重新搜索设备
            boolean isValidIp = checkEffectiveIP(mDeviceNetInfoEx);
            if (!isValidIp) {
                startSearchDevice();
            }
        }

    }

    private void startSearchDevice() {
        isDeviceSearching = true;
        SearchDeviceManager searchDeviceManager = SearchDeviceManager.getInstance();
        searchDeviceManager.registerListener(this);
        searchDeviceManager.startSearch();
        mHandler.postDelayed(mTimeOutRunnable, isOnlyNeedUnicast() ? NEW_V_SEARCH_DEVICE_WAIT_TIME : SEARCH_DEVICE_WAIT_TIME);
    }

    private void stopSearchDevice() {
        isDeviceSearching = false;
        SearchDeviceManager.getInstance().unRegisterListener(this);
        mHandler.removeCallbacks(mTimeOutRunnable);
    }

    private void errorAction() {
        mView.get().cancelProgressDialog();
        mView.get().goErrorTipPage();
    }

    private void initDevAccountByIp(DEVICE_NET_INFO_EX devicNetInfo, final String password) {
        mView.get().showProgressDialog();
        LCBusinessHandler initDevHandler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (mView.get() == null
                        || (mView.get() != null && !mView.get().isViewActive())) {
                    return;
                }
                isBeginInit = false;
                mIsHasInitbyIp = true;
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    boolean inited = (boolean) msg.obj;
                    if (inited) {
                        dispatchInitResult();
                    } else {
                        // 初始化失败页面
                        errorAction();
                    }
                }
            }
        };
        DeviceAddModel.newInstance().initDevByIp(devicNetInfo, password, initDevHandler);
    }

    @Override
    public void startDevInit() {
        mView.get().showProgressDialog();
        LCBusinessHandler initDevHandler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (mView.get() == null
                        || (mView.get() != null && !mView.get().isViewActive())) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    boolean inited = (boolean) msg.obj;
                    if (inited) {
                        dispatchInitResult();
                    } else {
                        if(mIsHasInitbyIp) {
                            // 初始化失败页面
                            errorAction();
                        } else {
                            //组播失败，直接走单播
                            DEVICE_NET_INFO_EX deviceNetInfoEx = SearchDeviceManager.getInstance().getDeviceNetInfo(mDeviceSn);
                            if (deviceNetInfoEx != null) {
                                mDeviceNetInfoEx = deviceNetInfoEx;
                            }
                            LogUtil.debugLog(TAG, "initDev failed,then try initDevByIp");
                            initDevAccountByIp(mDeviceNetInfoEx, mView.get().getInitPwd());
                        }
                    }
                }
            }
        };
        DeviceAddModel.newInstance().initDev(mDeviceNetInfoEx, mView.get().getInitPwd(), initDevHandler);
    }

    public void dispatchInitResult() {
        mView.get().cancelProgressDialog();
        isBeginInit = false;
        DeviceAddInfo.DeviceAddType deviceAddType = DeviceAddModel.newInstance().getDeviceInfoCache().getCurDeviceAddType();
        DeviceAddModel.newInstance().getDeviceInfoCache().setDevicePwd(mView.get().getInitPwd());

        if (DeviceAddInfo.DeviceAddType.SOFTAP.equals(deviceAddType)) {
            mView.get().goSoftAPWifiListPage();
        } else {
            // 初始化成功 开始接入乐橙云
            mView.get().goConnectCloudPage();
        }
    }


    @Override
    public void setDeviceEX(DEVICE_NET_INFO_EX deviceEX) {
        mDeviceNetInfoEx = deviceEX;
        mIsDeviceNewVersion = checkDeviceVersion(mDeviceNetInfoEx);
    }

    @Override
    public void playTipSound() {
        if (ProviderManager.getAppProvider().getAppType() != LCConfiguration.APP_LECHANGE_OVERSEA)//海外版本不提示语音
            mDHMusicPlayer.playRing(false);
    }

    @Override
    public void startDevInitByIp() {
        isBeginInit = true;
        LogUtil.debugLog(TAG, "click init button...");
        if (isOnlyNeedUnicast()) {
            boolean isEffectiveIp = checkEffectiveIP(mDeviceNetInfoEx);
            if (isDeviceSearching) {//正在搜索
                LogUtil.debugLog(TAG, "please be wait, searching device ip now...");
                mView.get().showProgressDialog();
            } else if(isEffectiveIp) {//设备ip有效，不需重新搜索
                LogUtil.debugLog(TAG, "ip is effective: initDevByIp: " +  new String(mDeviceNetInfoEx.szIP).trim());
                initDevAccountByIp(mDeviceNetInfoEx, mView.get().getInitPwd());
            } else {
                LogUtil.debugLog(TAG, "ip is not effective: initDev");
                startDevInit();
            }
        } else {
            LogUtil.debugLog(TAG, "old device or DHCP is closed: initDev");
            startDevInit();
        }
    }

    @Override
    public boolean isPwdValid() {
        int res = PasswordCheckRules.checkPasswordValidation(mView.get().getInitPwd(), mView.get().getInitPwd(), mView.get().getContextInfo());
        if (res == PasswordCheckRules.PASSWORD_NOT_MATCH) {
            // mView.get().showToastInfo(R.string.add_device_pwd_rule_tip1);
            return false;
        } else if (res == PasswordCheckRules.PASSWORD_INVALID
                || res == PasswordCheckRules.PASSWORD_NOT_SAFY
                || res == PasswordCheckRules.PASSWORD_INVALID_LENGTH
                || res == PasswordCheckRules.PASSWORD_INVALID_COMBINATION) {
            mView.get().showToastInfo(R.string.mobile_common_password_too_simple);
            return false;
        }

        return true;
    }


    @Override
    public void recyle() {
        mDHMusicPlayer.release();
        stopSearchDevice();
    }

    @Override
    public void onDeviceSearched(String sncode, DEVICE_NET_INFO_EX device_net_info_ex) {
        if (device_net_info_ex == null) {
            return;
        }
        String szSerialNo = new String(device_net_info_ex.szSerialNo).trim();
        if (isOnlyNeedUnicast()) {
            if (szSerialNo.equalsIgnoreCase(mDeviceSn)) {
                boolean isEffectiveIp = checkEffectiveIP(device_net_info_ex);
                mDeviceNetInfoEx = device_net_info_ex;
                if (isEffectiveIp) {
                    mHandler.obtainMessage(SEARCH_SUCCESS).sendToTarget();
                }
            }
        } else {
            if (device_net_info_ex != null && szSerialNo.equalsIgnoreCase(mDeviceSn) && device_net_info_ex.iIPVersion == 4) {
                mDeviceNetInfoEx = device_net_info_ex;
                mHandler.obtainMessage(SEARCH_SUCCESS).sendToTarget();
            }
        }
    }

    //是否只需要进行单播
    private boolean isOnlyNeedUnicast() {
        return mIsDeviceNewVersion && !isSoftApAddType;
    }
}
