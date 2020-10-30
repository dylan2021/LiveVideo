package com.common.openapi;

import android.os.Message;

import com.common.openapi.entity.DeviceAlarmStatusData;
import com.common.openapi.entity.DeviceChannelInfoData;
import com.common.openapi.entity.DeviceModifyNameData;
import com.common.openapi.entity.DeviceUnBindData;
import com.common.openapi.entity.DeviceVersionListData;
import com.mm.android.deviceaddmodule.device_wifi.CurWifiInfo;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessRunnable;
import com.mm.android.deviceaddmodule.mobilecommon.base.LCBusinessHandler;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.BusinessErrorTip;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;
import com.mm.android.deviceaddmodule.openapi.DeviceAddOpenApiManager;

public class DeviceDetailService {


    /**
     * 获取设备版本和可升级信息
     *
     * @param deviceVersionListData
     * @param deviceVersionCallBack
     */
    public void deviceVersionList(final DeviceVersionListData deviceVersionListData, final IGetDeviceInfoCallBack.IDeviceVersionCallBack deviceVersionCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (deviceVersionCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    deviceVersionCallBack.deviceVersion((DeviceVersionListData.Response) msg.obj);
                } else {
                    //失败
                    deviceVersionCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    DeviceVersionListData.Response response = DeviceInfoOpenApiManager.deviceVersionList(deviceVersionListData);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, response).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 修改设备或通道名称
     *
     * @param deviceModifyNameData
     * @param modifyDeviceCallBack
     */
    public void modifyDeviceName(final DeviceModifyNameData deviceModifyNameData, final IGetDeviceInfoCallBack.IModifyDeviceCallBack modifyDeviceCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (modifyDeviceCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    modifyDeviceCallBack.deviceModify((boolean) msg.obj);
                } else {
                    //失败
                    modifyDeviceCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    boolean b = DeviceInfoOpenApiManager.modifyDeviceName(deviceModifyNameData);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, b).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 解绑设备
     *
     * @param deviceUnBindData
     * @param unbindDeviceCallBack
     */
    public void unBindDevice(final DeviceUnBindData deviceUnBindData, final IGetDeviceInfoCallBack.IUnbindDeviceCallBack unbindDeviceCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (unbindDeviceCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    unbindDeviceCallBack.unBindDevice((boolean) msg.obj);
                } else {
                    //失败
                    unbindDeviceCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    boolean b = DeviceInfoOpenApiManager.unBindDevice(deviceUnBindData);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, b).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 单个设备通道的详细信息获取
     *
     * @param deviceChannelInfoData
     * @param deviceChannelInfoCallBack
     */
    public void bindDeviceChannelInfo(final DeviceChannelInfoData deviceChannelInfoData, final IGetDeviceInfoCallBack.IDeviceChannelInfoCallBack deviceChannelInfoCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (deviceChannelInfoCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    deviceChannelInfoCallBack.deviceChannelInfo((DeviceChannelInfoData.Response) msg.obj);
                } else {
                    //失败
                    deviceChannelInfoCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    DeviceChannelInfoData.Response response = DeviceInfoOpenApiManager.bindDeviceChannelInfo(deviceChannelInfoData);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, response).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 设置动检开关
     *
     * @param deviceAlarmStatusData
     * @param deviceAlarmStatusCallBack
     */
    public void modifyDeviceAlarmStatus(final DeviceAlarmStatusData deviceAlarmStatusData, final IGetDeviceInfoCallBack.IDeviceAlarmStatusCallBack deviceAlarmStatusCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (deviceAlarmStatusCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    deviceAlarmStatusCallBack.deviceAlarmStatus((boolean) msg.obj);
                } else {
                    //失败
                    deviceAlarmStatusCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    boolean b = DeviceInfoOpenApiManager.modifyDeviceAlarmStatus(deviceAlarmStatusData);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, b).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 设备升级
     *
     * @param deviceId
     * @param deviceUpdateCallBack
     */
    public void upgradeDevice(final String deviceId, final IGetDeviceInfoCallBack.IDeviceUpdateCallBack deviceUpdateCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (deviceUpdateCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    deviceUpdateCallBack.deviceUpdate((boolean) msg.obj);
                } else {
                    //失败
                    deviceUpdateCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    boolean b = DeviceInfoOpenApiManager.upgradeDevice(deviceId);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, b).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 设备当前连接热点信息
     *
     * @param deviceId
     * @param deviceCurrentWifiInfoCallBack
     */
    public void currentDeviceWifi(final String deviceId, final IGetDeviceInfoCallBack.IDeviceCurrentWifiInfoCallBack deviceCurrentWifiInfoCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (deviceCurrentWifiInfoCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    deviceCurrentWifiInfoCallBack.deviceCurrentWifiInfo((CurWifiInfo) msg.obj);
                } else {
                    //失败
                    deviceCurrentWifiInfoCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    CurWifiInfo curWifiInfo = DeviceAddOpenApiManager.currentDeviceWifi(deviceId);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, curWifiInfo).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

}
