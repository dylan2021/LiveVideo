package com.android.livevideo.act_other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;

/**
 * Gool  显示手机号
 */
public class ChangePhoneShowActivity extends BaseFgActivity {
    private String TAG = ChangePhoneShowActivity.class.getSimpleName();
    public static ChangePhoneShowActivity context;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_change_phone_show);
        context = this;
        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        initTitleBackBt("更改手机号");
        context=this;

        String phone = preferences.getString(KeyConst.username, "");
        ((TextView) findViewById(R.id.phone_tv)).setText(phone);
        init();
    }

    private void init() {
        Button changeBt = (Button) findViewById(R.id.register);
        changeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ChangePhoneActivity.class));

            }
        });


    }


}