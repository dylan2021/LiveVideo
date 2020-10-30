package com.mm.android.deviceaddmodule.mobilecommon.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.eventbus.event.BaseEvent;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity implements IActivityResultDispatch {

    private Toast mToast;
    private ProgressDialog mProgressDialog;
    private boolean isDestroyed = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseEvent event) {
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        mProgressDialog = new ProgressDialog(this, R.style.mobile_common_custom_dialog);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dissmissProgressDialog();
        isDestroyed = true;
        mProgressDialog = null;
    }

    protected void showProgressDialog(int layoutId) {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
            mProgressDialog.setContentView(layoutId);
        }
    }

    protected void dissmissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public boolean isActivityDestory() {
        return isDestroyed;
    }

    protected void toast(int res) {
        String content = "";
        try {
            content = getString(res);
        } catch (Resources.NotFoundException e) {
            LogUtil.debugLog("toast", "resource id not found!!!");
        }

        toast(content);
    }

    protected void toast(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 带错误码的toast
     *
     * @param errorCode
     */
    public void toast(int res, int errorCode) {
        if (mToast == null) {
            mToast = Toast.makeText(this, getString(res) + "(" + errorCode + ")",
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(getString(res) + "(" + errorCode + ")");
        }
        mToast.show();
    }


    protected void toastInCenter(int res) {
        String content = "";
        try {
            content = getString(res);
        } catch (Resources.NotFoundException e) {
            LogUtil.debugLog("toast", "resource id not found!!!");
        }
        toastInCenter(content);

    }

    protected void toastInCenter(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            TextView tv = mToast.getView().findViewById(android.R.id.message);
            tv.setGravity(Gravity.CENTER);
        } else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.mobile_common_slide_in_right, R.anim.mobile_common_slide_out_left);
    }

    public void startActivityNoAnimation(Intent intent) {
        super.startActivity(intent);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
//
    }

    public void startActivityForResultWithAnimation(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.mobile_common_slide_in_right, R.anim.mobile_common_slide_out_left);
    }

    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.mobile_common_slide_left_back_in
                , R.anim.mobile_common_slide_right_back_out);
    }

    public void finishNoAnimation() {
        super.finish();
    }


    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (listeners == null) return;
        synchronized (listeners) {
            for (OnActivityResultListener listener : listeners) {
                if (listener != null) {
                    listener.onActivityResult(requestCode, resultCode, data);
                }
            }

        }
    }

    private final List<IActivityResultDispatch.OnActivityResultListener> listeners = new ArrayList<>();

    public void addOnActivityResultListener(IActivityResultDispatch.OnActivityResultListener listener) {
        listeners.add(listener);
    }

    public void removeOnActivityResultListener(IActivityResultDispatch.OnActivityResultListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}
