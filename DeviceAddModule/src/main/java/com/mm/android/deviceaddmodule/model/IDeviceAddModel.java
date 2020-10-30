package com.mm.android.deviceaddmodule.model;

import android.os.Handler;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.entity.WlanInfo;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;

/**
 * 设备添加数据请求接口类
 **/
public interface IDeviceAddModel {
    void getDeviceInfo(String sn, String deviceCodeModel,String deviceModelName, String imeiCode, Handler handler);  //从服务请求设备相关信息

    void getDeviceInfoLoop(String sn, String model, String imeiCode, int timeout, Handler handler);  //从服务请求设备相关信息,轮询设备是否已上线

    void setLoop(boolean loop);

    void setMiddleTimeUp(boolean middleTimeUp);

    DeviceAddInfo getDeviceInfoCache();         //获取本地缓存设备信息


    void checkDevIntroductionInfo(String deviceModelName,Handler handler);              //检查设备引导信息是否有更新

    void getDevIntroductionInfo(String deviceModelName,Handler handler);               //获取设备添加引导信息

    void getDevIntroductionInfoCache(String deviceModelName,Handler handler);               //获取设备添加引导信息本地缓存

    void initDev(DEVICE_NET_INFO_EX device_net_info_ex, String pwd, Handler handler);                              //初始化设备

    void initDevByIp(DEVICE_NET_INFO_EX device_net_info_ex, String pwd, Handler handler);                                      //单播初始化设备

    void deviceIPLogin(String ip, String devPwd, Handler handler);                                   //设备IP登录

    void getSoftApWifiList(String ip, String devPwd, Handler handler);                                //获取wifi列表

    void getSoftApWifiList4Sc(final String ip, final Handler handler);               //获取wifi列表 SC码设备

    void connectWifi4Sc(final String ip, final WlanInfo wlanInfo, final String wifiPwd, final Handler handler); //连接wifi SC码设备

    void connectWifi(String ip, String devPwd, WlanInfo wlanInfo, String wifiPwd, Handler handler);                  //连接wifi

    void connectWifi4Hidden(final String ip, final String devPwd, final WlanInfo wlanInfo, final String wifiPwd, final Handler handler);

    void modifyDeviceName(String deviceId, String channelId, String name, Handler handler);                //修改设备名

    //配件
    void addApDevice(String deviceId, String apId, String apType, String apModel, Handler handle);                  //添加配件

    void modifyAPDevice(String deviceId, String apId, String apName, boolean toDevice, Handler handle);               //修改配件名

    void getAddApResultAsync(String deviceId, String apId, Handler handle);          //同步添加结果

    /**
     * 绑定设备
     *
     * @param sn          设备序列号
     * @param code        设备验证码，在设备能力集支持时填写
     * @param deviceKey   从设备拿到的一串随机字符串（随机密码），用于后续平台对设备的认证,国内使用
     * @param longitude   经度
     * @param latitude    纬度
     * @param devUserName 设备用户名
     * @param devPwd      设备密码
     * @param handler
     */
    void bindDevice(String sn, String code, String deviceKey, String imeiCode, String longitude,
                    String latitude, String devUserName, String devPwd, Handler handler);
}
