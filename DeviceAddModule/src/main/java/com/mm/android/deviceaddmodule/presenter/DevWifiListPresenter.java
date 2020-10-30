package com.mm.android.deviceaddmodule.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.dahua.mobile.utility.network.DHWifiUtil;
import com.mm.android.deviceaddmodule.contract.DevWifiListConstract;
import com.mm.android.deviceaddmodule.entity.WlanInfo;
import com.mm.android.deviceaddmodule.event.DeviceAddEvent;
import com.mm.android.deviceaddmodule.mobilecommon.base.LCBusinessHandler;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DevWifiListPresenter implements DevWifiListConstract.Presenter {
    WeakReference<DevWifiListConstract.View> mView;
    private List<WlanInfo> mListData;
    DHWifiUtil mDHWifiUtil;
    String mDeviceSn;
    private boolean mIsNotNeedLogin;
    private boolean isSupport5G;

    public DevWifiListPresenter(DevWifiListConstract.View view, boolean isNotNeedLogin) {
        mView = new WeakReference<>(view);
        mDHWifiUtil=new DHWifiUtil(mView.get().getContextInfo().getApplicationContext());
        mDeviceSn=DeviceAddModel.newInstance().getDeviceInfoCache().getDeviceSn();
        mListData=new ArrayList<>();
        mIsNotNeedLogin = isNotNeedLogin;
        String wifiMode = DeviceAddModel.newInstance().getDeviceInfoCache().getWifiTransferMode();
        if (!TextUtils.isEmpty(wifiMode)) {
            isSupport5G = wifiMode.toUpperCase().contains("5GHZ");
        }
        getWifiList();
    }

    @Override
    public boolean isDevSupport5G() {
        return isSupport5G;
    }

    @Override
    public void getWifiList(){
        mView.get().showProgressDialog();
        EventBus.getDefault().post(new DeviceAddEvent(DeviceAddEvent.SOFTAP_REFRSH_WIFI_LIST_DISABLE_ACTION));
        mListData.clear();
        String gatwayip= mDHWifiUtil.getGatewayIp();
        final DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
        String pwd = deviceAddInfo.getDevicePwd();

        LCBusinessHandler getWifiListHandler=new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if(mView.get()!=null&&mView.get().isViewActive()){
                    mView.get().cancelProgressDialog();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new DeviceAddEvent(DeviceAddEvent.SOFTAP_REFRSH_WIFI_LIST_ENABLE_ACTION));
                        }
                    }, 500);

                    if(msg.what== HandleMessageCode.HMC_SUCCESS){
                        dispatchListResult(msg);
                    } else if(msg.what== HandleMessageCode.HMC_BATCH_MIDDLE_RESULT) { // 登陆失败
                            mView.get().goDevLoginPage();
                    } else {
                        mView.get().showErrorInfoView();
                    }
                }
            }
        };

        if(mIsNotNeedLogin) {
            DeviceAddModel.newInstance().getSoftApWifiList4Sc(gatwayip, getWifiListHandler);
        } else {
            DeviceAddModel.newInstance().getSoftApWifiList(gatwayip, pwd, /*type == DHDeviceExtra.DeviceLoginType.SafeMode.ordinal(), */getWifiListHandler);
        }
    }

    private void dispatchListResult(Message msg){
        mView.get().showListView();
        mListData= (List<WlanInfo>) msg.obj;
        mView.get().updateWifiList(mListData);
    }
}
