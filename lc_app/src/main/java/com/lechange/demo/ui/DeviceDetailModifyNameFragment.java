package com.lechange.demo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceDetailService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceModifyNameData;
import com.lechange.demo.R;
import com.mm.android.deviceaddmodule.helper.DeviceAddHelper;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;
import com.mm.android.deviceaddmodule.mobilecommon.utils.NameLengthFilter;
import com.mm.android.deviceaddmodule.mobilecommon.utils.WordInputFilter;
import com.mm.android.deviceaddmodule.mobilecommon.widget.ClearEditText;

public class DeviceDetailModifyNameFragment extends Fragment implements View.OnClickListener, IGetDeviceInfoCallBack.IModifyDeviceCallBack {
    private static final String TAG = DeviceDetailModifyNameFragment.class.getSimpleName();
    private Bundle arguments;
    private ClearEditText newName;
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;
    private DeviceDetailActivity deviceDetailActivity;
    private final int MAXLETHER = 40;
    private IGetDeviceInfoCallBack.IModifyDeviceName modifyNameListener;
    private String name;
    private String fromWhere;

    public static DeviceDetailModifyNameFragment newInstance() {
        DeviceDetailModifyNameFragment fragment = new DeviceDetailModifyNameFragment();
        return fragment;
    }

    public void setModifyNameListener(IGetDeviceInfoCallBack.IModifyDeviceName modifyNameListener) {
        this.modifyNameListener = modifyNameListener;
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
        deviceDetailActivity.llOperate.setVisibility(View.VISIBLE);
        deviceDetailActivity.llOperate.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_modify_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DeviceDetailActivity deviceDetailActivity = (DeviceDetailActivity) getActivity();
        deviceDetailActivity.tvTitle.setText(getResources().getString(R.string.lc_demo_device_detail_title));
        initView(view);
    }

    private void initView(View view) {
        newName = view.findViewById(R.id.et_new_name);
        if (deviceListBean.channels.size() > 1) {
            //多通道
            if (MethodConst.ParamConst.fromList.equals(fromWhere)) {
                newName.setText(deviceListBean.name);
            } else {
                newName.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
            }
        } else if (deviceListBean.channels.size() == 1) {
            newName.setText(deviceListBean.channels.get(deviceListBean.checkedChannel).channelName);
        } else {
            newName.setText(deviceListBean.name);
        }
        if (!TextUtils.isEmpty(newName.getText().toString())) {
            newName.setSelection(newName.getText().toString().length());
        }
        newName.setFilters(new InputFilter[]{new WordInputFilter(WordInputFilter.REX_NAME), new NameLengthFilter(MAXLETHER)});
        newName.addTextChangedListener(mTextWatcher);
    }

    private final TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            String devName = s.toString().trim();
            if (!TextUtils.isEmpty(devName)) {
                deviceDetailActivity.llOperate.setEnabled(true);
                newName.removeTextChangedListener(mTextWatcher);
                String filterDevName = DeviceAddHelper.strDeviceNameFilter(devName);
                if (!filterDevName.equals(devName)) {
                    newName.setText(filterDevName);
                    newName.setSelection(filterDevName.length());
                }
                newName.addTextChangedListener(mTextWatcher);
            } else {
                deviceDetailActivity.llOperate.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void afterTextChanged(Editable arg0) {
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        //修改名称
        deviceDetailActivity.rlLoading.setVisibility(View.VISIBLE);
        if (newName == null || newName.getText().toString().trim() == null || newName.getText().toString().trim().isEmpty()) {
            return;
        }
        name = newName.getText().toString().trim();
        DeviceDetailService deviceDetailService = ClassInstanceManager.newInstance().getDeviceDetailService();
        DeviceModifyNameData deviceModifyNameData = new DeviceModifyNameData();
        deviceModifyNameData.data.name = name;
        deviceModifyNameData.data.deviceId = deviceListBean.deviceId;
        if (deviceListBean.channels.size() > 1 && !MethodConst.ParamConst.fromList.equals(fromWhere)) {
            //多通道
            deviceModifyNameData.data.channelId = deviceListBean.channels.get(deviceListBean.checkedChannel).channelId;
        }
        deviceDetailService.modifyDeviceName(deviceModifyNameData, this);
    }

    @Override
    public void deviceModify(boolean result) {
        if (!isAdded()) {
            return;
        }
        deviceDetailActivity.llOperate.setVisibility(View.GONE);
        deviceDetailActivity.rlLoading.setVisibility(View.GONE);
        Toast.makeText(getContext(), getResources().getString(R.string.lc_demo_device_modify_success), Toast.LENGTH_SHORT).show();
        if (modifyNameListener != null) {
            modifyNameListener.deviceName(name);
        }
        //多通道设备详情
        if (deviceListBean.channels.size() == 0 || (deviceListBean.channels.size() > 1 && MethodConst.ParamConst.fromList.equals(fromWhere))) {
            deviceListBean.name = name;
        } else {
            deviceListBean.channels.get(deviceListBean.checkedChannel).channelName = name;
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onError(Throwable throwable) {
        if (!isAdded()) {
            return;
        }
        deviceDetailActivity.rlLoading.setVisibility(View.GONE);
        LogUtil.errorLog(TAG, "error", throwable);
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
