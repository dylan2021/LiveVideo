package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Gool
 */
public class EmplyeeWageInfo implements Serializable {

    private int id;
    private String createTime;
    private String updateTime;
    private int employeeId;
    private String workDate;
    private int pieceWageId;
    private String hourNum;
    private String pieceNum;
    private String pieceMonthNum;
    /**
     * deduction : {"waterAmount":0,"fondAmount":0,"stayAmount":0,"attendAmount":0,"qualityAmount":0,"otherAmount":0}
     */

    private Object deduction;

    public String getPieceMonthNum() {
        return pieceMonthNum;
    }

    public void setPieceMonthNum(String pieceMonthNum) {
        this.pieceMonthNum = pieceMonthNum;
    }

    public double getTotalPieceMonthPrice() {
        return totalPieceMonthPrice;
    }

    public void setTotalPieceMonthPrice(double totalPieceMonthPrice) {
        this.totalPieceMonthPrice = totalPieceMonthPrice;
    }

    private double totalPieceMonthPrice;
    private String remark;
    private String projectName;
    private int creator;
    private double hourlyWage;
    private String pieceWage;
    private double attendReward;
    private double incomeTax;
    private double taxDeduction;
    private String employeeTypeStr;
    private String deptName;
    private String payableWage;
    private double taxAmount;
    private double realWage;
    private int deptId;
    private String employeeType;
    private String unit;
    private String employeeName;
    private double totalWage;

    private int wageYear;
    private int wageMonth;

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

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public int getPieceWageId() {
        return pieceWageId;
    }

    public void setPieceWageId(int pieceWageId) {
        this.pieceWageId = pieceWageId;
    }

    public String getHourNum() {
        return hourNum;
    }

    public void setHourNum(String hourNum) {
        this.hourNum = hourNum;
    }

    public String getPieceNum() {
        return pieceNum;
    }

    public void setPieceNum(String pieceNum) {
        this.pieceNum = pieceNum;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public String getPieceWage() {
        return pieceWage;
    }

    public void setPieceWage(String pieceWage) {
        this.pieceWage = pieceWage;
    }

    public double getAttendReward() {
        return attendReward;
    }

    public void setAttendReward(double attendReward) {
        this.attendReward = attendReward;
    }

    public double getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(double incomeTax) {
        this.incomeTax = incomeTax;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public String getEmployeeTypeStr() {
        return employeeTypeStr;
    }

    public void setEmployeeTypeStr(String employeeTypeStr) {
        this.employeeTypeStr = employeeTypeStr;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPayableWage() {
        return payableWage;
    }

    public void setPayableWage(String payableWage) {
        this.payableWage = payableWage;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getRealWage() {
        return realWage;
    }

    public void setRealWage(double realWage) {
        this.realWage = realWage;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getTotalWage() {
        return totalWage;
    }

    public void setTotalWage(double totalWage) {
        this.totalWage = totalWage;
    }

    public int getWageYear() {
        return wageYear;
    }

    public void setWageYear(int wageYear) {
        this.wageYear = wageYear;
    }

    public int getWageMonth() {
        return wageMonth;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setWageMonth(int wageMonth) {
        this.wageMonth = wageMonth;
    }

    public Object  getDeduction() {
        return deduction;
    }
}
