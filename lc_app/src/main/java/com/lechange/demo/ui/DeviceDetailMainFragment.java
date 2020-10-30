package com.lechange.demo.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceDetailService;
import com.common.openapi.DeviceLocalCacheService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceLocalCacheData;
import com.common.openapi.entity.DeviceUnBindData;
import com.lechange.demo.R;
import com.lechange.demo.tools.DialogUtils;
import com.lechange.demo.tools.MediaPlayHelper;
import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.device_wifi.CurWifiInfo;
import com.mm.android.deviceaddmodule.device_wifi.DeviceConstant;
import com.mm.android.deviceaddmodule.mobilecommon.entity.device.DHDevice;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import static android.app.Activity.RESULT_OK;
import static com.mm.android.deviceaddmodule.device_wifi.DeviceConstant.IntentCode.DEVICE_SETTING_WIFI_LIST;

public class DeviceDetailMainFragment extends Fragment implements View.OnClickListener, IGetDeviceInfoCallBack.IUnbindDeviceCallBack, IGetDeviceInfoCallBack.IDeviceCacheCallBack, IGetDeviceInfoCallBack.IModifyDeviceName {
    private static final String TAG = DeviceDetailMainFragment.class.getSimpleName();
    private RelativeLayout rlDeviceDetail;
    private RelativeLayout rlDetailVersion;
    private RelativeLayout rlDeployment;
    private RelativeLayout rlDetele;
    private TextView tvDeviceName;
    private ImageView ivDevicePic;
    private TextView tvDeviceVersion;
    private Bundle arguments;
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;
    private DeviceDetailActivity deviceDetailActivity;
    private DeviceDetailService deviceDetailService;
    private CurWifiInfo wifiInfo;
    private TextView tvCurrentWifi;
    private RelativeLayout rlCurWifi;
    private IGetDeviceInfoCallBack.IModifyDeviceName modifyNameListener;
    private String fromWhere;
    private TextView tvDeploymentTip;

    public static DeviceDetailMainFragment newInstance() {
        DeviceDetailMainFragment fragment = new DeviceDetailMainFragment();
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
        return inflater.inflate(R.layout.fragment_device_detail_main, container, false);
    }

