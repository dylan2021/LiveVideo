package com.mm.android.deviceaddmodule.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.SearchDeviceManager;
import com.mm.android.deviceaddmodule.contract.DeviceAddConstract;
import com.mm.android.deviceaddmodule.event.DeviceAddEvent;
import com.mm.android.deviceaddmodule.helper.DeviceAddHelper;
import com.mm.android.deviceaddmodule.mobilecommon.common.LCConfiguration;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.mobilecommon.utils.CommonHelper;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;
import com.zxing.Extras;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

public class DeviceAddPresenter implements DeviceAddConstract.Presenter {
    WeakReference<DeviceAddConstract.View> mView;
    private SearchDeviceManager mServiceManager;
    private String curTitleMode = DeviceAddHelper.TitleMode.BLANK.name();            //当前标题模式

    public DeviceAddPresenter(DeviceAddConstract.View view) {
        mView = new WeakReference<>(view);
        // 启动搜索设备的服务
        mServiceManager = SearchDeviceManager.getInstance();
        mServiceManager.connnectService();
    }

    @Override
    public String getCurTitleMode() {
        return curTitleMode;
    }

    @Override
    public void setCurTitleMode(String titleMode) {
        curTitleMode = titleMode;
    }

    @Override
    public void dispatchIntentData(Intent intent) {
        Bundle bundle = intent.getExtras();
        boolean isHubPair = false;
        String hubType = "";//hub 二代
        String sn = "";
        boolean offlineConfigType = false;
        String deviceModelName = "";
        boolean isDeviceDetail = false;
        String imeiCode = "";
        String scCode = "";
        String devPwd = "";
        boolean isFromDeviceDispatch = false;
        DeviceAddInfo dispatch = null;
        if (bundle != null) {

            isFromDeviceDispatch = bundle.getBoolean(Extras.device.EXTRAS_FROM_DISPATCH, false);
            dispatch = (DeviceAddInfo) bundle.getSerializable(Extras.device.EXTRAS_DEVICE_DISPATCH);

            sn = bundle.getString(LCConfiguration.DEVICESN_PARAM);
            isDeviceDetail = bundle.getBoolean(LCConfiguration.IS_DEVICE_DETAIL_PARAM);
            DeviceAddInfo.GatewayInfo gatewayInfo = new DeviceAddInfo.GatewayInfo();
            gatewayInfo.setSn(sn);
            DeviceAddModel.newInstance().getDeviceInfoCache().setGatewayInfo(gatewayInfo);
            // 是否为中间页添加
            DeviceAddModel.newInstance().getDeviceInfoCache().setDeviceDetail(isDeviceDetail);

            isHubPair = bundle.getBoolean(LCConfiguration.HUB_PAIR_PARAM);
            hubType = bundle.getString(LCConfiguration.HUB_TYPE_PARAM);
            offlineConfigType = bundle.getBoolean(LCConfiguration.OFFLINE_CONFIG_TYPE_PARAM);
            deviceModelName = bundle.getString(DeviceAddHelper.DEVICE_MODEL_NAME_PARAM);

            imeiCode = bundle.getString(LCConfiguration.DEVICE_IMEI_PARAM);
            scCode = bundle.getString(LCConfiguration.SC_CODE_PARAM);
            devPwd = bundle.getString(LCConfiguration.DEVICE_PWD_PARAM);

        }

        if (offlineConfigType) {
            DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
            deviceAddInfo.setWifiOfflineMode(true);
            deviceAddInfo.setDeviceSn(sn);
            deviceAddInfo.setImeiCode(imeiCode);
            deviceAddInfo.setSc(scCode);
            deviceAddInfo.setDevicePwd(devPwd);
            deviceAddInfo.setDeviceModel(deviceModelName);
            LogUtil.debugLog("DeviceAddPresenter", "offlineConfigType deviceAddModel: " + DeviceAddModel.newInstance());
            LogUtil.debugLog("DeviceAddPresenter", "offlineConfigType deviceAddInfo: " + deviceAddInfo);
            mView.get().setTitle(R.string.mobile_common_network_config);
            deviceAddInfo.setStartTime(System.currentTimeMillis());
            mView.get().goOfflineConfigPage(sn, deviceModelName, imeiCode);
            return;
        }
        if (isHubPair) {
            DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
            deviceAddInfo.setCurDeviceAddType(DeviceAddInfo.DeviceAddType.HUB);
            mView.get().goHubPairPage(sn, hubType);
        } else if (isFromDeviceDispatch && dispatch != null) {
            DeviceAddModel.newInstance().updateDeviceAllCache(dispatch);
            mView.get().goDispatchPage();
        } else {
            mView.get().goScanPage();
        }
    }

    @Override
    public void getGPSLocation() {
        double[] gps = CommonHelper.getGpsInfo(mView.get().getContextInfo());
        DeviceAddInfo.GPSInfo gpsInfo = DeviceAddModel.newInstance().getDeviceInfoCache().getGpsInfo();
        gpsInfo.setLongitude(String.valueOf(gps[0]));
        gpsInfo.setLatitude(String.valueOf(gps[1]));
    }

