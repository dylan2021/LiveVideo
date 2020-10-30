package com.mm.android.deviceaddmodule.openapi;

import com.google.gson.JsonObject;
import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.device_wifi.CurWifiInfo;
import com.mm.android.deviceaddmodule.device_wifi.WifiConfig;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.openapi.data.BindDeviceData;
import com.mm.android.deviceaddmodule.openapi.data.DeviceDetailListData;
import com.mm.android.deviceaddmodule.openapi.data.DeviceInfoBeforeBindData;
import com.mm.android.deviceaddmodule.openapi.data.DeviceLeadingInfoData;
import com.mm.android.deviceaddmodule.openapi.data.DeviceModelOrLeadingInfoCheckData;
import com.mm.android.deviceaddmodule.openapi.data.ModifyDeviceNameData;

import java.util.HashMap;

public class DeviceAddOpenApiManager {
    private static int TIME_OUT = 10 * 1000;
    private static int TOKEN_TIME_OUT = 4 * 1000;
    private static int DMS_TIME_OUT = 45 * 1000;

    /**
     * 获取token
     *
     * @return
     * @throws BusinessException
     */
    public static String getToken() throws BusinessException {
        // 获取管理员token
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        JsonObject jsonData = HttpSend.execute(paramsMap, CONST.METHOD_ACCESSTOKEN,TOKEN_TIME_OUT);
        String token = jsonData.get("accessToken").getAsString();
        return token;
    }

    /**
     * 设备未绑定状态
     *
     * @param deviceInfoBeforeBindData
     * @return
     * @throws BusinessException
     */
    public static DeviceInfoBeforeBindData.Response deviceInfoBeforeBind(DeviceInfoBeforeBindData deviceInfoBeforeBindData) throws BusinessException {
        // 未绑定设备信息获取
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", deviceInfoBeforeBindData.data.token);
        paramsMap.put("deviceId", deviceInfoBeforeBindData.data.deviceId);
        paramsMap.put("deviceCodeModel", deviceInfoBeforeBindData.data.deviceCodeModel);
        paramsMap.put("deviceModelName", deviceInfoBeforeBindData.data.deviceModelName);
        paramsMap.put("ncCode", deviceInfoBeforeBindData.data.ncCode);
        JsonObject json = HttpSend.execute(paramsMap, CONST.METHOD_UNBINDDEVICEINFO,TIME_OUT);
        DeviceInfoBeforeBindData.Response response = new DeviceInfoBeforeBindData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 根据设备市场型号获取设备添加流程引导页配置信息
     *
     * @param deviceLeadingInfoData
     * @return
     * @throws BusinessException
     */
    public static DeviceLeadingInfoData.Response deviceLeadingInfo(DeviceLeadingInfoData deviceLeadingInfoData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", deviceLeadingInfoData.data.token);
        paramsMap.put("deviceModelName", deviceLeadingInfoData.data.deviceModelName);
        JsonObject json = HttpSend.execute(paramsMap, CONST.METHOD_GUIDEINFOGET,TIME_OUT);
        DeviceLeadingInfoData.Response response = new DeviceLeadingInfoData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 校验设备添加流程引导页配置信息是否需更新
     *
     * @param deviceModelOrLeadingInfoCheckData
     * @return
     * @throws BusinessException
     */
    public static DeviceModelOrLeadingInfoCheckData.Response deviceModelOrLeadingInfoCheck(DeviceModelOrLeadingInfoCheckData deviceModelOrLeadingInfoCheckData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", deviceModelOrLeadingInfoCheckData.data.token);
        paramsMap.put("deviceModelName", deviceModelOrLeadingInfoCheckData.data.deviceModelName);
        paramsMap.put("updateTime", deviceModelOrLeadingInfoCheckData.data.updateTime);
        JsonObject json = HttpSend.execute(paramsMap, CONST.METHOD_GUIDEINFOCHECK,TIME_OUT);
        DeviceModelOrLeadingInfoCheckData.Response response = new DeviceModelOrLeadingInfoCheckData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 绑定设备
     *
     * @param bindDeviceData
     * @return
     * @throws BusinessException
     */
    public static BindDeviceData.Response userDeviceBind(BindDeviceData bindDeviceData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", bindDeviceData.data.token);
        paramsMap.put("deviceId", bindDeviceData.data.deviceId);
        paramsMap.put("code", bindDeviceData.data.code);
        JsonObject json = HttpSend.execute(paramsMap, CONST.METHOD_BINDDEVICE,DMS_TIME_OUT);
        BindDeviceData.Response response = new BindDeviceData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 修改设备或通道名称
     *
     * @param bindDeviceData
     * @return
     * @throws BusinessException
     */
    public static ModifyDeviceNameData.Response modifyDeviceName(ModifyDeviceNameData bindDeviceData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", bindDeviceData.data.token);
        paramsMap.put("deviceId", bindDeviceData.data.deviceId);
        paramsMap.put("channelId", bindDeviceData.data.channelId);
        paramsMap.put("name", bindDeviceData.data.name);
        JsonObject json = HttpSend.execute(paramsMap, CONST.METHOD_MODIFYDEVICENAME,TIME_OUT);
        ModifyDeviceNameData.Response response = new ModifyDeviceNameData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 设备当前连接热点信息
     *
     * @param deviceId
     * @return
     * @throws BusinessException
     */
    public static CurWifiInfo currentDeviceWifi(String deviceId) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId",deviceId);
        JsonObject json = HttpSend.execute(paramsMap, CONST.METHOD_CURRENT_DEVICE_WIFI,DMS_TIME_OUT);
        CurWifiInfo.Response response = new CurWifiInfo.Response();
        response.parseData(json);
        return response.data;
    }

    /**
     * 设备周边WIFI信息
     *
     * @param token
     * @param deviceId
     * @return
     * @throws BusinessException
     */
    public static WifiConfig wifiAround(String token, String deviceId) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", token);
        paramsMap.put("deviceId", deviceId);
        JsonObject json = HttpSend.execute(paramsMap, CONST.METHOD_WIFI_AROUND,DMS_TIME_OUT);
        WifiConfig.Response response = new WifiConfig.Response();
        response.parseData(json);
        return response.data;
    }

    /**
     * 控制设备连接热点
     *
     * @param token
     * @param deviceId
     * @return
     * @throws BusinessException
     */
    public static boolean controlDeviceWifi(String token, String deviceId, String ssid, String bssid, boolean linkEnable, String password) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", token);
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("ssid", ssid);
        paramsMap.put("bssid", bssid);
        paramsMap.put("linkEnable", linkEnable);
        paramsMap.put("password", password);
         HttpSend.execute(paramsMap, CONST.METHOD_CONTROL_DEVICE_WIFI,DMS_TIME_OUT);
        return true;
    }

    /**
     * 批量根据设备序列号、通道号列表和配件号列表，获取设备的详细信息
     *
     * @param deviceDetailListData
     * @return
     * @throws BusinessException
     */
    public static DeviceDetailListData.Response deviceOpenDetailList(DeviceDetailListData deviceDetailListData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceList", deviceDetailListData.data.deviceList);
        JsonObject json = HttpSend.execute(paramsMap, CONST.METHOD_DEVICE_OPEN_DETAIL_LIST,TIME_OUT);
        DeviceDetailListData.Response response = new DeviceDetailListData.Response();
        response.parseData(json);
        return response;
    }
}