    public void setModifyNameListener(IGetDeviceInfoCallBack.IModifyDeviceName modifyNameListener) {
        this.modifyNameListener = modifyNameListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rlDeviceDetail = view.findViewById(R.id.rl_device_detail);
        rlDetailVersion = view.findViewById(R.id.rl_detail_version);
        rlDeployment = view.findViewById(R.id.rl_deployment);
        tvDeploymentTip = view.findViewById(R.id.tv_deployment_tip);
        rlDetele = view.findViewById(R.id.rl_detele);
        tvDeviceName = view.findViewById(R.id.tv_device_name);
        ivDevicePic = view.findViewById(R.id.iv_device_pic);
        tvDeviceVersion = view.findViewById(R.id.tv_device_version);
        tvCurrentWifi = view.findViewById(R.id.tv_current_wifi);
        rlCurWifi = view.findViewById(R.id.rl_cur_wifi);

        rlDeployment.setOnClickListener(this);
        rlDetele.setOnClickListener(this);
        rlDeviceDetail.setOnClickListener(this);
        rlCurWifi.setOnClickListener(this);
        DeviceDetailActivity deviceDetailActivity = (DeviceDetailActivity) getActivity();
        deviceDetailActivity.tvTitle.setText(getResources().getString(R.string.lc_demo_device_detail_title));
        if (arguments == null) {
            return;
        }
        deviceListBean = (DeviceDetailListData.ResponseData.DeviceListBean) arguments.getSerializable(MethodConst.ParamConst.deviceDetail);
        //不为空 列表页跳转
        fromWhere = arguments.getString(MethodConst.ParamConst.fromList);
        if (deviceListBean == null) {
            return;
        }
        deviceDetailService = ClassInstanceManager.newInstance().getDeviceDetailService();
        if (deviceListBean.channels.size() > 1) {
            //多通道
            if (MethodConst.ParamConst.fromList.equals(fromWhere)) {
                //设备详情
                tvDeviceName.setText(deviceListBean.name);
                tvDeviceVersion.setText(deviceListBean.version);
                rlDeployment.setVisibility(View.GONE);
                tvDeploymentTip.setVisibility(View.GONE);
                rlCurWifi.setVisibility(View.VISIBLE);
                //设备才能获取WIFI
                getCurrentWifiInfo();
            } else {
                //通道详情
                tvDeviceName.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
                getDeviceLocalCache();
                rlDetailVersion.setVisibility(View.GONE);
                rlCurWifi.setVisibility(View.GONE);
                rlDetele.setVisibility(View.GONE);
            }
        } else if (deviceListBean.channels.size() == 1) {
            //单通道
            tvDeviceName.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
            getDeviceLocalCache();
            tvDeviceVersion.setText(deviceListBean.version);
            if (deviceListBean.deviceSource == 2) {
                rlDetele.setVisibility(View.GONE);
            }
            rlCurWifi.setVisibility(View.VISIBLE);
            //设备才能获取WIFI
            getCurrentWifiInfo();
        } else {
            //多通道但是没有挂载通道
            //设备详情
            tvDeviceName.setText(deviceListBean.name);
            tvDeviceVersion.setText(deviceListBean.version);
            rlDeployment.setVisibility(View.GONE);
            tvDeploymentTip.setVisibility(View.GONE);
            rlCurWifi.setVisibility(View.VISIBLE);
            //设备才能获取WIFI
            getCurrentWifiInfo();
        }
        if (deviceListBean.channels.size() == 0) {
            if ("offline".equals(deviceListBean.status)) {
                rlCurWifi.setVisibility(View.GONE);
            } else {
                rlDetailVersion.setOnClickListener(this);
            }
        } else {
            if ("offline".equals(deviceListBean.channels.get(deviceListBean.checkedChannel).status)) {
                rlCurWifi.setVisibility(View.GONE);
            } else {
                rlDetailVersion.setOnClickListener(this);
            }
        }
    }

    /**
     * 获取设备缓存信息
     */
    private void getDeviceLocalCache() {
        DeviceLocalCacheData deviceLocalCacheData = new DeviceLocalCacheData();
        deviceLocalCacheData.setDeviceId(deviceListBean.deviceId);
        if (deviceListBean.channels != null && deviceListBean.channels.size() > 0) {
            deviceLocalCacheData.setChannelId(deviceListBean.channels.get(deviceListBean.checkedChannel).channelId);
        }
        DeviceLocalCacheService deviceLocalCacheService = ClassInstanceManager.newInstance().getDeviceLocalCacheService();
        deviceLocalCacheService.findLocalCache(deviceLocalCacheData, this);
    }

