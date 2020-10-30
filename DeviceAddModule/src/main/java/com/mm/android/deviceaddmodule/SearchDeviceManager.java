package com.mm.android.deviceaddmodule;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import com.company.NetSDK.CB_fSDKLogCallBack;
import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.entity.DeviceNetInfo;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.ProviderManager;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 搜索局域网内设备信息的管理类
 */

public class SearchDeviceManager {
    private static final String TAG = "SeachDeviceService";

    private static volatile SearchDeviceManager sInstance;
    private volatile ConcurrentHashMap<String, DeviceNetInfo> mDeviceNetInfos = new ConcurrentHashMap<>();
    private SeachDeviceService.SearchDeviceBinder searchDevice;
    private ISearchDeviceListener mListener;
    boolean mIsConnected;               //service是否已连接
    private LogCallBack mLogCallBack;


    private SearchDeviceManager() {
        mListener = new SearchDeviceImpl();
        mDeviceNetInfos = new ConcurrentHashMap<>();
        mLogCallBack = new LogCallBack();
    }

    public static SearchDeviceManager getInstance() {
        if (sInstance == null) {
            synchronized (SearchDeviceManager.class) {
                if (sInstance == null) {
                    sInstance = new SearchDeviceManager();

                }
            }
        }
        return sInstance;
    }

    public synchronized void connnectService() {

        if(!mIsConnected){
            Intent intent = new Intent(ProviderManager.getAppProvider().getAppContext(), SeachDeviceService.class);
            mIsConnected = ProviderManager.getAppProvider().getAppContext().bindService(intent, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        }
        if(!mIsExist)mIsExist = true;
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            LogUtil.debugLog(TAG, "onServiceConnected");

            searchDevice = (SeachDeviceService.SearchDeviceBinder) arg1;

            if (searchDevice != null) {
                try {
                    searchDevice.linkToDeath(mBinderPoolDeathRecipient, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            registerListener(mListener);
            startSearch();
        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {

        @Override
        public void binderDied() {
            LogUtil.debugLog(TAG, "binderDied");
            if (searchDevice != null) {
                searchDevice.unlinkToDeath(mBinderPoolDeathRecipient, 0);
                searchDevice = null;
            }

            connnectService();
        }
    };

    public void registerListener(ISearchDeviceListener listener) {
        if (searchDevice != null) {
            searchDevice.registerListener(listener);
        }
    }

    public void unRegisterListener(ISearchDeviceListener listener) {
        if (searchDevice != null) {
            searchDevice.unRegisterListener(listener);
        }
    }
    private void stopSearch() {
        if (searchDevice != null) {
            searchDevice.stopSearchDevicesAsync();
            if(mIsConnected){
                ProviderManager.getAppProvider().getAppContext().unbindService(mBinderPoolConnection);
                mIsConnected = false;
            }

        }
    }

    public synchronized DEVICE_NET_INFO_EX getDeviceNetInfo(String snCode) {
        if (TextUtils.isEmpty(snCode))
            return null;
        if (mDeviceNetInfos != null&&mDeviceNetInfos.get(snCode)!=null) {
            return mDeviceNetInfos.get(snCode).getDevNetInfoEx();
        }
        return null;
    }

    /**
     * 搜索
     */
    public synchronized void startSearch() {
        removeInvalidDevice();

        if (searchDevice != null) {
            connnectService();
            searchDevice.startSearchDevices();

        }
    }

    public synchronized void clearDevice() {
        if (mDeviceNetInfos != null) {
            mDeviceNetInfos.clear();
            LogUtil.debugLog(TAG, "clear");
        }
    }

    /**
     * 列表中移除无效的设备信息
     */
    public synchronized void removeInvalidDevice() {
        if (mDeviceNetInfos != null) {
            LogUtil.debugLog(TAG, "removeInvalidDevice： " + mDeviceNetInfos);
            for (Map.Entry<String, DeviceNetInfo> entry : mDeviceNetInfos.entrySet()) {
                if (entry.getValue() != null) {
                    if (!entry.getValue().isValid()) {
                        // 移除无效的DeviceNetInfo
                        mDeviceNetInfos.remove(entry.getKey());
                        LogUtil.debugLog(TAG, "remove： " + entry.getKey());
                    } else {
                        // 将标志位重置
                        entry.getValue().setValid(false);
                    }
                }
            }
            LogUtil.debugLog(TAG, "removeInvalidDevice： " + mDeviceNetInfos);
        }
    }
    volatile  boolean  mIsExist = false;
    /**
     * 释放资源
     */
    public synchronized void checkSearchDeviceServiceDestory() {
        stopSearch();
        unRegisterListener(mListener);
        clearDevice();
        mIsExist = false;
    }

    public  synchronized boolean checkSearchDeviceServiceIsExist() {
        return mIsExist;
    }

    private class SearchDeviceImpl implements ISearchDeviceListener {

        @Override
        public void onDeviceSearched(String sncode, DEVICE_NET_INFO_EX info) {
            if (mDeviceNetInfos != null) {
                DeviceNetInfo deviceNetInfo=new DeviceNetInfo(info);
                mDeviceNetInfos.put(sncode, deviceNetInfo);
                LogUtil.debugLog(TAG, "onDeviceSearched： " + mDeviceNetInfos);
            }
        }
    }

    public interface ISearchDeviceListener {
        void onDeviceSearched(String sncode, DEVICE_NET_INFO_EX info);
    }

    private class LogCallBack implements CB_fSDKLogCallBack{

        @Override
        public int invoke(byte[] bytes, int length) {
            String netSDKLog = new String(bytes).trim();
            LogUtil.debugLog(TAG, netSDKLog);

            String type = "";
            String content = "";
            try {
                JSONObject jsonObject = new JSONObject(netSDKLog);
                type = jsonObject.optString("type");
                content = jsonObject.optString("log");
                LogUtil.debugLog(TAG, "type : " + type + " content : " + content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
