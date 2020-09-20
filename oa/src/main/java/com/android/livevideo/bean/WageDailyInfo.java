package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Gool Lee
 */
public class WageDailyInfo implements Serializable {


    private int deptId;
    private int pieceWageId;
    private String workDate;
    public int status;
    private String deptName;
    private String projectName;
    private int totalHourNum;
    private int pieceMonthCategoryId;
    private double totalHourlyWage;
    private double totalPieceNum;
    private double totalPieceWage;
    private double totalDeduction;
    private double totalWage;
    private int peopleNum;
    private String unit;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPieceMonthCategoryId() {
        return pieceMonthCategoryId;
    }

    public void setPieceMonthCategoryId(int pieceMonthCategoryId) {
        this.pieceMonthCategoryId = pieceMonthCategoryId;
    }

    //小料包
    private String pieceMonthCategoryName;
    private double totalPieceMonthNum;

    public String getPieceMonthCategoryName() {
        return pieceMonthCategoryName;
    }

    public void setPieceMonthCategoryName(String pieceMonthCategoryName) {
        this.pieceMonthCategoryName = pieceMonthCategoryName;
    }

    public double getTotalPieceMonthNum() {
        return totalPieceMonthNum;
    }

    public void setTotalPieceMonthNum(double totalPieceMonthNum) {
        this.totalPieceMonthNum = totalPieceMonthNum;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getPieceWageId() {
        return pieceWageId;
    }

    public void setPieceWageId(int pieceWageId) {
        this.pieceWageId = pieceWageId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getTotalHourNum() {
        return totalHourNum;
    }


    public double getTotalHourlyWage() {
        return totalHourlyWage;
    }


    public double getTotalPieceNum() {
        return totalPieceNum;
    }


    public double getTotalPieceWage() {
        return totalPieceWage;
    }


    public double getTotalDeduction() {
        return totalDeduction;
    }


    public double getTotalWage() {
        return totalWage;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
