package com.common.openapi.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

public class DeviceDetailListData implements Serializable {
    public DeviceDetailListData.RequestData data = new DeviceDetailListData.RequestData();

    public static class RequestData implements Serializable {
        public List<DeviceListBean> deviceList;
        public static class DeviceListBean implements Serializable {
            public String deviceId;
            public String channelList;
        }
    }

    public static class Response {
        public DeviceDetailListData.ResponseData data;
        public long baseBindId = -1;
        public long openBindId = -1;

        public void parseData(JsonObject json) {
            Gson gson = new Gson();
            this.data = gson.fromJson(json.toString(), DeviceDetailListData.ResponseData.class);
        }
    }

    public static class ResponseData implements Serializable {
        public int count;
        public List<DeviceListBean> deviceList;
        public static class DeviceListBean implements Serializable {
            public String deviceId;//6G083D2PAZC4AD4
            public String status;//online 在线  offline离线
            public String deviceModel;//DH-NVR5016-4KS2
            public String catalog;//NVR
            public String version;//Chn_P_V4.001.0000003.1.20200813
            public String name;//北盘江大桥
            public String ability;//Auth,DHP2P,HSEncrypt,CloudStorage,LocalStorage,PlaybackByFilename,MT,CK,RD,
            public int checkedChannel;// 0
            public String playToken;//aJNSeo5gjyJYum5zsKft5nkX+VlyRkAu9aBP9HY9QoM99XerPheBKaZ6lXX9zhTttwvUUL9GPfbBNfCqJvwve6HVQTu+y3OhtJ6frmkkM/Z5QsQ41
            public int deviceSource;// 1 开放平台返回  2 乐橙设备添加
            public List<ChannelsBean> channels;  //设备列表

            public static class ChannelsBean implements Serializable {
                public String channelId;//"0"
                public String deviceId;//6G083D2PAZC4AD4
                public String channelName;//东塔柱内侧壁
                public String ability;//RemoteControl,PTZ,AudioTalkV1,FrameReverse,AudioEncode,AudioEncodeControl,AlarmMD,MDW,MDS
                public String status;//online 在线
                public String cameraLive;
            }

        }
    }

}
