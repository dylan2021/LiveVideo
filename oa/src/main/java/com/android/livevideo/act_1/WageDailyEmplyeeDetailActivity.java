package com.android.livevideo.act_1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.bean.EmplyeeWageInfo;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Gool Lee
 * 人员工资详情
 */

public class WageDailyEmplyeeDetailActivity extends BaseFgActivity {
    private WageDailyEmplyeeDetailActivity context;
    private int id;
    private EmplyeeWageInfo info;
    private String remark, hourNum, pieceNum, deduction;
    private double totalWage = 0;
    private int type;
    private TextView remarkTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_daily_wage_emplyee_detail);

        context = this;
        type = getIntent().getIntExtra(KeyConst.type, 0);
        info = (EmplyeeWageInfo) getIntent().getSerializableExtra(KeyConst.OBJ_INFO);
        initTitleBackBt("");
        initView();
    }

    private void initView() {
        String employeeName = info.getEmployeeName();
        initTitleBackBt(employeeName + "-" + (type > 0 ? "工资详情" : "计件详情"));
        ((TextView) findViewById(R.id.wage_daily_name_tv)).setText(employeeName);
        ((TextView) findViewById(R.id.wage_daily_time_tv)).setText(info.getWorkDate());
        String projectName = type > 0 ? info.getProjectName() : Constant.xiaoliaobao_proj_name;
        ((TextView) findViewById(R.id.wage_daily_proj_tv)).setText(projectName);


        id = info.getId();
        remark = info.getRemark();

        totalWage = info.getTotalWage();

        hourNum = info.getHourNum() + "";
        pieceNum = info.getPieceNum() + "";
        //扣款
        deduction = info.getDeduction() + "";

        remarkTv = (TextView) findViewById(R.id.remark_tv);
        remarkTv.setText(remark);

        View.OnClickListener inputListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                MaterialDialog.Builder moneyInputDialog = DialogUtils.getMoneyInputDialog(context);
                moneyInputDialog.negativeText("确认修改");
                moneyInputDialog.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String moneyStr = ((EditText) dialog.getCustomView()).getText().toString();
                        if (TextUtils.isEmpty(moneyStr)) {
                            return;
                        }
                        switch (view.getId()) {
                            case R.id.wage_daily_hours_tv://计时
                                hourNum = moneyStr;
                                break;

                            case R.id.wage_daily_pieceNum_tv:
                                pieceNum = moneyStr;
                                break;
                            case R.id.deduction_tv://扣款
                                deduction = moneyStr;
                                double deduInt = Double.valueOf(deduction);
                                if (deduInt > totalWage) {
                                    ToastUtil.show(context, "当前修改的扣款金额大于合计金额");
                                }
                                break;
                        }

                        changeDataPost((TextView) view, moneyStr);
                    }
                }).show();
            }
        };

        //计时
        TextView hoursNumTv = findViewById(R.id.wage_daily_hours_tv);
        hoursNumTv.setOnClickListener(inputListener);
        hoursNumTv.setText(hourNum);
        //计件
        TextView pieceNumTv = findViewById(R.id.wage_daily_pieceNum_tv);
        pieceNumTv.setOnClickListener(inputListener);
        pieceNumTv.setText(pieceNum);
        //扣款
        TextView deductionTv = findViewById(R.id.deduction_tv);
        deductionTv.setOnClickListener(inputListener);
        deductionTv.setText(deduction);

        if (type == -1) {
            findViewById(R.id.detail_deduction_layout).setVisibility(View.GONE);
            findViewById(R.id.detail_hours_layout).setVisibility(View.GONE);
        }

    }

    private void changeDataPost(final TextView tv, final String moneyStr) {

        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        remark = remarkTv.getText().toString();
        Map<String, Object> map = new HashMap<>();


        map.put(KeyConst.id, id);
        map.put(KeyConst.pieceNum, pieceNum);
        map.put(KeyConst.remark, remark);

        String url;
        if (type > 0) {
            url = Constant.WEB_SITE + "/biz/wage/wageDetail";
            map.put(KeyConst.hourNum, hourNum);
            map.put(KeyConst.deduction, deduction);
        } else {
            url = Constant.WEB_SITE + "/biz/pieceMonthRecord/bizPieceMonthRecord";
        }
        JSONObject jsonObject = new JSONObject(map);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {

                if (result != null && result.toString().contains("200")) {
                    ToastUtil.show(context, "修改成功");
                    tv.setText(moneyStr);
                } else if (result != null && type == -1 && result.toString().contains("pieceNum")) {
                    ToastUtil.show(context, "修改成功");
                    tv.setText(moneyStr);
                } else {
                    ToastUtil.show(context, "修改失败,请稍后重试");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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


}
