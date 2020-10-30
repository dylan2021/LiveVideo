package com.mm.android.deviceaddmodule.openapi;

public class CONST {
    //获取token
    public static String METHOD_ACCESSTOKEN="accessToken";
    //未绑定设备信息获取
    public static String METHOD_UNBINDDEVICEINFO="unBindDeviceInfo";
    //获取设备添加流程引导页配置信息
    public static String METHOD_GUIDEINFOGET="deviceAddingProcessGuideInfoGet";
    //校验设备添加流程引导页配置信息
    public static String METHOD_GUIDEINFOCHECK="deviceAddingProcessGuideInfoCheck";
    //绑定设备
    public static String METHOD_BINDDEVICE="bindDevice";
    //修改设备或通道名称
    public static String METHOD_MODIFYDEVICENAME="modifyDeviceName";
    //设备当前连接热点信息
    public static String METHOD_CURRENT_DEVICE_WIFI="currentDeviceWifi";
    //设备周边WIFI信息
    public static String METHOD_WIFI_AROUND="wifiAround";
    //控制设备连接热点
    public static String METHOD_CONTROL_DEVICE_WIFI="controlDeviceWifi";
    //批量根据设备序列号、通道号列表和配件号列表，获取设备的详细信息
    public static String METHOD_DEVICE_OPEN_DETAIL_LIST = "deviceOpenDetailList";
    // URL地址
    public static String HOST = "";
    // 如果不知道appid，请登录open.lechange.com，开发者服务模块中创建应用
    public static String APPID = "";
    // 如果不知道appsecret，请登录open.lechange.com，开发者服务模块中创建应用
    public static String SECRET = "";

    public enum Envirment {
        CHINA_TEST("https://funcopenapi.lechange.cn:443"),
        CHINA_PRO("https://openapi.lechange.cn:443"),
        OVERSEAS_TEST("https://openapifunc.easy4ip.com:443"),
        OVERSEAS_PRO("https://openapi.easy4ip.com:443");
        public String url;

        Envirment(String url) {
            this.url = url;
        }
    }

    public static void makeEnv(String url,String appId,String secretKey) {
        APPID = appId;
        SECRET = secretKey;
        HOST = url;
    }
}
