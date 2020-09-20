package com.android.livevideo.bean;

/**
 * Gool Lee
 */
public class ProjDeptInfo {

    /**
     * id : 2
     * createTime : 2019-04-29 08:38:24
     * projectName : 测试项目
     * wage : 12
     * unit : 件
     * remark : 这个是测试项目
     * creator : 1
     */

    private int id;
    private String createTime;
    private String projectName;
    private String wage;
    private String unit;
    private String remark;
    private String deptName;
    private int creator;


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }
}
