package com.android.livevideo.act_3;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.AccountInfo;
import com.android.livevideo.bean.DictInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.FileUtil;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.UrlConstant;
import com.android.livevideo.exception.NoSDCardException;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户中心界面
 * Gool  Lee
 */
public class MeProfileActivity extends BaseFgActivity {

    public String TAG = MeProfileActivity.class.getSimpleName();
    private MeProfileActivity context;
    private String pwd;
    private SimpleDraweeView img_photo;
    private int REQUEST_CODE_CAPTURE_CAMERA = 1458;
    private String mCurrentPhotoPath;
    private File mTempDir;
    private String imgStrPost = "";
    private String avatarUrl;
    private SharedPreferences.Editor editor;
    private Dialog defAvatarDialog;
    private FragmentManager fm;
    private Uri fileUri;
    private RelativeLayout imgPhotoLayout;
    private Button titleRightBt;
    private JSONObject jsonObject;
    private TextView mNameTv;
    private String userId = "";
    private View itemView;
    private LinearLayout itemLayout;
    private AccountInfo info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_user_center);
        context = this;
        fm = getSupportFragmentManager();
        initTitleBackBt(getString(R.string.me_profile));


        try {
            mTempDir = new File(Utils.getImageBasePath());
        } catch (NoSDCardException e) {
            e.printStackTrace();
        }
        if (mTempDir != null && !mTempDir.exists()) {
            mTempDir.mkdirs();
        }
        img_photo = (SimpleDraweeView) findViewById(R.id.img_photo);
        imgPhotoLayout = (RelativeLayout) findViewById(R.id.img_photo_layout);

        setLayoutData();

        imgPhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改头像
                ToastUtil.show(context, "暂不支持修改头像");
                //showChangeAvatarDialog();
            }
        });


        //默认头像地址
        for (int i = 1; i < 21; i++) {
            if (i < 10) {
                mUrlList.add(UrlConstant.RECOMMED_URL_START + "0" + i + ".png");
            } else {
                mUrlList.add(UrlConstant.RECOMMED_URL_START + i + ".png");
            }
        }
        defAvatarDialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_recommend_avatar,
                null);
        GridView gridView = (GridView) inflate.findViewById(R.id.recommend_grid_view);
        gridView.setAdapter(new AvatarAdapter());
        defAvatarDialog.setContentView(inflate);//将布局设置给Dialog
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgStrPost = mUrlList.get(position);
                img_photo.setImageURI(imgStrPost);
                defAvatarDialog.dismiss();
            }
        });

    }

    private void requestTypeData(final String userType) {
        String url = Constant.WEB_SITE + "/dict/dicts/cached/EMP_Type";
        Response.Listener<List<DictInfo.DictValuesBean>> successListener = new Response
                .Listener<List<DictInfo.DictValuesBean>>() {
            @Override
            public void onResponse(List<DictInfo.DictValuesBean> result) {
                if (result != null) {
                    for (DictInfo.DictValuesBean dictValue : result) {
                        String value = dictValue.getValue();
                        if (value.equals(userType)) {
                            itemLayout.removeViewAt(6);
                            addItem("员工类型", dictValue.getName(), 6);
                            break;
                        }
                    }

                }
            }
        };

        Request<List<DictInfo.DictValuesBean>> versionRequest = new
                GsonRequest<List<DictInfo.DictValuesBean>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("字典异常", "");
                    }
                }, new TypeToken<List<DictInfo.DictValuesBean>>() {
                }.getType()) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
        App.requestQueue.add(versionRequest);
    }

    private void requestStatusData(final String status) {
        String url = Constant.WEB_SITE + "/dict/dicts/cached/EMP_Status";
        Response.Listener<List<DictInfo.DictValuesBean>> successListener = new Response
                .Listener<List<DictInfo.DictValuesBean>>() {
            @Override
            public void onResponse(List<DictInfo.DictValuesBean> result) {
                if (result != null) {
                    for (DictInfo.DictValuesBean dictValue : result) {
                        String value = dictValue.getValue();
                        if (value.equals(status)) {
                            itemLayout.removeViewAt(7);
                            addItem("员工状态", dictValue.getName(), 7);
                            break;
                        }
                    }

                }
            }
        };

        Request<List<DictInfo.DictValuesBean>> versionRequest = new
                GsonRequest<List<DictInfo.DictValuesBean>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<List<DictInfo.DictValuesBean>>() {
                }.getType()) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
        App.requestQueue.add(versionRequest);
    }

    private void setLayoutData() {
 /*       info = (AccountInfo) getIntent().getSerializableExtra(KeyConst.OBJ_INFO);
        if (info == null) {
            ToastUtil.show(context, R.string.server_exception);
            return;
        }*/
        itemLayout = (LinearLayout) findViewById(R.id.item_layout);
        addItem("姓名", App.username);
        addItem("部门", "工程部");
        addItem("工号", "100466523");
        //addItem("邮箱", "");
        addItem("电话", "17644326728");
        addItem("职位", "管理员");
        addItem("员工类型", "正式员工");
        addItem("员工状态", "在职");
        addItem("入职日期", "2015-08-22");
        addItem("转正日期", "2015-11-21");
  /*      String probationPeriod = info.probationPeriod;
        if (!TextUtil.isEmpty(probationPeriod)) {
            probationPeriod = probationPeriod.replace(".00", "");
        }
        addItem("试用期", probationPeriod + "个月");
        requestLeave(info.employeeId);
        requestTypeData(info.employeeType);
        requestStatusData(info.employeeStatus);*/
    }

    private void requestLeave(int employeeId) {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/upms/employees/" + employeeId;
        Response.Listener<AccountInfo> successListener = new Response
                .Listener<AccountInfo>() {
            @Override
            public void onResponse(AccountInfo info) {
                if (info == null) {
                    return;
                }
                itemLayout.removeViewAt(5);
                addItem("岗位职级", info.employeeLevel, 5);
            }
        };

        Request<AccountInfo> versionRequest = new
                GsonRequest<AccountInfo>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<AccountInfo>() {
                }.getType()) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
        App.requestQueue.add(versionRequest);

    }

    private void addItem(String title, String value) {
        itemView = View.inflate(context, R.layout.item_month_wage_emplyee_detail, null);
        TextView keyTv = (TextView) itemView.findViewById(R.id.item_key_tv);
        TextView valueTv = (TextView) itemView.findViewById(R.id.item_value_tv);
        keyTv.setText(title);
        valueTv.setText(value);

        itemLayout.addView(itemView);
    }

    private void addItem(String title, String value, int index) {
        itemView = View.inflate(context, R.layout.item_month_wage_emplyee_detail, null);
        TextView keyTv = (TextView) itemView.findViewById(R.id.item_key_tv);
        TextView valueTv = (TextView) itemView.findViewById(R.id.item_value_tv);
        keyTv.setText(title);
        valueTv.setText(value);

        itemLayout.addView(itemView, index);
    }


    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            avatarUrl = uri.toString();
            img_photo.setImageURI(avatarUrl);

            String path = uri.getPath();
            //File file = new File(path);
            imgStrPost = ImageUtil.getImageStr(path);
            android.util.Log.d(TAG, path + "修改参数:图片地址:" + imgStrPost);
        } else if (resultCode == Crop.RESULT_ERROR) {
            //Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //修改头像
    public void showChangeAvatarDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_change_avatar,
                null);

        View.OnClickListener mDialogClickLstener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                int id = v.getId();
                if (id == R.id.choose_local_tv) {//本地相册
                    Crop.pickImage(context);
                } else if (id == R.id.choose_camera_tv) {//相机
                    getImageFromCamera();
                } else if (id == R.id.choose_recomend_tv) {
                    //选择推荐头像
                    setDialogWindow(defAvatarDialog);
                }
            }
        };
        inflate.findViewById(R.id.choose_local_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_recomend_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_camera_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_cancel_tv).setOnClickListener(mDialogClickLstener);

        dialog.setContentView(inflate);//将布局设置给Dialog
        setDialogWindow(dialog);
    }

    private List<String> mUrlList = new ArrayList<>();

    //默认头像适配器
    public class AvatarAdapter extends BaseAdapter {
        public AvatarAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return mUrlList.size();
        }

        @Override
        public Object getItem(int position) {
            return mUrlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            AvatarAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new AvatarAdapter.ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout
                        .layout_avatar_item, null);
                holder.mIconIv = (SimpleDraweeView) convertView.findViewById(R.id
                        .recommend_icon_gv_iv);
                convertView.setTag(holder);
            } else {
                holder = (AvatarAdapter.ViewHolder) convertView.getTag();
            }
            final String uriString = mUrlList.get(position);
            holder.mIconIv.setImageURI(uriString);
            return convertView;
        }

        class ViewHolder {
            private SimpleDraweeView mIconIv;
        }
    }

    private void setDialogWindow(Dialog dialog) {
        Window dialogWindow = dialog.getWindow(); //获取当前Activity所在的窗体
        dialogWindow.setGravity(Gravity.BOTTOM);//设置Dialog从窗体底部弹出
        WindowManager.LayoutParams params = dialogWindow.getAttributes();   //获得窗体的属性
        //params.y = 20;  Dialog距离底部的距离
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(params); //将属性设置给窗体
        dialog.show();//显示对话框
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Crop.REQUEST_PICK) {
                beginCrop(result.getData());
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, result);
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMERA) {
                if (fileUri != null) {
                    beginCrop(fileUri);
                }
            }
        }
    }

    private void beginCrop(Uri source) {
        String fileName = "Temp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File cropFile = new File(mTempDir, fileName);
        Uri outputUri = Uri.fromFile(cropFile);
        new Crop(source).output(outputUri).asSquare().start(this);
    }

    protected void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "Temp_camera" + String.valueOf(System.currentTimeMillis());
        File cropFile = new File(mTempDir, fileName);
        fileUri = FileUtil.getUriForFile(context, cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file

        mCurrentPhotoPath = fileUri.getPath();
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
    }


    private void changeData() {

        JSONObject j = new JSONObject();
        try {
            j.put(KeyConst.id, 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Map<String, Object> map = new HashMap<>();
        map.put(KeyConst.dept, j);
        map.put(KeyConst.id, userId);

        try {
            map.put(KeyConst.employeeBirthday, jsonObject.getString(KeyConst.employeeBirthday));
            map.put(KeyConst.employeeEmail, jsonObject.getString(KeyConst.employeeEmail));
            map.put(KeyConst.employeeSex, jsonObject.getString(KeyConst.employeeSex));
            map.put(KeyConst.employeeStatus, jsonObject.getString(KeyConst.employeeStatus));
            map.put(KeyConst.employeeOrder, jsonObject.getString(KeyConst.employeeOrder));
            map.put(KeyConst.employeeType, jsonObject.getString(KeyConst.employeeType));
        } catch (Exception e) {
        }


        DialogHelper.showWaiting(fm, "加载中...");
        String url = Constant.WEB_SITE + Constant.url_system_employees;
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

        JSONObject jsonObject = new JSONObject(map);
        Log.d(TAG, "修改数据:" + jsonObject.toString());
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                if (result == null) {
                    ToastUtil.show(context, "修改失败");
                    return;
                }
                Log.d(TAG, "修改成功" + result);
                ToastUtil.show(context, "资料修改成功");
                DialogHelper.hideWaiting(fm);
                context.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "修改失败" + error);
                DialogHelper.hideWaiting(fm);
                ToastUtil.show(context, getString(R.string.server_exception));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConst.Content_Type, Constant.application_json);
                params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);

                return params;
            }
        };
        App.requestQueue.add(jsonRequest);

    }
}
