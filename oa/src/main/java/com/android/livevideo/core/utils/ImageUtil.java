package com.android.livevideo.core.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 获取图片的Base64字符串
 * Created by zeng on 2016/6/20.
 */
public class ImageUtil {

    private static float mRatio;
    private static int mScreenWidth;
    private static int mScreenHeight;
    private static float scale;

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static String getImageStr(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public static int getScreenWidth(Activity activity) {
        if (mScreenWidth == 0) {
            initialScreenParams(activity);
        }
        return mScreenWidth;
    }

    public static int getScreenHeight(Activity activity) {
        if (mScreenHeight == 0) {
            initialScreenParams(activity);
        }
        return mScreenHeight;
    }

    public static void initialScreenParams(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels - statusBarHeight;
        mRatio = mScreenWidth / (float) mScreenHeight;
        scale = activity.getResources().getDisplayMetrics().density;
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //设置GridView高度
    public static void setGridViewHeight(GridView gridView, int columnCount) {
        // 获取listview的adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int verticalSpacing = gridView.getVerticalSpacing();
        // 固定列宽，有多少列
        int col = columnCount/*gridView.getNumColumns()*/;
        int totalHeight = 0;
        // i每次加2，相当于listAdapter.getCount()小于等于2时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于4时计算两次高度相加
        int count = listAdapter.getCount();
        for (int i = 0; i < count; i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            // 获取item的高度和
            int measuredHeight = listItem.getMeasuredHeight() + verticalSpacing;
            totalHeight += measuredHeight;
        }
        if (count >= 2) {
            totalHeight = totalHeight - verticalSpacing;
        }
        // 获取listview的布局参数
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        gridView.setLayoutParams(params);
    }

    public static Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过第二个参数(文件名)来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), null);
        } catch (IOException e) {
            android.util.Log.d("skythinking", e.getMessage());
        }
        if (drawable == null) {
            //Log.d("skythinking", "null drawable");
        } else {
            //Log.d("skythinking", "not null drawable");
        }

        return drawable;
    }

    public static boolean isImageSuffix(String url) {
        if (null == url) {
            return false;
        }
        return url.contains(".png")
                || url.contains(".PNG")
                || url.contains(".jpg")
                || url.contains(".JPG")
                || url.contains(".jpeg")
                || url.contains(".JPEG");
    }

    public static void reSetLVHeight(Activity context, AbsListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        View view;
        int count = listAdapter.getCount();
        for (int i = 0; i < count; i++) {
            view = listAdapter.getView(i, null, listView);
            //宽度为屏幕宽度
            int i1 = View.MeasureSpec.makeMeasureSpec(ImageUtil.getScreenWidth(context),
                    View.MeasureSpec.EXACTLY);
            //根据屏幕宽度计算高度
            int i2 = View.MeasureSpec.makeMeasureSpec(i1, View.MeasureSpec.UNSPECIFIED);
            if (view != null) {
                view.measure(i1, i2);
                totalHeight += view.getMeasuredHeight();
            }
        }
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL
                    .openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // 关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void savePicUrl(final BaseFgActivity context, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();

                    savePicBitmap(context, bitmap);
                } catch (Exception e) {
                }
            }
        }).start();

    }

    public static void savePicBitmap(BaseFgActivity context, Bitmap bmp) {
        // 首先保存图片
        if (bmp == null) {
            ToastUtil.show(context, "保存失败1");
            return;
        }
        File appDir = new File(Environment.getExternalStorageDirectory(),
                "PIC_LOCAL_NOTICE");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);

            ToastUtil.show(context, "已保存到系统相册");

        } catch (Exception e) {
            ToastUtil.show(context, "保存失败2");
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
            reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap base64UrlStrToBitmap(String urlBase64) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(urlBase64.split(",")[1], Base64.DEFAULT);
            //解析base64类型的图片
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
        } catch (OutOfMemoryError e) {
            try {
                byte[] bitmapArray = Base64.decode(urlBase64.split(",")[1], Base64.DEFAULT);
                //解析base64类型的图片
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (OutOfMemoryError e1) {
            }
        }
        return bitmap;
    }
}
