package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Gool Lee
 */
public class MonthItemInfo implements Serializable {


    /**
     * id : 4
     * createTime : 2019-05-07 13:55:29
     * headline : 2019年3月工资汇总
     * name : 工资审核流程
     * applicant : 1
     * status : 1
     * type : WAGE_AUDIT
     * creator : 1
     * applicantName : 超级管理员
     * deptName : 总经办
     * object : {"wageYear":2019,"wageMonth":3}
     * personNum : 7
     * totalWage : 7444
     */

    private int id;
    private String createTime;
    private String headline;
    private String name;
    private int applicant;
    private int status;
    private String type;
    private int creator;
    private String applicantName;
    private String applicantDeptName;
    private String deptName;
    private ObjectBean object;
    private int personNum;
    private float totalWage;
    private String informNameList;
    private List<Integer> informList;
    public List<Integer> getInformList() {
        return informList;
    }

    public String getApplicantDeptName() {
        return applicantDeptName;
    }

    public void setApplicantDeptName(String applicantDeptName) {
        this.applicantDeptName = applicantDeptName;
    }

    public void setInformList(List<Integer> informList) {
        this.informList = informList;
    }

    public String getInformNameList() {
        return informNameList;
    }

    public void setInformNameList(String informNameList) {
        this.informNameList = informNameList;
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

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApplicant() {
        return applicant;
    }

    public void setApplicant(int applicant) {
        this.applicant = applicant;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public float getTotalWage() {
        return totalWage;
    }

    public void setTotalWage(float totalWage) {
        this.totalWage = totalWage;
    }

    public static class ObjectBean implements Serializable {
        /**
         * wageYear : 2019
         * wageMonth : 3
         */

        private int wageYear;
        private String wageMonth;
        private String remark;
        /**
         * informList : [3,71,72,74,89,96,101,112,118,125,126,129,135,139,158,159,161]
         * personNum : 17
         * totalWage : 9889
         * totalHourNum : 35
         * totalPieceNum : 50
         * totalDeduction : 35
         * totalAttendReward : 0
         */

        private int personNum;
        private double totalWage;
        private double totalHourNum;
        private double totalPieceNum;
        private double totalDeduction;
        private double totalAttendReward;


        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getWageYear() {
            return wageYear;
        }

        public void setWageYear(int wageYear) {
            this.wageYear = wageYear;
        }

        public String getWageMonth() {
            return wageMonth;
        }


        public int getPersonNum() {
            return personNum;
        }

        public void setPersonNum(int personNum) {
            this.personNum = personNum;
        }

        public double getTotalWage() {
            return totalWage;
        }

        public void setTotalWage(double totalWage) {
            this.totalWage = totalWage;
        }

        public double getTotalHourNum() {
            return totalHourNum;
        }

        public void setTotalHourNum(double totalHourNum) {
            this.totalHourNum = totalHourNum;
        }

        public double getTotalPieceNum() {
            return totalPieceNum;
        }

        public void setTotalPieceNum(double totalPieceNum) {
            this.totalPieceNum = totalPieceNum;
        }

        public double getTotalDeduction() {
            return totalDeduction;
        }

        public void setTotalDeduction(double totalDeduction) {
            this.totalDeduction = totalDeduction;
        }

        public double getTotalAttendReward() {
            return totalAttendReward;
        }

        public void setTotalAttendReward(int totalAttendReward) {
            this.totalAttendReward = totalAttendReward;
        }

    }
}
