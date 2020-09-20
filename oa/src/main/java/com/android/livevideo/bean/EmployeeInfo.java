package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Gool Lee
 */
public class EmployeeInfo implements Serializable {

    private int id;
    private boolean seleted;
    private String createTime;
    private String updateTime;
    private String employeeName;
    private int employeeId;
    private String employeeNo;
    private Object employeeCardNo;
    private Object employeeLevel;
    private Object graduateEducation;
    private Object graduateMajor;
    private Object graduateSchool;
    private Object graduateDate;
    private String employmentDate;
    private Object correctionDate;
    private String probationPeriod;
    private String employeeSex;
    private String employeeGender;
    private String employeeTypeStr;
    private String employeeBirthday;
    private String employeeEmail;
    private String positionName;
    private String employeeMobile;
    private String employeePhone;
    private Object employeeType;


    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    private String employeeStatus;
    private int employeeOrder;
    private int deptId;
    private int positionId;
    private boolean isChildChecked;

    public String getEmployeeTypeStr() {
        return employeeTypeStr;
    }

    public void setEmployeeTypeStr(String employeeTypeStr) {
        this.employeeTypeStr = employeeTypeStr;
    }

    private String regionCode;
    private String emergencyName;
    private Object emergencyPhone;
    private Object emergencyRelation;
    private Object address;
    private Object ethnicity;
    private Object maritalStatus;
    private Object leaveReason;
    private Object leaveDate;

    public EmployeeInfo(int id, String employeeName, String regionCode) {
        this.id = id;
        this.employeeName = employeeName;
        this.regionCode = regionCode;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public boolean getSeleted() {
        return seleted;
    }

    public void setSeleted(boolean seleted) {
        this.seleted = seleted;
    }

    public EmployeeInfo(String employeeName) {
        this.employeeName = employeeName;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public Object getEmployeeCardNo() {
        return employeeCardNo;
    }

    public void setEmployeeCardNo(Object employeeCardNo) {
        this.employeeCardNo = employeeCardNo;
    }

    public Object getEmployeeLevel() {
        return employeeLevel;
    }

    public void setEmployeeLevel(Object employeeLevel) {
        this.employeeLevel = employeeLevel;
    }

    public Object getGraduateEducation() {
        return graduateEducation;
    }

    public void setGraduateEducation(Object graduateEducation) {
        this.graduateEducation = graduateEducation;
    }

    public Object getGraduateMajor() {
        return graduateMajor;
    }

    public void setGraduateMajor(Object graduateMajor) {
        this.graduateMajor = graduateMajor;
    }

    public Object getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(Object graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public Object getGraduateDate() {
        return graduateDate;
    }

    public void setGraduateDate(Object graduateDate) {
        this.graduateDate = graduateDate;
    }

    public String getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Object getCorrectionDate() {
        return correctionDate;
    }

    public void setCorrectionDate(Object correctionDate) {
        this.correctionDate = correctionDate;
    }

    public String getProbationPeriod() {
        return probationPeriod;
    }

    public void setProbationPeriod(String probationPeriod) {
        this.probationPeriod = probationPeriod;
    }

    public String getEmployeeSex() {
        return employeeSex;
    }

    public String getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    public void setEmployeeSex(String employeeSex) {
        this.employeeSex = employeeSex;
    }

    public String getEmployeeBirthday() {
        return employeeBirthday;
    }

    public void setEmployeeBirthday(String employeeBirthday) {
        this.employeeBirthday = employeeBirthday;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeMobile() {
        return employeeMobile;
    }

    public void setEmployeeMobile(String employeeMobile) {
        this.employeeMobile = employeeMobile;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public Object getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Object employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public int getEmployeeOrder() {
        return employeeOrder;
    }

    public void setEmployeeOrder(int employeeOrder) {
        this.employeeOrder = employeeOrder;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public Object getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(Object emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public Object getEmergencyRelation() {
        return emergencyRelation;
    }

    public void setEmergencyRelation(Object emergencyRelation) {
        this.emergencyRelation = emergencyRelation;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Object ethnicity) {
        this.ethnicity = ethnicity;
    }

    public Object getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Object maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Object getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(Object leaveReason) {
        this.leaveReason = leaveReason;
    }

    public Object getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Object leaveDate) {
        this.leaveDate = leaveDate;
    }
}
