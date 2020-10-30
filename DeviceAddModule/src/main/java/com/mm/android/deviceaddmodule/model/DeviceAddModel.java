package com.mm.android.deviceaddmodule.model;

import android.os.Handler;
import android.text.TextUtils;

import com.company.NetSDK.CFG_NETAPP_WLAN;
import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.company.NetSDK.EM_LOGIN_SPAC_CAP_TYPE;
import com.company.NetSDK.EM_WLAN_SCAN_AND_CONFIG_TYPE;
import com.company.NetSDK.FinalVar;
import com.company.NetSDK.INetSDK;
import com.company.NetSDK.NET_DEVICEINFO_Ex;
import com.company.NetSDK.NET_IN_GET_DEV_WIFI_LIST;
import com.company.NetSDK.NET_IN_INIT_DEVICE_ACCOUNT;
import com.company.NetSDK.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY;
import com.company.NetSDK.NET_IN_SET_DEV_WIFI;
import com.company.NetSDK.NET_IN_WLAN_ACCESSPOINT;
import com.company.NetSDK.NET_OUT_GET_DEV_WIFI_LIST;
import com.company.NetSDK.NET_OUT_INIT_DEVICE_ACCOUNT;
import com.company.NetSDK.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY;
import com.company.NetSDK.NET_OUT_SET_DEV_WIFI;
import com.company.NetSDK.NET_OUT_WLAN_ACCESSPOINT;
import com.company.NetSDK.NET_WLAN_ACCESSPOINT_INFO;
import com.company.NetSDK.SDKDEV_NETINTERFACE_INFO;
import com.company.NetSDK.SDKDEV_WLAN_DEVICE_LIST_EX;
import com.company.NetSDK.SDK_PRODUCTION_DEFNITION;
import com.mm.android.deviceaddmodule.entity.WlanInfo;
import com.mm.android.deviceaddmodule.helper.DeviceAddHelper;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessRunnable;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.ProviderManager;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceBindResult;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceIntroductionInfo;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;
import com.mm.android.deviceaddmodule.mobilecommon.utils.UIUtils;
import com.mm.android.deviceaddmodule.service.DeviceAddService;

import java.util.ArrayList;
import java.util.List;

import static com.mm.android.deviceaddmodule.helper.DeviceAddHelper.printError;

/**
 * 设备添加数据请求类,由于设备添加页面流程单一，这里设计为单例
 **/
public class DeviceAddModel implements IDeviceAddModel {
    private static final int NET_ERROR_DEVICE_ALREADY_INIT = 1017;         // 设备已经初始化
    private volatile static DeviceAddModel deviceAddModel;
    DeviceAddInfo mDeviceAddInfo;
    final int DMS_TIMEOUT = 45 * 1000;
    int TIME_OUT = 10 * 1000;
    boolean loop = true;                 //获取设备信息轮询标志变量
    boolean middleTimeUp = false;      //设定的中间时间时间到，此时间之后如设备还是已在服务注册，且不在线以及仍为P2P类型，走绑定流程
    int LOOP_ONCE_TIME = 3 * 1000;            //轮询间隔时间3S

    DeviceAddService deviceAddService;

    private DeviceAddModel() {
        deviceAddService = new DeviceAddService();
    }

    public static DeviceAddModel newInstance() {
        if (deviceAddModel == null) {
            synchronized (DeviceAddModel.class) {
                if (deviceAddModel == null) {
                    deviceAddModel = new DeviceAddModel();
                }
            }
        }
        return deviceAddModel;
    }

    public void updateDeviceCache(DeviceAddInfo deviceAddInfo) {
        mDeviceAddInfo.setDeviceExist(deviceAddInfo.getDeviceExist());
        mDeviceAddInfo.setBindStatus(deviceAddInfo.getBindStatus());
        mDeviceAddInfo.setBindAcount(deviceAddInfo.getBindAcount());
        mDeviceAddInfo.setAccessType(deviceAddInfo.getAccessType());
        mDeviceAddInfo.setStatus(deviceAddInfo.getStatus());
        mDeviceAddInfo.setConfigMode(deviceAddInfo.getConfigMode());
        mDeviceAddInfo.setBrand(deviceAddInfo.getBrand());
        mDeviceAddInfo.setFamily(deviceAddInfo.getFamily());
        mDeviceAddInfo.setDeviceModel(deviceAddInfo.getDeviceModel());
        mDeviceAddInfo.setModelName(deviceAddInfo.getModelName());
        mDeviceAddInfo.setCatalog(deviceAddInfo.getCatalog());
        mDeviceAddInfo.setAbility(deviceAddInfo.getAbility());
        mDeviceAddInfo.setType(deviceAddInfo.getType());
        mDeviceAddInfo.setWifiTransferMode(deviceAddInfo.getWifiTransferMode());
        mDeviceAddInfo.setChannelNum(deviceAddInfo.getChannelNum());
        mDeviceAddInfo.setWifiConfigModeOptional(deviceAddInfo.isWifiConfigModeOptional());
        mDeviceAddInfo.setSupport(deviceAddInfo.isSupport());
    }

