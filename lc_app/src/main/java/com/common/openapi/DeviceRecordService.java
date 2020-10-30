package com.common.openapi;

import android.os.Message;

import com.common.openapi.entity.CloudRecordsData;
import com.common.openapi.entity.ControlMovePTZData;
import com.common.openapi.entity.DeleteCloudRecordsData;
import com.common.openapi.entity.LocalRecordsData;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessRunnable;
import com.mm.android.deviceaddmodule.mobilecommon.base.LCBusinessHandler;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.BusinessErrorTip;
import com.mm.android.deviceaddmodule.mobilecommon.businesstip.HandleMessageCode;

public class DeviceRecordService {

    /**
     * 倒序查询设备云录像片段
     *
     * @param cloudRecordsData
     * @param cloudRecordCallBack
     */
    public void getCloudRecords(final CloudRecordsData cloudRecordsData, final IGetDeviceInfoCallBack.IDeviceCloudRecordCallBack cloudRecordCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (cloudRecordCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    cloudRecordCallBack.deviceCloudRecord((CloudRecordsData.Response) msg.obj);
                } else {
                    //失败
                    cloudRecordCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    CloudRecordsData.Response response = DeviceInfoOpenApiManager.getCloudRecords(cloudRecordsData);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, response).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 查询设备设备录像片段
     *
     * @param localRecordsData
     * @param deviceLocalRecordCallBack
     */
    public void queryLocalRecords(final LocalRecordsData localRecordsData, final IGetDeviceInfoCallBack.IDeviceLocalRecordCallBack deviceLocalRecordCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (deviceLocalRecordCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    deviceLocalRecordCallBack.deviceLocalRecord((LocalRecordsData.Response) msg.obj);
                } else {
                    //失败
                    deviceLocalRecordCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    LocalRecordsData.Response response = DeviceInfoOpenApiManager.queryLocalRecords(localRecordsData);
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, response).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 云台移动控制接口（V2版本）
     *
     * @param controlMovePTZData
     */
    public void controlMovePTZ(final ControlMovePTZData controlMovePTZData) {
        new BusinessRunnable(new LCBusinessHandler() {

            @Override
            public void handleBusiness(Message msg) {

            }
        }) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    DeviceInfoOpenApiManager.controlMovePTZ(controlMovePTZData);
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }

    /**
     * 删除设备云录像片段
     *
     * @param deleteCloudRecordsData
     * @param deviceDeleteRecordCallBack
     */
    public void deleteCloudRecords(final DeleteCloudRecordsData deleteCloudRecordsData, final IGetDeviceInfoCallBack.IDeviceDeleteRecordCallBack deviceDeleteRecordCallBack) {
        final LCBusinessHandler handler = new LCBusinessHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if (deviceDeleteRecordCallBack == null) {
                    return;
                }
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    //成功
                    deviceDeleteRecordCallBack.deleteDeviceRecord();
                } else {
                    //失败
                    deviceDeleteRecordCallBack.onError(BusinessErrorTip.throwError(msg));
                }
            }
        };
        new BusinessRunnable(handler) {
            @Override
            public void doBusiness() throws BusinessException {
                try {
                    for (String id : deleteCloudRecordsData.data.recordRegionIds) {
                        DeviceInfoOpenApiManager.deleteCloudRecords(id);
                    }
                    handler.obtainMessage(HandleMessageCode.HMC_SUCCESS, null).sendToTarget();
                } catch (BusinessException e) {
                    throw e;
                }
            }
        };
    }
}
