package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * 通用JSON返回结果
 *
 */
public class JsonResult<T> implements Serializable{

    public int code;
    public String msg;
    public T data;
    public Object map;
    public String innerResult;
    public int page;
    public int pageSize;
    public int totals;

}