    public void updateDeviceAllCache(DeviceAddInfo deviceAddInfo) {
        mDeviceAddInfo.setDeviceExist(deviceAddInfo.getDeviceExist());
        mDeviceAddInfo.setBindStatus(deviceAddInfo.getBindStatus());
        mDeviceAddInfo.setBindAcount(deviceAddInfo.getBindAcount());
        mDeviceAddInfo.setAccessType(deviceAddInfo.getAccessType());
        mDeviceAddInfo.setStatus(deviceAddInfo.getStatus());
        mDeviceAddInfo.setConfigMode(deviceAddInfo.getConfigMode());
        mDeviceAddInfo.setBrand(deviceAddInfo.getBrand());
        mDeviceAddInfo.setFamily(deviceAddInfo.getFamily());
        mDeviceAddInfo.setDeviceModel(deviceAddInfo.getDeviceModel());
        mDeviceAddInfo.setModelName(deviceAddInfo.getModelName());
        mDeviceAddInfo.setCatalog(deviceAddInfo.getCatalog());
        mDeviceAddInfo.setAbility(deviceAddInfo.getAbility());
        mDeviceAddInfo.setType(deviceAddInfo.getType());
        mDeviceAddInfo.setWifiTransferMode(deviceAddInfo.getWifiTransferMode());
        mDeviceAddInfo.setChannelNum(deviceAddInfo.getChannelNum());
        mDeviceAddInfo.setWifiConfigModeOptional(deviceAddInfo.isWifiConfigModeOptional());
        mDeviceAddInfo.setSupport(deviceAddInfo.isSupport());

        mDeviceAddInfo.setDeviceSn(deviceAddInfo.getDeviceSn());
        mDeviceAddInfo.setDeviceCodeModel(deviceAddInfo.getDeviceCodeModel());
        mDeviceAddInfo.setRegCode(deviceAddInfo.getRegCode());
        mDeviceAddInfo.setSc(deviceAddInfo.getSc());
        mDeviceAddInfo.setNc(deviceAddInfo.getNc());  // 将16进制的字符串转换为数字
        // 支持SC码的设备，使用SC码作为设备密码
        mDeviceAddInfo.setDevicePwd(deviceAddInfo.getSc());
        mDeviceAddInfo.setImeiCode(deviceAddInfo.getImeiCode());
        String wifiConfigMode;
        if (TextUtils.isEmpty(deviceAddInfo.getConfigMode())) { // 若配网模式未返回，默认可进行有线无线切换 V5.1默认增加软AP
            wifiConfigMode = DeviceAddInfo.ConfigMode.SmartConfig.name() + ","
                    + DeviceAddInfo.ConfigMode.LAN.name() + ","
                    + DeviceAddInfo.ConfigMode.SoundWave.name() + ","
                    + DeviceAddInfo.ConfigMode.SoftAP.name();
        } else {
            wifiConfigMode = deviceAddInfo.getConfigMode();
        }
        mDeviceAddInfo.setConfigMode(wifiConfigMode);
    }

    @Override
    public void getDeviceInfo(final String sn, final String deviceCodeModel, final String deviceModelName, final String imeiCode, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    DeviceAddInfo deviceAddInfo = deviceAddService.deviceInfoBeforeBind(sn, deviceCodeModel, deviceModelName, mDeviceAddInfo.getNc(), TIME_OUT);
                    updateDeviceCache(deviceAddInfo);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, mDeviceAddInfo).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    @Override
    public void getDeviceInfoLoop(final String sn, final String model, final String imeiCode, final int timeout, final Handler handler) {
        loop = true;
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                while (loop) {
                    if (canInterruptLoop()) {
                        break;
                    }

                    DeviceAddInfo deviceAddInfo = deviceAddService.deviceInfoBeforeBind(sn, mDeviceAddInfo.getDeviceCodeModel(), model, mDeviceAddInfo.getNc(), TIME_OUT);
                    updateDeviceCache(deviceAddInfo);
                    if (canInterruptLoop()) {
                        break;
                    }
                    try {//间隔3S查询一次
                        Thread.sleep(LOOP_ONCE_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (loop)
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS).sendToTarget();
            }
        };
    }

    /**
     * 是否满足中断轮询的条件，可以走绑定流程
     *
     * @return
     */
    private boolean canInterruptLoop() {
        String status = mDeviceAddInfo.getStatus();
        if (TextUtils.isEmpty(status)) {
            status = DeviceAddInfo.Status.offline.name();
        }
        if (mDeviceAddInfo.isDeviceInServer() &&
                (DeviceAddInfo.Status.online.name().equals(status)
                        || (DeviceAddInfo.Status.offline.name().equals(status) && mDeviceAddInfo.isP2PDev() && middleTimeUp))) {
            //以下两种情况走绑定流程：
            // 1.已注册上服务并且设备DMS在线
            // 2.已注册上服务，设备DMS不在线，设备类型为P2P（此时无法区分设备到底是P2P设备还是PAAS设备），并且设定的最大等待时间已到）
            return true;
        }
        return false;
    }

    @Override
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public void setMiddleTimeUp(boolean middleTimeUp) {
        this.middleTimeUp = middleTimeUp;
    }

