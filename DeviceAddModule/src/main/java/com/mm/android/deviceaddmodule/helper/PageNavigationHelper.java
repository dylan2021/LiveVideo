package com.mm.android.deviceaddmodule.helper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.entity.WlanInfo;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.ProviderManager;
import com.mm.android.deviceaddmodule.mobilecommon.common.LCConfiguration;
import com.mm.android.deviceaddmodule.mobilecommon.entity.AddApResult;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;
import com.mm.android.deviceaddmodule.p_ap.ApBindSuccessFragment;
import com.mm.android.deviceaddmodule.p_ap.ApPairFragment;
import com.mm.android.deviceaddmodule.p_ap.GatewayListFragment;
import com.mm.android.deviceaddmodule.p_ap.TipApLightFragment;
import com.mm.android.deviceaddmodule.p_ap.TipApPowerFragment;
import com.mm.android.deviceaddmodule.p_ap.hubap.HubapGuide1Fragment;
import com.mm.android.deviceaddmodule.p_ap.hubap.HubapGuide2Fragment;
import com.mm.android.deviceaddmodule.p_ap.hubap.HubapGuide3Fragment;
import com.mm.android.deviceaddmodule.p_bindsuccess.BindSuccessFragment;
import com.mm.android.deviceaddmodule.p_cloudconnect.CloudConnectFragment;
import com.mm.android.deviceaddmodule.p_devicelocal.TipDeviceLocalFragment;
import com.mm.android.deviceaddmodule.p_devlogin.DevLoginFragment;
import com.mm.android.deviceaddmodule.p_devlogin.DevSecCodeFragment;
import com.mm.android.deviceaddmodule.p_errortip.ErrorTipFragment;
import com.mm.android.deviceaddmodule.p_init.InitFragment;
import com.mm.android.deviceaddmodule.p_init.SecurityCheckFragment;
import com.mm.android.deviceaddmodule.p_inputsn.DeviceDispatchFragment;
import com.mm.android.deviceaddmodule.p_inputsn.IMEIInputFragment;
import com.mm.android.deviceaddmodule.p_inputsn.ManualInputFragment;
import com.mm.android.deviceaddmodule.p_inputsn.ScanFragment;
import com.mm.android.deviceaddmodule.p_nb.TipNBFragment;
import com.mm.android.deviceaddmodule.p_offlineconfig.OfflineConfigFragment;
import com.mm.android.deviceaddmodule.p_softap.DevWifiListFragment;
import com.mm.android.deviceaddmodule.p_softap.HiddenWifiPwdFragment;
import com.mm.android.deviceaddmodule.p_softap.SoftApWifiPwdFragment;
import com.mm.android.deviceaddmodule.p_softap.TipSoftApConnectWifiFragment;
import com.mm.android.deviceaddmodule.p_softap.TipSoftApStep1Fragment;
import com.mm.android.deviceaddmodule.p_softap.TipSoftApStep2Fragment;
import com.mm.android.deviceaddmodule.p_softap.TipSoftApStep3Fragment;
import com.mm.android.deviceaddmodule.p_softap.TipSoftApStep4Fragment;
import com.mm.android.deviceaddmodule.p_softap.oversea.SoftApResultFragment;
import com.mm.android.deviceaddmodule.p_typechoose.TypeChooseFragment;
import com.mm.android.deviceaddmodule.p_wiredwireless.SmartConfigFragment;
import com.mm.android.deviceaddmodule.p_wiredwireless.TipLightFragment;
import com.mm.android.deviceaddmodule.p_wiredwireless.TipNetCablePluginFragment;
import com.mm.android.deviceaddmodule.p_wiredwireless.TipPowerFragment;
import com.mm.android.deviceaddmodule.p_wiredwireless.TipSameNetworkFragment;
import com.mm.android.deviceaddmodule.p_wiredwireless.TipSoundFragment;
import com.mm.android.deviceaddmodule.p_wiredwireless.TipWifiConnectFragment;
import com.mm.android.deviceaddmodule.p_wiredwireless.WifiPwdFragment;

