package com.android.livevideo.act_other;

import android.content.Intent;
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

import com.android.livevideo.dialogfragment.SimpleDialogFragment;
import com.android.livevideo.R;
import com.android.livevideo.App;
import com.android.livevideo.bean.JsonResult;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.Log;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 *Gool Lee
 */
public class RegisterActivity extends BaseFgActivity {

    public static final String TAG = RegisterActivity.class.getSimpleName();
    private Timer timer = new Timer();
    private Handler handler = new Handler();

    private TextView tv_captcha;
    private Button bt_register;
    private ImageButton bt_show_pwd;
    private EditText et_name, et_pwd, et_captcha;

    private boolean isShowPwd = false;

    private static final int WAIT_TIME = 61;
    private int second = 60;

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
                            tv_captcha.setText(getResources().getString(R.string
                                    .get_sms));
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
    RegisterActivity content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_register);
        content = RegisterActivity.this;

        initTitleBackBt("注册");

        et_name = (EditText) findViewById(R.id.et_login_user);
        et_captcha = (EditText) findViewById(R.id.et_captcha);
        bt_show_pwd = (ImageButton) findViewById(R.id.bt_show_pwd);
        et_pwd = (EditText) findViewById(R.id.et_login_pwd);

        bt_register = (Button) findViewById(R.id.register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_name.getText().toString();
                String pwd = et_pwd.getText().toString();
                String captcha = et_captcha.getText().toString();

                if (userName == null && userName.equals("")) {
                    ToastUtil.show(content, "邮箱/手机号不能为空");
                    return;
                }
                boolean isMobile = TextUtil.isMobile(userName);
                if (!isMobile && !TextUtil.isEmail(userName)) {
                    ToastUtil.show(content, "请输入正确的邮箱/手机号");
                    return;
                }
                if (captcha == null || captcha.equals("")) {
                    ToastUtil.show(content, "验证码不能为空");
                    return;
                }
                if (pwd == null || pwd.equals("")) {
                    ToastUtil.show(content, "密码不能为空");
                    return;
                }
                if (pwd.length() < 6) {
                    ToastUtil.show(content, "密码要大于六位");
                    return;
                }
                LOGIN_MODE = isMobile ? Constant.loginMode_Phone : Constant.loginMode_Email;
                doRegister(userName, pwd, captcha);
            }
        });

        tv_captcha = (TextView) findViewById(R.id.tv_captcha);
        tv_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = et_name.getText().toString();
                if (userName != null && !"".equals(userName)) {
                    boolean mobile = TextUtil.isMobile(userName);
                    if (!mobile && !TextUtil.isEmail(userName)) {
                        ToastUtil.show(content, "请输入正确的邮箱/手机号");
                    } else {
                        LOGIN_MODE = mobile ? Constant.loginMode_Phone : Constant.loginMode_Email;
                        getSMSCode(userName);
                    }
                } else {
                    ToastUtil.show(content, "邮箱/手机号不能为空");
                }
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

    String LOGIN_MODE = Constant.loginMode_Phone;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    /**
     * 获取手机验证码
     */
    private void getSMSCode(final String userName) {

        String url = Constant.WEB_SITE + Constant.URL_GET_AUTH_CODE;
        Response.Listener<JsonResult<Object>> successListener = new Response
                .Listener<JsonResult<Object>>() {
            @Override
            public void onResponse(JsonResult result) {
                if (result == null) {
                    ToastUtil.show(content, "服务端异常");
                    return;
                }
                if (result.code == 0) {
                    second = 60;
                    new Thread(runnable).start();

                    ToastUtil.show(content, "验证码已发送，请查收！");
                    Log.d(TAG, "HTTP请求成功：服务端返回：" + result.data);

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误：" + result.msg);
                    showDialog(false, result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                ToastUtil.show(content, "请检查网络连接");
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<Object>> versionRequest = new GsonRequest<JsonResult<Object>>(Request
                .Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConst.loginName, userName);
                params.put(KeyConst.loginMode, LOGIN_MODE);
                params.put(KeyConst.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                params.put(KeyConst.authType, Constant.authType_Register);//type 短信类型（1注册，2忘记密码）
                return params;
            }
        };
        App.requestQueue.add(versionRequest);
    }

    //注册
    private void doRegister(final String userName, final String pwd, final String captcha) {
        String url = Constant.WEB_SITE + Constant.URL_USER_REGISTER;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {

                if (result == null) {
                    ToastUtil.show(content, "服务端异常");
                    return;
                }

                if (result.code == 0) {
                    SharedPreferences preferences = getSharedPreferences(Constant
                            .CONFIG_FILE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    String token = (String) result.data;
                    editor.putString(Constant.SP_TOKEN, token);
                    editor.apply();

                    showDialog(true, "恭喜您，注册成功！");
                } else {
                    showDialog(false, result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                ToastUtil.show(RegisterActivity.this, "登录失败，请检查网络连接!");
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConst.loginName, userName);
                params.put(KeyConst.password, pwd);
                params.put(KeyConst.smsCode, captcha);
                params.put(KeyConst.loginMode, LOGIN_MODE);
                params.put(KeyConst.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
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
        TextView tv = new TextView(RegisterActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 20, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(getResources().getColor(R.color.mainColorTransparent));
        dialogFragment.setContentView(tv);

        int stringId;
        if (isSuccess) {
            stringId = R.string.login_now;
        } else {
            stringId = R.string.sure;
        }

        dialogFragment.setNegativeButton(stringId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                if (isSuccess) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        dialogFragment.show(ft, "successDialog");
    }
}
