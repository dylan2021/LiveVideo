package com.lechange.demo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceDetailService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceVersionListData;
import com.lechange.demo.R;
import com.lechange.demo.dialog.DeviceUpdateDialog;
import com.lechange.demo.view.LcProgressBar;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

public class DeviceDetailVersionFragment extends Fragment implements IGetDeviceInfoCallBack.IDeviceVersionCallBack, View.OnClickListener , IGetDeviceInfoCallBack.IDeviceUpdateCallBack {
    private static final String TAG = DeviceDetailVersionFragment.class.getSimpleName();
    private Bundle arguments;
    private LcProgressBar pgUpodate;
    private TextView tvVersionTip;
    private LinearLayout llNewVersion;
    private TextView tvNewVersionTip;
    private TextView tvNewVersion;
    private TextView tvDeviceCurrentVersion;
    private DeviceDetailActivity deviceDetailActivity;
    private DeviceDetailService deviceDetailService;
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;

    public static DeviceDetailVersionFragment newInstance() {
        DeviceDetailVersionFragment fragment = new DeviceDetailVersionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceDetailActivity = (DeviceDetailActivity) getActivity();
        deviceDetailActivity.llOperate.setVisibility(View.GONE);
        arguments = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_version, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DeviceDetailActivity deviceDetailActivity = (DeviceDetailActivity) getActivity();
        deviceDetailActivity.tvTitle.setText(getResources().getString(R.string.lc_demo_device_version_title));
        initView(view);
        initData();
    }

    private void initView(View view) {
        tvDeviceCurrentVersion = view.findViewById(R.id.tv_device_current_version);
        tvNewVersion = view.findViewById(R.id.tv_new_version);
        tvNewVersionTip = view.findViewById(R.id.tv_new_version_tip);
        llNewVersion = view.findViewById(R.id.ll_new_version);
        tvVersionTip = view.findViewById(R.id.tv_version_tip);
        pgUpodate = view.findViewById(R.id.pg_upodate);
        pgUpodate.setOnClickListener(this);
    }

    private void initData() {
        //获取设备版本信息
        if (arguments == null) {
            return;
        }
        deviceListBean = (DeviceDetailListData.ResponseData.DeviceListBean) arguments.getSerializable(MethodConst.ParamConst.deviceDetail);
        if (deviceListBean == null) {
            return;
        }
        deviceDetailActivity.rlLoading.setVisibility(View.VISIBLE);
        deviceDetailService = ClassInstanceManager.newInstance().getDeviceDetailService();
        DeviceVersionListData deviceVersionListData = new DeviceVersionListData();
        deviceVersionListData.data.deviceIds = deviceListBean.deviceId;
        deviceDetailService.deviceVersionList(deviceVersionListData, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void deviceVersion(DeviceVersionListData.Response responseData) {
        if (!isAdded()){
            return;
        }
        deviceDetailActivity.rlLoading.setVisibility(View.GONE);
        if (responseData.data == null || responseData.data.deviceVersionList == null || responseData.data.deviceVersionList.size() == 0) {
            return;
        }
        DeviceVersionListData.ResponseData.DeviceVersionListBean deviceVersionListBean = responseData.data.deviceVersionList.get(0);
        if (deviceVersionListBean == null) {
            return;
        }
        tvDeviceCurrentVersion.setText(deviceVersionListBean.version);
        if (deviceVersionListBean.getUpgradeInfo() == null) {
            //已是最新版本
            llNewVersion.setVisibility(View.GONE);
            pgUpodate.setVisibility(View.GONE);
            tvVersionTip.setVisibility(View.VISIBLE);
            tvVersionTip.setText(getResources().getString(R.string.lc_demo_device_version_new_tip));
        } else {
            //需要更新设备
            llNewVersion.setVisibility(View.VISIBLE);
            pgUpodate.setVisibility(View.VISIBLE);
            tvVersionTip.setVisibility(View.GONE);
            tvNewVersion.setText(deviceVersionListBean.getUpgradeInfo().getVersion());
            tvNewVersionTip.setText(deviceVersionListBean.getUpgradeInfo().getDescription());
        }
    }

    @Override
    public void deviceUpdate(boolean result) {
        if (!isAdded()){
            return;
        }
        deviceDetailActivity.rlLoading.setVisibility(View.GONE);
        Toast.makeText(getContext(), getResources().getString(R.string.lc_demo_device_update_success), Toast.LENGTH_SHORT).show();
        deviceDetailActivity.finish();
    }

    @Override
    public void onError(Throwable throwable) {
        if (!isAdded()){
            return;
        }
        deviceDetailActivity.rlLoading.setVisibility(View.GONE);
        LogUtil.errorLog(TAG, "error", throwable);
        pgUpodate.setText(getResources().getString(R.string.lc_demo_device_update));
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pg_upodate) {
            DeviceUpdateDialog deviceUpdateDialog = new DeviceUpdateDialog(getContext());
            deviceUpdateDialog.setOnOkClickLisenter(new DeviceUpdateDialog.OnOkClickLisenter() {
                @Override
                public void OnOK() {
                    deviceDetailActivity.rlLoading.setVisibility(View.VISIBLE);
                    pgUpodate.setText(getResources().getString(R.string.lc_demo_device_updateing));
                    if(deviceDetailService==null){
                        deviceDetailService =  ClassInstanceManager.newInstance().getDeviceDetailService();
                    }
                    deviceDetailService.upgradeDevice(deviceListBean.deviceId,DeviceDetailVersionFragment.this);
                }
            });
            deviceUpdateDialog.show();
        }
    }
}
