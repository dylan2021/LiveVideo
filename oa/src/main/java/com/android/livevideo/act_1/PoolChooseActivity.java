package com.android.livevideo.act_1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.PoolInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dylan
 */
public class PoolChooseActivity extends BaseFgActivity {
    private PoolChooseActivity context;
    private ListView mListView;
    private PoolChooseAdapter adapter;
    private RefreshLayout mRefreshLayout;
    private int TYPE;
    private String titleStr[] = {"选择池号", "选择配料桶次", "选择过磅单", "选择料号","选择原料入池检验单"};
    private String hintStr[] = {"池号", " 产品名称/规格或配料桶次", "产品名称、车牌号", "料号","产品名称、车牌号"};
    private int STATUS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_pool_choose_list);
        context = this;
        Intent intent = getIntent();
        //池号:0  配料桶次:1  过磅单号:2
        TYPE = intent.getIntExtra(KeyConst.type, 0);
        STATUS = intent.getIntExtra(KeyConst.status, 0);
        initTitleBackBt(titleStr[TYPE]);

        initView();
    }


    private void initView() {
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.autoRefresh();

        mListView = (ListView) findViewById(R.id.common_list_view);
        adapter = new PoolChooseAdapter(context, TYPE);
        mListView.setAdapter(adapter);

        Utils.setLoadHeaderFooter(context, mRefreshLayout);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                ToastUtil.show(context, getString(R.string.no_more_data));
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //请求数据
                getSearchData("");
            }
        });

        final EditText searchEt = findViewById(R.id.search_et);
        searchEt.setHint(hintStr[TYPE]);
        final View titleLayout = findViewById(R.id.activity_title_layout);
        final TextView clearBt = findViewById(R.id.search_clear_bt);
        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (adapter != null) {
                        adapter.setData(null);
                    }
                    clearBt.setVisibility(View.VISIBLE);
                    clearBt.setOnClickListener(new View.OnClickListener() {
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
                    clearBt.setVisibility(View.GONE);
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
                        getSearchData(URLEncoder.encode(String.valueOf(editable).trim()));
                    }
                }, 100);
            }
        });


    }

    private List<PoolInfo> infos = new ArrayList<>();


    //获取数据
    private void getSearchData(String searchStr) {
        adapter.setData(null);
        if (!NetUtil.isNetworkConnected(context)) {
            mRefreshLayout.finishRefresh(0);
            return;
        }
        String url = Constant.WEB_SITE + "/biz";
        //池号:0  配料桶次:1  过磅单号:2
        switch (TYPE) {
            case 0:
                url = url + "/producePool/list";
                break;
            case 1:
                url = url + "/ingredient/list";
                break;
            case 2:
                // 只显示 原料检验: 1(等待发起) 入池: 3 (已通过)
                url = url + "/process/weigh/list" + "?status=" + STATUS;
                break;
            case 3:
                url = url + "/product/detail/list";
                break;
            case 4://关联原料检验单
                url = url + "/process/rawMaterialCheck/list";
                break;
        }

        if (!TextUtil.isEmpty(searchStr)) {
            String searchKey = TYPE == 2 ? "&productName=" : "?search=";
            url = url + searchKey + searchStr;
        }
        Response.Listener<List<PoolInfo>> successListener = new Response.Listener<List<PoolInfo>>() {
            @Override
            public void onResponse(List<PoolInfo> result) {
                mRefreshLayout.finishRefresh(0);
                adapter.setData(infos);
                infos = result;
                adapter.setData(result);
                if (result == null || result.size() == 0) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "错误" + TextUtil.getErrorMsg(volleyError));
                ToastUtil.show(context, R.string.server_exception);
                mRefreshLayout.finishRefresh(0);
            }
        };

        Request<List<PoolInfo>> request = new GsonRequest<List<PoolInfo>>(Request.Method.GET,
                url, successListener, errorListener, new TypeToken<List<PoolInfo>>() {
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
    protected void onPause() {
        super.onPause();
        DialogUtils.hideKeyBorad(context);
    }

}