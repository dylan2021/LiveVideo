package com.android.livevideo.widget.mulpicture;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.livevideo.act_other.CommonBaseActivity;
import com.android.livevideo.act_other.PictBean;
import com.android.livevideo.R;
import com.android.livevideo.util.Utils;
import com.android.livevideo.core.utils.FileUtil;
import com.android.livevideo.util.StringUtil;
import com.android.livevideo.util.ToastUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * 列出本地相册中的图片
 * 本地相册
 */
public class MulPictureActivity extends CommonBaseActivity {
    private RelativeLayout ll_back;
    private TextView tv_title;

    private GridView gridView; //显示图片区域
    private MulPictureAdapter adapter; //适配器
    private RelativeLayout showSelectRelativeLayout; //下面显示区域
    private LinearLayout selectPictureLinearLayout;
    private Map<Integer, ImageView> hashImage = new HashMap<Integer, ImageView>();

    private List<PictBean> selectList = new ArrayList<PictBean>();   // 存放当前选中的图片
    private List<PictBean> list = new ArrayList<PictBean>(); // 所有图片的路径
    private boolean isShowCamera = true;
    //相机拍照
    private String path;
    private Uri photoUri;
    private String currentUploadFileName;
    private PictBean pictBean;
    //其它页面带过来需要带回去的数据
    private int imageNum; //当前可以选择的图片的总数
    private int imageAllNum = 0;
    //如果点击的是拍照，则先前的选择操作取消
    private List<PictBean> allPicturesTemp;
    private List<PictBean> picturesTemp;

