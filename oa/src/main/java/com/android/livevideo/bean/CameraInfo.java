package com.android.livevideo.bean;

import java.io.Serializable;

/**
 Goll Lee
 */
public class CameraInfo implements Serializable{


    /**
     * msg : 操作成功
     * code : 0
     * data : {"cameraSeq":"1","liveUrl":"http://cmgw-vpc.lechange.com:8888/LCO/5L052DCGAZ1D5AC/0/1/20200821T062645/dev_5L052DCGAZ1D5AC_20200821T062645.m3u8"}
     */

    private String msg;
    private int code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cameraSeq : 1
         * liveUrl : http://cmgw-vpc.lechange.com:8888/LCO/5L052DCGAZ1D5AC/0/1/20200821T062645/dev_5L052DCGAZ1D5AC_20200821T062645.m3u8
         */

        private String cameraSeq;
        private String liveUrl;

        public String getCameraSeq() {
            return cameraSeq;
        }

        public void setCameraSeq(String cameraSeq) {
            this.cameraSeq = cameraSeq;
        }

        public String getLiveUrl() {
            return liveUrl;
        }

        public void setLiveUrl(String liveUrl) {
            this.liveUrl = liveUrl;
        }
    }
}