    private void getCurrentWifiInfo() {
        //设备才能获取WIFI
        DialogUtils.show(getActivity());
        deviceDetailService.currentDeviceWifi(deviceListBean.deviceId, new IGetDeviceInfoCallBack.IDeviceCurrentWifiInfoCallBack() {
            @Override
            public void deviceCurrentWifiInfo(CurWifiInfo curWifiInfo) {
                DialogUtils.dismiss();
                if (!isAdded() || curWifiInfo == null) {
                    return;
                }
                rlCurWifi.setVisibility(View.VISIBLE);
                if (curWifiInfo.isLinkEnable()) {
                    wifiInfo = curWifiInfo;
                    tvCurrentWifi.setText(wifiInfo.getSsid());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                DialogUtils.dismiss();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void gotoModifyNamePage(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null || fragmentActivity.getSupportFragmentManager() == null) {
            return;
        }
        DeviceDetailNameFragment fragment = DeviceDetailNameFragment.newInstance();
        fragment.setArguments(arguments);
        fragment.setModifyNameListener(this);
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.hide(this).add(R.id.fr_content, fragment).addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void gotoUpdatePage(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null || fragmentActivity.getSupportFragmentManager() == null) {
            return;
        }
        DeviceDetailVersionFragment fragment = DeviceDetailVersionFragment.newInstance();
        fragment.setArguments(arguments);
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.hide(this).add(R.id.fr_content, fragment).addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void gotoDeploymentPage(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null || fragmentActivity.getSupportFragmentManager() == null) {
            return;
        }
        DeviceDetailDeploymentFragment fragment = DeviceDetailDeploymentFragment.newInstance();
        fragment.setArguments(arguments);
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.hide(this).add(R.id.fr_content, fragment).addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_device_detail) {
            gotoModifyNamePage(getActivity());
        } else if (id == R.id.rl_cur_wifi) {
            DHDevice device = new DHDevice();
            device.setDeviceId(deviceListBean.deviceId);
            device.setName(deviceListBean.name);
            device.setStatus(deviceListBean.status);
            LCDeviceEngine.newInstance().deviceOnlineChangeNet(getActivity(), device, wifiInfo);
        } else if (id == R.id.rl_deployment) {
            gotoDeploymentPage(getActivity());
        } else if (id == R.id.rl_detail_version) {
            gotoUpdatePage(getActivity());
        } else if (id == R.id.rl_detele) {
            //解绑设备
            deviceDetailActivity.rlLoading.setVisibility(View.VISIBLE);
            DeviceDetailService deviceDetailService = ClassInstanceManager.newInstance().getDeviceDetailService();
            DeviceUnBindData deviceUnBindData = new DeviceUnBindData();
            deviceUnBindData.data.deviceId = deviceListBean.deviceId;
            deviceDetailService.unBindDevice(deviceUnBindData, this);
        }
    }

    @Override
    public void unBindDevice(boolean result) {
        if (!isAdded()) {
            return;
        }
        deviceDetailActivity.rlLoading.setVisibility(View.GONE);
        Toast.makeText(getContext(), getResources().getString(R.string.lc_demo_device_unbind_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(DeviceConstant.IntentKey.DHDEVICE_UNBIND, true);
        deviceDetailActivity.setResult(RESULT_OK, intent);
        deviceDetailActivity.finish();
    }

    @Override
    public void deviceCache(DeviceLocalCacheData deviceLocalCacheData) {
        if (!isAdded()) {
            return;
        }
        BitmapDrawable bitmapDrawable = MediaPlayHelper.picDrawable(deviceLocalCacheData.getPicPath());
        if (bitmapDrawable != null) {
            ivDevicePic.setImageDrawable(bitmapDrawable);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (!isAdded()) {
            return;
        }
        deviceDetailActivity.rlLoading.setVisibility(View.GONE);
        LogUtil.errorLog(TAG, "error", throwable);
        if (!"null point".equals(throwable.getMessage()) && isAdded()) {
            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DEVICE_SETTING_WIFI_LIST && resultCode == RESULT_OK && data != null) {
            CurWifiInfo curWifiInfo = (CurWifiInfo) data.getSerializableExtra(DeviceConstant.IntentKey.DEVICE_CURRENT_WIFI_INFO);
            if (curWifiInfo != null) {
                wifiInfo = curWifiInfo;
                tvCurrentWifi.setText(wifiInfo.getSsid());
            }
        }
    }

    @Override
    public void deviceName(String newName) {
        tvDeviceName.setText(newName);
        //多通道设备详情
        if (deviceListBean.channels.size() == 0 || (deviceListBean.channels.size() > 1 && MethodConst.ParamConst.fromList.equals(fromWhere))) {
            deviceListBean.name = newName;
        } else {
            deviceListBean.channels.get(deviceListBean.checkedChannel).channelName = newName;
        }
        if (modifyNameListener != null) {
            modifyNameListener.deviceName(newName);
        }
    }
}
