package com.android.livevideo.act_3;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.act_other.ChangePhoneShowActivity;
import com.android.livevideo.act_other.ChangePwdActivity;
import com.android.livevideo.act_other.LoginActivity;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Gool Lee
 */
public class SysSettingsActivity extends BaseFgActivity {

    private ToggleButton but_load;
    private int delayMillis = 100;
    public static SysSettingsActivity content;
    private TextView tv_clear;
    private SharedPreferences.Editor sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_me_settings);
        content = this;
        Button left_but = (Button) findViewById(R.id.left_bt);
        TextView titleTv = (TextView) findViewById(R.id.center_tv);
        titleTv.setText("设置");
        left_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sp = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE).edit();
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        RelativeLayout changePhone = (RelativeLayout) findViewById(R.id.layout_1);
        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(content, ChangePhoneShowActivity.class));

            }
        });

        ((TextView) findViewById(R.id.version_tv)).setText(Utils.getVersionCode(content) );

    }

    public void onLogoutClick(View view) {
        showLogout();
    }

    public void onChangePwdClick(View view) {
        startActivity(new Intent(this, ChangePwdActivity.class));
    }


    private void showLogout() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_logout, null);

        inflate.findViewById(R.id.logout_yes_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constant.WEB_SITE + "/authorization/token/logout";
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, url,
                        new JSONObject(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        sp.putString(Constant.sp_pwd, "").commit();
                        content.finish();
                        if (MainActivity.context != null) {
                            MainActivity.context.finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sp.putString(Constant.sp_pwd, "").commit();
                        startActivity(new Intent(content, LoginActivity.class));
                        content.finish();
                        if (MainActivity.context != null) {
                            MainActivity.context.finish();
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
                App.requestQueue.add(jsonRequest);


                dialog.cancel();
            }
        });
        inflate.findViewById(R.id.logout_cancel_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setContentView(inflate);

        DialogUtils.setDialogWindow(content, dialog, Gravity.BOTTOM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        content = null;
    }
}
