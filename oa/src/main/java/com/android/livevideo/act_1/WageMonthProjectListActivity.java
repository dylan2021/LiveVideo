package com.android.livevideo.act_1;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.WageMonthProjectInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dylan
 */
public class WageMonthProjectListActivity extends BaseFgActivity {
    private WageMonthProjectListActivity context;
    private ListView mListView;
    private WageMonthProjectListAdapter adapter;
    private RefreshLayout mRefreshLayout;
    private int id;
    private EditText searchEt;
    private String searchData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_local_mateail_list);
        context = this;
        searchData = getIntent().getStringExtra(KeyConst.wageMonth);
        initTitleBackBt("项目月度工资(" + searchData + ")");

        initView();
    }


    private void initView() {

        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.autoRefresh();


        mListView = (ListView) findViewById(R.id.common_list_view);
        adapter = new WageMonthProjectListAdapter(context);
        mListView.setAdapter(adapter);

        Utils.setLoadHeaderFooter(context, mRefreshLayout);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                ToastUtil.longShow(context, getString(R.string.no_more_data));
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //请求数据
                getSearchData(searchData);
            }
        });

        searchEt = findViewById(R.id.search_et);
        final View titleLayout = findViewById(R.id.activity_title_layout);
        final TextView cancleBt = findViewById(R.id.search_cancle_bt);
        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    cancleBt.setVisibility(View.VISIBLE);
                    cancleBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            searchEt.setText("");
                            searchEt.clearFocus();
                            DialogUtils.hideKeyBorad(context);
                            getSearchData("");
                        }
                    });
                    titleLayout.setVisibility(View.GONE);
                } else {
                    cancleBt.setVisibility(View.GONE);
                    titleLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //getSearchData("?search=" + String.valueOf(editable).trim());
                    }
                }, 100);
            }
        });


       /* Button rightBt = getTitleRightBt("");
        Drawable filterDrawable = context.getResources().getDrawable(R.drawable.ic_add);
        rightBt.setCompoundDrawablesWithIntrinsicBounds(null, null, filterDrawable, null);
        rightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新增材料
                startActivity(new Intent(context, LocalMetarialAddActivity.class));
            }
        });*/

    }

    //获取数据
    private void getSearchData(String searchText) {
        adapter.setData(null);
        if (!NetUtil.isNetworkConnected(context)) {
            mRefreshLayout.finishRefresh(0);
            return;
        }
        String url = Constant.WEB_SITE + "/biz/wage/countWageOnMonth?size=300&searchDate=" + searchText;
        Response.Listener<WageMonthProjectInfo> successListener = new Response.Listener<WageMonthProjectInfo>() {
            @Override
            public void onResponse(WageMonthProjectInfo result) {
                mRefreshLayout.finishRefresh(0);
                if (result == null) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }
                adapter.setData(result.getRecords());
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.show(context, R.string.server_exception);
                mRefreshLayout.finishRefresh(0);
            }
        };

        Request<WageMonthProjectInfo> request = new GsonRequest<WageMonthProjectInfo>(Request.Method.GET,
                url, successListener, errorListener, new TypeToken<WageMonthProjectInfo>() {
        }.getType()) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                return params;
            }
        };
        App.requestQueue.add(request);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        searchEt.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DialogUtils.hideKeyBorad(context);
    }

}