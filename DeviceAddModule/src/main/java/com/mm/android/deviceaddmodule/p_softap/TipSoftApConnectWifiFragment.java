package com.mm.android.deviceaddmodule.p_softap;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.base.BaseDevAddFragment;
import com.mm.android.deviceaddmodule.contract.TipSoftApConnectWifiConstract;
import com.mm.android.deviceaddmodule.helper.DeviceAddHelper;
import com.mm.android.deviceaddmodule.helper.PageNavigationHelper;
import com.mm.android.deviceaddmodule.helper.Utils4AddDevice;
import com.mm.android.deviceaddmodule.mobilecommon.common.LCConfiguration;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.mobilecommon.eventbus.event.CloseTimeFilterEvent;
import com.mm.android.deviceaddmodule.mobilecommon.eventbus.event.NoticeToBackEvent;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;
import com.mm.android.deviceaddmodule.presenter.TipSoftApConnectWifiPresenter;
import com.mm.android.deviceaddmodule.services.TimeFilterService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 软AP添加引导提示页-自动连接wifi
 */
public class TipSoftApConnectWifiFragment extends BaseDevAddFragment implements TipSoftApConnectWifiConstract.View {
    TipSoftApConnectWifiConstract.Presenter mPresenter;
    TextView mShowTipTv, mWifiNameTv, mWifiPwdTv,mShowTip1Tv;
    TextView mGotoWifiSetting, mAboutWifiPwdTv;
    LinearLayout mWifiPwdLayout;
    ImageView mCopyIv;
    private boolean mIsBack;  //是否需要返回到上一页（主要是应用在长时间未连接热点，点击通知栏需要返回到上一页）

    public static TipSoftApConnectWifiFragment newInstance() {
        TipSoftApConnectWifiFragment fragment = new TipSoftApConnectWifiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NoticeToBackEvent event) {
        mIsBack = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tip_soft_ap_connect_wifi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        mShowTipTv = view.findViewById(R.id.tv_image_show_tip);
        mShowTipTv.setText(R.string.add_device_wait_to_connect_wifi);
        mShowTip1Tv = view.findViewById(R.id.tv_show_tip);
        mWifiNameTv = view.findViewById(R.id.tv_wifi_name);

        mWifiPwdLayout = view.findViewById(R.id.layout_wifi_pwd);
        mWifiPwdTv = view.findViewById(R.id.tv_wifi_pwd);
        mWifiPwdTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        mWifiPwdTv.getPaint().setAntiAlias(true);//抗锯齿
        mWifiPwdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.copyWifiPwd();
            }
        });

        mCopyIv = view.findViewById(R.id.iv_copy);
        mCopyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.copyWifiPwd();
            }
        });

        mGotoWifiSetting = (TextView)view.findViewById(R.id.tv_goto_connect);
        mGotoWifiSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消之前的定时任务
                EventBus.getDefault().post(new CloseTimeFilterEvent());
                Intent intent = new Intent(getActivity(), TimeFilterService.class);
                intent.putExtra(LCConfiguration.SSID, mPresenter.getHotSSID());
                getActivity().startService(intent);
                //  CommonHelper.gotoWifiSetting(getActivity());
                Intent wifiIntent  = new Intent();
                wifiIntent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY|intent.FLAG_ACTIVITY_NEW_TASK);
                wifiIntent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                startActivity(wifiIntent);
            }
        });
        mAboutWifiPwdTv = (TextView)view.findViewById(R.id.tv_about_wifi_pwd);
        mAboutWifiPwdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageNavigationHelper.gotoErrorTipPage(TipSoftApConnectWifiFragment.this, DeviceAddHelper.ErrorCode.COMMON_ERROR_ABOUT_WIFI_PWD);
            }
        });
    }

    protected void initData(){
        mPresenter = new TipSoftApConnectWifiPresenter(this);
        mPresenter.connectWifiAction(true);

        DeviceAddInfo deviceAddInfo = DeviceAddModel.newInstance().getDeviceInfoCache();
        if (deviceAddInfo.getConfigMode().contains(DeviceAddInfo.ConfigMode.LAN.name())) {
            DeviceAddHelper.updateTile(DeviceAddHelper.TitleMode.MORE2);
        } else {
            DeviceAddHelper.updateTile(DeviceAddHelper.TitleMode.MORE);
        }
    }

    @Override
    public void updateWifiName(String wifiName) {
        mWifiNameTv.setText(wifiName);
    }

    @Override
    public void updateConnectFailedTipText(String wifiName, String wifiPwd, boolean isSupportAddBySc, boolean isManualInput) {
        mGotoWifiSetting.setVisibility(View.VISIBLE);
        mAboutWifiPwdTv.setVisibility(isSupportAddBySc ? View.VISIBLE : View.GONE);
        mShowTipTv.setText(getString(R.string.add_device_connect_wifi_failed));
        mShowTipTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.adddevice_icon_help,0);
        mShowTipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageNavigationHelper.gotoErrorTipPage(TipSoftApConnectWifiFragment.this, DeviceAddHelper.ErrorCode.COMMON_ERROR_CONNECT_FAIL);
            }
        });
        mShowTip1Tv.setText(getString(isSupportAddBySc ? R.string.add_device_wait_to_connect_wifi_failed_sc : R.string.add_device_wait_to_connect_wifi_failed, wifiName));
        mWifiPwdLayout.setVisibility(isSupportAddBySc ? View.VISIBLE : View.GONE);
        mWifiPwdTv.setText(wifiPwd);
        mWifiPwdTv.setClickable(!isManualInput);
        mCopyIv.setVisibility(isManualInput ? View.GONE : View.VISIBLE);
    }

    @Override
    protected IntentFilter createBroadCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LCConfiguration.CONNECTIVITY_CHAGET_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        return filter;
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        if (isDestoryView())
            return;
        if (LCConfiguration.CONNECTIVITY_CHAGET_ACTION.equals(intent.getAction())
                || WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
                || WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            LogUtil.debugLog("bz", "onReceive");
            final int netWorkState = Utils4AddDevice.getNetWorkState(getContextInfo());
            LogUtil.debugLog("bz", "netWorkState : " + netWorkState);
            mPresenter.dispatchHotConnected();
        }
    }

    @Override
    public void goSecurityCheckPage() {
        PageNavigationHelper.gotoSecurityCheckPage(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsBack) {
            if (getActivity() == null)
                return;
            getActivity().getSupportFragmentManager().popBackStack();
            mIsBack = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        cancelProgressDialog();
        //取消定时任务
        EventBus.getDefault().post(new CloseTimeFilterEvent());
    }
}
