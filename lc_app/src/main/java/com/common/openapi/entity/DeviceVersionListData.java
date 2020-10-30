package com.common.openapi.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

public class DeviceVersionListData implements Serializable {
    public DeviceVersionListData.RequestData data = new DeviceVersionListData.RequestData();

    public static class Response {
        public DeviceVersionListData.ResponseData data;

        public void parseData(JsonObject json) {
            Gson gson = new Gson();
            this.data = gson.fromJson(json.toString(),  DeviceVersionListData.ResponseData.class);
        }
    }

    public static class ResponseData implements Serializable {
        public String count;
        public List<DeviceVersionListData.ResponseData.DeviceVersionListBean> deviceVersionList;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public static class DeviceVersionListBean implements Serializable {
            public boolean canBeUpgrade;
            public String deviceId;
            public String version;
            public UpgradeInfoBean upgradeInfo;

            public boolean isCanBeUpgrade() {
                return canBeUpgrade;
            }

            public void setCanBeUpgrade(boolean canBeUpgrade) {
                this.canBeUpgrade = canBeUpgrade;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public UpgradeInfoBean getUpgradeInfo() {
                return upgradeInfo;
            }

            public void setUpgradeInfo(UpgradeInfoBean upgradeInfo) {
                this.upgradeInfo = upgradeInfo;
            }

            public static class UpgradeInfoBean implements Serializable {
                public String version;
                public String description;
                public String packageUrl;

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getPackageUrl() {
                    return packageUrl;
                }

                public void setPackageUrl(String packageUrl) {
                    this.packageUrl = packageUrl;
                }
            }
        }
    }

    public static class RequestData implements Serializable {
        public String token;
        public String deviceIds;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDeviceIds() {
            return deviceIds;
        }

        public void setDeviceIds(String deviceIds) {
            this.deviceIds = deviceIds;
        }
    }

}
