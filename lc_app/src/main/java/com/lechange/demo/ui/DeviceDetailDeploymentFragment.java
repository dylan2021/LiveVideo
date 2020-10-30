package com.lechange.demo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceDetailService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceAlarmStatusData;
import com.common.openapi.entity.DeviceChannelInfoData;
import com.common.openapi.entity.DeviceDetailListData;
import com.lechange.demo.R;
import com.lechange.demo.tools.DialogUtils;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

public class DeviceDetailDeploymentFragment extends Fragment implements IGetDeviceInfoCallBack.IDeviceChannelInfoCallBack, View.OnClickListener, IGetDeviceInfoCallBack.IDeviceAlarmStatusCallBack {
    private static final String TAG = DeviceDetailDeploymentFragment.class.getSimpleName();
    private Bundle arguments;
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;
    private DeviceDetailActivity deviceDetailActivity;
    private ImageView ivMoveCheck;
    private int alarmStatus;

    public static DeviceDetailDeploymentFragment newInstance() {
        DeviceDetailDeploymentFragment fragment = new DeviceDetailDeploymentFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceDetailActivity deviceDetailActivity = (DeviceDetailActivity) getActivity();
        deviceDetailActivity.llOperate.setVisibility(View.GONE);
        arguments = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_detail_deployment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        deviceDetailActivity = (DeviceDetailActivity) getActivity();
        deviceDetailActivity.tvTitle.setText(getResources().getString(R.string.lc_demo_device_deployment_title));
        if (arguments == null) {
            return;
        }
        deviceListBean = (DeviceDetailListData.ResponseData.DeviceListBean) arguments.getSerializable(MethodConst.ParamConst.deviceDetail);
        if (deviceListBean == null) {
            return;
        }
        initData();
    }

    private void initView(View view) {
        ivMoveCheck = view.findViewById(R.id.iv_move_check);
        ivMoveCheck.setOnClickListener(this);
    }

    private void initData() {
        //获取动检状态/设备详情
        DialogUtils.show(getActivity());
        DeviceDetailService deviceDetailService =  ClassInstanceManager.newInstance().getDeviceDetailService();
        DeviceChannelInfoData deviceChannelInfoData = new DeviceChannelInfoData();
        deviceChannelInfoData.data.deviceId = deviceListBean.deviceId;
        deviceChannelInfoData.data.channelId = deviceListBean.channels.get(deviceListBean.checkedChannel).channelId;
        deviceDetailService.bindDeviceChannelInfo(deviceChannelInfoData, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void deviceChannelInfo(DeviceChannelInfoData.Response result) {
        if (!isAdded()){
            return;
        }
        DialogUtils.dismiss();
        this.alarmStatus = result.data.alarmStatus;
        if (result.data.alarmStatus == 1) {
            ivMoveCheck.setImageDrawable(getResources().getDrawable(R.mipmap.lc_demo_switch_on));
        } else {
            ivMoveCheck.setImageDrawable(getResources().getDrawable(R.mipmap.lc_demo_switch_off));
        }
    }

    @Override
    public void deviceAlarmStatus(boolean result) {
        if (!isAdded()){
            return;
        }
        DialogUtils.dismiss();
        this.alarmStatus = (this.alarmStatus == 1) ? 0 : 1;
        if (this.alarmStatus == 1) {
            ivMoveCheck.setImageDrawable(getResources().getDrawable(R.mipmap.lc_demo_switch_on));
        } else {
            ivMoveCheck.setImageDrawable(getResources().getDrawable(R.mipmap.lc_demo_switch_off));
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (!isAdded()){
            return;
        }
        DialogUtils.dismiss();
        LogUtil.errorLog(TAG, "error", throwable);
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        //设置动检状态
        DialogUtils.show(getActivity());
        DeviceDetailService deviceDetailService =  ClassInstanceManager.newInstance().getDeviceDetailService();
        DeviceAlarmStatusData deviceAlarmStatusData = new DeviceAlarmStatusData();
        deviceAlarmStatusData.data.deviceId = deviceListBean.deviceId;
        deviceAlarmStatusData.data.channelId = deviceListBean.channels.get(deviceListBean.checkedChannel).channelId;
        //现在是开启状态，则关闭，反之
        deviceAlarmStatusData.data.enable = alarmStatus == 1 ? false : true;
        deviceDetailService.modifyDeviceAlarmStatus(deviceAlarmStatusData, this);
    }
}
