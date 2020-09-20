package com.android.livevideo.util;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.livevideo.R;

public class ToastUtil {
    private static Toast toast;

    public static void show(Context context, String message) {
        try {
            if (toast == null) {
                toast=Toast.makeText(context,null, Toast.LENGTH_SHORT);
                toast.setText(message);
            } else {
                toast.setText(message);
            }
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    public static void show(Context context, int strId) {
        show(context, context.getString(strId));
    }

    public static boolean showCannotEmpty(Context context, String message, String toastText) {
        if (message == null || message.trim().length() <= 0) {
            show(context, toastText + context.getString(R.string.cannot_empty));
            return true;
        }
        return false;
    }

    public static void longShow(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    // 带图片的toast
    public static void ImageToast(Context context, int imgid, String msg) {
        Toast toast = Toast.makeText(context,
                msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        imageCodeProject.setImageResource(imgid);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }
}
