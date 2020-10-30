package com.common.openapi.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.Serializable;
import java.util.List;

public class DeviceListData implements Serializable{
    public DeviceListData.RequestData data = new DeviceListData.RequestData();

    public static class Response {
        public DeviceListData.ResponseData data;

        public void parseData(JsonObject json) {
            Gson gson = new Gson();
            this.data = gson.fromJson(json.toString(), DeviceListData.ResponseData.class);
        }
    }

    public static class ResponseData implements Serializable {
        public String count;
        public List<DeviceListElement> deviceList;

        @Override
        public String toString() {
            return "ResponseData{" +
                    "count='" + count + '\'' +
                    ", deviceList=" + deviceList +
                    '}';
        }

        public static class DeviceListElement implements Serializable {
            public String bindId;
            public String deviceId;
            public List<ChannelsElement> channels;
            public List<AplistElement> aplist;

            @Override
            public String toString() {
                return "DeviceListElement{" +
                        "bindId='" + bindId + '\'' +
                        ", deviceId='" + deviceId + '\'' +
                        ", channels=" + channels +
                        ", aplist=" + aplist +
                        '}';
            }

            public static class ChannelsElement implements Serializable {
                public String channelName;
                public String channelId;

                @Override
                public String toString() {
                    return "ChannelsElement{" +
                            "channelName='" + channelName + '\'' +
                            ", channelId='" + channelId + '\'' +
                            '}';
                }
            }

            public static class AplistElement implements Serializable {
                public String apId;
                public String apName;
                public String apType;

                @Override
                public String toString() {
                    return "AplistElement{" +
                            "apId='" + apId + '\'' +
                            ", apName='" + apName + '\'' +
                            ", apType='" + apType + '\'' +
                            '}';
                }
            }
        }

    }

    public static class RequestData implements Serializable {
        public String token;
        public String limit="8";
        public String type="bind";
        public String needApInfo="false";
        public long baseBindId=-1;
        public long openBindId=-1;

        @Override
        public String toString() {
            return "RequestData{" +
                    "token='" + token + '\'' +
                    ", limit='" + limit + '\'' +
                    ", type='" + type + '\'' +
                    ", needApInfo='" + needApInfo + '\'' +
                    ", baseBindId=" + baseBindId +
                    ", openBindId=" + openBindId +
                    '}';
        }
    }
}