/**
 * 设备添加页面跳转帮助类
 **/
public class PageNavigationHelper {
    public static final String TIP_POWER_FRAGMENT_TAG = "tip_power_fragment";
    public static final String WIFI_PWD_TAG = "wifi_pwd_fragment";
    public static final String SOFT_AP_TIP_TAG = "soft_ap_tip_fragment";
    public static final String AP_TIP_TAG = "ap_tip_fragment";
    public static final String SECURITY_CHECK_TAG = "security_check_fragment";

    private static void setFragmentAnimations(FragmentTransaction transaction, boolean anim) {
        if (anim) {
            transaction.setCustomAnimations(R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_left_back_in,
                    R.anim.slide_right_back_out);
        }
    }

    private static void setFragmentAnimations(FragmentTransaction transaction) {
        setFragmentAnimations(transaction, true);
    }

    /**
     * 跳转至二维码扫描页
     *
     * @param fragmentActivity
     */
    public static void gotoScanPage(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null || fragmentActivity.getSupportFragmentManager() == null) {
            return;
        }
        ScanFragment fragment = ScanFragment.newInstance();
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转至二维码扫描页
     *
     * @param fragmentActivity
     */
    public static void gotoDispatchPage(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null || fragmentActivity.getSupportFragmentManager() == null) {
            return;
        }
        DeviceDispatchFragment fragment = DeviceDispatchFragment.newInstance();
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转至手动输入序列号页
     *
     * @param from
     */
    public static void gotoManualInputPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        ManualInputFragment fragment = ManualInputFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转至输入imei页
     *
     * @param from
     */
    public static void gotoIMEIInputPage(Fragment from, boolean anim) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        IMEIInputFragment fragment = IMEIInputFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction, anim);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void gotoIMEIInputPage(Fragment from) {
        gotoIMEIInputPage(from, true);
    }

    /**
     * 跳转至设备类型选择页
     *
     * @param from
     */
    public static void gotoTypeChoosePage(Fragment from, boolean anim) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TypeChooseFragment fragment = TypeChooseFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction, anim);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void gotoTypeChoosePage(Fragment from) {
        gotoTypeChoosePage(from, true);
    }

    /**
     * 跳转至电源提示页
     *
     * @param from
     * @param isWirelessConfig
     */
    public static void gotoPowerTipPageNoAnim(Fragment from, boolean isWirelessConfig) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipPowerFragment fragment = TipPowerFragment.newInstance(isWirelessConfig);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(TIP_POWER_FRAGMENT_TAG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转至电源提示页
     *
     * @param from
     * @param isWirelessConfig
     */
    public static void gotoPowerTipPage(Fragment from, boolean isWirelessConfig) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipPowerFragment fragment = TipPowerFragment.newInstance(isWirelessConfig);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(TIP_POWER_FRAGMENT_TAG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转至网线提示页
     *
     * @param from
     */
    public static void gotoNetCableTipPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipNetCablePluginFragment fragment = TipNetCablePluginFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转至同一网络提示页
     *
     * @param from
     */
    public static void gotoSameNetworkTipPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipSameNetworkFragment fragment = TipSameNetworkFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转至wifi连接提示页
     *
     * @param from
     */
    public static void gotoWifiConnectTipPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipWifiConnectFragment fragment = TipWifiConnectFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转至wifi密码输入页
     *
     * @param parent 父fragment
     * @param from
     */
    public static void gotoWifiPwdPage(Fragment parent, Fragment from) {
        if (parent == null || parent.getFragmentManager() == null) {
            return;
        }
        WifiPwdFragment fragment = WifiPwdFragment.newInstance();
        FragmentTransaction transaction = parent.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);

        transaction.addToBackStack(WIFI_PWD_TAG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转设备灯光提示页
     *
     * @param from
     */
    public static void gotoLightTipPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipLightFragment fragment = TipLightFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转设备声音提示页
     *
     * @param from
     */
    public static void gotoSoundTipPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipSoundFragment fragment = TipSoundFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转设备声音提示页
     *
     * @param from
     */
    public static void gotoSmartConfigPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        SmartConfigFragment fragment = SmartConfigFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    /**
     * 跳转到安全检查页
     *
     * @param from
     */
    public static void gotoSecurityCheckPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        SecurityCheckFragment fragment = SecurityCheckFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(SECURITY_CHECK_TAG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到设备初始化页
     *
     * @param from
     */
    public static void gotoInitPage(Fragment from, DEVICE_NET_INFO_EX device_net_info_ex) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        InitFragment fragment = InitFragment.newInstance(device_net_info_ex);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到云平台连接页
     *
     * @param from
     */
    public static void gotoCloudConnectPage(Fragment from, boolean anim) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        CloudConnectFragment fragment = CloudConnectFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction, anim);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
    }

    public static void gotoCloudConnectPage(Fragment from) {
        gotoCloudConnectPage(from, true);
    }

    /**
     * 跳转到设备登录页
     *
     * @param from
     */
    public static void gotoDevLoginPage(Fragment from, boolean anim) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        DevLoginFragment fragment = DevLoginFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction, anim);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
        //离线进入的都认为走过初始化检查流程了，都默认上报
        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
    }

    public static void gotoDevLoginPage(Fragment from) {
        gotoDevLoginPage(from, true);
    }

    /**
     * 跳转到设备安全码验证页
     *
     * @param from
     */
    public static void gotoDevSecCodePage(Fragment from, boolean anim) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        DevSecCodeFragment fragment = DevSecCodeFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction, anim);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void gotoDevSecCodePage(Fragment from) {
        gotoDevSecCodePage(from, true);
    }

    /**
     * 跳转到绑定设备连接页
     *
     * @param from
     */
    public static void gotoDeviceBindPage(Fragment from, boolean anim) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        CloudConnectFragment fragment = CloudConnectFragment.newInstance(true);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction, anim);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void gotoDeviceBindPage(Fragment from) {
        gotoDeviceBindPage(from, true);
    }

    /**
     * 跳转到绑定成功提示页
     *
     * @param from
     */
    public static void gotoBindSuccessPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        BindSuccessFragment fragment = BindSuccessFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
    }

    /**
     *
     *
     * @param from
     */
    public static void gotoErrorTipPage(Fragment from, int errorCode, boolean anim) {
        ErrorTipFragment fragment = ErrorTipFragment.newInstance(errorCode);
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction, anim);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void gotoErrorTipPage(Fragment from, int errorCode) {
        gotoErrorTipPage(from, errorCode, true);
    }

    /**
     * 跳转到到软Ap添加引导页
     *
     * @param from
     */
    public static void gotoSoftApTipPage(Fragment from) {
        gotoSoftApTip1Page(from);
    }

    public static void gotoSoftApTip1Page(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipSoftApStep1Fragment fragment = TipSoftApStep1Fragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(SOFT_AP_TIP_TAG);
        transaction.commitAllowingStateLoss();

    }

    public static void gotoSoftApTipPageNoAnim(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipSoftApStep1Fragment fragment = TipSoftApStep1Fragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment, TipSoftApStep1Fragment.class.getName());
        transaction.addToBackStack(SOFT_AP_TIP_TAG);
        transaction.commitAllowingStateLoss();

    }

    public static void gotoSoftApTip2Page(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipSoftApStep2Fragment fragment = TipSoftApStep2Fragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }

    public static void gotoSoftApTip3Page(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipSoftApStep3Fragment fragment = TipSoftApStep3Fragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }

    public static void gotoSoftApTip4Page(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipSoftApStep4Fragment fragment = TipSoftApStep4Fragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void gotoSoftApTipConnectWifiPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipSoftApConnectWifiFragment fragment = TipSoftApConnectWifiFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到软Ap添加wifi列表
     *
     * @param from
     */
    public static void gotoSoftApWifiListPage(Fragment from, boolean isNotNeedLogin) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }

        DevWifiListFragment fragment = DevWifiListFragment.newInstance(isNotNeedLogin);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到软Ap添加wifi列表
     *
     * @param from
     */
    public static void gotoSoftApWifiListPage(Fragment from) {
        gotoSoftApWifiListPage(from, false);
    }

    /**
     * 跳转到到软Ap添加wifi密码输入页
     *
     * @param from
     */
    public static void gotoSoftApWifiPwdPage(Fragment from, WlanInfo wlanInfo, boolean isNotNeedLogin) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        SoftApWifiPwdFragment fragment = SoftApWifiPwdFragment.newInstance(wlanInfo, isNotNeedLogin);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到软Ap添加wifi密码输入页
     *
     * @param from
     */
    public static void gotoHiddenWifiPwdPage(Fragment from, boolean isNotNeedLogin) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        HiddenWifiPwdFragment fragment = HiddenWifiPwdFragment.newInstance(isNotNeedLogin);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到软Ap添加wifi连接结果页（海外）
     *
     * @param from
     */
    public static void gotoSoftApResultdPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        SoftApResultFragment fragment = SoftApResultFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到配件添加网关列表页
     *
     * @param from
     */
    public static void gotoGatewayListPage(Fragment from, boolean hasSelectGateway) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        GatewayListFragment fragment = GatewayListFragment.newInstance(hasSelectGateway);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到配件电源引导页
     *
     * @param from
     */
    public static void gotoApPowerTipPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipApPowerFragment fragment = TipApPowerFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(AP_TIP_TAG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到配件电源引导页
     *
     * @param from
     */
    public static void gotoApLightPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipApLightFragment fragment = TipApLightFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到配件配对页
     *
     * @param from
     */
    public static void gotoApPairPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        ApPairFragment fragment = ApPairFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到到配件绑定成功页
     *
     * @param from
     */
    public static void gotoApBindSuccessPage(Fragment from, AddApResult addApResult) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        ApBindSuccessFragment fragment = ApBindSuccessFragment.newInstance(addApResult);
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到电池相机配对引导页
     *
     * @param fragmentActivity
     */
    public static void gotoHubGuide1Page(FragmentActivity fragmentActivity, String sn, String hubType) {
        if (fragmentActivity == null || fragmentActivity.getSupportFragmentManager() == null) {
            return;
        }
        HubapGuide1Fragment fragment = HubapGuide1Fragment.newInstance(sn, hubType);
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到电池相机配对引导页
     *
     * @param from
     */
    public static void gotoHubGuide2Page(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        HubapGuide2Fragment fragment = HubapGuide2Fragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到hub灯提示页
     *
     * @param from
     */
    public static void gotoHubGuide3Page(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        HubapGuide3Fragment fragment = HubapGuide3Fragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到NB设备添加引导页
     *
     * @param from
     */
    public static void gotoNBTipPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipNBFragment fragment = TipNBFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到离线配网
     *
     * @param fragmentActivity
     * @param sn
     * @param devModelName
     */
    public static void gotoOfflineConfigPage(FragmentActivity fragmentActivity, String sn, String devModelName, String imei) {
        if (fragmentActivity == null || fragmentActivity.getSupportFragmentManager() == null) {
            return;
        }
        OfflineConfigFragment fragment = OfflineConfigFragment.newInstance(sn, devModelName, imei);
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void gotoDeviceSharePage(FragmentActivity fragmentActivity, String deviceSn) {
        Bundle bundle = new Bundle();
        bundle.putString(LCConfiguration.Device_ID, deviceSn);
        ProviderManager.getAppProvider().goDeviceSharePage(fragmentActivity, bundle);
    }

    /**
     * 跳转到设备本地配网引导页面
     *
     * @param from
     */
    public static void gotoLocationTipPage(Fragment from) {
        if (from == null || from.getFragmentManager() == null) {
            return;
        }
        TipDeviceLocalFragment fragment = TipDeviceLocalFragment.newInstance();
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        setFragmentAnimations(transaction);
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 跳转到蓝牙模块
     */
    public static void gotoAddBleLockPage(Bundle bundle) {
        //TODO 不会有逻辑进入
    }
}
