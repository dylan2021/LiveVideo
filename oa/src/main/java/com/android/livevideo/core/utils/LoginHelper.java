package com.android.livevideo.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户登录辅助类
 * Created by zeng on 2016/7/26.
 */
public class LoginHelper {

    public static final String TAG = "777";

    private Context context;
    private SharedPreferences preferences;
    private String userName;
    private String passWord;

    public LoginHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(Constant.CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        userName = preferences.getString(KeyConst.username, "");
        passWord = preferences.getString(Constant.sp_pwd, "");
    }

    /**
     * 重新登录
     */
    public void reLogin() {

    }
}
