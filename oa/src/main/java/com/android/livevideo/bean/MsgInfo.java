package com.android.livevideo.bean;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Gool Lee
 */
public class MsgInfo implements Serializable {

    public int id;
    public String title;
    public String time;
    public String startTime;
    public String endTime;
    public int status;
    public String createTime;
    public String updateTime;
    public String content;
    public String msgType;
    public String summary;
    public String isAudit;
    public String publishTime;
    public String publisherName;
    public String publisherDeptName;
    public String headline;
    public String name;
    public int applicant;
    public String type;
    public String procInstId;
    public String creator;
    public String createName;
    public JsonElement informList;
    public JsonObject object;
    public String applicantName;
    public String deptName;
    public String applicantDeptName;
    public String informNameList;
    public String auditor;
    public String auditorId;
    public String procNum;
    public String createDeptName;
    public String isRead;
    public String lastNode;
    public String directorNameOfApplicant;
    public int resultStatus;
    public double resultValue;
    public String resultPic;


    public MsgInfo(int id, String title, String time, String msgType,
                   String startTime, String endTime, int status) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.msgType = msgType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

}
