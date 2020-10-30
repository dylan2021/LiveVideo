package com.common.openapi.entity;

import java.io.Serializable;
import java.util.List;

public class DeleteCloudRecordsData implements Serializable {
    public DeleteCloudRecordsData.RequestData data = new DeleteCloudRecordsData.RequestData();

    public static class RequestData implements Serializable {
        public String token;
        public List<String> recordRegionIds;

    }
}
