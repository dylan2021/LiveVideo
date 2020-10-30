package com.lechange.demo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceDetailListData;
import com.lechange.demo.R;

public class DeviceDetailNameFragment extends Fragment implements View.OnClickListener, IGetDeviceInfoCallBack.IModifyDeviceName {
    private static final String TAG = DeviceDetailNameFragment.class.getSimpleName();
    private Bundle arguments;
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;
    private DeviceDetailActivity deviceDetailActivity;
    private TextView tvDeviceName;
    private IGetDeviceInfoCallBack.IModifyDeviceName modifyNameListener;
    private String fromWhere;

    public static DeviceDetailNameFragment newInstance() {
        DeviceDetailNameFragment fragment = new DeviceDetailNameFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getArguments();
        if (arguments == null) {
            return;
        }
        deviceListBean = (DeviceDetailListData.ResponseData.DeviceListBean) arguments.getSerializable(MethodConst.ParamConst.deviceDetail);
        //不为空 列表页跳转
        fromWhere = arguments.getString(MethodConst.ParamConst.fromList);
        if (deviceListBean == null) {
            return;
        }
        deviceDetailActivity = (DeviceDetailActivity) getActivity();
        deviceDetailActivity.llOperate.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_detail_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DeviceDetailActivity deviceDetailActivity = (DeviceDetailActivity) getActivity();
        deviceDetailActivity.tvTitle.setText(getResources().getString(R.string.lc_demo_device_detail_title));
        initView(view);
    }

    private void initView(View view) {
        view.findViewById(R.id.rl_device_modify).setOnClickListener(this);
        tvDeviceName = view.findViewById(R.id.tv_device_name);
        RelativeLayout rlDeviceType = view.findViewById(R.id.rl_detail_type);
        RelativeLayout rlDeviceSerial = view.findViewById(R.id.rl_device_serial);
        TextView tvDeviceType = view.findViewById(R.id.tv_device_type);
        TextView tvDeviceSerial = view.findViewById(R.id.tv_device_serial);
        if (deviceListBean.channels.size() > 1) {
            //多通道
            if (MethodConst.ParamConst.fromList.equals(fromWhere)) {
                tvDeviceName.setText(deviceListBean.name);
                tvDeviceType.setText(deviceListBean.deviceModel);
                tvDeviceSerial.setText(deviceListBean.deviceId);
            }else {
                tvDeviceName.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
                rlDeviceType.setVisibility(View.GONE);
                rlDeviceSerial.setVisibility(View.GONE);
            }
        } else if (deviceListBean.channels.size() == 1){
            tvDeviceName.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
            tvDeviceType.setText(deviceListBean.deviceModel);
            tvDeviceSerial.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).deviceId);
        }else {
            tvDeviceName.setText(deviceListBean.name);
            tvDeviceType.setText(deviceListBean.deviceModel);
            tvDeviceSerial.setText(deviceListBean.deviceId);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (getActivity() == null || getActivity().getSupportFragmentManager() == null) {
            return;
        }
        DeviceDetailModifyNameFragment fragment = DeviceDetailModifyNameFragment.newInstance();
        fragment.setModifyNameListener(this);
        fragment.setArguments(arguments);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.hide(this).add(R.id.fr_content, fragment).addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void setModifyNameListener(IGetDeviceInfoCallBack.IModifyDeviceName modifyNameListener) {
        this.modifyNameListener = modifyNameListener;
    }

    @Override
    public void deviceName(String newName) {
        tvDeviceName.setText(newName);
        //多通道设备详情
        if (deviceListBean.channels.size() == 0 ||(deviceListBean.channels.size() > 1 && MethodConst.ParamConst.fromList.equals(fromWhere))) {
            deviceListBean.name = newName;
        } else {
            deviceListBean.channels.get(deviceListBean.checkedChannel).channelName = newName;
        }
        if (modifyNameListener != null) {
            modifyNameListener.deviceName(newName);
        }
    }
}
