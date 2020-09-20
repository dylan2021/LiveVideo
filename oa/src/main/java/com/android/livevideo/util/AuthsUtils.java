package com.android.livevideo.util;

import android.util.Log;
import android.view.View;

import com.android.livevideo.App;

/**
 * Gool
 */
public class AuthsUtils {
    //有权限->显示   无->隐藏
    public static void setViewAuth(View view, String viewAuthStr) {
        boolean isContains = false;
        Log.d("", "解析" + App.token);
        if (App.authsArr != null) {
            for (int i = 0; i < App.authsArr.length; i++) {
                if (viewAuthStr.equals(App.authsArr[i])) {
                    isContains = true;
                    break;
                }
            }
            view.setVisibility(isContains ? View.VISIBLE : View.GONE);
        }
    }

    //解析token
    public static void resolveToken() {
        try {
            //JWT jwt = new JWT(App.token);
            //Claim claim = jwt.getClaim(KeyConstant.authorities);
          /*  if (jsonArr == null) {
                return;
            }
            int length = jsonArr.size();
            App.authsArr = new String[length];
            for (int i = 0; i < length; i++) {
                JsonObject authObj = jsonArr.get(i);
                if (null != authObj ) {
                    App.authsArr[i] = authObj.toString();
                    Log.d("", "解析"+ App.authsArr[i] );
                }
            }*/
        } catch (Exception e) {
            Log.d("解析", "解析异常" + e);
        }
    }
}
