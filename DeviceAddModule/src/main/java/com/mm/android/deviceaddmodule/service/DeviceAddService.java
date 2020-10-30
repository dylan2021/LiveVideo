package com.mm.android.deviceaddmodule.service;

import android.text.TextUtils;

import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceBindResult;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceIntroductionInfo;
import com.mm.android.deviceaddmodule.openapi.DeviceAddOpenApiManager;
import com.mm.android.deviceaddmodule.openapi.data.BindDeviceData;
import com.mm.android.deviceaddmodule.openapi.data.DeviceInfoBeforeBindData;
import com.mm.android.deviceaddmodule.openapi.data.DeviceLeadingInfoData;
import com.mm.android.deviceaddmodule.openapi.data.DeviceModelOrLeadingInfoCheckData;
import com.mm.android.deviceaddmodule.openapi.data.ModifyDeviceNameData;

/**
 * 设备添加模块网络协议栈
 */
public class DeviceAddService{
    private static final String TAG = "DeviceAddService";

    /**
     * openApi
     *
     * 绑定设备前，获取设备信息
     *
     * @param deviceId        String 必须 设备序列号
     * @param deviceCodeModel String 必须 二维码型号
     * @param deviceModelName String 必须 APP展示的设备型号名称(产品称之为市场型号,用户自己选择设备类型时选的是市场型号)
     * @param ncCode          String 必须 用于标识设备配网能力
     * @param timeout
     * @return
     * @throws BusinessException
     */
    public DeviceAddInfo deviceInfoBeforeBind( String deviceId, String deviceCodeModel, String deviceModelName, String ncCode, int timeout) throws BusinessException {
        DeviceInfoBeforeBindData beforeBindData=new DeviceInfoBeforeBindData();
        beforeBindData.data.token= LCDeviceEngine.newInstance().accessToken;
        beforeBindData.data.deviceId=deviceId;
        beforeBindData.data.deviceCodeModel=deviceCodeModel;
        beforeBindData.data.deviceModelName=deviceModelName;
        beforeBindData.data.ncCode=ncCode;
        DeviceInfoBeforeBindData.Response response = DeviceAddOpenApiManager.deviceInfoBeforeBind(beforeBindData);
        return DeviceAddEntityChangeHelper.parse2DeviceAddInfo(response.data);
    }

    /**
     * openApi
     *
     * 校验设备型号或者设备引导信息配置信息是否更新
     *
     * @param checkType       String 必须 要校验的类型，DEVICE_MODEL:设备产品型号信息；DEVICE_LEADING_INFO:设备引导信息
     * @param deviceModelName String 可选 设备市场型号，checkType为DEVICE_LEADING_INFO时要传
     * @param updateTime      String 必须 APP本地缓存了配置后，请求使用上次请求配置服务返回的时间，检查是否需要更新配置
     * @param timeout
     * @return
     * @throws BusinessException
     */
    public String deviceModelOrLeadingInfoCheck(String checkType, String deviceModelName, String updateTime, int timeout) throws BusinessException {
        DeviceModelOrLeadingInfoCheckData req=new DeviceModelOrLeadingInfoCheckData();
        req.data.token = LCDeviceEngine.newInstance().accessToken;
        req.data.deviceModelName = deviceModelName;
        req.data.updateTime = updateTime;
        DeviceModelOrLeadingInfoCheckData.Response response = DeviceAddOpenApiManager.deviceModelOrLeadingInfoCheck(req);
        return response.data.isUpdated + "";
    }

    /**
     * openApi
     *
     * 根据设备市场型号获取设备添加流程引导页配置信息  以deviceModel_语言为索引
     *
     * @param deviceModel String 必须 设备市场型号
     * @param timeout
     * @return
     * @throws BusinessException
     */
    public DeviceIntroductionInfo deviceLeadingInfo(String deviceModel, int timeout) throws BusinessException {
        DeviceLeadingInfoData req=new DeviceLeadingInfoData();
        req.data.token = LCDeviceEngine.newInstance().accessToken;
        req.data.deviceModelName = deviceModel;
        DeviceLeadingInfoData.Response response = DeviceAddOpenApiManager.deviceLeadingInfo(req);
        FileSaveHelper.saveToJsonInfo(response.body, deviceModel + "_" + "zh_CN" + "_" + FileSaveHelper.INTRODUCTION_INFO_NAME);
        return DeviceAddEntityChangeHelper.parse2DeviceIntroductionInfo(response.data);
    }

    /**
     * 根据设备市场型号获取设备添加流程引导页配置信息，从本地缓存获取 这个接口不属于网络交互，需要移出去，放到Model里面
     *
     * @param deviceModel
     * @return
     * @throws BusinessException
     */
    public DeviceIntroductionInfo introductionInfosGetCache(String deviceModel) throws BusinessException {
        return FileSaveHelper.getIntroductionInfoCache(deviceModel, "zh_CN");
    }

    /**
     * openApi
     *
     * @param deviceId  String 必须 设备序列号
     * @param code      必须 设备验证码
     *                  code统称为设备验证码，但是针对不同的设备传的code值也会不一样。需要判断所需绑定设备是否有auth能力级：
     *                  1.如果该设备有auth能力级，code值传设备初始化后的设备密码；
     *                  2.如果该设备没有auth能力级但是设备底部标签中（或二维码中）有6为数字的安全码，code值传该6位数字；
     *                  3.如果该设备没有auth能力级并且设备底部标签中（或二维码中）没有6为数字的安全码，则code值传空即可；
     *                  只支持绑定paas设备，故code只会传设备密码或者SC码
     * @param timeout
     * @return
     * @throws BusinessException
     */
    public DeviceBindResult userDeviceBind(String deviceId, String code, int timeout) throws BusinessException {
        BindDeviceData req=new BindDeviceData();
        req.data.token= LCDeviceEngine.newInstance().accessToken;
        req.data.deviceId = deviceId;
        req.data.code = code;
        BindDeviceData.Response response = DeviceAddOpenApiManager.userDeviceBind(req);
        DeviceBindResult deviceBindResult = new DeviceBindResult();
        deviceBindResult.setBindStatus(response.data.bindStatus);
        deviceBindResult.setDeviceName(response.data.deviceName);
        deviceBindResult.setUserAccount(response.data.userAccount);
        return deviceBindResult;
    }

    /**
     * openApi
     * 修改设备或者通道名，channelId为空则为修改设备名，不为空为修改通道名
     *
     * @param deviceId  String 必须 设备序列号
     * @param channelId String 可选 设备通道号
     * @param name      String 必须 设备名称
     * @param timeout
     * @return
     * @throws BusinessException
     */
    public boolean modifyDeviceName(String deviceId, String channelId, String name, int timeout) throws BusinessException {
        ModifyDeviceNameData req=new ModifyDeviceNameData();
        req.data.token = LCDeviceEngine.newInstance().accessToken;
        req.data.deviceId = deviceId;
        if (!TextUtils.isEmpty(channelId)) {
            req.data.channelId = channelId;
        }
        req.data.name = name;
        ModifyDeviceNameData.Response response = DeviceAddOpenApiManager.modifyDeviceName(req);
        return response != null;
    }
}
