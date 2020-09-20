package com.android.livevideo.bean;

import java.io.Serializable;

/**
 *Gool
 */
public class FileInfo implements Serializable {
    public String name;
    public String url;

    public FileInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
