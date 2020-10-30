package com.common.openapi.entity;

import java.io.Serializable;

public class DeviceUnBindData implements Serializable {
    public DeviceUnBindData.RequestData data = new DeviceUnBindData.RequestData();

    public static class RequestData implements Serializable {
        public String token;
        public String deviceId;

    }
}
