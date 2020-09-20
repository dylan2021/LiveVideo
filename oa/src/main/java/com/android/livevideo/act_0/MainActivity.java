package com.android.livevideo.act_0;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_1.FragmentWork;
import com.android.livevideo.act_2.FragmentAddress;
import com.android.livevideo.act_3.MeApplyListActivity;
import com.android.livevideo.act_3.MeAttendanceActivity;
import com.android.livevideo.act_3.SysSettingsActivity;
import com.android.livevideo.act_3.MeProfileActivity;
import com.android.livevideo.bean.AccountInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.AppDownloadManager;
import com.android.livevideo.util.Utils;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.dialogfragment.SimpleDialogFragment;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.ContractInfo;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.UrlConstant;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Gool Lee
 */
public class MainActivity extends BaseFgActivity {
    public static MainActivity context;
    private FragmentMsg msgFragment;

    private FragmentManager fragmentManager;
    private LinearLayout video, manager, home;
    private RelativeLayout menu_game_hub;
    private Button tabBt0, tabBt1, tabBt4;
    private TextView tabTv1, tabTv4, tabTv3;
    private int colorDark;
    private int colorNormal;
    private TextView iconTv;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Button tabBt3;
    private TextView deptNameTv, mMenuNameTv;
    private TextView tabTv0;
    private FragmentWork workFragment;
    private int chooseId = 0;
    private String employee = "";
    private RelativeLayout meLayout;
    private FragmentAddress addressFragment;
    private List<ContractInfo> contractsInfoList = new ArrayList<>();
    private AccountInfo accountInfo;
    private SimpleDialogFragment appUpdateDialog;
    private AppDownloadManager mDownloadManager;
    private SimpleDialogFragment progressDialog;
    private NumberProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_main);
        context = this;
        checkAppUpdate();
        sp = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        editor = sp.edit();
        chooseId = sp.getInt(KeyConst.SP_CHOOSE_ID, 0);
        Utils.initLocalData(context);

        home = (LinearLayout) findViewById(R.id.main_tab_0);

        menu_game_hub = (RelativeLayout) findViewById(R.id.main_tab_3);
        video = (LinearLayout) findViewById(R.id.main_tab_1);
        manager = (LinearLayout) findViewById(R.id.main_tab_4);
        meLayout = (RelativeLayout) findViewById(R.id.main_me_layout);

        tabBt0 = (Button) findViewById(R.id.menu_tab_0_bt);
        tabBt1 = (Button) findViewById(R.id.menu_video_bt);
        tabBt3 = (Button) findViewById(R.id.menu_game_hub_bt);
        tabBt4 = (Button) findViewById(R.id.menu_manager_bt);

        findViewById(R.id.main_me_item_setting).setOnClickListener(mItemLayoutClickListener);

        tabTv0 = (TextView) findViewById(R.id.menu_home_tv);
        tabTv1 = (TextView) findViewById(R.id.menu_video_tv);
        tabTv3 = (TextView) findViewById(R.id.menu_gamehub_tv);
        tabTv4 = (TextView) findViewById(R.id.menu_manager_tv);

        iconTv = (TextView) findViewById(R.id.me_icon_tv);
        mMenuNameTv = (TextView) findViewById(R.id.me_user_name_tv);
        deptNameTv = (TextView) findViewById(R.id.me_department_tv);

        iconTv.setOnClickListener(mItemLayoutClickListener);
        mMenuNameTv.setOnClickListener(mItemLayoutClickListener);

        colorDark = getResources().getColor(R.color.mainColor);
        colorNormal = getResources().getColor(R.color.color999);

        fragmentManager = getSupportFragmentManager();
        setCurrentMenu(0);    //当前选中标签

        home.setOnClickListener(mTabClickListener);
        menu_game_hub.setOnClickListener(mTabClickListener);
        video.setOnClickListener(mTabClickListener);
        manager.setOnClickListener(mTabClickListener);

    }

    private void getAmPmTime() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/biz/attend/attendTime";
        Response.Listener<JsonObject> successListener = new Response
                .Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject info) {
                if (info == null || info.isJsonNull()) {
                    return;
                }

                String amTimeObj = Utils.getObjStr(info, KeyConst.amTime);
                String pmTimeObj = Utils.getObjStr(info, KeyConst.pmTime);
                App.amTime = TextUtil.isEmpty(amTimeObj) ? Constant.DEFT_AM : amTimeObj;
                App.pmTime = TextUtil.isEmpty(amTimeObj) ? Constant.DEFT_PM : pmTimeObj;
            }
        };

        Request<JsonObject> versionRequest = new
                GsonRequest<JsonObject>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<JsonObject>() {
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

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
        Utils.requestDictData(context);
        getAmPmTime();

        if (mDownloadManager != null) {
            mDownloadManager.resume();
        }
    }

    private void checkAppUpdate() {
        mDownloadManager = new AppDownloadManager(context);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                ToastUtil.show(context, R.string.no_store_permission);
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            String url = Constant.WEB_SITE + "/biz/appVersion/lastVersion";
            StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            Log.d(TAG, "app:成功" + result);
                            if (result != null) {
                                int serverVersionCode = 0;
                                String apkUrl = "";
                                String remark = "无";
                                int localVersionCode = Utils.getVersionName(context);
                                try {
                                    JSONObject info = new JSONObject(result);
                                    serverVersionCode = info.getInt(KeyConst.name);
                                    remark = info.getString(KeyConst.remark);
                                    //把除了头之外的内容读取出来 存为新的jsonobject 对象
                                    JSONObject appFile = info.getJSONObject(KeyConst.appFile);
                                    apkUrl = appFile.getString(KeyConst.url);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //有新版本
                                if (serverVersionCode > localVersionCode) {
                                    showUpdateDialog("新版本更新", remark, apkUrl);
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "app:失败" + TextUtil.getErrorMsg(error));
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(KeyConst.Content_Type, Constant.application_json);
                    params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                    return params;
                }

            };

            App.requestQueue.add(jsonObjRequest);


        }
    }

    private void showUpdateDialog(final String title, final String remark, final String apkUrl) {
        if (appUpdateDialog != null && appUpdateDialog.isShow()) {
            return;
        }
        appUpdateDialog = new SimpleDialogFragment();
        appUpdateDialog.setDialogWidth(220);
        appUpdateDialog.setCancelable(false);

        TextView tv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        params.gravity = Gravity.LEFT;
        tv.setLayoutParams(params);
        tv.setLineSpacing(10, 1);
        tv.setGravity(Gravity.LEFT);
        tv.setText(remark);
        tv.setTextColor(getResources().getColor(R.color.color666));
        appUpdateDialog.setTitle(title);//通知栏
        appUpdateDialog.setContentView(tv);

        appUpdateDialog.setNegativeButton(R.string.update_app, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateDialog.dismiss();
                ToastUtil.show(context, "开始下载...");
                if (progressDialog == null) {
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("progressDialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    progressDialog = new SimpleDialogFragment();

                    progressDialog.setCancelable(false);
                    progressDialog.setDialogWidth(255);

                    LayoutInflater inflater = getLayoutInflater();
                    LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout
                            .layout_dialog_download, null);
                    pb = (NumberProgressBar) contentView.findViewById(R.id.progress_bar);
                    pb.setProgress(0);
                    progressDialog.setContentView(contentView);

                    progressDialog.setPositiveButton("关闭", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog.dismiss();
                            finish();
                        }
                    });

                    progressDialog.show(ft, "progressDialog");

                }
                mDownloadManager.setUpdateListener(new AppDownloadManager.OnUpdateListener() {
                    @Override
                    public void update(int curProgress, int maxValue) {
                        if (maxValue <= 0) {
                            return;
                        }
                        int process = (int) (((float) curProgress / maxValue) * 100);
                        pb.setProgress(process);
                        pb.setProgressTextColor(getResources().getColor(R.color.color666));
                       /* if ((progress == maxValue) && maxValue != 0) {
                            progressDialog.dismiss();
                        }*/

                    }
                });

                mDownloadManager.downloadApk(apkUrl, "监控预警", remark);//通知栏展示
            }
        });
        appUpdateDialog.show(getSupportFragmentManager().beginTransaction(), "successDialog");
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mDownloadManager != null) {
            mDownloadManager.onPause();
        }

    }

    private void openAPK(String fileSavePath) {
        File apkfile = new File(fileSavePath);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    View.OnClickListener mTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_tab_0:
                    setCurrentMenu(0);
                    break;
                case R.id.main_tab_1:
                    setCurrentMenu(1);
                    break;
                case R.id.main_tab_3:
                    setCurrentMenu(3);
                    break;
                case R.id.main_tab_4:
                    setCurrentMenu(4);
                    break;
            }
        }
    };
    View.OnClickListener mItemLayoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_me_item_setting:
                    startActivity(new Intent(context, SysSettingsActivity.class));
                    break;
                case R.id.me_icon_tv:
                case R.id.me_user_name_tv:
                    Intent intent2 = new Intent(context, MeProfileActivity.class);
                    intent2.putExtra(KeyConst.OBJ_INFO, accountInfo);
                    startActivity(intent2);
                    break;
            }
        }
    };

    private void getUserData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/upms" + UrlConstant.accounts_current;
        Response.Listener<AccountInfo> successListener = new Response
                .Listener<AccountInfo>() {
            @Override
            public void onResponse(AccountInfo info) {
                if (info == null) {
                    return;
                }
                accountInfo = info;
                App.employeeName = "李国良";
                App.employeeId = info.employeeId;

                mMenuNameTv.setText(App.employeeName);
                iconTv.setText(TextUtil.getLast2(App.employeeName));

                setCompanyName();

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

    public void setCurrentMenu(int currentMenu) {
        tabBt0.setSelected(false);
        tabBt1.setSelected(false);
        tabBt4.setSelected(false);
        tabBt3.setSelected(false);
        tabTv0.setTextColor(colorNormal);
        tabTv1.setTextColor(colorNormal);
        tabTv3.setTextColor(colorNormal);
        tabTv4.setTextColor(colorNormal);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (null == addressFragment) {
            addressFragment = new FragmentAddress(chooseId);
            transaction.add(R.id.main_fragments, addressFragment);
        }
        if (null == msgFragment) {
            msgFragment = new FragmentMsg(chooseId);
            transaction.add(R.id.main_fragments, msgFragment);
        }
        if (null == workFragment) {
            workFragment = new FragmentWork(chooseId);
            transaction.add(R.id.main_fragments, workFragment);
        }
        switch (currentMenu) {
            case 0:
                meLayout.setVisibility(View.GONE);
                transaction.show(addressFragment).hide(msgFragment).hide(workFragment);
                tabBt0.setSelected(true);
                tabTv0.setTextColor(colorDark);
                break;
            case 1:
                meLayout.setVisibility(View.GONE);
                transaction.show(msgFragment).hide(addressFragment).hide(workFragment);
                tabBt1.setSelected(true);
                tabTv1.setTextColor(colorDark);
                break;
//            case 2:
//                //弹框
//                showReportAddDialog();
//                break;
            case 3:
                transaction.show(msgFragment).hide(workFragment).hide(addressFragment);
                tabBt3.setSelected(true);
                tabTv3.setTextColor(colorDark);
                meLayout.setVisibility(View.GONE);
                break;
            case 4:
                transaction.hide(addressFragment).hide(workFragment).hide(msgFragment);
                tabBt4.setSelected(true);
                tabTv4.setTextColor(colorDark);
                meLayout.setVisibility(View.VISIBLE);

                break;
        }
        transaction.commitAllowingStateLoss();
    }

    public void setCompanyName() {
        if (accountInfo != null) {
            String deptName = accountInfo.deptName;
            deptNameTv.setText("武汉预警监控有限公司" + deptName);
            if (workFragment != null) {
                ((TextView) findViewById(R.id.work_center_title_tv)).setText(deptName);
            }
        }
    }


    public void onMe05Click(View view) {
        startActivity(new Intent(context, MeAttendanceActivity.class));
    }


    public void onMe01Click(View view) {
        onMeClick(0, "我发起的");
    }

    public void onMe00Click(View view) {
        onMeClick(-1, "我的待办");
    }

    public void onMe02Click(View view) {
        onMeClick(1, "我的已办");
    }

    public void onMe03Click(View view) {
        onMeClick(2, "抄送我的");
    }

    public void onMe04Click(View view) {
        onMeClick(3, "我的草稿");
    }

    private void onMeClick(int type, String title) {
        Intent intent = new Intent(context, MeApplyListActivity.class);
        intent.putExtra(KeyConst.id, chooseId);
        intent.putExtra(KeyConst.type, type);
        intent.putExtra(KeyConst.title, title);
        startActivity(intent);
    }

    private boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                ToastUtil.show(context, "再点一次退出");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            } else {
                context.finish();
            }
        }
        return false;
    }

}