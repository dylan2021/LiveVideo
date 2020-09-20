package com.android.livevideo.act_other;

import java.io.Serializable;

/**
 *Gool Lee
 */
public class PictBean implements Serializable{
    private String localURL; //图片本地地址

    public String getLocalURL() {
        return localURL;
    }

    public void setLocalURL(String localURL) {
        this.localURL = localURL;
    }
}
