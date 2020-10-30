package com.mm.android.deviceaddmodule.contract;

import com.mm.android.deviceaddmodule.base.IBasePresenter;
import com.mm.android.deviceaddmodule.base.IBaseView;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.ScanResult;

/**
 * 二维码扫描契约类，定义二维码扫描页面相关View层和Presenter层的接口
 **/
public interface ScanContract {
    interface Presenter extends IBasePresenter{
        ScanResult parseScanStr(String scanStr, String sc);        //解析扫描到的二维码
        void getDeviceInfo(String deviceSn, String deviceCodeModel);   //从服务获取设备信息
        boolean isManualInputPage();                    //是否为手动输入设备序列号页面
        boolean isSnInValid(String sn);
        void recyle();
        void resetCache();
        boolean isScCodeInValid(String scCode);
    }

    interface View extends IBaseView<Presenter>{
        void goTypeChoosePage();            //跳转到设备类型选择页
        void goNotSupportBindTipPage();     //跳转到不支持绑定的设备页面
        void goOtherUserBindTipPage();      //跳转至设备被其他用户绑定提示页
        void showAddBoxTip();               //盒子添加提示，维持乐橙逻辑
        void goCloudConnectPage();         //跳转至云平台连接页
        void goDeviceLoginPage();            //跳转至设备登录页
        void goSecCodePage();               //跳转至安全码验证页
        void goDeviceBindPage();            //跳转至设备绑定页
        void goIMEIInputPage();            //跳转至输入imei页
    }
}
