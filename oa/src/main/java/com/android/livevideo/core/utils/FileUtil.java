package com.android.livevideo.core.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.livevideo.exception.NoSDCardException;
import com.android.livevideo.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 进行文件操作的工具类
 * Gool
 */
public class FileUtil {

    public static final String TAG = FileUtil.class.getSimpleName();
    public static final int RESULT_FILE_UN_EXIST = 3;
    public static final int RESULT_FILE_EXIST_OLD = 2;
    public static final int RESULT_FILE_EXIST = 1;
    public static final int SIZETYPE_B = 1;      // 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;     // 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;     // 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;     // 获取文件大小单位为GB的double值

    /**
     * 校验文件是否存在
     *
     */
    public static int checkFileExist(String fileName, String md5) {

        int state;

        if (TextUtil.isAnyEmpty(fileName, md5)) {
            return RESULT_FILE_UN_EXIST;
        }

        try {

            File file = new File(Utils.getFileLoadBasePath());
            if (!file.exists()) {
                return RESULT_FILE_UN_EXIST;
            }

            file = new File(file, fileName);

            if (file.exists() && file.isFile()) {
                String localMd5 = MD5.getFileMD5(file);
                if (md5.equals(localMd5)) {
                    state = RESULT_FILE_EXIST;
                } else {
                    state = RESULT_FILE_EXIST_OLD;
                }
            } else {
                state = RESULT_FILE_UN_EXIST;
            }
        } catch (NoSDCardException e) {
            e.printStackTrace();
            state = RESULT_FILE_UN_EXIST;
        }
        return state;
    }

