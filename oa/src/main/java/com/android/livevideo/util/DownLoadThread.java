package com.android.livevideo.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载工具fi
 */
public class DownLoadThread extends Thread {
    private String downLoadUrl,name;
    private Context context;
    private FileOutputStream out = null;
    private File downLoadFile = null;
    private File sdCardFile = null;
    private InputStream in = null;
    public DownLoadThread( Context context,String downLoadUrl,String name) {
        super();
        this.downLoadUrl = downLoadUrl;
        this.name = name;
        this.context = context;
    }
    @Override
    public void run() {
        try {
            URL httpUrl = new URL(downLoadUrl);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setDoInput(true);// 如果打算使用 URL 连接进行输入，则将 DoInput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 true。
            conn.setDoOutput(true);// 如果打算使用URL 连接进行输出，则将 DoOutput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 false。
            in = conn.getInputStream();
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(context, "SD卡不可用！", Toast.LENGTH_SHORT).show();
                return;
            }
            downLoadFile = Environment.getExternalStorageDirectory();
            sdCardFile = new File(downLoadFile, name);
            out = new FileOutputStream(sdCardFile);
            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}