package com.common.openapi;

import com.common.openapi.entity.CloudRecordsData;
import com.common.openapi.entity.ControlMovePTZData;
import com.common.openapi.entity.DeviceAlarmStatusData;
import com.common.openapi.entity.DeviceChannelInfoData;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceListData;
import com.common.openapi.entity.DeviceModifyNameData;
import com.common.openapi.entity.DeviceUnBindData;
import com.common.openapi.entity.DeviceVersionListData;
import com.common.openapi.entity.LocalRecordsData;
import com.google.gson.JsonObject;
import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.openapi.HttpSend;

import java.util.HashMap;

public class DeviceInfoOpenApiManager {

    private static int TIME_OUT = 10 * 1000;
    private static int DMS_TIME_OUT = 45 * 1000;

    /**
     * 分页获取乐橙app添加或分享的设备通道基本信息
     *
     * @param deviceListData
     * @return
     * @throws BusinessException
     */
    public static DeviceListData.Response deviceBaseList(DeviceListData deviceListData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("bindId", deviceListData.data.baseBindId);
        paramsMap.put("limit", deviceListData.data.limit);
        paramsMap.put("type", deviceListData.data.type);
        paramsMap.put("needApInfo", deviceListData.data.needApInfo);
        JsonObject json = HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_BASE_LIST,TIME_OUT);
        DeviceListData.Response response = new DeviceListData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 分页获取开放平台添加设备的通道基本信息
     *
     * @param deviceListData
     * @return
     * @throws BusinessException
     */
    public static DeviceListData.Response deviceOpenList(DeviceListData deviceListData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("bindId", deviceListData.data.openBindId);
        paramsMap.put("limit", deviceListData.data.limit);
        paramsMap.put("type", deviceListData.data.type);
        paramsMap.put("needApInfo", deviceListData.data.needApInfo);
        JsonObject json = HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_OPEN_LIST,TIME_OUT);
        DeviceListData.Response response = new DeviceListData.Response();
        response.parseData(json);
        return response;
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
        JsonObject json = HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_OPEN_DETAIL_LIST,TIME_OUT);
        DeviceDetailListData.Response response = new DeviceDetailListData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 批量根据设备序列号、通道号列表和配件号列表，获取乐橙app添加或分享的设备的详细信息
     *
     * @param deviceDetailListData
     * @return
     * @throws BusinessException
     */
    public static DeviceDetailListData.Response deviceBaseDetailList(DeviceDetailListData deviceDetailListData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceList", deviceDetailListData.data.deviceList);
        JsonObject json = HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_BASE_DETAIL_LIST,TIME_OUT);
        DeviceDetailListData.Response response = new DeviceDetailListData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 解绑设备
     *
     * @param deviceUnBindData
     * @return
     * @throws BusinessException
     */
    public static boolean unBindDevice(DeviceUnBindData deviceUnBindData) throws BusinessException {
        // 解绑设备
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId", deviceUnBindData.data.deviceId);
        HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_UN_BIND,TIME_OUT);
        return true;
    }

    /**
     * 获取设备版本和可升级信息
     *
     * @param deviceVersionListData
     * @return
     * @throws BusinessException
     */
    public static DeviceVersionListData.Response deviceVersionList(DeviceVersionListData deviceVersionListData) throws BusinessException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceIds", deviceVersionListData.data.deviceIds);
        JsonObject json = HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_VERSION_LIST,TIME_OUT);
        DeviceVersionListData.Response response = new DeviceVersionListData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 修改设备或通道名称
     *
     * @param deviceModifyNameData
     * @return
     * @throws BusinessException
     */
    public static boolean modifyDeviceName(DeviceModifyNameData deviceModifyNameData) throws BusinessException {
        // 解绑设备
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId", deviceModifyNameData.data.deviceId);
        paramsMap.put("channelId", deviceModifyNameData.data.channelId);
        paramsMap.put("name", deviceModifyNameData.data.name);
        HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_MODIFY_NAME,TIME_OUT);
        return true;
    }

    /**
     * 单个设备通道的详细信息获取
     *
     * @param deviceChannelInfoData
     * @return
     * @throws BusinessException
     */
    public static DeviceChannelInfoData.Response bindDeviceChannelInfo(DeviceChannelInfoData deviceChannelInfoData) throws BusinessException {
        // 单个设备通道的详细信息获取
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId", deviceChannelInfoData.data.deviceId);
        paramsMap.put("channelId", deviceChannelInfoData.data.channelId);
        JsonObject json = HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_CHANNEL_INFO, DMS_TIME_OUT);
        DeviceChannelInfoData.Response response = new DeviceChannelInfoData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 设置动检开关
     *
     * @param deviceAlarmStatusData
     * @return
     * @throws BusinessException
     */
    public static boolean modifyDeviceAlarmStatus(DeviceAlarmStatusData deviceAlarmStatusData) throws BusinessException {
        // 解绑设备
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId", deviceAlarmStatusData.data.deviceId);
        paramsMap.put("channelId", deviceAlarmStatusData.data.channelId);
        paramsMap.put("enable", deviceAlarmStatusData.data.enable);
        HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_MODIFY_ALARM_STATUS, DMS_TIME_OUT);
        return true;
    }

    /**
     * 设备升级
     *
     * @param deviceId
     * @return
     * @throws BusinessException
     */
    public static boolean upgradeDevice(String deviceId) throws BusinessException {
        // 解绑设备
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId", deviceId);
        HttpSend.execute(paramsMap, MethodConst.METHOD_DEVICE_UPDATE, DMS_TIME_OUT);
        return true;
    }

    /**
     * 倒序查询设备云录像片段
     *
     * @param cloudRecordsData
     * @return
     * @throws BusinessException
     */
    public static CloudRecordsData.Response getCloudRecords(CloudRecordsData cloudRecordsData) throws BusinessException {
        // 倒序查询设备云录像片段
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId", cloudRecordsData.data.deviceId);
        paramsMap.put("channelId", cloudRecordsData.data.channelId);
        paramsMap.put("beginTime", cloudRecordsData.data.beginTime);
        paramsMap.put("endTime", cloudRecordsData.data.endTime);
        paramsMap.put("nextRecordId", cloudRecordsData.data.nextRecordId);
        paramsMap.put("count", cloudRecordsData.data.count);
        JsonObject json = HttpSend.execute(paramsMap, MethodConst.METHOD_GET_CLOUND_RECORDS,TIME_OUT);
        CloudRecordsData.Response response = new CloudRecordsData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 查询设备设备录像片段
     *
     * @param localRecordsData
     * @return
     * @throws BusinessException
     */
    public static LocalRecordsData.Response queryLocalRecords(LocalRecordsData localRecordsData) throws BusinessException {
        // 查询设备设备录像片段
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId", localRecordsData.data.deviceId);
        paramsMap.put("channelId", localRecordsData.data.channelId);
        paramsMap.put("beginTime", localRecordsData.data.beginTime);
        paramsMap.put("endTime", localRecordsData.data.endTime);
        paramsMap.put("type", localRecordsData.data.type);
        paramsMap.put("queryRange", localRecordsData.data.queryRange);
        JsonObject json = HttpSend.execute(paramsMap, MethodConst.METHOD_QUERY_LOCAL_RECORD, DMS_TIME_OUT);
        LocalRecordsData.Response response = new LocalRecordsData.Response();
        response.parseData(json);
        return response;
    }

    /**
     * 云台移动控制接口（V2版本）
     *
     * @param controlMovePTZData
     * @return
     * @throws BusinessException
     */
    public static void controlMovePTZ(ControlMovePTZData controlMovePTZData) throws BusinessException {
        // 云台移动控制接口（V2版本）
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("deviceId", controlMovePTZData.data.deviceId);
        paramsMap.put("channelId", controlMovePTZData.data.channelId);
        paramsMap.put("operation", controlMovePTZData.data.operation);
        paramsMap.put("duration", controlMovePTZData.data.duration);
        HttpSend.execute(paramsMap, MethodConst.METHOD_CONTROL_MOVE_PTZ, DMS_TIME_OUT);
    }

    /**
     * 删除设备云录像片段
     *
     * @param recordRegionId
     * @return
     * @throws BusinessException
     */
    public static boolean deleteCloudRecords(String recordRegionId) throws BusinessException {
        // 删除设备云录像片段
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("token", LCDeviceEngine.newInstance().accessToken);
        paramsMap.put("recordRegionId", recordRegionId);
        HttpSend.execute(paramsMap, MethodConst.METHOD_DELETE_CLOUND_RECORDS,TIME_OUT);
        return true;
    }
}
