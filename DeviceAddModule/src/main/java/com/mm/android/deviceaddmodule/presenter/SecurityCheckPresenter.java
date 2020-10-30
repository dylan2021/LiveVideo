package com.mm.android.deviceaddmodule.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.SearchDeviceManager;
import com.mm.android.deviceaddmodule.contract.SecurityCheckConstract;
import com.mm.android.deviceaddmodule.helper.DeviceAddHelper;
import com.mm.android.deviceaddmodule.helper.Utils4AddDevice;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.ProviderManager;
import com.mm.android.deviceaddmodule.mobilecommon.common.LCConfiguration;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;

import java.lang.ref.WeakReference;

public class SecurityCheckPresenter implements SecurityCheckConstract.Presenter, SearchDeviceManager.ISearchDeviceListener {
    private static final int SEARCH_SUCCESS = 1;                            //搜索成功
    WeakReference<SecurityCheckConstract.View> mView;
    String mDeviceSn;
    DEVICE_NET_INFO_EX mDeviceNetInfoEx;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mView.get() != null &&
                    mView.get().isViewActive()) {
                stopSearchDevice();
                switch (msg.what) {
                    case SEARCH_SUCCESS:
                        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();

                        if (DeviceAddHelper.isDeviceNeedInit(mDeviceNetInfoEx)) {
                            // 支持sc码的软ap,未初始化时，走netSDK不需要登陆的获取wifi列表和配网接口
                            if (DeviceAddHelper.isSupportAddBySc(deviceAddInfo) && DeviceAddInfo.DeviceAddType.SOFTAP.equals(deviceAddInfo.getCurDeviceAddType())) {
                                mView.get().goSoftApWifiListPage(true);
                            } else {
                                mView.get().goInitPage(mDeviceNetInfoEx);
                            }
                        } else {
                            if (DeviceAddInfo.DeviceAddType.SOFTAP.equals(deviceAddInfo.getCurDeviceAddType())) {
                                String devPwd = DeviceAddModel.newInstance().getDeviceInfoCache().getDevicePwd();
                                //不支持初始化的密码默认为admin（目前仅国内K5设备是不支持初始化，需要用admin登录）
                                if (ProviderManager.getAppProvider().getAppType() != LCConfiguration.APP_LECHANGE_OVERSEA && !DeviceAddHelper.isDeviceSupportInit(mDeviceNetInfoEx)){
                                    devPwd = "admin";
                                    DeviceAddModel.newInstance().getDeviceInfoCache().setDevicePwd(devPwd);
                                }
                                if (TextUtils.isEmpty(devPwd)) {
                                    mView.get().goDevLoginPage();
                                } else {
                                    mView.get().goSoftApWifiListPage(false);
                                }
                            } else {
                                mView.get().goCloudConnetPage();
                            }

                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public SecurityCheckPresenter(SecurityCheckConstract.View view) {
        mView = new WeakReference<>(view);
        mDeviceSn = DeviceAddModel.newInstance().getDeviceInfoCache().getDeviceSn();
        mDeviceNetInfoEx = SearchDeviceManager.getInstance().getDeviceNetInfo(mDeviceSn);
    }

    @Override
    public void checkDevice() {
        startSearchDevice();
        if (mDeviceNetInfoEx == null
                ||(mDeviceNetInfoEx!=null && Utils4AddDevice.checkDeviceVersion(mDeviceNetInfoEx)
                &&!Utils4AddDevice.checkEffectiveIP(mDeviceNetInfoEx))) {//未搜索到设备或新设备且其IP无效，重新搜索
            startSearchDevice();
        } else {
            mHandler.obtainMessage(SEARCH_SUCCESS).sendToTarget();
        }

    }

    @Override
    public void recyle() {
        stopSearchDevice();
    }

    private void startSearchDevice() {
        SearchDeviceManager searchDeviceManager = SearchDeviceManager.getInstance();
        searchDeviceManager.registerListener(this);
        searchDeviceManager.startSearch();
    }

    private void stopSearchDevice() {
        SearchDeviceManager searchDeviceManager = SearchDeviceManager.getInstance();
        searchDeviceManager.unRegisterListener(this);
    }

    @Override
    public void onDeviceSearched(String sncode, DEVICE_NET_INFO_EX device_net_info_ex) {
        if (device_net_info_ex == null) {
            return;
        }
        String szSerialNo = new String(device_net_info_ex.szSerialNo).trim();
        if (szSerialNo.equalsIgnoreCase(mDeviceSn)) {
            mDeviceNetInfoEx = device_net_info_ex;
            mHandler.obtainMessage(SEARCH_SUCCESS).sendToTarget();
        }
    }
}
