package com.android.livevideo.act_other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.act_3.SysSettingsActivity;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.Log;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.dialogfragment.SimpleDialogFragment;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Gool  修改手机号
 */
public class ChangePhoneActivity extends BaseFgActivity {
    private ChangePhoneActivity context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText et_name;
    private EditText et_captcha;
    private Button bt_register;
    private TextView tv_captcha;

    private String mToken;
    private static final int WAIT_TIME = 61;
    private int second = 60;
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_change_phone);
        context = this;
        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        initTitleBackBt("");

        editor = preferences.edit();
        mToken = App.token;
        dialogHelper = new DialogHelper(getSupportFragmentManager(), context);
        init();
    }

    private void init() {
        String phone = getString(R.string.change_phone_tip1) +
                preferences.getString(KeyConst.username, "");
        ((TextView) findViewById(R.id.change_phone_tip1_tv)).setText(
                phone);


        et_name = (EditText) findViewById(R.id.et_login_user);
        et_name.setHint("输入新号码");
        et_captcha = (EditText) findViewById(R.id.et_captcha);

        bt_register = (Button) findViewById(R.id.register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_name.getText().toString();
                String captcha = et_captcha.getText().toString();

                if (ToastUtil.showCannotEmpty(context, userName, "手机号")) {
                    return;
                }
                if (!TextUtil.isMobile(userName)) {
                    ToastUtil.show(context, "请输入正确的手机号");
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, captcha, "验证码")) {
                    return;
                }
                post(userName, captcha);
            }
        });

        tv_captcha = (TextView) findViewById(R.id.tv_captcha);
        tv_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = et_name.getText().toString();
                if (userName != null && !"".equals(userName)) {
                    if (!TextUtil.isMobile(userName)) {
                        ToastUtil.show(context, "请输入正确的手机号");
                    } else {
                        tv_captcha.setText("正在获取...");
                        tv_captcha.setClickable(false);
                        getSMSCode(userName);
                    }
                } else {
                    ToastUtil.show(context, "手机号不能为空");
                }
            }
        });


    }

    private void post(final String mobile, final String smsCode) {
        String url = Constant.WEB_SITE + "/upms/accounts/change-mobile";

        //添加
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        Response.Listener<Boolean> successListener = new Response
                .Listener<Boolean>() {
            @Override
            public void onResponse(Boolean result) {
                showDialog(true, "手机号修改成功");
            }
        };

        Request<Boolean> versionRequest = new
                GsonRequest<Boolean>(
                        Request.Method.POST, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                     /*   if (null != volleyError && volleyError.networkResponse != null) {
                            byte[] htmlBodyBytes = volleyError.networkResponse.data;  //回应的报文的包体内容
                            String msg = new String(htmlBodyBytes);
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(msg);
                                if (obj != null && obj.getInt(KeyConst.error) == 20006) {
                                    DialogUtils.showTipDialog(context, "改手机号在系统中已存在");
                                    return;
                                }
                            } catch (JSONException e) {
                            }
                        }*/
                        showDialog(false, "修改失败\n手机号或验证码有误");
                    }
                }, new TypeToken<Boolean>() {
                }.getType()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.mobile, mobile);
                        params.put(KeyConst.smsCode, smsCode);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Content_Type, Constant.application_json);
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
        App.requestQueue.add(versionRequest);
    }

    /**
     * 显示注册结果对话框
     */
    private void showDialog(final boolean isSuccess, String msg) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setDialogWidth(220);
        dialogFragment.setCancelable(false);

        TextView tv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(getResources().getColor(R.color.color666));
        dialogFragment.setContentView(tv);

        dialogFragment.setNegativeButton(isSuccess ? R.string.reLogin : R.string.sure
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogFragment.dismiss();
                        if (isSuccess) {
                            SharedPreferences.Editor sp = getSharedPreferences(Constant.CONFIG_FILE_NAME,
                                    MODE_PRIVATE).edit();
                            ChangePhoneShowActivity.context.finish();
                            SysSettingsActivity.content.finish();
                            MainActivity.context.finish();
                            sp.putString(Constant.sp_pwd, "").commit();
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            context.finish();
                        }
                    }
                });
        dialogFragment.show(ft, "successDialog");
    }

    /**
     * 执行倒计时操作
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < WAIT_TIME; i++) {
                if (second <= 1) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_captcha.setText(R.string
                                    .get_sms);
                            tv_captcha.setBackgroundResource(R.drawable
                                    .shape_bg_verif_code_bt_send);
                            tv_captcha.setClickable(true);
                            return;
                        }
                    });
                } else {
                    second--;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_captcha.setText("重新发送(" + second + "s)");
                            tv_captcha.setBackgroundResource(R.drawable
                                    .shape_bg_verif_code_bt_waiting);
                            tv_captcha.setClickable(false);
                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 获取手机验证码
     */
    private void getSMSCode(final String phone) {
        dialogHelper.showAlert("加载中...", true);

        String url = Constant.WEB_SITE + "/upms/accounts/change-mobile/" + phone;//新接口
        Log.d(TAG, "获取验证码" + url);
        Response.Listener<Boolean> successListener = new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean result) {
                if (null != context && !context.isFinishing()) {
                    dialogHelper.hideAlert();
                }
                if (result) {
                    second = 60;
                    new Thread(runnable).start();
                    ToastUtil.show(context, R.string.get_sms_success);
                } else {
                    tv_captcha.setClickable(true);
                    tv_captcha.setText(R.string.get_sms);
                    tv_captcha.setBackgroundResource(R.drawable.shape_bg_verif_code_bt_send);

                    ToastUtil.show(context, R.string.get_sms_failed);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (null != context && !context.isFinishing()) {
                    dialogHelper.hideAlert();
                }
                tv_captcha.setClickable(true);
                tv_captcha.setText(getResources().getString(R.string.get_sms));
                tv_captcha.setBackgroundResource(R.drawable.shape_bg_verif_code_bt_send);

                if (null != volleyError && volleyError.networkResponse != null) {
                    byte[] htmlBodyBytes = volleyError.networkResponse.data;  //回应的报文的包体内容
                    String msg = new String(htmlBodyBytes);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(msg);
                        if (obj != null && obj.getInt(KeyConst.error) == 20006) {
                            DialogUtils.showTipDialog(context, "该手机号在系统中已存在");
                            et_name.setText("");
                            return;
                        }
                    } catch (JSONException e) {
                    }
                }
                ToastUtil.show(context, R.string.get_sms_failed);
            }
        };

        Request<Boolean> versionRequest = new GsonRequest<Boolean>(Request.Method.GET, url,
                successListener, errorListener, new TypeToken<Boolean>() {
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
}