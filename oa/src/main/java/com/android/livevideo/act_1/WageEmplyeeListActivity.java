package com.android.livevideo.act_1;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.bean.EmplyeeWageInfo;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.ReportBean;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gool Lee
 */
public class WageEmplyeeListActivity extends BaseFgActivity {
    private WageEmplyeeListActivity context;
    private TextView titleTv;
    private List<ReportBean.ContentBean> mDataList = new ArrayList<>();
    private ListView mListView;
    private WageEmplyeeListAdapter mAdapter;
    private Button rightBt;
    private TabLayout mTopTab;
    private RefreshLayout mRefreshLayout;
    private String[] topArrReport = {"正式员工", "临时员工"};
    private int tabPosition;
    private String url_param = "";
    private int type = 1;
    private String attendRewardNum;
    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_wage_emplyee_list);
        context = this;
        initTitleBackBt("工资详情");
        url_param = getIntent().getStringExtra(KeyConst.id);
        type = getIntent().getIntExtra(KeyConst.type, 1);
        status = getIntent().getIntExtra(KeyConst.status, 0);

        initView();
    }


    private void initView() {
        if (2 == type && status == 1) {
            //请求
            getEmplyeeList();
            Button filterBt = getTitleRightBt("全勤奖");
            filterBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (setEmployeeList == null || setEmployeeList.size() == 0) {
                        //重新请求
                        getEmplyeeList();
                    }
                    showEmplyeeChoosedDialog(setEmployeeList);
                }
            });
        }
        mTopTab = (TabLayout) findViewById(R.id.fragment0_report_top_tab);
        titleTv = (TextView) findViewById(R.id.center_tv);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.autoRefresh();

        String[] tabArr = topArrReport;
        for (String tabTitle : tabArr) {
            TabLayout.Tab tab = mTopTab.newTab();
            tab.setTag(tabTitle);
            tab.setText(tabTitle);
            mTopTab.addTab(tab);
        }
        mTopTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                getListData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mListView = (ListView) findViewById(R.id.common_list_view);
        mAdapter = new WageEmplyeeListAdapter(context, type, status);
        mListView.setAdapter(mAdapter);

        Utils.setLoadHeaderFooter(context, mRefreshLayout);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //请求数据
                getListData();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                ToastUtil.show(context, getString(R.string.no_more_data));
            }
        });
    }

    private void getEmplyeeList() {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        String url = Constant.WEB_SITE + "/biz/wage/attendReward/" + url_param;
        Response.Listener<List<EmployeeInfo>> successListener = new Response
                .Listener<List<EmployeeInfo>>() {
            @Override
            public void onResponse(List<EmployeeInfo> result) {
                setEmployeeList = result;
            }
        };

        Request<List<EmployeeInfo>> versionRequest = new
                GsonRequest<List<EmployeeInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<List<EmployeeInfo>>() {
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

    private List<EmployeeInfo> setEmployeeList = new ArrayList<>();

    private void showEmplyeeChoosedDialog(final List<EmployeeInfo> list) {
        final JSONArray wageStatisticsIds = new JSONArray();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style
                .dialog_appcompat_theme_fullscreen);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_emplyee_all_work_money, null);
        final TextView allWorkMoneyTv = (TextView) v.findViewById(R.id.all_work_money_tv);
        allWorkMoneyTv.setText(attendRewardNum);
        allWorkMoneyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog.Builder moneyInputDialog = DialogUtils.getMoneyInputDialog(context);
                moneyInputDialog.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        attendRewardNum = ((EditText) dialog.getCustomView()).getText().toString();
                        allWorkMoneyTv.setText(attendRewardNum);
                    }
                }).show();
            }
        });
        LinearLayout itemsLayout = (LinearLayout) v.findViewById(R.id.emplyee_seleted_items_layout);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final EmployeeInfo itemInfo = list.get(i);
                if (itemInfo == null) {
                    return;
                }
                View itemView = View.inflate(context, R.layout.layout_dialog_emplyee_all_work_money_item, null);
                final TextView nameTv = (TextView) itemView.findViewById(R.id.emplyee_name_tv);
                final TextView typeTv = (TextView) itemView.findViewById(R.id.emplyee_type_tv);
                final TextView sexTv = (TextView) itemView.findViewById(R.id.emplyee_sex_tv);
                String employeeTypeStr = itemInfo.getEmployeeTypeStr();
                typeTv.setText("  (" + TextUtil.remove_N(employeeTypeStr) + ")");

                nameTv.setText(itemInfo.getEmployeeName());
                nameTv.setSelected(itemInfo.getSeleted());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean selected = nameTv.isSelected();
                        nameTv.setSelected(!selected);

                        itemInfo.setSeleted(!selected);
                    }
                });
                itemsLayout.addView(itemView);
            }

        } else {
            ToastUtil.show(context, "无人员");
        }
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);

        v.findViewById(R.id.dialog_btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.emplyee_seleted_save_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    EmployeeInfo employeeInfo = list.get(i);
                    boolean seleted = employeeInfo.getSeleted();
                    setEmployeeList.get(i).setSeleted(seleted);
                    if (seleted) {
                        wageStatisticsIds.put(employeeInfo.getId());
                    }
                }
                if (TextUtils.isEmpty(attendRewardNum)) {
                    ToastUtil.show(context, "请设置全勤奖金额");
                    return;
                }
                if ("0".equals(attendRewardNum)) {
                    ToastUtil.show(context, "全勤奖金额不能为0");
                    return;
                }

                //正式
                if (wageStatisticsIds.length() == 0) {
                    ToastUtil.show(context, "请至少选择一个人员");
                    return;
                }
                //设置post
                setAllAttendReward(wageStatisticsIds, attendRewardNum, dialog);

            }
        });
    }

    //设置全勤奖
    private void setAllAttendReward(JSONArray wageStatisticsIds, String attendRewardNum, final Dialog dialog) {

        Map<String, Object> map = new HashMap<>();
        map.put(KeyConst.wageStatisticsId, wageStatisticsIds);
        map.put(KeyConst.attendReward, attendRewardNum);
        //添加
        final FragmentManager fm = getSupportFragmentManager();
        DialogHelper.showWaiting(fm, "加载中...");
        String url = Constant.WEB_SITE + "/biz/wage/wageStatistics/attendReward";
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url,
                new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        if (result == null) {
                            DialogHelper.hideWaiting(fm);
                            ToastUtil.show(context, "设置失败,稍后重试");
                            return;
                        }
                        ToastUtil.show(context, "设置成功");
                        DialogHelper.hideWaiting(fm);
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogHelper.hideWaiting(fm);
                String errorMsg = TextUtil.getErrorMsg(error);
                if (errorMsg != null) {
                    try {
                        JSONObject obj = new JSONObject(errorMsg);
                        if (obj != null && obj.getInt(KeyConst.error) == 20001) {
                            DialogUtils.showTipDialog(context, obj.getString(KeyConst.message));
                            return;
                        }
                    } catch (JSONException e) {
                    }
                }

                ToastUtil.show(context, getString(R.string.server_exception));
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

    //获取数据
    private void getListData() {
        mAdapter.setData(null);
        if (!NetUtil.isNetworkConnected(context)) {
            mRefreshLayout.finishRefresh(0);
            return;
        }
        String p_xiaoliaobao = "/biz/pieceMonthRecord/bizPieceMonthRecord/list?" + url_param + "&employeeType=";
        String p_1 = "/biz/wage/wageDetail/all?" + url_param + "&employeeType=";
        String p_2 = "/biz/wage/wageStatistics/all/" + url_param + "?employeeType=";
        String url = Constant.WEB_SITE + (type == -1 ? p_xiaoliaobao : type == 1 ? p_1 : p_2) + (tabPosition + 1);//1  正式   2 临时
        Log.d(TAG, "请求数据:" + url);
        Response.Listener<List<EmplyeeWageInfo>> successListener = new Response.Listener<List<EmplyeeWageInfo>>() {
            @Override
            public void onResponse(List<EmplyeeWageInfo> result) {
                mRefreshLayout.finishRefresh(0);
                if (result == null || result.size() == 0) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }
                Log.d(TAG, "请求数据成功:" + result.size());
                mAdapter.setData(result);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "请求数据失败:" + volleyError);
                mRefreshLayout.finishRefresh(0);
            }
        };

        Request<List<EmplyeeWageInfo>> request = new GsonRequest<List<EmplyeeWageInfo>>(Request.Method.GET,
                url, successListener, errorListener, new TypeToken<List<EmplyeeWageInfo>>() {
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
        if (null != mRefreshLayout) {
            mRefreshLayout.autoRefresh(0);
        }
    }

}