package com.common.openapi;

public class MethodConst {
    //分页获取乐橙app添加或分享的设备通道基本信息
    public static String METHOD_DEVICE_BASE_LIST = "deviceBaseList";
    //分页获取开放平台添加设备的通道基本信息
    public static String METHOD_DEVICE_OPEN_LIST = "deviceOpenList";
    //批量根据设备序列号、通道号列表和配件号列表，获取设备的详细信息
    public static String METHOD_DEVICE_OPEN_DETAIL_LIST = "deviceOpenDetailList";
    //批量根据设备序列号、通道号列表和配件号列表，获取乐橙app添加或分享的设备的详细信息
    public static String METHOD_DEVICE_BASE_DETAIL_LIST = "deviceBaseDetailList";
    //解绑设备
    public static String METHOD_DEVICE_UN_BIND = "unBindDevice";
    //获取设备版本和可升级信息
    public static String METHOD_DEVICE_VERSION_LIST = "deviceVersionList";
    //修改设备或通道名称
    public static String METHOD_DEVICE_MODIFY_NAME = "modifyDeviceName";
    //单个设备通道的详细信息获取
    public static String METHOD_DEVICE_CHANNEL_INFO = "bindDeviceChannelInfo";
    //设置动检开关
    public static String METHOD_DEVICE_MODIFY_ALARM_STATUS = "modifyDeviceAlarmStatus";
    //设备升级
    public static String METHOD_DEVICE_UPDATE = "upgradeDevice";
    //倒序查询设备云录像片段
    public static String METHOD_GET_CLOUND_RECORDS = "getCloudRecords";
    //查询设备设备录像片段
    public static String METHOD_QUERY_LOCAL_RECORD = "queryLocalRecords";
    //云台移动控制接口（V2版本）
    public static String METHOD_CONTROL_MOVE_PTZ = "controlMovePTZ";
    //查询设备云录像片
    public static String METHOD_QUERY_CLOUND_RECORDS = "queryCloudRecords";
    //删除设备云录像片段
    public static String METHOD_DELETE_CLOUND_RECORDS = "deleteCloudRecords";

    public interface ParamConst {
        public String deviceDetail = "deviceDetail";
        public String recordType = "recordType";
        public String recordData = "recordsData";
        public int recordTypeLocal = 2;
        public int recordTypeCloud = 1;
        public String fromList = "list";
    }
}