    @Override
    public void dispatchPageNavigation() {
        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
        if (DeviceAddModel.newInstance().getDeviceInfoCache().isWifiConfigModeOptional()) {
            EventBus.getDefault().post(new DeviceAddEvent(DeviceAddEvent.SHOW_TYPE_CHOSE_ACTION));
            return;
        }
        if (DeviceAddInfo.DeviceType.ap.name().equals(deviceAddInfo.getType())) {
            //TODO 当前版本不支持配件添加，直接提示返回

            mView.get().goNotSupportBindTipPage();
            return;
        } else {
            //设备
            if (TextUtils.isEmpty(deviceAddInfo.getModelName()) && TextUtils.isEmpty(deviceAddInfo.getNc())) {//modelName 为空的情况 进入设备选择页 V3.9+
                mView.get().goTypeChoosePage();
            } else {
                if (TextUtils.isEmpty(deviceAddInfo.getConfigMode())) {
                    deviceAddInfo.setCurDeviceAddType(DeviceAddInfo.DeviceAddType.LAN);
                    mView.get().goWiredwirelessPage(false);
                } else {
                    if (deviceAddInfo.getConfigMode().contains(DeviceAddInfo.ConfigMode.SoftAP.name())) {       //软AP
                        // 局域网内搜索到则直接走初始化或连接云平台流程
                        DEVICE_NET_INFO_EX deviceNetInfoEx = SearchDeviceManager.getInstance().getDeviceNetInfo(deviceAddInfo.getDeviceSn());
                        if (deviceNetInfoEx != null) {
                            // 支持sc码,进入云配置流程
                            if (DeviceAddHelper.isSupportAddBySc(deviceAddInfo)) {
                                mView.get().goCloudConnetPage();
                            } else if (DeviceAddHelper.isDeviceNeedInit(deviceNetInfoEx)) {
                                mView.get().goInitPage(deviceNetInfoEx);
                            } else {
                                mView.get().goCloudConnetPage();
                            }
                        } else {
                            deviceAddInfo.setCurDeviceAddType(DeviceAddInfo.DeviceAddType.SOFTAP);
                            mView.get().goSoftApPage();
                        }
                    } else if (deviceAddInfo.getConfigMode().contains(DeviceAddInfo.ConfigMode.NBIOT.name())) {    //NB配网
                        deviceAddInfo.setCurDeviceAddType(DeviceAddInfo.DeviceAddType.NBIOT);
                        if (TextUtils.isEmpty(deviceAddInfo.getImeiCode())) {
                            mView.get().goIMEIInputPage();
                        } else {
                            mView.get().goNBPage();
                        }
                    } else if (deviceAddInfo.getConfigMode().contains(DeviceAddInfo.ConfigMode.SoundWave.name())
                            || deviceAddInfo.getConfigMode().contains(DeviceAddInfo.ConfigMode.SmartConfig.name())) {   //无线
                        deviceAddInfo.setCurDeviceAddType(DeviceAddInfo.DeviceAddType.WLAN);
                        mView.get().goWiredwirelessPage(true);
                    } else if (deviceAddInfo.getConfigMode().contains(DeviceAddInfo.ConfigMode.LAN.name())) {      //有线
                        deviceAddInfo.setCurDeviceAddType(DeviceAddInfo.DeviceAddType.LAN);
                        mView.get().goWiredwirelessPage(false);
                    } else if (deviceAddInfo.getConfigMode().contains(DeviceAddInfo.ConfigMode.Location.name())) {      //设备本地配网
                        deviceAddInfo.setCurDeviceAddType(DeviceAddInfo.DeviceAddType.LOCAL);
                        mView.get().goLocationPage();
                    } else if (deviceAddInfo.getConfigMode().contains(DeviceAddInfo.ConfigMode.Bluetooth.name())) { // 蓝牙锁配网
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(LCConfiguration.BUNDLE_DEVICE_ADD_INFO, deviceAddInfo);
                        bundle.putString(LCConfiguration.DEVICE_MODEL, deviceAddInfo.getModelName());
                        mView.get().gotoAddBleLockPage(bundle);
                    } else {
                        deviceAddInfo.setCurDeviceAddType(DeviceAddInfo.DeviceAddType.LAN);
                        mView.get().goWiredwirelessPage(false);
                    }
                }
            }
        }
    }

    @Override
    public void uninit() {
        mServiceManager = SearchDeviceManager.getInstance();
        if (mServiceManager.checkSearchDeviceServiceIsExist()) {
            mServiceManager = SearchDeviceManager.getInstance();
            mServiceManager.checkSearchDeviceServiceDestory();
        }
        DeviceAddModel.newInstance().recyle();
        DeviceAddHelper.clearNetWork();
    }

    @Override
    public void getDeviceShareInfo() {
        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();

        if (TextUtils.isEmpty(deviceAddInfo.getDeviceSn())) {
            mView.get().showToastInfo(R.string.mobile_common_bec_common_network_exception);
            return;
        }

        mView.get().gotoDeviceSharePage(deviceAddInfo.getDeviceSn());
    }

    @Override
    public boolean canBeShare() {
        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();

        //配件不能共享
        if (DeviceAddInfo.DeviceType.ap.name().equals(deviceAddInfo.getType())) {
            return false;
        }

        //DS11不能共享
        if ("DS11".equalsIgnoreCase(deviceAddInfo.getDeviceModel())) {
            return false;
        }

        //H1G不能共享
        if ("ARC2000E-GSW".equalsIgnoreCase(deviceAddInfo.getDeviceModel())) {
            return false;
        }
        return true;
    }

    @Override
    public void changeToWireless() {
        mView.get().goWiredwirelessPageNoAnim(true);
    }

    @Override
    public void changeToWired() {
        // 软ap与有线流程切换后，设备密码会被清掉，故需要重新将sc码设置成设备密码
        DeviceAddHelper.setDevicePwdBySc();
        mView.get().goWiredwirelessPageNoAnim(false);
    }

    @Override
    public void changeToSoftAp() {
        // 软ap与有线流程切换后，设备密码会被清掉，故需要重新将sc码设置成设备密码
        DeviceAddHelper.setDevicePwdBySc();
        mView.get().goSoftApPageNoAnim();
    }

    @Override
    public void changeToNB() {
        mView.get().goNBPage();
    }
}
