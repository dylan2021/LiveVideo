package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Goll Lee
 */
public class FileListInfo implements Serializable {
    public int fileId;
    public String fileName;
    public String filePath;
    public String fileUrl;
    public long fileSize;

    public FileListInfo(String fileName, String filePath, long fileSize, String url) {

        this.fileName = fileName;
        this.fileUrl = url;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public FileListInfo(String fileName, String filePath,String url) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileUrl = url;
    }
}
