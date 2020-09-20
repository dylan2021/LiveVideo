package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Gool Lee
 */
public class KeyValueInfo implements Serializable {
    private String itemKey;
    private String itemValue;

    public KeyValueInfo(String itemKey, String itemValue) {
        this.itemKey = itemKey;
        this.itemValue = itemValue;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}
