package com.android.livevideo.act_other;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.dialogfragment.SimpleDialogFragment;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.Log;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Gool Lee
 */
public class FindPwdActivity extends BaseFgActivity {

    public static final String TAG = "000";

    private Button bt_find_pwd;
    private ImageButton bt_show_pwd;
    private TextView tv_captcha;
    private EditText et_name, et_captcha, et_pwd;
    private boolean isShowPwd = false;
    private Handler handler = new Handler();
    private static final int WAIT_TIME = 61;
    private int second = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStatusBar();
        setContentView(R.layout.activity_find_pwd);
        context = this;
        initTitleBackBt("");

        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        phoneSp = preferences.getString(KeyConst.username, "");
        bt_find_pwd = (Button) findViewById(R.id.bt_find_pwd);
        et_name = (EditText) findViewById(R.id.et_login_user);
        et_name.setText(phoneSp);
        //et_name.setText("15902729579");
        et_captcha = (EditText) findViewById(R.id.et_captcha);

        bt_show_pwd = (ImageButton) findViewById(R.id.bt_show_pwd);
        et_pwd = (EditText) findViewById(R.id.et_login_pwd);

        tv_captcha = (TextView) findViewById(R.id.tv_captcha);
        tv_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_name.getText().toString().trim();

                if (!TextUtil.isMobile(userName)) {
                    ToastUtil.show(context, "请输入正确的手机号");
                    return;
                }
                tv_captcha.setText("正在获取...");
                tv_captcha.setClickable(false);
                getSmsCode(userName);
            }
        });

        bt_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_name.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                String captcha = et_captcha.getText().toString().trim();

                if (TextUtil.isEmpty(userName) || !TextUtil.isMobile(userName)) {
                    ToastUtil.show(context, "请输入正确的手机号");
                    return;
                }
                if (captcha == null || captcha.length() <= 0) {
                    ToastUtil.show(context, "验证码不能为空");
                    return;
                }
                if (pwd == null || pwd.length() <= 0) {
                    ToastUtil.show(context, "新密码不能为空");
                    return;
                }
                if (pwd.length() < 6) {
                    ToastUtil.show(context, "密码要大于六位");
                    return;
                }

                doFindPwd(userName, pwd, captcha);
            }
        });
        bt_show_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowPwd) {
                    isShowPwd = true;
                    bt_show_pwd.setSelected(true);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                } else {
                    isShowPwd = false;
                    bt_show_pwd.setSelected(false);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                }
            }
        });

    }

    /**
     * 获取验证码
     */
    private void getSmsCode(final String phone) {
        String url = Constant.WEB_SITE + "/upms/accounts/forget-password/" + phone;//新接口
        Response.Listener<String> successListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d(TAG, "验证码" + result.toString());
                if (result == null) {
                    tv_captcha.setClickable(true);
                    tv_captcha.setText(getResources().getString(R.string.get_sms));
                    tv_captcha.setBackgroundResource(R.drawable.shape_bg_verif_code_bt_send);
                    ToastUtil.show(context, getString(R.string.server_exception));
                    return;
                }

                if (result != null) {
                    second = 60;
                    new Thread(runnable).start();
                    ToastUtil.show(context, "验证码已发送成功，请注意查收");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tv_captcha.setClickable(true);
                tv_captcha.setText(getResources().getString(R.string.get_sms));
                tv_captcha.setBackgroundResource(R.drawable.shape_bg_verif_code_bt_send);
                if (volleyError != null && volleyError.networkResponse != null &&
                        volleyError.networkResponse.statusCode >= 400) {
                    DialogUtils.showTipDialog(context, "该手机号未注册或不是系统账号");
                } else {
                    ToastUtil.show(context, "获取验证码失败,请稍后重试");
                }
            }
        };

        Request<String> versionRequest = new GsonRequest<String>(Request.Method.GET, url,
                successListener, errorListener, new TypeToken<String>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put(KeyConstant.Authorization, Constant.authorization);
                return params;
            }
        };
        App.requestQueue.add(versionRequest);
    }

    //找回密码
    private void doFindPwd(final String phone, final String password, final String smsCode) {
        String url = Constant.WEB_SITE + "/upms/accounts/change-password-sms";

        //添加
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        Response.Listener<Boolean> successListener = new Response
                .Listener<Boolean>() {
            @Override
            public void onResponse(Boolean result) {
                android.util.Log.d(TAG, "修改密码成功:" + result);
                if (result != null && result) {
                    showDialog(true, "密码重置成功");
                } else {
                    showDialog(false, "密码重置失败");
                }
            }
        };

        Request<Boolean> versionRequest = new
                GsonRequest<Boolean>(
                        Request.Method.POST, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showDialog(false, "手机号或验证码有误");
                    }
                }, new TypeToken<Boolean>() {
                }.getType()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.mobile, phone);
                        params.put(KeyConst.smsCode, smsCode);
                        params.put(KeyConst.password, password);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Content_Type, Constant.application_json);
                        return params;
                    }
                };
        App.requestQueue.add(versionRequest);
    }

    /**
     * 显示登录对话框
     */
    private void showDialog(final boolean isSuccess, String msg) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();

        dialogFragment.setDialogWidth(220);
        dialogFragment.setCancelable(false);

        TextView tv = new TextView(FindPwdActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        dialogFragment.setContentView(tv);

        dialogFragment.setNegativeButton(R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();

                if (isSuccess) {
                    finish();
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
                            tv_captcha.setText(getResources().getString(R.string.get_sms));
                            tv_captcha.setBackgroundResource(R.drawable.shape_bg_verif_code_bt_send);
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
                            tv_captcha.setBackgroundResource(R.drawable.shape_bg_verif_code_bt_waiting);
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
    private FindPwdActivity context;
    private SharedPreferences preferences;
    private String phoneSp;
}
