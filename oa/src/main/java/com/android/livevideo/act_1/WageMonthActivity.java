package com.android.livevideo.act_1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.MonthItemInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.TimeUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gool Lee
 * <p>
 */
public class WageMonthActivity extends BaseFgActivity {
    private WageMonthActivity context;
    private List<MonthItemInfo> mDataList = new ArrayList<>();
    private ListView mListView;
    private WageMonthAdapter mAdapter;
    private RefreshLayout mRefreshLayout;
    private String id = "";
    private String timeYMchoosed;
    private TextView emptyTv;
    private int departmentId;
    private String buildWageMonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();

        setContentView(R.layout.activity_wage_month);
        context = this;
        id = getIntent().getStringExtra(KeyConst.id);

        init();
    }


    private void init() {
        initTitleBackBt("月度工资");
        Button filterBt = getTitleRightBt("");
        emptyTv = findViewById(R.id.empty_tv);

        Drawable filterDrawable = context.getResources().getDrawable(R.drawable.ic_filter);
        filterBt.setCompoundDrawablesWithIntrinsicBounds(null, null, filterDrawable, null);
        filterBt.setVisibility(View.VISIBLE);
        filterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TimePickerDialog.Builder monthPicker = DialogUtils.getMonthPicker(context);
                monthPicker.setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        timeYMchoosed = TimeUtils.getTimeYM(millseconds);
                        initTitleBackBt("月度工资(" + timeYMchoosed.replace("-", ".") + ")");
                        getDatas(timeYMchoosed);

                    }
                });
                monthPicker.build().show(context.getSupportFragmentManager(), "");
            }
        });

        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        mListView = (ListView) findViewById(R.id.list_view);

        mAdapter = new WageMonthAdapter(context, mDataList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonthItemInfo monthItemInfo = mDataList.get(position);
                Intent intent = new Intent(context, WageMonthAddActivity.class);
                intent.putExtra(KeyConst.id, monthItemInfo.getId());
                context.startActivity(intent);
            }
        });

        Utils.setLoadHeaderFooter(context, mRefreshLayout);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mDataList.clear();
                initTitleBackBt("月度工资");
                getDatas("");
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

    @Override
    protected void onStart() {
        super.onStart();
        mRefreshLayout.autoRefresh();
    }

    private void getDatas(String searchData) {
        TextUtil.initEmptyTv(context, emptyTv);
        if (!NetUtil.isNetworkConnected(context)) {
            mRefreshLayout.finishRefresh(0);
            return;
        }
        String url = Constant.WEB_SITE + "/biz/process/wage/all"
                + (TextUtil.isEmpty(searchData) ? "" : "?searchDate=" +
                searchData.replace("-", ""));

        Response.Listener<List<MonthItemInfo>> successListener = new Response
                .Listener<List<MonthItemInfo>>() {
            @Override
            public void onResponse(List<MonthItemInfo> result) {
                mDataList.clear();
                mAdapter.setData(result);
                if (result == null || result.size() == 0) {
                    mRefreshLayout.finishRefresh(0);
                    emptyTv.setText(context.getString(R.string.no_data));
                    mAdapter.setData(null);
                    emptyTv.setVisibility(View.VISIBLE);
                    return;
                }
                mDataList = result;
                mAdapter.setData(result);

                mRefreshLayout.finishRefresh(0);
            }
        };

        Request<List<MonthItemInfo>> versionRequest = new
                GsonRequest<List<MonthItemInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDataList.clear();
                        mAdapter.setData(null);
                        mRefreshLayout.finishRefresh(0);
                        emptyTv.setText(context.getString(R.string.server_exception));
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                }, new TypeToken<List<MonthItemInfo>>() {
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

    //筛选
    private String[] departmentItems = {"部门1", "部门2", "部门3"};

    private void showBuildMonthWageDialog() {
        final Dialog dialog = new Dialog(context, R.style.dialog_appcompat_theme);
        View inflate = LayoutInflater.from(context).inflate(R.layout.
                layout_wage_month_filter, null);
        final TextView departmentTv = inflate.findViewById(R.id.filter_time_bt_0);
        TextView maxTv = inflate.findViewById(R.id.filter_time_bt_1);
        View.OnClickListener mDialogClickLstener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.menu_ok_bt:
                        if (buildWageMonth == null) {
                            ToastUtil.show(context, "请选择要生成的月份");
                            return;
                        }

                        buildMonthWagePost();

                        dialog.dismiss();
                        break;
                    case R.id.filter_time_bt_0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        // 设置了标题就不要设置builder.setMessage()了，否则列表不起作用。

                        builder.setItems(departmentItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                departmentTv.setText(departmentItems[i]);
                                departmentId = i + 1;
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        break;
                    case R.id.filter_time_bt_1:
                        TimePickerDialog.Builder monthPicker = DialogUtils.getMonthPicker(context);
                        monthPicker.setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                if (millseconds > System.currentTimeMillis()) {
                                    ToastUtil.show(context, "不能大于本月");
                                    return;
                                }
                                buildWageMonth = TimeUtils.getTimeYM(millseconds);
                                ((TextView) v).setText(buildWageMonth);
                            }
                        });
                        monthPicker.build().show(context.getSupportFragmentManager(), "");
                        break;
                    case R.id.menu_cancel_bt:
                        //选择
                        dialog.dismiss();
                        break;
                }
            }
        };
        inflate.findViewById(R.id.menu_ok_bt).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.menu_cancel_bt).setOnClickListener(mDialogClickLstener);

        departmentTv.setOnClickListener(mDialogClickLstener);
        maxTv.setOnClickListener(mDialogClickLstener);
        dialog.setContentView(inflate);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawable(ContextCompat.
                getDrawable(context, R.drawable.shape_f5f5f5_20px));
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);

        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = ImageUtil.getScreenWidth(context) - 200;
        dialogWindow.setAttributes(params);
        dialog.show();

    }

    //生成月度工资
    private void buildMonthWagePost() {

        String url = Constant.WEB_SITE + "/biz/wage/wageSummary?wageCycle=" + buildWageMonth.replace("-", "");
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

        final FragmentManager fm = getSupportFragmentManager();
        DialogHelper.showWaiting(fm, "加载中...");

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        DialogHelper.hideWaiting(fm);
                        if (result == null || context == null) {
                            ToastUtil.show(context, "生成月度工资失败,稍后重试");
                            return;
                        }
                        try {
                            int code = result.getInt(KeyConst.status);
                            if (200 == code) {
                                ToastUtil.show(context, "生成月度工资成功");
                                mRefreshLayout.autoRefresh();
                            } else {
                                String message = result.getString(KeyConst.message);
                                DialogUtils.showTipDialog(context, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogHelper.hideWaiting(fm);
                String errorMsg = TextUtil.getErrorMsg(error);
                try {
                    if (errorMsg != null) {
                        JSONObject obj = new JSONObject(errorMsg);
                        if (obj != null) {
                            int errInt = obj.getInt(KeyConst.error);
                            String message = obj.getString(KeyConst.message);
                            DialogUtils.showTipDialog(context, message);
                            return;
                        }
                    }
                } catch (JSONException e) {
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

    public void buildMonthWageClick(View view) {
        showBuildMonthWageDialog();
    }
}