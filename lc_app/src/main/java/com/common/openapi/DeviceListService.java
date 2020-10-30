package com.common.openapi;

import android.os.Message;

import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceListData;
import com.google.gson.Gson;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessRunnable;
import com.mm.android.deviceaddmodule.mobilecommon.base.LCBusinessHandler;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.BusinessErrorTip;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;

import java.util.ArrayList;
import java.util.List;

public class DeviceListService {

    public static final int pageSize=8;


    /**
     * 获取设备列表 1：开放平台添加的 2：乐橙App添加的
     *
     * @param deviceListData
     * @param getDeviceListCallBack
     */
    public void deviceBaseList(final DeviceListData deviceListData, final IGetDeviceInfoCallBack.IDeviceListCallBack getDeviceListCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (getDeviceListCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    getDeviceListCallBack.deviceList((DeviceDetailListData.Response) msg.obj);
                } else {
                    //失败
                    getDeviceListCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    dealDeviceDetailList(handler, deviceListData);
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    private void dealDeviceDetailList(LCBusinessHandler handler, DeviceListData deviceListData) throws BusinessException {
        if (deviceListData == null) {
            deviceListData = new DeviceListData();
        }
        //成功返回值
        DeviceDetailListData.Response result = null;
        //获取乐橙添加的设备
        DeviceDetailListData.Response deviceBaseDetailList = getDeviceDetailListFromCloud(false, deviceListData);
        //乐橙返回
        if (deviceBaseDetailList.data != null && deviceBaseDetailList.data.deviceList != null && deviceBaseDetailList.data.deviceList.size() > 0) {
            result = new DeviceDetailListData.Response();
            result.data = new DeviceDetailListData.ResponseData();
            result.data.count = deviceBaseDetailList.data.deviceList.size();
            result.data.deviceList = new ArrayList<>();
            for (DeviceDetailListData.ResponseData.DeviceListBean a : deviceBaseDetailList.data.deviceList) {
                DeviceDetailListData.ResponseData.DeviceListBean b = new DeviceDetailListData.ResponseData.DeviceListBean();
                Gson gson = new Gson();
                b = gson.fromJson(gson.toJson(a), DeviceDetailListData.ResponseData.DeviceListBean.class);
                b.deviceSource=2;
                result.data.deviceList.add(b);
            }
            result.baseBindId = deviceBaseDetailList.baseBindId;
            if (result.data.deviceList.size() >= pageSize) {
                //单次已经达到8条不再拉取
                handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, result).sendToTarget();
                return;
            } else {
                //单次没有达到8条
                deviceListData.data.limit = (8 - result.data.deviceList.size()) + "";
            }
        }
        //获取开放平台添加的设备
        DeviceDetailListData.Response deviceOpenDetailList = getDeviceDetailListFromCloud(true, deviceListData);
        //开放平台返回
        if (deviceOpenDetailList.data != null && deviceOpenDetailList.data.deviceList != null && deviceOpenDetailList.data.deviceList.size() > 0) {
            if (result == null) {
                result = new DeviceDetailListData.Response();
                result.data = new DeviceDetailListData.ResponseData();
                result.data.count = deviceOpenDetailList.data.deviceList.size();
                result.data.deviceList = new ArrayList<>();
                for (DeviceDetailListData.ResponseData.DeviceListBean a : deviceOpenDetailList.data.deviceList) {
                    DeviceDetailListData.ResponseData.DeviceListBean b = new DeviceDetailListData.ResponseData.DeviceListBean();
                    Gson gson = new Gson();
                    b = gson.fromJson(gson.toJson(a), DeviceDetailListData.ResponseData.DeviceListBean.class);
                    b.deviceSource=1;
                    result.data.deviceList.add(b);
                }
            } else {
                result.data.count = deviceOpenDetailList.data.count + result.data.count;
                for (DeviceDetailListData.ResponseData.DeviceListBean a : deviceOpenDetailList.data.deviceList) {
                    DeviceDetailListData.ResponseData.DeviceListBean b = new DeviceDetailListData.ResponseData.DeviceListBean();
                    Gson gson = new Gson();
                    b = gson.fromJson(gson.toJson(a), DeviceDetailListData.ResponseData.DeviceListBean.class);
                    result.data.deviceList.add(b);
                }
            }
            result.openBindId = deviceOpenDetailList.openBindId;
        }
        if (result == null) {
            result = new DeviceDetailListData.Response();
        }
        handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, result).sendToTarget();
    }

    private DeviceDetailListData.Response getDeviceDetailListFromCloud(boolean isOpenPlatform, DeviceListData deviceListDat) throws BusinessException {
        DeviceDetailListData.Response result = new DeviceDetailListData.Response();
        DeviceListData.Response deviceList = null;
        //获取到设备基本信息列表
        if (isOpenPlatform) {
            deviceList = DeviceInfoOpenApiManager.deviceOpenList(deviceListDat);
        } else {
            deviceList = DeviceInfoOpenApiManager.deviceBaseList(deviceListDat);
        }
        if (deviceList.data == null || deviceList.data.deviceList == null || deviceList.data.deviceList.size() == 0) {
            return result;
        }
        //获取设备详情列表request参数封装
        DeviceDetailListData deviceDetailListData = new DeviceDetailListData();
        List<DeviceDetailListData.RequestData.DeviceListBean> deviceListBeans = new ArrayList<>();
        for (DeviceListData.ResponseData.DeviceListElement deviceListElement : deviceList.data.deviceList) {
            DeviceDetailListData.RequestData.DeviceListBean deviceListBean = new DeviceDetailListData.RequestData.DeviceListBean();
            deviceListBean.deviceId = deviceListElement.deviceId;
            StringBuilder stringBuilder=new StringBuilder();
            if (deviceListElement.channels.size()>0){
                for (DeviceListData.ResponseData.DeviceListElement.ChannelsElement channelsElement:deviceListElement.channels){
                    stringBuilder.append(channelsElement.channelId).append(",");
                }
                deviceListBean.channelList=stringBuilder.substring(0,stringBuilder.length()-1);
            }
            deviceListBeans.add(deviceListBean);
        }
        deviceDetailListData.data.deviceList = deviceListBeans;
        //获取设备详情列表
        if (isOpenPlatform) {
            result = DeviceInfoOpenApiManager.deviceOpenDetailList(deviceDetailListData);
        } else {
            result = DeviceInfoOpenApiManager.deviceBaseDetailList(deviceDetailListData);
        }
        if (result == null) {
            result = new DeviceDetailListData.Response();
        }
        if (isOpenPlatform) {
            result.openBindId = Long.parseLong(deviceList.data.deviceList.get(deviceList.data.deviceList.size() - 1).bindId);
        } else {
            result.baseBindId = Long.parseLong(deviceList.data.deviceList.get(deviceList.data.deviceList.size() - 1).bindId);
        }
        return result;
    }
}
