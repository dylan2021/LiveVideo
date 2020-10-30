package com.mm.android.deviceaddmodule.mobilecommon.base;

import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

public class BaseProvider implements IBaseProvider {
    public static final String TAG = "BaseProvider";
    @Override
    public void uninit() {
        LogUtil.debugLog(TAG,"unint");
    }

}
