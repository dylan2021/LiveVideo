package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 */
public class StatInfo implements Serializable {

    private int id;
    private String name;
    private List<EmployeeInfo> details;

    public StatInfo(int id, String name, List<EmployeeInfo> details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmployeeInfo> getDetails() {
        return details;
    }

    public void setDetails(List<EmployeeInfo> details) {
        this.details = details;
    }

}
