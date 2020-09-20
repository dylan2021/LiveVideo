package com.android.livevideo.bean;

import java.io.Serializable;

/**
 *Gool
 */
public class ProgressOverdueInfo implements Serializable {


    /**
     * date : 明洞与洞门
     * id : 2
     * plan : 0.98       98%
     * actual : 0.3      30%
     */

    private String name;
    private String planStartDate;
    private int id;
    private double plan;
    private double actual;

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPlan() {
        return plan;
    }

    public void setPlan(double plan) {
        this.plan = plan;
    }

    public double getActual() {
        return actual;
    }

    public void setActual(double actual) {
        this.actual = actual;
    }
}
