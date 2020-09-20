package com.android.livevideo.act_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.bean.WageDailyInfo;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gool Lee
 */

public class WageDailyListAdapter extends BaseAdapter {
    private boolean hasAuth;
    private LayoutInflater mInflater;
    private List<WageDailyInfo> progressList = new ArrayList<>();
    private WageDailyListActivity context;
    private int type;

    public WageDailyListAdapter(WageDailyListActivity context, int type, boolean hasAuth) {
        this.context = context;
        this.type = type;
        this.hasAuth = hasAuth;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return progressList == null ? 0 : progressList.size();
    }

    @Override
    public Object getItem(int i) {
        return progressList == null ? null : progressList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final WageDailyInfo info = progressList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new WageDailyListAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.item_daily_wage, viewGroup, false);
            holder.auditLayoutTv = convertView.findViewById(R.id.audit_layout);
            holder.audit_bt_1 = convertView.findViewById(R.id.audit_bt_1);
            holder.audit_bt_2 = convertView.findViewById(R.id.audit_bt_2);
            holder.nameTv = convertView.findViewById(R.id.name_tv);
            holder.timeTv = convertView.findViewById(R.id.dialy_wage_item_time_tv);
            holder.nextTv = convertView.findViewById(R.id.next_tv);

            holder.totalWageTv = convertView.findViewById(R.id.total_wage_tv);
            holder.everyHoursWageTv = convertView.findViewById(R.id.every_hours_wage_tv);
            holder.everyNumberWageTv = convertView.findViewById(R.id.every_number_wage_tv);
            holder.moneyDeductTv = convertView.findViewById(R.id.status_tv);

            holder.employeeNumberTv = convertView.findViewById(R.id.employee_number_tv);
            holder.totalSumHoursTv = convertView.findViewById(R.id.total_hours_tv);
            holder.totalSumNumberTv = convertView.findViewById(R.id.total_sum_number_tv);
            convertView.setTag(holder);
        } else {
            holder = (WageDailyListAdapter.ViewHolder) convertView.getTag();
        }

        if (null != info) {
            int status = info.getStatus();
            holder.nextTv.setText(Utils.getStatusText(status + 1));
            holder.nextTv.setTextColor(Utils.getStatusColor(context, status));

            if (hasAuth) {
                // status=待审核  1:正在审核   2. 通过  3. 驳回
                holder.auditLayoutTv.setVisibility(status == 1 ? View.VISIBLE : View.GONE);
            }

            int pieceWageId = info.getPieceWageId();
            int deptId = info.getDeptId();
            String workDate = info.getWorkDate();

            String url_str = "workDate=" + workDate +
                    "&pieceWageId=" + pieceWageId + "&deptId=" + deptId;
            if (type == -1) {

                url_str = "workDate=" + workDate +
                        "&pieceMonthCategoryId=" + Constant.xiaoliaobao_proj_id + "&deptId=" + deptId;
            }
            final String url_param = url_str;
            String s = info.getPieceMonthCategoryName() + "(" + workDate + ")";
            holder.nameTv.setText((type > 0 ? info.getProjectName() : s) + "(" + info.getDeptName() + ")");
            holder.employeeNumberTv.setText("参与人数：" + info.getPeopleNum() + "人");
            String unit = TextUtil.remove_N(info.getUnit());
            if (type > 0) {
                holder.timeTv.setText(workDate);
                holder.totalWageTv.setText("合计工资：" + info.getTotalWage() + "元");

                holder.everyHoursWageTv.setText("计时工资：" + info.getTotalHourlyWage() + "元");

                holder.everyNumberWageTv.setText("计件工资：" + info.getTotalPieceWage() + "元");
                holder.moneyDeductTv.setText("扣款：" + info.getTotalDeduction() + "元");

                holder.totalSumHoursTv.setText("总计时：" + info.getTotalHourNum() + "小时");
                holder.totalSumNumberTv.setText("总件数：" + info.getTotalPieceNum() + unit);
            } else {
                holder.timeTv.setPadding(0, 3, 0, 0);
                holder.everyHoursWageTv.setText("件数：" + info.getTotalPieceMonthNum() + unit);
                holder.totalWageTv.setVisibility(View.GONE);
                holder.everyNumberWageTv.setVisibility(View.GONE);
                holder.moneyDeductTv.setVisibility(View.GONE);
                holder.totalSumHoursTv.setVisibility(View.GONE);
                holder.totalSumNumberTv.setVisibility(View.GONE);
            }
            final Map<String, Object> map = new HashMap<>();
            if (type > 0) {
                map.put(KeyConst.pieceWageId, pieceWageId);
            } else {
                map.put(KeyConst.pieceMonthCategoryId, info.getPieceMonthCategoryId());
            }
            map.put(KeyConst.deptId, deptId);
            map.put(KeyConst.workDate, workDate);
            holder.audit_bt_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map.put(KeyConst.status, 2);//通过
                    postAudit(new JSONObject(map), context.getString(R.string.wage_daily_agress));
                }
            });
            holder.audit_bt_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map.put(KeyConst.status, 3);//驳回
                    postAudit(new JSONObject(map), context.getString(R.string.wage_daily_reject));
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WageEmplyeeListActivity.class);
                    intent.putExtra(KeyConst.id, url_param);
                    intent.putExtra(KeyConst.type, type);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private void postAudit(JSONObject jsonObject, String title) {
        MaterialDialog.Builder dialog = DialogUtils.getTwoBtDialog(context, title);
        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                String url = Constant.WEB_SITE + (type > 0 ? "/biz/wage/auditWageOnDay" :
                        "/biz/pieceMonthRecord/auditPieceMonthPer");
                final FragmentManager fm = context.getSupportFragmentManager();
                DialogHelper.showWaiting(fm, context.getString(R.string.loading));
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url,
                        jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        dialog.dismiss();
                        if (null != context && !context.isFinishing()) {
                            DialogHelper.hideWaiting(fm);
                        }
                        if (result == null) {
                            ToastUtil.show(context, context.getString(R.string.commit_faild));
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        DialogHelper.hideWaiting(fm);
                        if (error != null && error.toString().contains("End of input at character 0 of")) {
                            ToastUtil.show(context, context.getString(R.string.commit_success));
                            context.notifyData();
                        } else {
                            ToastUtil.show(context, context.getString(R.string.commit_faild));
                        }

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

    public void setData(List<WageDailyInfo> processorList) {
        progressList = processorList;
        notifyDataSetChanged();
    }


    public class ViewHolder {
        private RelativeLayout auditLayoutTv;
        private Button audit_bt_1, audit_bt_2;
        private TextView nameTv, nextTv, timeTv, totalSumHoursTv,
                totalSumNumberTv, employeeNumberTv,
                totalWageTv, moneyDeductTv, everyHoursWageTv, everyNumberWageTv;

    }

}
