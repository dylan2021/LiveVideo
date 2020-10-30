package com.common.openapi.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

public class LocalRecordsData implements Serializable {
    public LocalRecordsData.RequestData data = new LocalRecordsData.RequestData();

    public static class Response {
        public LocalRecordsData.ResponseData data;

        public void parseData(JsonObject json) {
            Gson gson = new Gson();
            this.data = gson.fromJson(json.toString(),   LocalRecordsData.ResponseData.class);
        }
    }

    public static class ResponseData implements Serializable {
        public List<LocalRecordsData.ResponseData.RecordsBean> records;

        public static class RecordsBean implements Serializable {
            public String recordId;
            public long fileLength;
            public String channelID;
            public String beginTime;
            public String endTime;
            public String type;
        }
    }

    public static class RequestData implements Serializable {
        public String token;
        public String deviceId;
        public String channelId = "0";
        public String beginTime;
        public String endTime;
        public String type;
        public String queryRange;

    }
}