    @Override
    public DeviceAddInfo getDeviceInfoCache() {
        if (mDeviceAddInfo == null) {
            mDeviceAddInfo = new DeviceAddInfo();
            LogUtil.debugLog("DeviceAddModel", "getDeviceInfoCache");
        }
        return mDeviceAddInfo;
    }

    @Override
    public void checkDevIntroductionInfo(final String deviceModelName, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                String lan = ProviderManager.getAppProvider().getAppLanguage();
                DeviceIntroductionInfo deviceIntroductionInfo = deviceAddService.introductionInfosGetCache(deviceModelName);
                boolean result = true;
                if (deviceIntroductionInfo != null) {
                    mDeviceAddInfo.setDevIntroductionInfos(deviceIntroductionInfo);
                    if (!TextUtils.isEmpty(deviceIntroductionInfo.getUpdateTime())) {
                        String resultStr = deviceAddService.deviceModelOrLeadingInfoCheck("DEVICE_LEADING_INFO", deviceModelName, deviceIntroductionInfo.getUpdateTime(), TIME_OUT);
                        result = resultStr.equalsIgnoreCase("true");
                    }
                }
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, result ? null : deviceIntroductionInfo).sendToTarget();
            }
        };
    }

    @Override
    public void getDevIntroductionInfo(final String deviceModelName, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                String lan = ProviderManager.getAppProvider().getAppLanguage();
                String modelName = mDeviceAddInfo.getModelName();
                if (TextUtils.isEmpty(modelName)) {
                    modelName = deviceModelName;
                }
                DeviceIntroductionInfo introductionInfos = deviceAddService.deviceLeadingInfo(modelName, TIME_OUT);
                mDeviceAddInfo.setDevIntroductionInfos(introductionInfos);
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS).sendToTarget();
            }
        };
    }

    @Override
    public void getDevIntroductionInfoCache(final String deviceModelName, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                String lan = ProviderManager.getAppProvider().getAppLanguage();
                String modelName = mDeviceAddInfo.getModelName();
                if (TextUtils.isEmpty(modelName)) {
                    modelName = deviceModelName;
                }
                DeviceIntroductionInfo introductionInfos = deviceAddService.introductionInfosGetCache(modelName);
                mDeviceAddInfo.setDevIntroductionInfos(introductionInfos);
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS).sendToTarget();
            }
        };
    }

    @Override
    public void initDev(final DEVICE_NET_INFO_EX device_net_info_ex, final String pwd, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                if (device_net_info_ex != null) {
                    final NET_IN_INIT_DEVICE_ACCOUNT in = new NET_IN_INIT_DEVICE_ACCOUNT();
                    in.byPwdResetWay = device_net_info_ex.byPwdResetWay;
                    in.byInitStatus = device_net_info_ex.byInitStatus;
                    System.arraycopy(device_net_info_ex.szMac, 0, in.szMac, 0, device_net_info_ex.szMac.length);
                    System.arraycopy(pwd.getBytes(), 0, in.szPwd, 0, pwd.getBytes().length);
                    System.arraycopy("admin".getBytes(), 0, in.szUserName, 0, "admin".getBytes().length);
                    NET_OUT_INIT_DEVICE_ACCOUNT out = new NET_OUT_INIT_DEVICE_ACCOUNT();

                    //5秒请求一次，请求3次
                    boolean init = false;
                    for (int i = 0; i < 3; i++) {
                        init = INetSDK.InitDevAccount(in, out, 5 * 1000, null);
                        int error = (INetSDK.GetLastError() & 0x7fffffff);

                        if (init) {
                            break;
                        } else {
                            LogUtil.debugLog("InitPresenter", "initDev:error:" + error);
                            if (error == NET_ERROR_DEVICE_ALREADY_INIT) {    // 设备已初始化
                                init = true;
                                break;
                            }
                        }
                    }
                    LogUtil.debugLog("InitPresenter", "init : " + init);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, init).sendToTarget();
                }
                handler.obtainMessage(HandleMessageCode.HMC_EXCEPTION).sendToTarget();
            }
        };
    }

    @Override
    public void initDevByIp(final DEVICE_NET_INFO_EX device_net_info_ex, final String pwd, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                NET_IN_INIT_DEVICE_ACCOUNT inInit = new NET_IN_INIT_DEVICE_ACCOUNT();
                String username = "admin";
                String deviceMacAdd = new String(device_net_info_ex.szMac).trim();
                byte pwdResetWay = device_net_info_ex.byPwdResetWay;
                String deviceIp = new String(device_net_info_ex.szIP).trim();
                String cellphone = "";
                String mail = "";

                System.arraycopy(deviceMacAdd.getBytes(), 0, inInit.szMac, 0, deviceMacAdd.getBytes().length);
                System.arraycopy(username.getBytes(), 0, inInit.szUserName, 0, username.getBytes().length);
                System.arraycopy(pwd.getBytes(), 0, inInit.szPwd, 0, pwd.getBytes().length);
                System.arraycopy(cellphone.getBytes(), 0, inInit.szCellPhone, 0, cellphone.getBytes().length);
                System.arraycopy(mail.getBytes(), 0, inInit.szMail, 0, mail.getBytes().length);

                inInit.byPwdResetWay = pwdResetWay; //设备搜索到的,必须与搜索到的一致

                NET_OUT_INIT_DEVICE_ACCOUNT outInit = new NET_OUT_INIT_DEVICE_ACCOUNT();
                boolean init = false;
                for (int i = 0; i < 2; i++) {
                    init = INetSDK.InitDevAccountByIP(inInit, outInit, 5000, null, deviceIp);
                    int error = (INetSDK.GetLastError() & 0x7fffffff);

                    if (init) {
                        break;
                    } else {
                        LogUtil.debugLog("InitPresenter", "initDev:error:" + error);
                        if (error == NET_ERROR_DEVICE_ALREADY_INIT) {    // 设备已初始化
                            init = true;
                            break;
                        }
                    }
                }
                LogUtil.debugLog("InitPresenter", "init : " + init);
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, init).sendToTarget();

            }
        };
    }

    @Override
    public void deviceIPLogin(final String ip, final String devPwd, /*final boolean useSafeModeLogin, */final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                long handle;
                handle = loginWithHighLevelSecurity(ip, devPwd);
                int ret = 0;
                if (handle == 0) {
                    ret = INetSDK.GetLastError();
                } else {
                    ret = FinalVar.NET_NOERROR;
                }
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, ret).sendToTarget();
                if (handle != 0) {
                    INetSDK.Logout(handle);
                }
            }
        };
    }

    private long ipLogin(String ip, String devPwd) {
        NET_DEVICEINFO_Ex net_deviceinfo_ex = new NET_DEVICEINFO_Ex();
        int cap = 20;
        //netsdk 使用局限，只能使用 类对象传递参数。
        Integer errorCode = new Integer(0);
        long handle = INetSDK.LoginEx2(ip, 37777, "admin", devPwd, cap, null, net_deviceinfo_ex, errorCode);
        return handle;
    }

    // 安全模式登陆
    private long loginWithHighLevelSecurity(String ip, String devPwd) {
        NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY stuIn = new NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
        System.arraycopy(ip.getBytes(), 0, stuIn.szIP, 0, ip.getBytes().length);
        stuIn.nPort = 37777;
        System.arraycopy("admin".getBytes(), 0, stuIn.szUserName, 0, "admin".getBytes().length);
        System.arraycopy(devPwd.getBytes(), 0, stuIn.szPassword, 0, devPwd.getBytes().length);
        stuIn.emSpecCap = EM_LOGIN_SPAC_CAP_TYPE.EM_LOGIN_SPEC_CAP_TCP;
        NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY stuOut = new NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
        long handle = INetSDK.LoginWithHighLevelSecurity(stuIn, stuOut);
        return handle;
    }

    @Override
    public void connectWifi4Sc(final String ip, final WlanInfo wlanInfo, final String wifiPwd, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                NET_IN_SET_DEV_WIFI stIn = new NET_IN_SET_DEV_WIFI();
                NET_OUT_SET_DEV_WIFI stOu = new NET_OUT_SET_DEV_WIFI();

                if (ip != null) {
                    System.arraycopy(ip.getBytes(), 0, stIn.szDevIP, 0, ip.getBytes().length);
                }
                stIn.nPort = 37777;

                String SSID = wlanInfo.getWlanSSID();
                System.arraycopy(SSID.getBytes(), 0, stIn.szSSID, 0, SSID.getBytes().length);

                int encryption = wlanInfo.getWlanEncry();
                stIn.nEncryption = encryption;
                LogUtil.debugLog("DeviceAddModel", "nEncryption : " + stIn.nEncryption);

                String szWPAKeys = wifiPwd;
                if (encryption >= 4 && encryption <= 12) {
                    System.arraycopy(szWPAKeys.getBytes(), 0, stIn.szWPAKeys, 0, szWPAKeys.getBytes().length);
                } else {
                    System.arraycopy(szWPAKeys.getBytes(), 0, stIn.szKeys[0], 0, szWPAKeys.getBytes().length);
                }

                stIn.nConnectedFlag = 1;
                boolean bRet = INetSDK.SetDevWifiInfo(stIn, stOu, 20000);
                if (bRet) {
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, true).sendToTarget();
                } else {
                    printError();
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, false).sendToTarget();
                }
            }
        };

    }

    @Override
    public void getSoftApWifiList4Sc(final String ip, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                NET_IN_GET_DEV_WIFI_LIST stIn = new NET_IN_GET_DEV_WIFI_LIST();
                NET_OUT_GET_DEV_WIFI_LIST stOut = new NET_OUT_GET_DEV_WIFI_LIST();

                stIn.nPort = 37777;
                if (ip != null) {
                    System.arraycopy(ip.getBytes(), 0, stIn.szDevIP, 0, ip.getBytes().length);
                }
                LogUtil.debugLog("DeviceAddModel", "ip : " + ip);
                boolean bRet = INetSDK.GetDevWifiListInfo(stIn, stOut, 20000);
                // 若失败则再请求一次
                if (!bRet) {
                    bRet = INetSDK.GetDevWifiListInfo(stIn, stOut, 10000);
                }

                if (bRet) {
                    int wlanNum = stOut.nWlanDevCount;
                    List<WlanInfo> listData = new ArrayList<>();
                    for (int i = 0; i < wlanNum; i++) {
                        int encry = UIUtils.getEncry4Sc(stOut.stuWlanDev[i].byAuthMode, stOut.stuWlanDev[i].byEncrAlgr);
                        String ssid = new String(stOut.stuWlanDev[i].szSSID).trim();
                        LogUtil.debugLog("DeviceAddModel", "ssid :" + ssid + " byAuthMode : " + stOut.stuWlanDev[i].byAuthMode + " byEncrAlgr : " + stOut.stuWlanDev[i].byEncrAlgr + " encry : " + encry);
                        if (TextUtils.isEmpty(ssid)) {
                            continue;
                        }
                        WlanInfo info = new WlanInfo();
                        info.setWlanQuality(stOut.stuWlanDev[i].nRSSIQuality + 100);//nRSSIQuality的范围[-100,0]
                        info.setWlanSSID(ssid);
                        info.setWlanEncry(encry);
                        info.setWlanAuthMode(stOut.stuWlanDev[i].byAuthMode);
                        info.setWlanEncrAlgr(stOut.stuWlanDev[i].byEncrAlgr);
                        listData.add(info);
                    }
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, listData).sendToTarget();
                } else {
                    printError();
                    handler.obtainMessage(HandleMessageCode.HMC_EXCEPTION).sendToTarget();
                }
            }
        };

    }

    @Override
    public void getSoftApWifiList(final String ip, final String devPwd, /*final boolean useSafeModeLogin, */final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                long loginHandle;
                loginHandle = loginWithHighLevelSecurity(ip, devPwd);
                if (loginHandle == 0) {
                    handler.obtainMessage(HandleMessageCode.HMC_BATCH_MIDDLE_RESULT/*, ret*/).sendToTarget();
                    return;
                }

                // 获取是否支持三代扫描能力
                SDK_PRODUCTION_DEFNITION sdk_production_defnition = new SDK_PRODUCTION_DEFNITION();
                boolean result = INetSDK.QueryProductionDefinition(loginHandle, sdk_production_defnition, 5000);
                LogUtil.debugLog("DeviceAddModel", "result : " + result);

                boolean isV3 = false;
                if (result) {
                    isV3 = sdk_production_defnition.emWlanScanAndConfig == EM_WLAN_SCAN_AND_CONFIG_TYPE.EM_WLAN_SCAN_AND_CONFIG_V3;
                }
                LogUtil.debugLog("DeviceAddModel", "isV3 : " + isV3);
                if (isV3) {
                    thirdScan(loginHandle, handler);
                } else {
                    secondScan(loginHandle, handler);
                }
                //使用结束登出,以防超出最大登录数
                if (loginHandle != 0) {
                    INetSDK.Logout(loginHandle);
                }
            }
        };

    }

    // 二代协议搜索
    private void secondScan(long handle, final Handler handler) {
        SDKDEV_WLAN_DEVICE_LIST_EX wlanList = new SDKDEV_WLAN_DEVICE_LIST_EX();
        Object[] outData = new Object[1];
        outData[0] = wlanList;
        //netsdk 使用局限，只能使用 类对象传递参数。
        Integer in = new Integer(-1);
        boolean ret = INetSDK.GetDevConfig(handle, FinalVar.SDK_DEV_WLAN_DEVICE_CFG_EX, 0, outData, in, 20 * 1000);

        if (ret) {
            int wlanNum = wlanList.bWlanDevCount;
            List<WlanInfo> listData = new ArrayList<>();
            for (int i = 0; i < wlanNum; i++) {
                int encry = UIUtils.getEncryV2(wlanList.lstWlanDev[i].byAuthMode, wlanList.lstWlanDev[i].byEncrAlgr);
                LogUtil.debugLog("DeviceAddModel", "byAuthMode : " + wlanList.lstWlanDev[i].byAuthMode + " byEncrAlgr : " + wlanList.lstWlanDev[i].byEncrAlgr + " encry : " + encry);
                String ssid = new String(wlanList.lstWlanDev[i].szSSID).trim();
                if (TextUtils.isEmpty(ssid)) {
                    continue;
                }
                WlanInfo info = new WlanInfo();
                info.setWlanQuality(wlanList.lstWlanDev[i].nRSSIQuality + 100);//nRSSIQuality的范围[-100,0]
                info.setWlanSSID(ssid);
                info.setWlanEncry(encry);
                info.setWlanAuthMode(wlanList.lstWlanDev[i].byAuthMode);
                info.setWlanEncrAlgr(wlanList.lstWlanDev[i].byEncrAlgr);
                listData.add(info);
            }
            handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, listData).sendToTarget();
        } else {

            handler.obtainMessage(HandleMessageCode.HMC_EXCEPTION).sendToTarget();
        }
    }

    // 三代扫描
    private void thirdScan(long handle, final Handler handler) {
        // 查询网络接口信息
        SDKDEV_NETINTERFACE_INFO[] stuNetInterface = new SDKDEV_NETINTERFACE_INFO[FinalVar.SDK_MAX_NETINTERFACE_NUM];
        for (int i = 0; i < stuNetInterface.length; ++i) {
            stuNetInterface[i] = new SDKDEV_NETINTERFACE_INFO();
        }
        Integer validCount = new Integer(0);
        boolean bRet = INetSDK.QueryDevStateEx(handle, FinalVar.SDK_DEVSTATE_NETINTERFACE, stuNetInterface, 5000, validCount);


        boolean hasWlan0 = false;
        if (bRet) {
            for (int i = 0; i < validCount.intValue(); i++) {
                String name = new String(stuNetInterface[i].szName).trim();
                LogUtil.debugLog("DeviceAddModel", "name:" + name);
                if ("wlan0".equalsIgnoreCase(name)) {
                    hasWlan0 = true;
                    break;
                }
            }
        }

        // 查询无线网络接入点信息, 目前支持的无线个数增加到128
        // 入参
        NET_IN_WLAN_ACCESSPOINT stIn = new NET_IN_WLAN_ACCESSPOINT();
        // 需要获取信息的无线网络名称,为空时搜索所有网络
        String ssid = "";
        System.arraycopy(ssid.getBytes(), 0, stIn.szSSID, 0, ssid.getBytes().length);

        //  网卡名称, 为空时, 默认为eth2
        String name = "";
        if (hasWlan0) {
            name = "wlan0";
        }
        System.arraycopy(name.getBytes(), 0, stIn.szName, 0, name.getBytes().length);

        // 出参
        NET_OUT_WLAN_ACCESSPOINT stOut = new NET_OUT_WLAN_ACCESSPOINT();

        boolean ret = INetSDK.QueryDevInfo(handle, FinalVar.NET_QUERY_WLAN_ACCESSPOINT, stIn, stOut, null, 20000);

        if (ret) {
            int wlanNum = stOut.nCount;
            List<WlanInfo> listData = new ArrayList<>();
            for (int i = 0; i < wlanNum; i++) {
                int encry = UIUtils.getEncry(stOut.stuInfo[i].nAuthMode, stOut.stuInfo[i].nEncrAlgr);
                String ssidName = new String(stOut.stuInfo[i].szSSID).trim();   // 无线网络名称
                LogUtil.debugLog("DeviceAddModel", "ssidName : " + ssidName + " nAuthMode : " + stOut.stuInfo[i].nAuthMode + " nEncrAlgr : " + stOut.stuInfo[i].nEncrAlgr + " encry : " + encry);
                if (TextUtils.isEmpty(ssidName)) {
                    continue;
                }
                WlanInfo info = new WlanInfo();
                info.setWlanQuality(stOut.stuInfo[i].nStrength);    // 信号强度(范围0-100)
                info.setWlanSSID(ssidName);
                info.setWlanEncry(encry);
                info.setWlanAuthMode(stOut.stuInfo[i].nAuthMode);   // 认证模式
                info.setWlanEncrAlgr(stOut.stuInfo[i].nEncrAlgr);   // 加密模式
                listData.add(info);
            }
            handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, listData).sendToTarget();
        } else {
            handler.obtainMessage(HandleMessageCode.HMC_EXCEPTION).sendToTarget();
        }
    }

    @Override
    public void connectWifi(final String ip, final String devPwd, final WlanInfo wlanInfo,
                            final String wifiPwd, /*final boolean useSafeModeLogin, */final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                boolean connectResult = false;
                final int BUFFERLEN = 1024 * 1024; //获取配置用到的缓冲区长度
                final int channelID = -1;
                final String strCommand = FinalVar.CFG_CMD_WLAN;
                CFG_NETAPP_WLAN stCfg = new CFG_NETAPP_WLAN();

                long handle;
                handle = loginWithHighLevelSecurity(ip, devPwd);
                if (DeviceAddHelper.getNewDevConfig(handle, channelID, strCommand, BUFFERLEN, stCfg, mDeviceAddInfo.getRequestId())) {
                    stCfg.stuWlanInfo[0].szSSID = new byte[FinalVar.CFG_MAX_SSID_LEN];
                    stCfg.stuWlanInfo[0].szKeys[0] = new byte[32];
                    //设置wifi名称
                    System.arraycopy(wlanInfo.getWlanSSID().getBytes(), 0, stCfg.stuWlanInfo[0].szSSID, 0, wlanInfo.getWlanSSID().getBytes().length);
                    //设置wifi密码
                    System.arraycopy(wifiPwd.getBytes(), 0, stCfg.stuWlanInfo[0].szKeys[0], 0, wifiPwd.getBytes().length);
                    //设置wlan加密方式
                    stCfg.stuWlanInfo[0].nEncryption = wlanInfo.getWlanEncry();
                    stCfg.stuWlanInfo[0].bEnable = true;
                    stCfg.stuWlanInfo[0].bConnectEnable = true;
                    stCfg.stuWlanInfo[0].nKeyID = 0;
                    stCfg.stuWlanInfo[0].bKeyFlag = false;
                    // 国内该字段需要使用true,注意不要合并进国内(凯波说的有一个设备必须用true)
                    stCfg.stuWlanInfo[0].bLinkEnable = false;

                    for (int i = 0; i < stCfg.stuWlanInfo.length; i++) {
                        LogUtil.debugLog("DeviceAddModel", "szWlanName : " + new String(stCfg.stuWlanInfo[i].szWlanName).trim());
                    }

                    //设置DNS
                    stCfg.stuWlanInfo[0].stuNetwork.szDnsServers[0] = new byte[FinalVar.AV_CFG_IP_Address_Len_EX];
                    stCfg.stuWlanInfo[0].stuNetwork.szDnsServers[1] = new byte[FinalVar.AV_CFG_IP_Address_Len_EX];
                    System.arraycopy("8.8.8.8".getBytes(), 0, stCfg.stuWlanInfo[0].stuNetwork.szDnsServers[0], 0, "8.8.8.8".getBytes().length);
                    System.arraycopy("8.8.4.4".getBytes(), 0, stCfg.stuWlanInfo[0].stuNetwork.szDnsServers[1], 0, "8.8.4.4".getBytes().length);
                    //netsdk 使用局限，只能使用 类对象传递参数。
                    Integer error = new Integer(0);
                    Integer restart = new Integer(0);
                    char szBuffer[] = new char[BUFFERLEN];
                    for (int i = 0; i < BUFFERLEN; i++) szBuffer[i] = 0;

                    boolean ret = INetSDK.PacketData(FinalVar.CFG_CMD_WLAN, stCfg, szBuffer, BUFFERLEN);

                    if (!ret) {
                        connectResult = false;
                        handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, connectResult).sendToTarget();
                        return;
                    }

                    ret = INetSDK.SetNewDevConfig(handle, FinalVar.CFG_CMD_WLAN, -1, szBuffer, BUFFERLEN, error, restart, 10 * 1000);

                    if (!ret) {
                        connectResult = false;
                        handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, connectResult).sendToTarget();
                        return;
                    }
                    connectResult = true;
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, connectResult).sendToTarget();
                    return;
                }
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, connectResult).sendToTarget();
            }
        };
    }

    @Override
    public void connectWifi4Hidden(final String ip, final String devPwd, final WlanInfo wlanInfo,
                                   final String wifiPwd, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                boolean connectResult = false;
                final int BUFFERLEN = 1024 * 1024; //获取配置用到的缓冲区长度
                final int channelID = -1;
                final String strCommand = FinalVar.CFG_CMD_WLAN;
                CFG_NETAPP_WLAN stCfg = new CFG_NETAPP_WLAN();

                long handle = loginWithHighLevelSecurity(ip, devPwd);

                // 获取是否支持三代扫描能力
                int wlanEncry = 12;
                SDK_PRODUCTION_DEFNITION sdk_production_defnition = new SDK_PRODUCTION_DEFNITION();
                boolean result = INetSDK.QueryProductionDefinition(handle, sdk_production_defnition, 5000);
                LogUtil.debugLog("DeviceAddModel", "result : " + result);

                boolean isV3 = false;
                if (result) {
                    isV3 = sdk_production_defnition.emWlanScanAndConfig == EM_WLAN_SCAN_AND_CONFIG_TYPE.EM_WLAN_SCAN_AND_CONFIG_V3;
                }
                LogUtil.debugLog("DeviceAddModel", "isV3 : " + isV3);
                if (isV3) {
                    // 支持三代
                    NET_IN_WLAN_ACCESSPOINT in = new NET_IN_WLAN_ACCESSPOINT();
                    System.arraycopy(wlanInfo.getWlanSSID().getBytes(), 0, in.szSSID, 0, wlanInfo.getWlanSSID().getBytes().length);

                    NET_OUT_WLAN_ACCESSPOINT out = new NET_OUT_WLAN_ACCESSPOINT();
                    Integer validCount = new Integer(0);
                    boolean isSucces = INetSDK.QueryDevInfo(handle, FinalVar.NET_QUERY_WLAN_ACCESSPOINT, in, out, validCount, 5000);
                    if (isSucces) {
                        LogUtil.debugLog("DeviceAddModel", "out.stuInfo : " + out.stuInfo);
                        if (out != null && out.stuInfo != null && out.stuInfo.length > 0) {
                            NET_WLAN_ACCESSPOINT_INFO stuInfo = out.stuInfo[0];
                            wlanEncry = UIUtils.getEncry(stuInfo.nAuthMode, stuInfo.nEncrAlgr);
                            LogUtil.debugLog("DeviceAddModel", "nAuthMode : " + stuInfo.nAuthMode + " nEncrAlgr: " + stuInfo.nEncrAlgr);
                        }
                    }
                }

                LogUtil.debugLog("DeviceAddModel", "wlanEncry : " + wlanEncry);
                if (DeviceAddHelper.getNewDevConfig(handle, channelID, strCommand, BUFFERLEN, stCfg, mDeviceAddInfo.getRequestId())) {
                    stCfg.stuWlanInfo[0].szSSID = new byte[FinalVar.CFG_MAX_SSID_LEN];
                    stCfg.stuWlanInfo[0].szKeys[0] = new byte[32];
                    //设置wifi名称
                    System.arraycopy(wlanInfo.getWlanSSID().getBytes(), 0, stCfg.stuWlanInfo[0].szSSID, 0, wlanInfo.getWlanSSID().getBytes().length);
                    //设置wifi密码
                    System.arraycopy(wifiPwd.getBytes(), 0, stCfg.stuWlanInfo[0].szKeys[0], 0, wifiPwd.getBytes().length);
                    //设置wlan加密方式
                    stCfg.stuWlanInfo[0].nEncryption = wlanEncry;
                    stCfg.stuWlanInfo[0].bEnable = true;
                    stCfg.stuWlanInfo[0].bConnectEnable = true;
                    stCfg.stuWlanInfo[0].nKeyID = 0;
                    stCfg.stuWlanInfo[0].bKeyFlag = false;
                    // 国内该字段需要使用true,注意不要合并进国内(凯波说的有一个设备必须用true)
                    stCfg.stuWlanInfo[0].bLinkEnable = false;

                    for (int i = 0; i < stCfg.stuWlanInfo.length; i++) {
                        LogUtil.debugLog("DeviceAddModel", "szWlanName : " + new String(stCfg.stuWlanInfo[i].szWlanName).trim());
                    }

                    //设置DNS
                    stCfg.stuWlanInfo[0].stuNetwork.szDnsServers[0] = new byte[FinalVar.AV_CFG_IP_Address_Len_EX];
                    stCfg.stuWlanInfo[0].stuNetwork.szDnsServers[1] = new byte[FinalVar.AV_CFG_IP_Address_Len_EX];
                    System.arraycopy("8.8.8.8".getBytes(), 0, stCfg.stuWlanInfo[0].stuNetwork.szDnsServers[0], 0, "8.8.8.8".getBytes().length);
                    System.arraycopy("8.8.4.4".getBytes(), 0, stCfg.stuWlanInfo[0].stuNetwork.szDnsServers[1], 0, "8.8.4.4".getBytes().length);
                    //netsdk 使用局限，只能使用 类对象传递参数。
                    Integer error = new Integer(0);
                    Integer restart = new Integer(0);
                    char szBuffer[] = new char[BUFFERLEN];
                    for (int i = 0; i < BUFFERLEN; i++) szBuffer[i] = 0;
                    boolean ret = INetSDK.PacketData(FinalVar.CFG_CMD_WLAN, stCfg, szBuffer, BUFFERLEN);
                    if (!ret) {
                        connectResult = false;
                        handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, connectResult).sendToTarget();
                        //使用结束登出,以防超出最大登录数
                        return;
                    }

                    ret = INetSDK.SetNewDevConfig(handle, FinalVar.CFG_CMD_WLAN, -1, szBuffer, BUFFERLEN, error, restart, 10 * 1000);
                    if (!ret) {
                        connectResult = false;
                        handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, connectResult).sendToTarget();
                        return;
                    }
                    connectResult = true;
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, connectResult).sendToTarget();
                    return;
                }
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, connectResult).sendToTarget();
            }
        };
    }

    @Override
    public void modifyDeviceName(final String deviceId, final String channelId, final String name, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                boolean isSucceed = deviceAddService.modifyDeviceName(deviceId, channelId, name, TIME_OUT);
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, isSucceed).sendToTarget();
            }
        };
    }

    @Override
    public void addApDevice(final String deviceId, final String apId, final String apType, final String apModel, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                //TODO 配件绑定需要实现接口，当前版本不支持

            }
        };
    }

    @Override
    public void modifyAPDevice(final String deviceId, final String apId, final String apName, final boolean toDevice, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                //TODO 配件绑定需要实现接口

            }
        };
    }

    @Override
    public void getAddApResultAsync(final String deviceId, final String apId, final Handler handler) {
        loop = true;
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                //TODO 配件绑定需要实现接口

            }
        };
    }


    @Override
    public void bindDevice(final String sn, final String code, final String deviceKey, final String imeiCode,
                           final String longitude, final String latitude, final String devUserName,
                           final String devPwd, final Handler handler) {
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                DeviceBindResult deviceBindResult = deviceAddService.userDeviceBind( sn, devPwd, DMS_TIMEOUT);
                mDeviceAddInfo.setDeviceDefaultName(deviceBindResult.getDeviceName());
                mDeviceAddInfo.setBindStatus(deviceBindResult.getBindStatus());
                mDeviceAddInfo.setBindAcount(deviceBindResult.getUserAccount());
                mDeviceAddInfo.setRecordSaveDays(deviceBindResult.getRecordSaveDays());
                mDeviceAddInfo.setStreamType(deviceBindResult.getStreamType());
                mDeviceAddInfo.setServiceTime(deviceBindResult.getServiceTime());
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS).sendToTarget();
            }
        };
    }

    //添加流程完成，需调用此方法，以释放相关资源
    public void recyle() {
        deviceAddModel.setLoop(false);
        mDeviceAddInfo = null;
        deviceAddModel = null;
    }
}
