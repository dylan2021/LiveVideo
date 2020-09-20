package com.android.livevideo.act_1;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.EmplyeeWageInfo;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Gool Lee
 * 人员工资详情
 */

public class WageMonthEmplyeeDetailActivity extends BaseFgActivity {
    private WageMonthEmplyeeDetailActivity context;
    private int id, status;
    private EmplyeeWageInfo info;
    private LinearLayout itemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_month_wage_emplyee_detail);

        context = this;

        id = getIntent().getIntExtra(KeyConst.id, 0);
        status = getIntent().getIntExtra(KeyConst.status, 0);
        info = (EmplyeeWageInfo) getIntent().getSerializableExtra(KeyConst.OBJ_INFO);
        String employeeName = info.getEmployeeName();

        initTitleBackBt(employeeName + "-工资详情");
        setTv(R.id.name_tv, info.getEmployeeName());
        setTv(R.id.time_tv, info.getWageYear() + "年" + info.getWageMonth() + "月");
        setTv(R.id.wage_should_tv, info.getPayableWage());
        setTv(R.id.wage_real_tv, info.getRealWage() + "");


        initItemsLayout();
    }

    private void initItemsLayout() {
        itemLayout = (LinearLayout) findViewById(R.id.item_layout);
        String moneyUnit = "(元)";

        setItemView("个人所得税", moneyUnit, info.getIncomeTax()+"");
        setItemView("应纳税额准用扣除", moneyUnit, info.getTaxDeduction()+"");
        setItemView("应纳税所得额", moneyUnit, info.getTaxAmount()+"");
        setItemView("全勤奖", moneyUnit, info.getAttendReward()+"");

        setItemView("总计时", "(时)", info.getHourNum());
        setItemView("总计时工资", moneyUnit, info.getHourlyWage()+"");

        setItemView("总计件", "", info.getPieceNum());
        setItemView("总计件工资", moneyUnit, info.getPieceWage() + "");

        //草稿箱进来,配料明细列表
        HashMap map = (HashMap)info.getDeduction();
        if (map==null) {
            map=new HashMap();
        }
        double waterAmount = (double) map.get(KeyConst.waterAmount);
        double attendAmount = (double) map.get(KeyConst.attendAmount);
        double fondAmount = (double) map.get(KeyConst.fondAmount);
        double qualityAmount = (double) map.get(KeyConst.qualityAmount);
        double stayAmount = (double) map.get(KeyConst.stayAmount);
        double otherAmount = (double) map.get(KeyConst.otherAmount);
        double totalDeduction = waterAmount + stayAmount + fondAmount + attendAmount + qualityAmount + otherAmount;
        setItemView("总扣款", moneyUnit, totalDeduction + "");
        setItemView("扣款-水电费", moneyUnit, waterAmount + "");
        setItemView("扣款-住宿", moneyUnit, stayAmount + "");
        setItemView("扣款-伙食", moneyUnit, fondAmount + "");
        setItemView("扣款-考勤", moneyUnit, attendAmount + "");
        setItemView("扣款-质量", moneyUnit, qualityAmount + "");
        setItemView("扣款-其他", moneyUnit, otherAmount + "");
    }

    private void setItemView(String key, String unit, final String value) {
        View itemView = View.inflate(context, R.layout.item_month_wage_emplyee_detail, null);
        TextView keyTv = (TextView) itemView.findViewById(R.id.item_key_tv);
        TextView unitTv = (TextView) itemView.findViewById(R.id.item_unit_tv);
        unitTv.setText(unit);
        TextView valueTv = (TextView) itemView.findViewById(R.id.item_value_tv);

        if (key.contains("总扣款")) {
            keyTv.setTextColor(ContextCompat.getColor(context, R.color.red_deduction));
            unitTv.setTextColor(ContextCompat.getColor(context, R.color.red_deduction));
            valueTv.setTextColor(ContextCompat.getColor(context, R.color.red_deduction));
        } else if (key.contains("扣款-")) {
            keyTv.setTextColor(ContextCompat.getColor(context, R.color.a5a5a5));
        }

        keyTv.setText(key);
        valueTv.setText(value);
        if (key.equals("个人所得税") && status == 1) {
            Drawable ic_next = context.getResources().getDrawable(
                    R.drawable.ic_next);
            valueTv.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
            valueTv.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_next, null);
            valueTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    MaterialDialog.Builder moneyInputDialog =
                            DialogUtils.getMoneyInputDialog(context, value);
                    moneyInputDialog.onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            final String incomeTax = ((EditText) dialog.getCustomView()).getText().toString();
                            if (TextUtil.isEmpty(incomeTax)) {
                                return;
                            }
                            DialogHelper.showWaiting(getSupportFragmentManager(), "加载中...");
                            String url = Constant.WEB_SITE + "/biz/wage/wageStatistics/incomeTax";
                            Map<String, Object> map = new HashMap<>();
                            JSONArray wageStatisticsId = new JSONArray();
                            wageStatisticsId.put(id);
                            map.put(KeyConst.wageStatisticsId, wageStatisticsId);
                            map.put(KeyConst.incomeTax, incomeTax);
                            JSONObject jsonObject = new JSONObject(map);
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url,
                                    jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject result) {
                                            DialogHelper.hideWaiting(getSupportFragmentManager());
                                            if (result != null && result.toString().contains("200")) {
                                                ToastUtil.show(context, "设置成功");
                                                ((TextView) view).setText(incomeTax);
                                            } else {
                                                ToastUtil.show(context, "设置失败");
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    DialogHelper.hideWaiting(getSupportFragmentManager());
                                    String errorMsg = TextUtil.getErrorMsg(error);
                                    Log.d(TAG, "修改失败" + errorMsg);

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
                    }).show();
                }
            });
        }
        itemLayout.addView(itemView);
    }
}