    /**
     * 校验文件是否存在
     */
    public static boolean isFileExist(String fileName) {

        if (TextUtil.isAnyEmpty(fileName)) {
            return false;
        }

        try {

            File file = new File(Utils.getFileLoadBasePath());
            if (!file.exists()) {
                return false;
            }

            file = new File(file, fileName);
            return (file.exists() && file.isFile());

        } catch (NoSDCardException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String fileName) {

        if (fileName.endsWith(".ngk")) {

            String[] names = fileName.split("\\.");
            try {
                File pathDir = new File(Utils.getFileLoadBasePath(), names[0]);
                File pathZip = new File(Utils.getFileLoadBasePath(), names[0] + ".zip");
                if (pathDir != null) {
                    deleteAllFilesOfDir(pathDir);
                }
                if (pathZip != null) {
                    deleteAllFilesOfDir(pathZip);
                }
            } catch (NoSDCardException e) {
                e.printStackTrace();
            }

        } else {
            File oldFile = null;
            File newFile = null;
            try {
                oldFile = new File(Utils.getFileLoadBasePath(), fileName);
                newFile = new File(Utils.getFileLoadBasePath(), "temp" + fileName);
            } catch (NoSDCardException e) {
                e.printStackTrace();
            }
            if (oldFile != null && oldFile.exists() && oldFile.isFile()) {
                oldFile.renameTo(newFile);
                if (newFile != null)
                    newFile.delete();
                Log.d(TAG, "删除本地文件！");
            }
        }
    }

    private static void deleteAllFilesOfDir(File path) {

        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }

    /**
     * 复制文件夹
     *
     */
    public static void copyDirectory(String sourceDir, String targetDir) {

        // 新建目标目录
        File tempFile = new File(targetDir);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }

        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new
                        File(new File(targetDir).getAbsolutePath()
                        + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectory(dir1, dir2);
            }
        }
    }

    /**
     * 复制文件
     */
    private static void copyFile(File sourceFile, File targetFile) {

        try {
            FileInputStream input = null;
            BufferedInputStream inBuff = null;
            FileOutputStream output = null;
            BufferedOutputStream outBuff = null;
            try {
                // 新建文件输入流并对它进行缓冲
                input = new FileInputStream(sourceFile);
                inBuff = new BufferedInputStream(input);

                // 新建文件输出流并对它进行缓冲
                output = new FileOutputStream(targetFile);
                outBuff = new BufferedOutputStream(output);

                // 缓冲数组
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = inBuff.read(b)) != -1) {
                    outBuff.write(b, 0, len);
                }
                // 刷新此缓冲的输出流
                outBuff.flush();

            } finally {
                //关闭流
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 删除指定目录下面的所有文件
    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory() || dir.listFiles() == null) return;

        for (File file : dir.listFiles()) {
            if (file.isFile()) file.delete(); // 删除所有文件
            else if (file.isDirectory()) deleteDir(path); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    // ImageLoad 中使用的方法
    public static DisplayImageOptions getModelOptions(int imgbgId, int round) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(imgbgId) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(imgbgId) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(imgbgId) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                // .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片 置成圆角图片
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    // ImageLoad 中使用的方法
    public static DisplayImageOptions getRoundOptions(int imgbgId, int round) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(imgbgId) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(imgbgId) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(imgbgId) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片 置成圆角图片
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    // 获取压缩图片的流
    public static ByteArrayOutputStream getZipFiles(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        ByteArrayOutputStream baos = null;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 720, 1280);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inTempStorage = new byte[12 * 1024];
        options.inJustDecodeBounds = false;

        File file = new File(filePath);
        FileInputStream fs = null;
        Bitmap bm = null;
        int optionsA = 100; // 100表示不压缩
        try {
            fs = new FileInputStream(file);
            if (fs != null) {
                bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
            }
            //Bitmap bm = BitmapFactory.decodeFile(filePath, options);
            if (bm == null) {
                return null;
            }

            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, optionsA, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            while ((baos.toByteArray().length / 1024) > 200) { // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                optionsA -= 10;// 每次都减少10
                bm.compress(Bitmap.CompressFormat.JPEG, optionsA, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(baos, fs);
            if (bm != null) {
                bm.recycle();
                System.gc();
            }
        }
        return baos;

    }

    // 获取文件大小B
    public static double getFileSize(String filePath, int sizeType) {
        long blockSize = 0;
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                fis = new FileInputStream(file);
                blockSize = fis.available();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {

                }
            }
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        // DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = fileS;
                break;
            case SIZETYPE_KB:
                fileSizeLong = fileS / 1024;
                break;
            case SIZETYPE_MB:
                fileSizeLong = fileS / 1048576;
                break;
            case SIZETYPE_GB:
                fileSizeLong = fileS / 1073741824;
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 纠正图片角度（有些相机拍照后相片会被系统旋转）
     *
     * @param filePath 图片路径
     */
    public static void correctCameraPictureAngle(String filePath) {
        int angle = BitmapUtil.readPictureDegree(filePath);

        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bfOptions);
        bfOptions.inSampleSize = calculateInSampleSize(bfOptions, 720, 1280);
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inTempStorage = new byte[12 * 1024];
        bfOptions.inJustDecodeBounds = false;
        File file = new File(filePath);
        FileInputStream fs = null;
        Bitmap bm = null;
        BufferedOutputStream bos = null;
        try {
            fs = new FileInputStream(file);
            if (fs != null) {
                bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
            }
            if (angle != 0) {
                Matrix mtx = new Matrix();
                mtx.postRotate(angle);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mtx, true);
            }

            File dirFile = new File(filePath);
            //检测图片是否存在
            if (dirFile.exists()) {
                dirFile.delete();  //删除原图片
            }
            File myCaptureFile = new File(filePath);
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //100表示不进行压缩，70表示压缩率为30%
            bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(bos, fs);
            if (bm != null) {
                bm.recycle();
                System.gc();
            }
        }
    }

    // 获取缩放比例
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(FileUtil.class.getClass().getName(), e);
            }
        }
    }

    /**
     * 引导图（根据屏幕的宽高，适当的缩放及裁剪）
     *
     * @param activity
     * @param view
     * @param drawableResId
     */
    public static void scaleImage(final Activity activity, final View view, int drawableResId) {
        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
        // 解析将要被处理的图片
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);
        if (resourceBitmap == null) {
            return;
        }
        // 开始对图片进行拉伸或者缩放
        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());
        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这里防止图像的重复创建，避免申请不必要的内存空间
                if (scaledBitmap.isRecycled())
                    //必须返回true
                    return true;
                // 当UI绘制完毕，我们对图片进行处理
                int viewHeight = view.getMeasuredHeight();
                // 计算将要裁剪的图片的顶部以及底部的偏移量
                int offset = Math.abs(scaledBitmap.getHeight() - viewHeight) / 2;
                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),
                        scaledBitmap.getHeight() - offset * 2);
                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
                    scaledBitmap.recycle();
                    System.gc();
                }
                // 设置图片显示
                view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), finallyBitmap));
                return true;
            }
        });
    }
    /**
     * 读取文件内容
     *
     * @return 文件内容
     */
    public static String readFile() {
        String str = "";
        try {
            File readFile = new File(Environment.getExternalStorageDirectory(), Constant.FILE_NAME_SD_CRAD_APP_PKGNAME);
            if (!readFile.exists()) {
                return null;
            }
            FileInputStream inStream = new FileInputStream(readFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            str = stream.toString();
            stream.close();
            inStream.close();
            return str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 向sdcard中写入文件
     *
     * @param content  文件内容
     */
    public static void writeFile2SDCard( String content) {
        String en = Environment.getExternalStorageState();
        //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
        if (en.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), Constant.FILE_NAME_SD_CRAD_APP_PKGNAME);
                OutputStream out = new FileOutputStream(file);
                out.write(content.getBytes());
                out.close();
                android.util.Log.d(TAG, "saveToSDCard: 保存成功");
            } catch (Exception e) {
                android.util.Log.d(TAG, "saveToSDCard: 保存失败");
            }
        } else {
            //提示用户SDCard不存在或者为写保护状态
            android.util.Log.d(TAG, "saveToSDCard: SDCard不存在或者为写保护状态");
        }

    }

    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(),
                    "com.android.livevideo.provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