    public static final int CAMERA_PERMISSION = 21; //拍照权限
    public static final int SDCARD_WRITER = 22; //sdcard读写权限
    public static final int SDCARD_READ = 23; //sdcard读写权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mul_picture);
        // mStickyLV = getAllPictureUrl();
        getBundle();
        initViews();
    }

    //获得界面传递的参数
    public void getBundle() {
        super.getBundle();
        if (bundle != null) {
            picturesTemp = pictures;
            if (pictures != null && pictures.size() > 0) {
                selectList.addAll(pictures);
            }
            imageNum = bundle.getInt("imageNum", 0);
            imageAllNum = bundle.getInt("imageAllNum");
            if (imageAllNum == 0) {
                imageNum = 9;
            } else {
                imageNum = imageAllNum;
            }
        }
    }

    //设置界面传递的参数
    public Bundle setBundle() {
        super.setBundle();
        return bundle;
    }

    //加载控件
    private void initViews() {
        initTitleBackBt("选择图片");

        gridView = (GridView) findViewById(R.id.gv_mul_picture);
        showSelectRelativeLayout = (RelativeLayout) findViewById(R.id.rl_select_picture_layout);
        selectPictureLinearLayout = (LinearLayout) findViewById(R.id.ll_selected_picture_layout);
        selectPictureLinearLayout.setVisibility(View.GONE);
    }

    private void setAdapter() {
        adapter = new MulPictureAdapter(this, list, onItemClickClass, isShowCamera, selectList);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
        if (list == null || list.size() == 0) {
            getAllImageUrls();
        }
    }

    public void getAllImageUrls() {
        if (!FileUtil.isSDCardEnable()) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        } else {
//            dialog = new AppLoadingDialog(this, "加载中...", 2);
//            dialog.setCancelable(true);
//            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list = new ArrayList<PictBean>();
                    ContentResolver cr = getContentResolver();
                    String[] pro = {MediaStore.Images.Media._ID, MediaStore.Images.Media
                            .MIME_TYPE, MediaStore.Images.Media.DATA};
                    Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, pro,
                            MediaStore.Images.Media.MIME_TYPE + "=? or "
                                    + MediaStore.Images.Media.MIME_TYPE + "=?", new
                                    String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media
                                    .DATE_ADDED + " DESC");
                    int size = cursor.getCount();
                    while (cursor.moveToNext()) {
                        PictBean pictBean = new PictBean();
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images
                                .Media.DATA));
                        pictBean.setLocalURL(path);
                        list.add(pictBean);
                    }
                    // 通知Handler扫描图片完成
                    mHandler.sendEmptyMessage(0x110);
                }
            }).start();
        }
    }


    // 调用相机拍照
    public void camera() {
        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String sdcardPathDir = Utils.getImageBasePath();
            File file = null;
            File fileDir = new File(sdcardPathDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            file = new File(sdcardPathDir + "Temp_" + String.valueOf(System.currentTimeMillis
                    ()) + ".jpg");
            if (file != null) {
                path = file.getPath();
                photoUri = Uri.fromFile(file);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(openCameraIntent, 1);
            }
        } catch (Exception e) {
            ToastUtil.show(this, "内存不足");
            Log.d(TAG, "相机:" + e.getMessage());

        }
    }

    //处理选中图片后返回的操作
    MulPictureAdapter.OnItemClickClass onItemClickClass = new MulPictureAdapter.OnItemClickClass() {
        @Override
        public void OnItemClick(View v, int Position, CheckBox checkBox, View imageOverLay) {
            if (selectList != null) {
                selectList.clear();
            }
            if (Position == 0) {
                MPermissions.requestPermissions(MulPictureActivity.this, CAMERA_PERMISSION,
                        Manifest.permission.CAMERA);
                //camera();
            } else {
                PictBean bean = isShowCamera == true ? list.get(Position - 1) : list.get
                        (Position);
              /*  if (checkBox.isChecked()) { //取消选中
                    checkBox.setChecked(false);
                    //imageOverLay.setVisibility(View.GONE);
                    for (int i = 0; i < selectList.size(); i++) {
                        if (selectList.get(i).getLocalURL().equals(bean.getLocalURL())) {
                            selectList.remove(i);
                            break;
                        }
                    }
                    // selectList.remove(bean); 从快速查询页把已选择的对象带过来时，remove不掉
                    removePicture(bean);
                } else { //选中*/
                try {
                    //selectPictureLinearLayout.getChildCount()
                    //if (selectList.size() < imageNum) { // 最多上传 张图片
                    //判断所选的图片是否正常,是否损坏
                    Bitmap bitmap = FileUtil.getImageThumbnail(bean.getLocalURL(), 5, 5);
                    if (bitmap == null) {
                        ToastUtil.show(MulPictureActivity.this, "图片" + bean.getLocalURL()
                                + "异常无法上传");
                    } else if (FileUtil.getFileSize(bean.getLocalURL(), FileUtil
                            .SIZETYPE_KB) < 3) {
                        ToastUtil.show(MulPictureActivity.this, "图片清晰度不够");
                    } else {
                        bitmap.recycle();
                        //checkBox.setChecked(true);
                        //imageOverLay.setVisibility(View.VISIBLE);
                        selectList.add(bean);
                        addPicture(bean);
                    }

                    sendfiles();
                    // } else {
                    // ToastUtil.show(MulPictureActivity.this, "最多只能选择" + imageNum + "张照片");
                    // }
                } catch (Exception e) {
                    ToastUtil.show(MulPictureActivity.this, "异常信息");
                }

                //}

            }
        }
    };

    /**
     * 设置数据
     */
