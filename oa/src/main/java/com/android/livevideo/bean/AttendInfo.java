package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Gool
 */
public class AttendInfo implements Serializable {


    /**
     * attendDays : 0
     * comeLateTimes : 0
     * leaveEarlyTimes : 0
     * amMissRecordTimes : 0
     * pmMissRecordTimes : 0
     * absentDays : 0
     * businessTripDays : 0
     * employeeName : 超级管理员
     * deptName : 总经办
     * totalLeave : 0
     * totalOvertime : 0
     */
    public int attendDays;
    public int comeLateTimes;
    public int leaveEarlyTimes;
    public int amMissRecordTimes;
    public int pmMissRecordTimes;
    public int absentDays;
    public int businessTripDays;
    public String employeeName;
    public String deptName;
    public int totalLeave;
    public int totalOvertime;

 
}
