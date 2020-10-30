package com.mm.android.deviceaddmodule.mobilecommon.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;
import com.mm.android.deviceaddmodule.mobilecommon.utils.BusinessAuthUtil;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 文件描述：文件名、类名 功能说明： 版权申明：
 * 
 */

public abstract class BaseHandler extends Handler {

    private final static String TAG = "LeChange.BaseHandler";

    private AtomicBoolean isCancled = new AtomicBoolean(false);

    public BaseHandler(){
        super();
    }
    public BaseHandler(Looper looper){
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (!isCancled.get()) {
            if (msg.what == HandleMessageCode.HMC_EXCEPTION) {
                LogUtil.debugLog(TAG, "base hander throw exception. what =" + msg.what);

                if (BusinessAuthUtil.isAuthFailed(msg.arg1)) {
                    authError(msg);
                    return ;
                }
            }
            handleBusiness(msg);
        }
    }

    /**
     * 回调信息
     * 
     * @param msg
     */
    public abstract void handleBusiness(Message msg);

    /**
     * 鉴权信息失败
     */
    public void authError(Message msg) {

    }

    /**
     * 取消数据回调
     */
    public void cancle() {
        isCancled.set(true);
    }

    /**
     * 是否继续运行
     * 
     * @return
     */
    public boolean isCanceled() {
        return isCancled.get();
    }
}
