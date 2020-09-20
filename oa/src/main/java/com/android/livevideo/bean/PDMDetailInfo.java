package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Gool Lee
 */
public class PDMDetailInfo implements Serializable {


    private int id;
    private String createTime;
    private String updateTime;

    //设备
    private String code;
    private String name;
    private String owner;
    private String ownerPhone;
    private String spec;
    private String status;
    private String source;
    private int deviceId;
    private String realInDate;
    private String realOutDate;


    //材料
    private int numbers;
    private String type;
    private String user;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

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

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getRealInDate() {
        return realInDate;
    }

    public void setRealInDate(String realInDate) {
        this.realInDate = realInDate;
    }

    public String getRealOutDate() {
        return realOutDate;
    }

    public void setRealOutDate(String realOutDate) {
        this.realOutDate = realOutDate;
    }
}
