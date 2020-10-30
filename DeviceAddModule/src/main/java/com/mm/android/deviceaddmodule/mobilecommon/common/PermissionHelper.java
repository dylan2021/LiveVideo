package com.mm.android.deviceaddmodule.mobilecommon.common;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.dialog.LCAlertDialog;
import com.mm.android.deviceaddmodule.mobilecommon.utils.AppUtils;
import com.mm.android.deviceaddmodule.mobilecommon.utils.CommonHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态权限二次封装帮助类，切记不可在onResume里使用
 */
public class PermissionHelper {
    public static final int REQUEST_CODE_PERMISSION = 0X123;
    private FragmentActivity mActivity;

    public PermissionHelper(FragmentActivity activity) {
        mActivity = activity;
    }

    /**
     * 如果在fragment使用，必须传fragment，而非getActivity
     *
     * @param fragment
     */
    public PermissionHelper(Fragment fragment) {
        mActivity = fragment.getActivity();
    }

    /**
     * 是否有指定权限
     *
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        if (TextUtils.isEmpty(permission)) {
            return false;
        }
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否始终允许位置权限，兼容Q系统
     *
     * @return
     */
    public boolean hasAlawaysLocationPermission() {
        if (CommonHelper.isAndroidQOrLater()) {
            return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        } else {
            return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void requestPermissions(String[] permissions, final IPermissionListener listener) {
        //存放待授权的权限
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (permissionList.isEmpty()) {      //集合为空，则都已授权
            if (listener != null) {
                listener.onGranted();
                return;
            }
        } else {      //不为空，去授权
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (AppUtils.getTargetSdkVersion(mActivity) >= Build.VERSION_CODES.M) {
                    mActivity.requestPermissions(permissionList.toArray(new String[permissionList.size()]), Constants.PERMISSION_REQUEST_ID);
                }
            }
        }
    }

    public void gotoSettingPage(String[] permission, final IPermissionListener listener) {
        LCAlertDialog.Builder builder = new LCAlertDialog.Builder(mActivity);
        builder.setTitle(R.string.mobile_common_permission_apply)
                .setCancelable(false)
                .setMessage(getExplain(permission[0]))
                .setCancelButton(R.string.mobile_common_common_ignore, new LCAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(LCAlertDialog dialog, int which, boolean isChecked) {
                        if (listener != null) {
                            listener.onGiveUp();
                        }
                    }
                })
                .setConfirmButton(R.string.go_to_setting,
                        new LCAlertDialog.OnClickListener() {

                            @Override
                            public void onClick(LCAlertDialog dialog,
                                                int which, boolean isChecked) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package", mActivity.getPackageName(), null));
                                mActivity.startActivityForResult(intent, REQUEST_CODE_PERMISSION);
                                dialog.dismiss();
                            }
                        });
        LCAlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show(mActivity.getSupportFragmentManager(), null);
    }

    /**
     * 自定义提示信息框
     *
     * @param tip
     */
    public void showCustomTip(String tip, LCAlertDialog.OnClickListener listener) {
        LCAlertDialog.Builder builder = new LCAlertDialog.Builder(mActivity);
        builder.setTitle(R.string.mobile_common_permission_apply)
                .setCancelable(false)
                .setMessage(tip)
                .setCancelButton(R.string.mobile_common_common_ignore, listener)
                .setConfirmButton(R.string.go_to_setting,
                        new LCAlertDialog.OnClickListener() {
                            @Override
                            public void onClick(LCAlertDialog dialog, int which, boolean isChecked) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package", mActivity.getPackageName(), null));
                                mActivity.startActivityForResult(intent, REQUEST_CODE_PERMISSION);
                                dialog.dismiss();
                            }
                        });
        LCAlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show(mActivity.getSupportFragmentManager(), null);
    }

    /**
     * 根据不同的权限提示不同的文案内容
     *
     * @param permission
     * @return
     */
    public String getExplain(String permission) {
        String explain = mActivity.getString(R.string.mobile_common_lack_permission_then_exit);
        switch (permission) {
            case Manifest.permission.READ_CONTACTS:
                explain = mActivity.getString(R.string.mobile_common_permission_explain_get_accounts);
                break;

            case Manifest.permission.WRITE_CONTACTS:
                explain = mActivity.getString(R.string.mobile_common_permission_explain_write_accounts);
                break;

            case Manifest.permission.CALL_PHONE:
                explain = mActivity.getString(R.string.mobile_common_permission_explain_read_phone_state);
                break;

            case Manifest.permission.CAMERA:
                explain = mActivity.getString(R.string.mobile_common_permission_explain_camera);
                break;

            case Manifest.permission.ACCESS_FINE_LOCATION:
                explain = mActivity.getString(R.string.mobile_common_permission_explain_access_location_usage);
                break;

            case Manifest.permission.ACCESS_COARSE_LOCATION:
                explain = mActivity.getString(R.string.mobile_common_permission_explain_access_location_usage);
                break;

            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                explain = mActivity.getString(R.string.mobile_common_permission_explain_external_storage);
                break;

            case Manifest.permission.RECORD_AUDIO:
                explain = mActivity.getString(R.string.mobile_common_permission_explain_record_audio);
                break;

            case Manifest.permission.ACCESS_BACKGROUND_LOCATION:
                explain = mActivity.getString(R.string.geofence_backgroud_location_permission_explain);
                break;
        }
        return explain;
    }

    public interface IPermissionListener {
        /**
         * 授权成功
         */
        void onGranted();

        /**
         * 授权失败
         *
         * @return true：调用者自己处理  false：默认处理，弹出需要权限的原因，引导用户跳转到设置页面
         */
        boolean onDenied();

        /**
         * 引导用户跳转到设置页时，用户点击取消
         */
        void onGiveUp();
    }
}
