package com.android.livevideo.bean;

import java.util.List;

/**
 * 字典
 */
public class DictInfo {

    private int id;
    private String createTime;
    private String updateTime;
    private String code;
    private String name;
    private String desc;
    private String type;
    private String status;
    private Object creator;
    private Object updator;
    private List<DictValuesBean> dictValues;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getCreator() {
        return creator;
    }

    public void setCreator(Object creator) {
        this.creator = creator;
    }

    public Object getUpdator() {
        return updator;
    }

    public void setUpdator(Object updator) {
        this.updator = updator;
    }

    public List<DictValuesBean> getDictValues() {
        return dictValues;
    }

    public void setDictValues(List<DictValuesBean> dictValues) {
        this.dictValues = dictValues;
    }

    public static class DictValuesBean {
        /**
         * code : aircraft
         * name : 飞机
         * value : 1
         * desc : 这是交通工具飞机
         * status : 1
         */

        private String code;
        private String name;
        private String value;
        private String desc;
        private String status;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
