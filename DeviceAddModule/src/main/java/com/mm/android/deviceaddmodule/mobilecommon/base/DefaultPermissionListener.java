package com.mm.android.deviceaddmodule.mobilecommon.base;

import com.mm.android.deviceaddmodule.mobilecommon.common.PermissionHelper;

/**
 * 动态权限回调的默认实现类
 */
public abstract class DefaultPermissionListener  implements PermissionHelper.IPermissionListener {

    @Override
    public boolean onDenied() {
        return false;
    }

    @Override
    public void onGiveUp() {

    }
}
