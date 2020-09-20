package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Gool Lee
 */
public class ItemInfo implements Serializable {
    //厂商
    public int id;
    public String str0;
    public String str1;
    public String str2;
    public String str3;
    public String str4;
    public String str5;

    public ItemInfo(int id, String str0, String str1, String str2,
                    String str3, String str4) {
        this.id = id;
        this.str0 = str0;
        this.str1 = str1;
        this.str2 = str2;
        this.str3 = str3;
        this.str4 = str4;
    }

    public ItemInfo() {
    }

    public ItemInfo(String str0, String str1) {
        this.str0 = str0;
        this.str1 = str1;
    }

    public ItemInfo(int id, String str0) {
        this.str0 = str0;
        this.id = id;
    }

    public ItemInfo(int id, String str0, String str1) {
        this.str0 = str0;
        this.str1 = str1;
        this.id = id;
    }

    public ItemInfo(String str0, String str1, String str2) {
        this.str0 = str0;
        this.str1 = str1;
        this.str2 = str2;
    }

    public ItemInfo(int id, String str0, String str1, String str2) {
        this.id = id;
        this.str0 = str0;
        this.str1 = str1;
        this.str2 = str2;
    }
}
