package com.mm.android.deviceaddmodule.p_wiredwireless;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.base.BaseDevAddFragment;
import com.mm.android.deviceaddmodule.contract.SmartConfigConstract;
import com.mm.android.deviceaddmodule.helper.DeviceAddHelper;
import com.mm.android.deviceaddmodule.helper.PageNavigationHelper;
import com.mm.android.deviceaddmodule.mobilecommon.entity.deviceadd.DeviceAddInfo;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;
import com.mm.android.deviceaddmodule.mobilecommon.widget.CircleCountDownView;
import com.mm.android.deviceaddmodule.model.DeviceAddModel;
import com.mm.android.deviceaddmodule.presenter.SmartConfigPresenter;

import static com.mm.android.deviceaddmodule.helper.PageNavigationHelper.WIFI_PWD_TAG;

/**
 * 设备wifi配置页面
 */
public class SmartConfigFragment extends BaseDevAddFragment implements SmartConfigConstract.View, CircleCountDownView.OnCountDownFinishListener,
        View.OnClickListener {
    SmartConfigConstract.Presenter mPresenter;
    CircleCountDownView mCountDownView;
    ImageView mWifiAnimationView;
    Animatable mWifiAnimation;
    TextView mTip2Txt, mTipWifiPwdErrorTxt;
    long mEventStartTime;       //统计开始时间

    public static SmartConfigFragment newInstance(/*boolean isQRCodeConfig*/) {
        SmartConfigFragment fragment = new SmartConfigFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_smart_config, container, false);
    }

    protected void initView(View view) {
        mTipWifiPwdErrorTxt = view.findViewById(R.id.tip_wifi_pwd_error);
        mTip2Txt = view.findViewById(R.id.tip2_txt);
        mCountDownView = view.findViewById(R.id.countdown_view);
        mCountDownView.setCountDownListener(this);
        mWifiAnimationView = view.findViewById(R.id.wifi_animation_view);
        mWifiAnimation = (Animatable) mWifiAnimationView.getDrawable();
        mWifiAnimation.start();
        mTipWifiPwdErrorTxt.setOnClickListener(this);
    }

    protected void initData() {
        mPresenter = new SmartConfigPresenter(this/*, isQRCodeConfig*/);
        //配对界面更多按钮只展示重新添加和取消
        DeviceAddHelper.updateTile(DeviceAddHelper.TitleMode.MORE);

        mPresenter.startSmartConfig();
        mCountDownView.startCountDown();
        mEventStartTime = System.currentTimeMillis();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.debugLog("AudioConfig", "onPause");
        mPresenter.pauseAudio();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.debugLog("AudioConfig", "onResume");
        mPresenter.playAudio();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mWifiAnimation.isRunning()) {
            mWifiAnimation.stop();
        }
        LogUtil.debugLog("AudioConfig", "onDestroyView");
        mPresenter.releaseAudio();
        mPresenter.recyle();
    }

    @Override
    public void goDevInitPage(DEVICE_NET_INFO_EX device_net_info_ex) {
        PageNavigationHelper.gotoSecurityCheckPage(this);
    }

    @Override
    public void goConnectCloudPage() {
        PageNavigationHelper.gotoCloudConnectPage(this);
    }

    @Override
    public void goDevLoginPage() {
        PageNavigationHelper.gotoDevLoginPage(this);
    }

    @Override
    public void goConfigTimeoutPage() {
        PageNavigationHelper.gotoErrorTipPage(this, DeviceAddHelper.ErrorCode.WIRED_WIRELESS_ERROR_CONFIG_TIMEOUT);
    }

    @Override
    public void goWfiPwdPage() {
        getActivity().getSupportFragmentManager().popBackStackImmediate(WIFI_PWD_TAG, 0);
    }

    @Override
    public void stopCountDown() {
        mCountDownView.stopCountDown();
    }

    @Override
    public void updateTip2Txt(boolean isSupportSoundWave, boolean isSupportSoundWaveV2) {
        if (isSupportSoundWave || isSupportSoundWaveV2) {
            mTip2Txt.setText(R.string.add_device_adjust_phone_volume_to_hear_bugu);
        } else {
            mTip2Txt.setText(R.string.add_device_keep_phone_close_to_device);
        }
    }

    @Override
    public void hideTipWifiPwdErrorTxt(boolean isOversea) {
        mTipWifiPwdErrorTxt.setVisibility(isOversea ? View.GONE : View.VISIBLE);
    }

    @Override
    public void countDownFinished() {
        goConfigTimeoutPage();
    }

    @Override
    public void middleTimeUp() {

    }

    @Override
    public void completeAction() {
        if (getActivity() != null) getActivity().finish();
    }

    @Override
    public void goBindDevicePage() {
        PageNavigationHelper.gotoDeviceBindPage(this);
    }

    @Override
    public void onClick(View v) {
        if (mPresenter != null) {
            mPresenter.wifiPwdErrorClick();
        }
    }
}
