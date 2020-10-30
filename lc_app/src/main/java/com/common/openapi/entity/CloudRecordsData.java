package com.common.openapi.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

public class CloudRecordsData implements Serializable {
    public CloudRecordsData.RequestData data = new CloudRecordsData.RequestData();

    public static class Response {
        public CloudRecordsData.ResponseData data;

        public void parseData(JsonObject json) {
            Gson gson = new Gson();
            this.data = gson.fromJson(json.toString(),  CloudRecordsData.ResponseData.class);
        }
    }

    public static class ResponseData implements Serializable {
        public List<CloudRecordsData.ResponseData.RecordsBean> records;

        public static class RecordsBean implements Serializable {
            public String recordId;
            public String deviceId;
            public String channelId;
            public String beginTime;
            public String endTime;
            public String size;
            public String thumbUrl;
            public int encryptMode;
            public String recordRegionId;
            public String type;
        }
    }

    public static class RequestData implements Serializable {
        public String token;
        public String deviceId;
        public String channelId = "0";
        public String beginTime;
        public String endTime;
        public long nextRecordId=-1;
        public long count;
        public String queryRange;
    }
}
