package com.android.livevideo.bean;

/**
Gool Lee
 */

public class HistoryInfo {

    /**
     * taskName : 财务主管审批
     * employeeName :超级管理员
     * auditTime : 2019-05-18 13:24:00
     * auditStaus : 已同意
     */

    private String taskName;
    private String employeeName;
    private String auditTime;
    private String auditStaus;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditStaus() {
        return auditStaus;
    }

    public void setAuditStaus(String auditStaus) {
        this.auditStaus = auditStaus;
    }
}