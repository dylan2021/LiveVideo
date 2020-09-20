package com.android.livevideo.act_3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.AttendInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.TimeUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Dylan
 * <p>
 * 我的考勤
 */
public class MeAttendanceActivity extends BaseFgActivity {
    private MeAttendanceActivity context;
    private TextView monthSeletedTv;
    private LinearLayout itemLayout;
    private String yyyymm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_me_attendance);
        context = this;

        initTitleBackBt("我的考勤");

        initView();
        getData(yyyymm);
    }


    private void getData(String yyyymm) {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/biz/attend/app/attendStatistics" +
                "?attendCycle=" + yyyymm;
        Response.Listener<AttendInfo> successListener = new Response
                .Listener<AttendInfo>() {

            @Override
            public void onResponse(AttendInfo info) {
                if (info == null) {
                    ToastUtil.show(context, getString(R.string.no_data));
                    return;
                }
                String employeeName = info.employeeName;
                String deptName = info.deptName;

                setLayoutData(info);

                ((TextView) findViewById(R.id.me_name_tv)).setText(employeeName);
                ((TextView) findViewById(R.id.me_icon_tv)).setText(TextUtil.getLast2(employeeName));
                ((TextView) findViewById(R.id.me_department_tv)).setText("考勤组:" + deptName);

            }
        };

        Request<AttendInfo> versionRequest = new
                GsonRequest<AttendInfo>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<AttendInfo>() {
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

    private void setLayoutData(AttendInfo info) {
        itemLayout.removeAllViews();
        addItem("出勤天数", info.attendDays + "天");
        addItem("迟到次数", info.comeLateTimes + "次");
        addItem("早退次数", info.leaveEarlyTimes + "次");
        addItem("上班缺卡次数", info.amMissRecordTimes + "次");
        addItem("下班缺卡次数", info.pmMissRecordTimes + "次");
        addItem("旷工天数", info.absentDays + "天");
        addItem("出差天数", info.businessTripDays + "天");
        addItem("请假时长", info.totalLeave + "小时");
        addItem("加班时长", info.totalOvertime + "小时");
    }

    private void addItem(String title, String value) {
        View itemView = View.inflate(context, R.layout.item_month_wage_emplyee_detail, null);
        TextView keyTv = (TextView) itemView.findViewById(R.id.item_key_tv);
        TextView valueTv = (TextView) itemView.findViewById(R.id.item_value_tv);
        keyTv.setText(title);
        valueTv.setText(value);

        itemLayout.addView(itemView);
    }


    private void initView() {
        itemLayout = (LinearLayout) findViewById(R.id.item_layout);

        monthSeletedTv = findViewById(R.id.me_month_seleted_tv);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1); //得到前一个月
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;//月份加一天
        yyyymm = year + (month < 10 ? "0" + month : "" + month);
        final String yyyy_mm = year + (month < 10 ? "-0" + month : "-" + month);
        monthSeletedTv.setText(yyyy_mm);
        monthSeletedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.Builder monthPicker = DialogUtils.getMonthPicker(context);
                monthPicker.setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        yyyymm = TimeUtils.getTimeYM(millseconds);
                        monthSeletedTv.setText(yyyymm);
                        //重新请求数据
                        getData(yyyymm.replace("-", ""));
                    }
                });
                monthPicker.build().show(context.getSupportFragmentManager(), "");
            }
        });
    }

}