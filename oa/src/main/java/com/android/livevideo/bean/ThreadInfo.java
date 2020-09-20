package com.android.livevideo.bean;

import java.io.Serializable;

/**
 Goll Lee
 */
public class ThreadInfo implements Serializable{

    public int id;          //数据库自增长ID
    public String name;     //文件名
    public String url;      //下载地址
    public long start;       //原始起始下载位置
    public long finished;    //已下载的数据
    public long end;         //结束下载位置

    public ThreadInfo() {}

    public ThreadInfo(String name, String url, long start, long end) {
        this.name = name;
        this.url = url;
        this.start = start;
        this.end = end;
    }
}
