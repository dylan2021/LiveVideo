package com.mm.android.deviceaddmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.lechange.opensdk.api.LCOpenSDK_Api;
import com.lechange.opensdk.device.LCOpenSDK_DeviceInit;
import com.mm.android.deviceaddmodule.device_wifi.CurWifiInfo;
import com.mm.android.deviceaddmodule.device_wifi.DeviceWifiListActivity;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.EnvironmentConfig;
import com.mm.android.deviceaddmodule.mobilecommon.entity.device.DHDevice;
import com.mm.android.deviceaddmodule.openapi.CONST;
import com.mm.android.deviceaddmodule.openapi.DeviceAddOpenApiManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zxing.ContextHelper;

import static com.mm.android.deviceaddmodule.device_wifi.DeviceConstant.IntentCode.DEVICE_SETTING_WIFI_LIST;

/**
 * 添加设备组件 唯一入口
 */
public class LCDeviceEngine {
    private boolean sdkHasInit = false;
    private volatile static LCDeviceEngine lcDeviceEngine;
    public String accessToken;
    private Throwable throwable;
    public CommonParam commonParam;
    public String userId = "";

    public static LCDeviceEngine newInstance() {
        if (lcDeviceEngine == null) {
            synchronized (LCDeviceEngine.class) {
                if (lcDeviceEngine == null) {
                    lcDeviceEngine = new LCDeviceEngine();
                }
            }
        }
        return lcDeviceEngine;
    }

    public boolean init(CommonParam commonParam) throws Throwable {
        this.commonParam = commonParam;
        this.accessToken = "";
        this.userId = "";
        this.sdkHasInit = false;
        if (commonParam == null) {
            throw new Exception("commonParam must not null");
        }
        //参数校验
        commonParam.checkParam();
        //初始化参数
        initParam(commonParam);
        //获取开放平台token
        initToken();
        if (TextUtils.isEmpty(accessToken)) {
            throw throwable;
        }
        //组件初始化
        LCOpenSDK_Api.setHost(CONST.HOST.replace("https://", ""), accessToken);
        LCOpenSDK_DeviceInit.getInstance();
        sdkHasInit = true;
        return true;
    }

    public void addDevice(Activity activity) throws Exception {
        if (!sdkHasInit) {
            throw new Exception("not init");
        }
        //开启添加页面
        activity.startActivity(new Intent(commonParam.getContext(), DeviceAddActivity.class));
    }

    public boolean deviceOnlineChangeNet(Activity activity,DHDevice device, CurWifiInfo wifiInfo)  {
        if (!sdkHasInit) {
            return false;
        }
        //开启设备在线配网
        Intent intent = new Intent(commonParam.getContext(), DeviceWifiListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DHDEVICE_INFO", device);
        bundle.putSerializable("DEVICE_CURRENT_WIFI_INFO", wifiInfo);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, DEVICE_SETTING_WIFI_LIST);
        return true;
    }

    private String initToken() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    accessToken = DeviceAddOpenApiManager.getToken();
                } catch (BusinessException e) {
                    throwable = new Throwable(e.errorDescription);
                }
            }
        });
        try {
            thread.start();
            thread.join(4000);
        } catch (InterruptedException e) {
            throwable = e;
        }
        return accessToken;
    }

    private void initParam(CommonParam commonParam) throws Exception {
        userId = TextUtils.isEmpty(commonParam.getUserId()) ? commonParam.getAppId() : commonParam.getUserId();
        //传入参数 AppId SecretKey  环境切换
        CONST.makeEnv(commonParam.getEnvirment(), commonParam.getAppId(), commonParam.getAppSecret());
        ContextHelper.init(commonParam.getContext());
        new EnvironmentConfig.Builder().setContext(commonParam.getContext()).build();
        initImageLoader(commonParam.getContext());
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache((int) (Runtime.getRuntime().maxMemory() / 16)))
                .memoryCacheExtraOptions(600, 400)
                .diskCacheExtraOptions(600, 400, null).diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .imageDownloader(new BaseImageDownloader(context))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

}
