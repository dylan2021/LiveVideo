package com.android.livevideo.bean;


import java.io.Serializable;

/**
 * Gool Lee
 */
public class TripItemInfo implements Serializable {
    private int id;
    private String titleName;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    private long startTimeL;
    private String startTime;
    private long endTimeL;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private String vehicle;
    private String type;
    private String departure;
    private String destination;
    private double period;


    public TripItemInfo(int id, String vehicle, String type,
                        long startTimeL, long endTimeL) {
        this.id = id;
        this.vehicle = vehicle;
        this.type = type;
        this.startTimeL = startTimeL;
        this.endTimeL = endTimeL;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTimeL() {
        return startTimeL;
    }

    public void setStartTimeL(long startTimeL) {
        this.startTimeL = startTimeL;
    }

    public long getEndTimeL() {
        return endTimeL;
    }

    public void setEndTimeL(long endTimeL) {
        this.endTimeL = endTimeL;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }
}
