package com.mm.android.deviceaddmodule;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.company.NetSDK.CB_fSearchDevicesCB;
import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.company.NetSDK.INetSDK;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.ProviderManager;
import com.mm.android.deviceaddmodule.mobilecommon.common.LCConfiguration;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;

import java.util.concurrent.CopyOnWriteArrayList;

public class SeachDeviceService extends Service {
    public static final int SEND_START = 1;
    public static final String TAG = "SeachDeviceService";

    private long mLRet;

    private CopyOnWriteArrayList<SearchDeviceManager.ISearchDeviceListener> mListenerList;
    long lastReceiveTime = 0;
    private BroadcastReceiver mReceiver;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!SearchDeviceManager.getInstance().mIsExist) return;
            if (System.currentTimeMillis() - lastReceiveTime > 2000) {
                handler.removeMessages(SEND_START);
                handler.removeCallbacks(searchRunnable);
                handler.postDelayed(searchRunnable, 500);           //若2S内没有再搜索到设备，则重新开始搜索
            } else {
                handler.removeMessages(SEND_START);
                handler.sendEmptyMessageDelayed(SEND_START, 500);
            }
        }
    };
    Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            SearchDeviceManager.getInstance().removeInvalidDevice();
            if (mLRet != 0) {
                stopSearchDevices();
            }
            mLRet = INetSDK.StartSearchDevices(new CB_fSearchDevicesCB() {
                @Override
                public void invoke(DEVICE_NET_INFO_EX device_net_info_ex) {
                    lastReceiveTime = System.currentTimeMillis();
                    if (device_net_info_ex != null) {
                        String szSerialNo = new String(device_net_info_ex.szSerialNo).trim();
                        if (device_net_info_ex.iIPVersion == 4 && mListenerList != null) {
                            for (int i = 0; i < mListenerList.size(); i++) {
                                SearchDeviceManager.ISearchDeviceListener listener = mListenerList.get(i);
                                if (listener != null) {
                                    listener.onDeviceSearched(szSerialNo, device_net_info_ex);
                                }
                            }
                        }
                    }
                }
            });
            if (System.currentTimeMillis() - lastReceiveTime > 500) {
                handler.removeMessages(SEND_START);
                handler.removeCallbacks(searchRunnable);
                handler.postDelayed(searchRunnable, 2000);           //若2S内没有再搜索到设备，则重新开始搜索
            } else {
                handler.removeMessages(SEND_START);
                handler.sendEmptyMessageDelayed(SEND_START, 500);
            }
            if (mLRet == 0) {
                DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mListenerList = new CopyOnWriteArrayList<>();
        // 注册广播监听器监听网络变化
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LCConfiguration.CONNECTIVITY_CHAGET_ACTION);
        mReceiver = new Broadcast();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SearchDeviceBinder();
    }

    /**
     * 局域网内搜索设备信息
     */
    private void startSearchDevices() {
        handler.removeCallbacks(searchRunnable);
        handler.post(searchRunnable);
    }

    /**
     * 停止搜索
     */
    private void stopSearchDevicesAsync() {
        handler.removeCallbacks(searchRunnable);
        if (mLRet != 0) {
            ProviderManager.getDeviceAddProvider().stopSearchDevicesAsync(mLRet, null);
        }
    }

    private void stopSearchDevices() {
        handler.removeCallbacks(searchRunnable);
        if (mLRet != 0) {
            DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
            ProviderManager.getDeviceAddProvider().stopSearchDevices(mLRet, deviceAddInfo.getRequestId());
        }
    }

    private void registerListener(SearchDeviceManager.ISearchDeviceListener listener) {
        if (mListenerList != null && !mListenerList.contains(listener)) {
            mListenerList.add(listener);
        }
    }

    private void unRegisterListener(SearchDeviceManager.ISearchDeviceListener listener) {
        if (mListenerList != null && mListenerList.contains(listener)) {
            mListenerList.remove(listener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        stopSearchDevicesAsync();
        if (mListenerList != null) {
            mListenerList.clear();
        }
        mListenerList = null;
    }

    public class SearchDeviceBinder extends Binder {

        public void startSearchDevices() {
            SeachDeviceService.this.startSearchDevices();
        }

        public void stopSearchDevicesAsync() {
            SeachDeviceService.this.stopSearchDevicesAsync();
        }

        public void stopSearchDevices() {
            SeachDeviceService.this.stopSearchDevices();
        }

        public void registerListener(SearchDeviceManager.ISearchDeviceListener listener) {
            SeachDeviceService.this.registerListener(listener);
        }

        public void unRegisterListener(SearchDeviceManager.ISearchDeviceListener listener) {
            SeachDeviceService.this.unRegisterListener(listener);
        }
    }

    private class Broadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LCConfiguration.CONNECTIVITY_CHAGET_ACTION.equals(intent.getAction())) {
                SearchDeviceManager.getInstance().clearDevice();
                startSearchDevices();
            }
        }
    }
}
