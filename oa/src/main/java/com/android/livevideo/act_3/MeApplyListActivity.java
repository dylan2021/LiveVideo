package com.android.livevideo.act_3;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.TimeUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.livevideo.view.ExRadioGroup;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gool Lee
 * @Date 回复列表
 */

@SuppressLint("WrongConstant")
public class MeApplyListActivity extends BaseFgActivity {
    public String TAG = MeApplyListActivity.class.getSimpleName();
    private ListView listview;
    private MeApplyListAdapter adapter;
    private int TYPE, id;
    private List<MsgInfo> applisList = new ArrayList<>();
    private MeApplyListActivity context;
    private RefreshLayout mRefreshLayout;
    private long startTimeL, endTimeL;
    private ExRadioGroup applyRg;
    private String TYPE_CODE = "";
    private String startTime = "19800101", endTime = "20800101";

    String[] TYPE_CODE_ARR = {"LEAVE", "BUSINESS_TRIP", "OVERTIME", "REGULAR_WORKER",
            "RECRUIT", "DIMISSION", "OFFICIAL_DOCUMENT", "REIMBURSE", "PURCHASE", "PAY",
            "PETTY_CASH", "NOTICE", "WAGE_AUDIT",Constant.WEIGH,Constant.RAW_PUT_IN_POOL,
            Constant.INGREDIENT_PUT_IN_POOL,Constant.SPRINKLE,  Constant.TAKE_OUT_POOL,
            Constant.RAW_MATERIAL_CHECK,  Constant.SEMIFINISHED_PRODUCT_CHECK
    };
    String[] TYPE_NAME_ARR = {"请假", "出差", "加班", "转正", "招聘", "离职", "公文",
            "报销", "请购单", "付款", "备用金", "公告", "工资审核",
            "过磅", "入池", "配料入池", "回淋", "起池", "原料检验","半成品检..",
    };
    private int choosedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_list);
        initStatusBar();

        context = this;
        Intent intent = getIntent();
        initTitleBackBt(intent.getStringExtra(KeyConst.title));
        id = intent.getIntExtra(KeyConst.id, 0);
        TYPE = intent.getIntExtra(KeyConst.type, 0);

        initView();
    }

    private void getData(String TYPE_CODE, String startDate, String endDate) {
        if (!NetUtil.isNetworkConnected(context)) {
            mRefreshLayout.finishRefresh(0);
            return;
        }
        String url = Constant.WEB_SITE + "/biz/process";
        switch (TYPE) {
            case -1://我的待办
                url = url + "/needDo/list";
                break;
            case 0:
                url = url + "/launch/all";
                break;
            case 1:
                url = url + "/done/all";
                break;
            case 2:
                url = url + "/inform/all";
                break;
            case 3:
                url = url + "/draft/all";
                break;
        }

        if (TextUtil.isEmpty(TYPE_CODE)) {
            url = url + "?startTime=" + startDate
                    + "&endTime=" + endDate;
        } else {
            url = url + "?type=" + TYPE_CODE + "&startTime=" + startDate + "&endTime=" + endDate;
        }
        Log.d(TAG, "数据"+url);
        Response.Listener<List<MsgInfo>> successListener = new Response
                .Listener<List<MsgInfo>>() {
            @Override
            public void onResponse(List<MsgInfo> result) {
                if (null != context && !context.isFinishing()) {
                    mRefreshLayout.finishRefresh(0);
                }
                adapter.setData(result, TYPE);
                if (result == null || result.size() == 0) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }
            }
        };

        Request<List<MsgInfo>> versionRequest = new
                GsonRequest<List<MsgInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (null != context && !context.isFinishing()) {
                            mRefreshLayout.finishRefresh(0);
                        }
                        ToastUtil.show(context, R.string.no_data);
                    }
                }, new TypeToken<List<MsgInfo>>() {
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


    @Override
    protected void onRestart() {
        super.onRestart();
        getData(TYPE_CODE, startTime, endTime);
    }

    private void initView() {
        Button filterBt = getTitleRightBt("");
        Drawable filterDrawable = context.getResources().getDrawable(R.drawable.ic_filter);
        filterBt.setCompoundDrawablesWithIntrinsicBounds(null, null, filterDrawable, null);
        filterBt.setVisibility(View.VISIBLE);
        filterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });

        listview = (ListView) findViewById(R.id.listView);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.autoRefresh(0);
        mRefreshLayout.setPrimaryColors(Color.WHITE);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                TYPE_CODE = "";
                choosedIndex = -1;
                startTimeL = 0;
                endTimeL = 0;
                startTime = "19800101";
                endTime = "20800101";
                getData(TYPE_CODE, startTime, endTime);
            }
        });
        Utils.setLoadHeaderFooter(context, mRefreshLayout);//设置头部,底部样式

        adapter = new MeApplyListAdapter(this, applisList);
        listview.setAdapter(adapter);
    }

    private void filter() {
        final Dialog dialog = new Dialog(context, R.style.dialog_top_to_bottom);
        dialog.setCanceledOnTouchOutside(true);

        View inflate = LayoutInflater.from(context).inflate(R.layout.
                layout_me_apply_top_filter, null);
        applyRg = inflate.findViewById(R.id.apply_rg);

        initRg();

        final TextView startTimeTv = inflate.findViewById(R.id.filter_time_bt_0);
        if (startTimeL > 0) {
            startTimeTv.setText(TimeUtils.getTimeYmd(startTimeL));
            startTimeTv.setBackgroundResource(R.drawable.shape_rg_seleted);
            startTimeTv.setTextColor(context.getResources().getColor(R.color.mainColor));
        }
        final TextView endTimeTv = inflate.findViewById(R.id.filter_time_bt_1);
        if (endTimeL > 0) {
            endTimeTv.setText(TimeUtils.getTimeYmd(endTimeL));
            endTimeTv.setBackgroundResource(R.drawable.shape_rg_seleted);
            endTimeTv.setTextColor(context.getResources().getColor(R.color.mainColor));
        }
        View.OnClickListener mDialogClickLstener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    //完成
                    case R.id.filter_ok_bt:
                        if (TextUtil.isEmpty(TYPE_CODE)) {
                            ToastUtil.show(context, "请选择搜索类型或者时间");
                            return;
                        }
                        if (startTimeL > 0) {
                            startTime = TimeUtils.getTimeYYYYMMDD(startTimeL);
                        }
                        if (endTimeL > 0) {
                            endTime = TimeUtils.getTimeYYYYMMDD(endTimeL);
                        }
                        getData(TYPE_CODE, startTime, endTime);
                        dialog.dismiss();
                        break;
                    case R.id.filter_cancel_bt:
                        dialog.dismiss();
                        break;
                    case R.id.filter_time_bt_0:
                        setTime(0, startTimeTv);
                        break;
                    case R.id.filter_time_bt_1:
                        setTime(1, endTimeTv);
                        break;
                }
            }
        };
        inflate.findViewById(R.id.filter_ok_bt).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.filter_cancel_bt).setOnClickListener(mDialogClickLstener);

        startTimeTv.setOnClickListener(mDialogClickLstener);
        endTimeTv.setOnClickListener(mDialogClickLstener);
        dialog.setContentView(inflate);

        DialogUtils.setDialogWindow(context, dialog, Gravity.TOP);
    }

    private void initRg() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 20, 5);
        lp.width = ImageUtil.getScreenWidth(context) / 5;
        lp.height = getResources().getDimensionPixelSize(R.dimen.me_filter_rb_height);
        for (int i = 0; i < TYPE_NAME_ARR.length; i++) {
            final TextView itemTv = new TextView(context);
            itemTv.setGravity(Gravity.CENTER);
            itemTv.setTextSize(14.5f);
            itemTv.setText(TYPE_NAME_ARR[i]);
            itemTv.setSingleLine();
            if (i == choosedIndex) {
                itemTv.setSelected(true);
            }

            itemTv.setBackgroundResource(R.drawable.selector_me_apply_rb_bg);
            itemTv.setLayoutParams(lp);

            final int finalI = i;
            itemTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choosedIndex = finalI;
                    TYPE_CODE = TYPE_CODE_ARR[finalI];
                    int childCount = applyRg.getChildCount();
                    for (int j = 0; j < childCount; j++) {
                        TextView childAt = (TextView) applyRg.getChildAt(j);
                        childAt.setSelected(finalI == j);
                        childAt.setTextColor(ContextCompat.getColor(context,
                                finalI == j ? R.color.mainColor : R.color.color212121));
                    }
                }
            });

            applyRg.addView(itemTv);
        }
    }

    private void setTime(final int type, final TextView timeTv) {
        TimePickerDialog.Builder timePickerDialog = DialogUtils.getTimePicker(context);
        timePickerDialog.setCallBack(new OnDateSetListener() {
            @Override
            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                if (0 == type) {
                    if (millseconds > System.currentTimeMillis()) {
                        ToastUtil.show(context, "开始时间不能大于现在");
                        return;
                    }
                    if (endTimeL != 0 && millseconds >= endTimeL) {
                        ToastUtil.show(context, "开始时间要小于结束时间");
                        return;
                    }
                    startTimeL = millseconds;
                } else {
                    if (millseconds < startTimeL) {
                        ToastUtil.show(context, "结束时间要大于开始时间");
                        return;
                    }
                    endTimeL = millseconds;
                }

                timeTv.setText(TimeUtils.getTimeYmd(millseconds));
                timeTv.setBackgroundResource(R.drawable.shape_rg_seleted);
                timeTv.setTextColor(context.getResources().getColor(R.color.mainColor));

            }
        });

        timePickerDialog.build().show(context.getSupportFragmentManager(), "");
    }

}
