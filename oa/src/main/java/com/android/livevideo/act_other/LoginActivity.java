package com.android.livevideo.act_other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lechange.demo.tools.RootUtil;
import com.mm.android.deviceaddmodule.CommonParam;
import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.mobilecommon.utils.PreferencesHelper;
import com.mm.android.deviceaddmodule.openapi.CONST;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Gool Lee
 */
public class LoginActivity extends BaseFgActivity implements View.OnClickListener {
    private MaterialEditText et_pwd, et_user;
    private TextView bt_find_pwd, bt_register;
    private Button bt_login;
    private SharedPreferences sp;
    private LoginActivity context;
    private DialogHelper dialogHelper;
    private String accessToken;
    private boolean isFirstLuncher;
    private String pwd, username;
    private ImageView welcomeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_login);
        context = this;
        sp = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        isFirstLuncher = sp.getBoolean(KeyConst.IS_FIRST_LUNCHER_SP, true);

        et_user = (MaterialEditText) findViewById(R.id.et_login_user);
        et_user.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.account_digits)));
        et_pwd = (MaterialEditText) findViewById(R.id.et_login_pwd);
        username = sp.getString(KeyConst.username, "admin");//统之源 13100637291 111111
        pwd = sp.getString(Constant.sp_pwd, "hpdi12345");

        welcomeIv = (ImageView) findViewById(R.id.welcome_iv);
        bt_find_pwd = (TextView) findViewById(R.id.tv_find_pwd);
        bt_find_pwd.setOnClickListener(this);
        bt_register = (TextView) findViewById(R.id.tv_register);
        bt_register.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.but_login);
        bt_login.setOnClickListener(this);

        dialogHelper = new DialogHelper(getSupportFragmentManager(), context);
        findViewById(R.id.login_qq_bt).setOnClickListener(this);
        findViewById(R.id.login_wechat_bt).setOnClickListener(this);
        findViewById(R.id.login_sina_bt).setOnClickListener(this);

        et_user.setText(username);
        et_user.setSelection(et_user.getText().length());
        if (!TextUtil.isEmpty(username) && !TextUtil.isEmpty(pwd)) {
            welcomeIv.setVisibility(View.VISIBLE);
            et_pwd.setText(pwd);
            doLogin(true);
        } else {
            Utils.requestStoragePermissions(context);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    welcomeIv.setVisibility(View.GONE);
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                }
            }, 1500);

        }

        boolean deviceRooted = false;
        try {
            deviceRooted = RootUtil.isDeviceRooted();
            if (deviceRooted) {
                Toast.makeText(this, "设备已被Root", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        appUrl = PreferencesHelper.getInstance(this).getString(DOMESTIC_URL, CONST.Envirment.CHINA_PRO.url);

        try {
            CommonParam commonParam = new CommonParam();
            commonParam.setEnvirment(appUrl);
            commonParam.setContext(getApplication());
            commonParam.setAppId(appId);
            commonParam.setAppSecret(appSecret);
            LCDeviceEngine.newInstance().init(commonParam);

            PreferencesHelper.getInstance(context).set(DOMESTIC_APP_ID_KEY, appId);
            PreferencesHelper.getInstance(context).set(DOMESTIC_APP_SECRET_KEY, appSecret);
            PreferencesHelper.getInstance(context).set(DOMESTIC_URL, appUrl);
        } catch (Throwable e) {
            ToastUtil.show(context, "项目配置已更改,请联系管理员");
            Log.d("项目失败", "项目失败:" + e.toString());
            //return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_login:
                username = et_user.getText().toString();
                if (TextUtil.isEmpty(username)) {
                    ToastUtil.show(context, "请输入账号");
                    return;
                }
                pwd = et_pwd.getText().toString();
                if (TextUtil.isEmpty(pwd)) {
                    ToastUtil.show(context, "请输入密码");
                    return;
                }
                if (!NetUtil.isNetworkConnected(context)) {
                    ToastUtil.show(context, getString(R.string.no_network));
                    return;
                }
                dialogHelper.showAlert("登录中...", true);
                doLogin(false);
                break;
            case R.id.tv_find_pwd:
                startActivity(new Intent(this, FindPwdActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private static final String DOMESTIC_APP_ID_KEY = "DOMESTIC_APP_ID";
    private static final String DOMESTIC_APP_SECRET_KEY = "DOMESTIC_APP_SECRET";
    private static final String DOMESTIC_URL = "DOMESTIC_URL";

    private String appUrl = "";
    private String appId = Constant.appId;
    private String appSecret = Constant.appSecret;

    private void doLogin(final boolean isAutoLogin) {
        String url_login = Constant.WEB_SITE + Constant.URL_USER_LOGIN
                + "?username=" + username + "&password=" + pwd;

        StringRequest versionRequest = new StringRequest(Request.Method.GET, url_login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        if (null != context && !context.isFinishing()) {
                            dialogHelper.hideAlert();
                        }
                        if (result == null) {
                       /*     if (isAutoLogin) {
                                startActivity(new Intent(context, MainActivity.class));
                                context.finish();
                                return;
                            }*/
                            ToastUtil.show(context, getString(R.string.server_exception));
                            return;
                        }
                        try {
                            JSONObject object = new JSONObject(result);
                            int code = object.getInt(KeyConst.code);
                            String msg = object.getString(KeyConst.msg);
                            if (0 == code) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Constant.SP_TOKEN, accessToken);
                                editor.putString(KeyConst.username, username);
                                editor.putString(Constant.sp_pwd, pwd);
                                editor.apply();
                                App.token = accessToken;
                                App.passWord = pwd;
                                App.username = username;
                                App.phone = username;
                                App.phone = username;
                                //startActivity(new Intent(context, DeviceListActivity.class));
                                startActivity(new Intent(context, MainActivity.class));

                                context.finish();
                                return;
                            } else {
                                DialogUtils.showTipDialog(context, msg);
                            }
                        } catch (JSONException e) {
                            ToastUtil.show(context, R.string.server_exception);
                        }
                     /*   if (isAutoLogin) {
                            startActivity(new Intent(context, MainActivity.class));
                            context.finish();
                            return;
                        }*/
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            /*    if (isAutoLogin) {
                    startActivity(new Intent(context, MainActivity.class));
                    context.finish();
                    return;
                }*/
                if (null != context && !context.isFinishing()) {
                    dialogHelper.hideAlert();
                }
                String errorMsg = TextUtil.getErrorMsg(error);
                try {
                    if (errorMsg != null) {
                        JSONObject obj = new JSONObject(errorMsg);
                        if (obj != null) {
                            int errInt = obj.getInt(KeyConst.error);
                            //账号密码错误
                            if (errInt == 10001) {
                                DialogUtils.showTipDialog(context, getString(R.string.account_pwd_error));
                                return;
                                //账号冻结
                            } else if (errInt == 10003) {
                                DialogUtils.showTipDialog(context, getString(R.string.contact_admin));
                                return;
                            }
                        }

                    }
                } catch (JSONException e) {
                }
                ToastUtil.show(context, R.string.server_exception);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Requested-With", "XMLHttpRequest");
                //params.put(KeyConst.Content_Type,Constant.application_json);

                return params;
            }
        };
        App.requestQueue.add(versionRequest);

    }
}
