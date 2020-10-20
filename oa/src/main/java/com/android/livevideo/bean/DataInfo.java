package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 Goll Lee
 */
public class DataInfo implements Serializable{


    /**
     * msg : 操作成功
     * code : 0
     * data : {"cameraSeq":"1","liveUrl":"http://cmgw-vpc.lechange.com:8888/LCO/5L052DCGAZ1D5AC/0/1/20200821T062645/dev_5L052DCGAZ1D5AC_20200821T062645.m3u8"}
     */

    private String msg;
    private int code;
    private DataBean data;

    @Override
    public String toString() {
        return "DataInfo{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }


    public DataBean getData() {
        return data;
    }


    public static class DataBean {

        private List<MsgInfo> list;

        public List<MsgInfo> getList() {
            return list;
        }

        public void setList(List<MsgInfo> list) {
            this.list = list;
        }

    }
}
