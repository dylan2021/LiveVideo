package com.mm.android.deviceaddmodule.mobilecommon.base;

import android.content.Intent;

public interface IActivityResultDispatch {
    public interface OnActivityResultListener {
        /**
         * callBack
         *
         * @param requestCode requestCode
         * @param resultCode  resultCode
         * @param data        data
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public void addOnActivityResultListener(OnActivityResultListener listener);

    public void removeOnActivityResultListener(OnActivityResultListener listener);
}
