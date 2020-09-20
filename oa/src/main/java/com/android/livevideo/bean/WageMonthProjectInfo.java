package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Dylan
 */
public class WageMonthProjectInfo implements Serializable{
    private List<RecordsBean> records;
    public List<RecordsBean>  getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }
    public static class RecordsBean {

        private int totalHourlyWage;
        private double totalPieceWage;
        private Object totalWage;
        private String deptName;
        private String employeeName;
        private String projectName;
        private double totalWageOnMonth;
        private double totalHourOnMonth;
        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        private  String unit;;

        public int getTotalHourlyWage() {
            return totalHourlyWage;
        }

        public void setTotalHourlyWage(int totalHourlyWage) {
            this.totalHourlyWage = totalHourlyWage;
        }

        public double getTotalPieceWage() {
            return totalPieceWage;
        }

        public void setTotalPieceWage(double totalPieceWage) {
            this.totalPieceWage = totalPieceWage;
        }

        public Object getTotalWage() {
            return totalWage;
        }

        public void setTotalWage(Object totalWage) {
            this.totalWage = totalWage;
        }

        public Object getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public Object getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public double getTotalWageOnMonth() {
            return totalWageOnMonth;
        }

        public void setTotalWageOnMonth(double totalWageOnMonth) {
            this.totalWageOnMonth = totalWageOnMonth;
        }

        public double getTotalHourOnMonth() {
            return totalHourOnMonth;
        }

        public void setTotalHourOnMonth(double totalHourOnMonth) {
            this.totalHourOnMonth = totalHourOnMonth;
        }
    }
}
