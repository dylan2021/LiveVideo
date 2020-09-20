package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Gool
 */
public class ProgressOverdueObj implements Serializable {


    /**
     * date : 明洞与洞门
     * id : 2
     * plan : 0.98       98%
     * actual : 0.3      30%
     */

    private String name;
    private int id;
    private double plan;
    private double actual;
    /**
     * minDate : 2018/11/15
     * data : [{"name":"其他","id":1,"plan":0.07,"actual":0.05},{"name":"明洞与洞门","id":2,"plan":0.18,"actual":0.02},{"name":"填方路基","id":5,"plan":0.41,"actual":0}]
     * maxDate : 2018/12/01
     */

    private String minDate;
    private String maxDate;
    private List<ProgressOverdueInfo> data;

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

    public String getMinDate() {
        return minDate;
    }

    public void setMinDate(String minDate) {
        this.minDate = minDate;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public List<ProgressOverdueInfo> getData() {
        return data;
    }

    public void setData(List<ProgressOverdueInfo> data) {
        this.data = data;
    }
}