//    public void getTestDatas() {
//        allPictures = new ArrayList<PictureBean>();
//        for (int i = 0; i < 10; i++) {
//            PictureBean bean = new PictureBean();
//            allPictures.add(bean);
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: // 调用相机拍照
                if (resultCode == Activity.RESULT_OK) {
                    pictBean = new PictBean();
                    pictBean.setLocalURL(path);
                    FileUtil.correctCameraPictureAngle(path);// 纠正部分手机拍照后自动旋转的问题
                    refreshImagePhoto(pictBean.getLocalURL());// 拍照后刷新，使拍照的图片显示到相册中
//                    pictureBean.setPictureId(FileTools.getLocalImageId(getContentResolver(),
// path));
                    selectList = new ArrayList<PictBean>();
                    selectList.add(pictBean);
//                    allPictures = allPicturesTemp;
                    pictures = picturesTemp;
                    addPicture(pictBean);
                    sendfiles();

                }
                break;
            default:
                break;
        }
    }

    // 在相册中快速显示图片
    public void refreshImagePhoto(String fileUrl) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment
                .MEDIA_MOUNTED); // 判断sd卡是否存在
        if (!sdCardExist) {
            return;
        }
        // 将新增加的图片加到系统数据库中去，便于在相册中快速显示图片
        try {
            ContentValues values = new ContentValues();
            values.put("datetaken", new Date().toString());
            values.put("mime_type", "image/png");
            values.put("_data", fileUrl);
            ContentResolver cr = getApplication().getContentResolver();
            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    //点击提交按钮
    public void sendfiles() {
        intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(setBundle());
        if (getIntent().hasExtra("drugId"))
            intent.putExtra("drugId", imageNum + "");
        setResult(selectList.size(), intent);
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("type", android.R.attr.type);
//        outState.putSerializable("allPictures", (Serializable) allPictures);
        outState.putSerializable("pictures", (Serializable) pictures);
        outState.putSerializable("mStickyLV", (Serializable) list);
        outState.putSerializable("selectList", (Serializable) selectList);
        outState.putSerializable("allPicturesTemp", (Serializable) allPicturesTemp);
        outState.putSerializable("picturesTemp", (Serializable) picturesTemp);
        outState.putInt("imageNum", imageNum);
        outState.putString("path", path != null ? path : "");
        outState.putBoolean("isShowCamera", isShowCamera);
        outState.putString("currentUploadFileName", currentUploadFileName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if (dialog != null) {
//            dialog.dismiss();
//        }
        if (savedInstanceState != null) {
//            allPictures = (List<PictureBean>) savedInstanceState.getSerializable("allPictures");
            pictures = (List<PictBean>) savedInstanceState.getSerializable("pictures");
            allPicturesTemp = (List<PictBean>) savedInstanceState.getSerializable
                    ("allPicturesTemp");
            picturesTemp = (List<PictBean>) savedInstanceState.getSerializable("picturesTemp");
            selectList = (List<PictBean>) savedInstanceState.getSerializable("selectList");
            list = (List<PictBean>) savedInstanceState.getSerializable("mStickyLV");

            imageNum = savedInstanceState.getInt("imageNum");
            path = savedInstanceState.getString("path");
            isShowCamera = savedInstanceState.getBoolean("isShowCamera");
            currentUploadFileName = savedInstanceState.getString("currentUploadFileName");

            if (!StringUtil.isTextEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    path = file.getPath();
                    photoUri = Uri.fromFile(file);
                }
            }
        }
    }

    public void addPicture(PictBean pictBean) {
        if (null != pictures) {
            pictures.clear();
            pictures.add(pictBean);
        }
    }

    public void removePicture(PictBean pictBean) {

        for (int i = 0; i < pictures.size(); i++) {
            if (pictBean.getLocalURL().equals(pictures.get(i).getLocalURL())) {
                pictures.remove(i);
                break;
            }
        }

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//            if (dialog != null) {
//                dialog.dismiss();
//            }
            setAdapter();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDatas();
    }

    // 清除数据，避免内存溢出，特别是引用的图片控件
    public void clearDatas() {
        adapter = null;
        gridView = null;
        hashImage = null;
        list = null;
        selectList = null;
        path = null;
        photoUri = null;
        currentUploadFileName = null;
        pictBean = null;
    }

    //暂时同意权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //同意权限
    @PermissionGrant(CAMERA_PERMISSION)
    public void requestCameraSuccess() {
        MPermissions.requestPermissions(this, SDCARD_WRITER, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);
    }

    //拒绝权限
    @PermissionDenied(CAMERA_PERMISSION)
    public void requestCameraFailed() {
        ToastUtil.show(this, "请开启允许拍照权限");
    }

    //同意权限
    @PermissionGrant(SDCARD_WRITER)
    public void requestSdcardWriteSuccess() {
        camera();
    }

    //拒绝权限
    @PermissionDenied(SDCARD_WRITER)
    public void requestSdcardWriteFailed() {
        ToastUtil.show(this, "用户拒绝读写手机存储功能!");
    }
}
