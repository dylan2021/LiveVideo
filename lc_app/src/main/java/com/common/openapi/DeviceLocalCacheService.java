package com.common.openapi;

import android.os.Message;

import com.common.openapi.entity.DeviceLocalCacheData;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessRunnable;
import com.mm.android.deviceaddmodule.mobilecommon.base.LCBusinessHandler;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.BusinessErrorTip;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;

public class DeviceLocalCacheService {

    /**
     * 添加设备缓存信息
     * @param localCacheData
     */
    public void addLocalCache(final DeviceLocalCacheData localCacheData) {
        new BusinessRunnable(null) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    DeviceLocalCacheManager deviceLocalCacheManager = ClassInstanceManager.newInstance().getDeviceLocalCacheManager();
                    deviceLocalCacheManager.addLocalCache(localCacheData);
                } catch (Throwable e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 获取设备缓存信息
     * @param localCacheData
     * @param deviceCacheCallBack
     */
    public void findLocalCache(final DeviceLocalCacheData localCacheData, final IGetDeviceInfoCallBack.IDeviceCacheCallBack deviceCacheCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (deviceCacheCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    deviceCacheCallBack.deviceCache((DeviceLocalCacheData) msg.obj);
                } else {
                    //失败
                    deviceCacheCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    DeviceLocalCacheData localCache = ClassInstanceManager.newInstance().getDeviceLocalCacheManager().findLocalCache(localCacheData);
                    if (localCache != null) {
                        handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, localCache).sendToTarget();
                    } else {
                        throw new BusinessException("null point");
                    }
                } catch (Throwable e) {
                    throw e;
                }
            }
        };
    }
}
