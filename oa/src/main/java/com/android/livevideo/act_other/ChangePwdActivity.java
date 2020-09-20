package com.android.livevideo.act_other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.act_3.SysSettingsActivity;
import com.android.livevideo.dialogfragment.SimpleDialogFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.UrlConstant;
import com.android.livevideo.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 修改密码
 * Gool Lee
 */
public class ChangePwdActivity extends BaseFgActivity {

    private Button bt_find_pwd;
    private EditText et_old_pwd, newPwdET1;
    private SharedPreferences.Editor editor;
    private ChangePwdActivity context;
    private String tokenSp = "";
    private String spPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_change_pwd);

        initTitleBackBt("");
        SharedPreferences sp = getSharedPreferences(Constant.CONFIG_FILE_NAME,
                MODE_PRIVATE);
        tokenSp = sp.getString(Constant.SP_TOKEN, "");
        spPwd = sp.getString(Constant.sp_pwd, "");
        editor = sp.edit();
        context = ChangePwdActivity.this;


        bt_find_pwd = (Button) findViewById(R.id.bt_find_pwd);
        et_old_pwd = (EditText) findViewById(R.id.old_pwd_et);
        newPwdET1 = (EditText) findViewById(R.id.new_pwd_et1);

        bt_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwdStr = et_old_pwd.getText().toString().trim();
                String newPwdETStr1 = newPwdET1.getText().toString().trim();

                if (oldPwdStr == null || oldPwdStr.length() <= 0) {
                    ToastUtil.show(context, "原密码不能为空");
                    return;
                }
                if (!oldPwdStr.equals(spPwd)) {
                    ToastUtil.show(context, "原密码不正确");
                    return;
                }
                if (oldPwdStr.equals(newPwdETStr1)) {
                    ToastUtil.show(context, "新密码和原密码不能相同");
                    return;
                }
                if (newPwdETStr1 == null || newPwdETStr1.length() <= 0) {
                    ToastUtil.show(context, "请输入新密码");
                    return;
                }
                if (newPwdETStr1.length() < 6) {
                    ToastUtil.show(context, "新密码不能少于六位");
                    return;
                }

                doFindPwd(oldPwdStr, newPwdETStr1);
            }
        });
    }

    private void doFindPwd(final String oldPwdStr, final String newPwdETStr1) {
        String url = Constant.WEB_SITE + UrlConstant.url_change_pwd;
        final FragmentManager fm = getSupportFragmentManager();
        DialogHelper.showWaiting(fm, "加载中...");
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        final Map<String, String> map = new HashMap<>();
        map.put(KeyConst.newPassword, newPwdETStr1);
        map.put(KeyConst.oldPassword, oldPwdStr);
        JSONObject mapObj = new JSONObject(map);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                mapObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        if (result == null) {
                            DialogHelper.hideWaiting(fm);
                            ToastUtil.show(context, "密码修改失败");
                            return;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogHelper.hideWaiting(fm);
                if (error != null && error.toString().contains("End of input at character 0 of")) {
                    editor.putString(Constant.sp_pwd, "");
                    editor.commit();
                    showDialog(getString(R.string.pwd_change_success_reLogin_msg));
                } else {
                    ToastUtil.show(context, getString(R.string.server_exception));
                }
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

    private void showDialog(String msg) {
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

        dialogFragment.setNegativeButton(R.string.reLogin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                MainActivity.context.finish();
                SysSettingsActivity.content.finish();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                context.finish();
            }
        });
        dialogFragment.show(ft, "successDialog");
    }
}
