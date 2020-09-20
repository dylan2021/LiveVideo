package com.android.livevideo.bean;

/**
 * Created by gp on 2017/2/21 0021.
 */

public class SearchHistoryBean {
    public int id;
    public String title;
    public String type;
    public Long date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
