package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 Goll Lee
 */
public class CamerasListInfo implements Serializable{

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cameraId : 1
         * cameraAddr : 桥上左1
         * cameraMsg : 这个点位风光最好
         * cameraOrder : 1
         * onlineStatus : 1
         */

        private int cameraId;
        private String cameraAddr;
        private String cameraMsg;
        private int cameraOrder;
        private String onlineStatus;

        public int getCameraId() {
            return cameraId;
        }

        public void setCameraId(int cameraId) {
            this.cameraId = cameraId;
        }

        public String getCameraAddr() {
            return cameraAddr;
        }

        public void setCameraAddr(String cameraAddr) {
            this.cameraAddr = cameraAddr;
        }

        public String getCameraMsg() {
            return cameraMsg;
        }

        public void setCameraMsg(String cameraMsg) {
            this.cameraMsg = cameraMsg;
        }

        public int getCameraOrder() {
            return cameraOrder;
        }

        public void setCameraOrder(int cameraOrder) {
            this.cameraOrder = cameraOrder;
        }

        public String getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(String onlineStatus) {
            this.onlineStatus = onlineStatus;
        }
    }
}
