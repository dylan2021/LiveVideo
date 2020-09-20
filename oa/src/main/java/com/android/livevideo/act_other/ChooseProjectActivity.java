/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.livevideo.act_other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.adapter.ChooseDataAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.bean.ProjDeptInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.AuthsConstant;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.UrlConstant;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gool Lee
 */
public class ChooseProjectActivity extends BaseFgActivity {

    private Button title_bar;
    private TextView tv_content;
    private TextView guideContentTv, titleTv;
    private LinearLayout guildeLayout;
    private TabLayout mTopTab0, mTopTab1;
    private RecyclerView recyclerView;
    private ArrayList<String> pinYinList = new ArrayList<>();
    private ChooseDataAdapter adapter;
    private SharedPreferences.Editor spEd;
    private ChooseProjectActivity context;
    private int AUTH_TYPE = 0;
    private View finishBt;
    private SharedPreferences sp;
    private boolean isFirstLuncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        context = this;
        setContentView(R.layout.activity_choose_project);

        finishBt = findViewById(R.id.left_bt);
        titleTv = (TextView) findViewById(R.id.center_tv);

        sp = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        spEd = sp.edit();
        isFirstLuncher = sp.getBoolean(KeyConst.IS_FIRST_LUNCHER_SP, true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ChooseDataAdapter(null, context, isFirstLuncher);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChooseDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int id, String name) {
                spEd.putInt(KeyConst.SP_CHOOSE_ID, id);
                spEd.putInt(AuthsConstant.CHOOSE_AUTH_TYPE, AUTH_TYPE);
                spEd.putInt(AuthsConstant.AUTH_TYPE, AUTH_TYPE);
                spEd.putString(KeyConst.SP_PROJECT_IMG_URL, "");
                spEd.putString(KeyConst.SP_MAIN_NAME, name);
                spEd.putBoolean(KeyConst.IS_FIRST_LUNCHER_SP, false);
                spEd.commit();

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                context.finish();
                if (!isFirstLuncher) {
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        getData();
        finishBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBtfinish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backBtfinish();
    }

    private void backBtfinish() {
        //不是第一次启动
        if (!isFirstLuncher) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            finish();
        }
    }

    //获取权限数据列表
    private void getData() {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        final DialogHelper dialogHelper = new DialogHelper(getSupportFragmentManager(), context);
        dialogHelper.showAlert("加载中...", true);
        String url = Constant.WEB_SITE + UrlConstant.url_biz_data;
        Response.Listener<ProjDeptInfo> successListener = new Response
                .Listener<ProjDeptInfo>() {
            @Override
            public void onResponse(ProjDeptInfo result) {
                if (null != context && !context.isFinishing()) {
                    dialogHelper.hideAlert();
                }
                if (result == null) {
                    ToastUtil.show(context, "暂无权限");
                    return;
                }

            }
        };

        Request<ProjDeptInfo> versionRequest = new
                GsonRequest<ProjDeptInfo>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        if (null != context && !context.isFinishing()) {
                            dialogHelper.hideAlert();
                        }
                        if (volleyError != null && volleyError.networkResponse != null &&
                                volleyError.networkResponse.statusCode == 403) {
                            DialogUtils.showTipDialog(context, "该账号暂无任何数据查看权限");
                        } else {
                            ToastUtil.show(context, getString(R.string.request_failed_retry_later));
                        }
                    }
                }, new TypeToken<ProjDeptInfo>() {
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
