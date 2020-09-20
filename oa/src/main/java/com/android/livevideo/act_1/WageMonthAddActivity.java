package com.android.livevideo.act_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.DeptInfo;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.bean.GroupInfo;
import com.android.livevideo.bean.MonthItemInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  @author Gool Lee
 */
public class WageMonthAddActivity extends BaseFgActivity {
    private List<EmployeeInfo> formalEmployeeList = new ArrayList<>();
    private List<EmployeeInfo> temporaryEmployeeList = new ArrayList<>();
    private WageMonthAddActivity context;
    private TextView timeTv, belongDeptTv;
    private TextView emplyeesTv, totalWageSumTv;
    private TextView moneyDeductTv, formalEmployeeTv, temporaryEmployeeTv;
    private TextView applyerNameTv;
    private long inTime;
    private Button commitBt, deleteBt;
    private MonthItemInfo info;
    private int processId;
    private EditText remarkTv;
    private TextView informsTv;
    private RelativeLayout informsLaout;
    private String title;
    private int status;
    private int wageYear;
    private String WAGE_MONTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_wage_month_add);
        context = this;
        processId = getIntent().getIntExtra(KeyConst.id, 0);
        initTitleBackBt("");

        initView();

        getData();

       /* Button rightBt = getTitleRightBt("明细");
        rightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void getData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        DialogHelper.showWaiting(getSupportFragmentManager(), "加载中...");
        String url = Constant.WEB_SITE + "/biz/process/" + processId;
        Response.Listener<MonthItemInfo> successListener = new Response
                .Listener<MonthItemInfo>() {
            @Override
            public void onResponse(MonthItemInfo result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }
                info = result;
                setView();
            }
        };

        Request<MonthItemInfo> versionRequest = new
                GsonRequest<MonthItemInfo>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, R.string.server_exception);
                        DialogHelper.hideWaiting(getSupportFragmentManager());
                    }
                }, new TypeToken<MonthItemInfo>() {
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

    private void setView() {
        title = info.getHeadline();
        status = info.getStatus();
        initTitleBackBt(title);

        applyerNameTv.setText(info.getApplicantName());
        belongDeptTv.setText(info.getApplicantDeptName());
        MonthItemInfo.ObjectBean timeObj = info.getObject();
        if (timeObj != null) {
            wageYear = timeObj.getWageYear();
            WAGE_MONTH = timeObj.getWageMonth();
            if (!TextUtil.isEmpty(WAGE_MONTH)&&WAGE_MONTH.length()==1) {
                WAGE_MONTH="0"+WAGE_MONTH;
            }
            timeTv.setText(wageYear + "年" + WAGE_MONTH + "月");
        }

        commitBt = findViewById(R.id.commit_bt);
        deleteBt = findViewById(R.id.delete_bt);
        LinearLayout auditLayout = findViewById(R.id.audit_layout);

        remarkTv = (EditText) findViewById(R.id.month_remark_tv);
        MonthItemInfo.ObjectBean object = info.getObject();
        String informListName = info.getInformNameList();
        informsTv.setText(TextUtil.isEmpty(informListName) ? "无" : informListName);

        List<Integer> informList = info.getInformList();
        if (employeeIdArr != null && informList != null) {
            for (Integer id : informList) {
                employeeIdArr.add(id);
            }
        }

        if (object != null) {
            emplyeesTv.setText(info.getObject().getPersonNum() + "人");
            totalWageSumTv.setText(info.getObject().getTotalWage() + "元");

        }


        if (info.getStatus() == 1) {//等待发起
            auditLayout.setVisibility(View.VISIBLE);
            commitBt.setText(R.string.commit);
            commitBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commit();
                }
            });
            //抄送人
            informsLaout.setVisibility(View.VISIBLE);
            informsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) parentList);//序列化,要注意转化(Serializable)
                        Intent intent = new Intent(context, ChooseEmplyeeActivity.class);
                        intent.putExtras(bundle);
                        context.startActivityForResult(intent, 1);
                }
            });

            deleteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteSureDialog();
                }
            });
        } else {//不是等待发起状态
            informsLaout.setVisibility(View.VISIBLE);
            informsTv.setCompoundDrawables(null, null, null, null);
            informsTv.setText(info.getInformNameList());
            TextUtil.setEtNoFocusable(remarkTv);
            if (timeObj != null) {
                remarkTv.setText(timeObj.getRemark());
                remarkTv.setTextColor(getResources().getColor(R.color.a5a5a5));
            }

            //撤销按钮去掉!
            if (info.getStatus() == 2) {
                auditLayout.setVisibility(View.GONE);
                commitBt.setText(R.string.recall);
                commitBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!NetUtil.isNetworkConnected(context)) {
                            ToastUtil.show(context, getString(R.string.no_network));
                            return;
                        }

                        String url = Constant.WEB_SITE + "/biz/process/launch/" + processId;

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url,
                                new JSONObject(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject result) {
                                if (result != null && result.toString().contains("200")) {
                                    context.finish();
                                } else {
                                    ToastUtil.show(context, R.string.commit_faild);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "返回数据异常:" + error.toString());
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
                });

            }
        }

    }

    private void showDeleteSureDialog() {
        final FragmentManager fm = getSupportFragmentManager();
        DialogUtils.getTwoBtDialog(context, "确定删除此月度工资吗?")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String url = Constant.WEB_SITE + "/biz/process/draft/" + processId;
                        int postType = Request.Method.DELETE;

                        DialogHelper.showWaiting(fm, "加载中...");
                        JsonObjectRequest jsonRequest = new JsonObjectRequest(postType, url,
                                new JSONObject(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject result) {
                                DialogHelper.hideWaiting(fm);
                                if (result != null && result.toString().contains("200")) {
                                    DialogHelper.hideWaiting(fm);

                                    ToastUtil.show(context, "删除成功");
                                    context.finish();
                                } else {
                                    ToastUtil.show(context, "删除失败,稍后重试");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                DialogHelper.hideWaiting(fm);
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

    private void initView() {
        applyerNameTv = (TextView) findViewById(R.id.applyer_name_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        //所属部门
        belongDeptTv = (TextView) findViewById(R.id.department_tv);

        //参与人员
        emplyeesTv = (TextView) findViewById(R.id.emplyees_tv);
        totalWageSumTv = (TextView) findViewById(R.id.total_wage_sum_tv);
        informsTv = (TextView) findViewById(R.id.inform_people_tv);
        informsLaout = (RelativeLayout) findViewById(R.id.inform_people_layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //同行人员
        if (requestCode == 1 && resultCode == 2) {
            JsonArray choosedEmployeeIds = new JsonArray();
            String nameStr = "";
            parentList = (List<EmployeeInfo>) data.
                    getSerializableExtra(KeyConst.OBJ_INFO);

            for (EmployeeInfo employeeInfo : parentList) {

                choosedEmployeeIds.add(employeeInfo.getId());
                String name = employeeInfo.getEmployeeName();
                if (choosedEmployeeIds.size() == 1) {
                    nameStr = name;
                } else if (choosedEmployeeIds.size() < Constant.EMPLYEE_SHOW_NUMBER) {
                    nameStr = name + "、" + nameStr;
                }

            }
            if (choosedEmployeeIds.size() >= Constant.EMPLYEE_SHOW_NUMBER) {
                nameStr = nameStr + getString(R.string.ellipsis_more) + choosedEmployeeIds.size() + "人";
            }

            informsTv.setText(nameStr);
            employeeIdArr = choosedEmployeeIds;
        }
    }

    JsonArray employeeIdArr = new JsonArray();

    //获取
    private void getDeptData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/upms/departments/tree";
        Response.Listener<List<DeptInfo>> successListener = new Response
                .Listener<List<DeptInfo>>() {
            @Override
            public void onResponse(List<DeptInfo> result) {
                if (result == null || result.size() == 0) {
                    return;
                }
                int size = result.size();
                for (int i = 0; i < size; i++) {
                    List<DeptInfo.ChildrenBeanX> deptList = result.get(i).getChildren();
                    int size1 = deptList.size();
                    for (int j = 0; j < size1; j++) {
                        DeptInfo.ChildrenBeanX child = deptList.get(j);
                        int id = child.getId();
                        String title = child.getTitle();
                        //最后一个,更新数据
                        boolean lastIndex = i == size - 1 && j == size1 - 1;
                        int orderBy = child.getOrderBy();
                        getItemData(id, title, lastIndex,orderBy);
                    }
                }
            }
        };

        Request<List<DeptInfo>> versionRequest = new
                GsonRequest<List<DeptInfo>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<List<DeptInfo>>() {
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

    private void getItemData(final int deptId, final String title, final boolean lastIndex, final int orderBy) {
        if (parentList == null) {
            parentList = new ArrayList<>();
        }
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/upms/departments/" + deptId + "/employees/all?included=0";
        Response.Listener<List<GroupInfo.ChildrenBean>> successListener = new Response
                .Listener<List<GroupInfo.ChildrenBean>>() {
            @Override
            public void onResponse(List<GroupInfo.ChildrenBean> result) {
                //parentList.add(new GroupInfo(deptId, title, false, result,orderBy));
            }
        };

        Request<List<GroupInfo.ChildrenBean>> versionRequest = new
                GsonRequest<List<GroupInfo.ChildrenBean>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<List<GroupInfo.ChildrenBean>>() {
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

    private List<EmployeeInfo> parentList = new ArrayList<>();

    private void commit() {
        Map<String, Object> map = new HashMap<>();
        map.put(KeyConst.id, processId);
        map.put(KeyConst.remark, remarkTv.getText().toString());
        map.put(KeyConst.status, 2);

        if (employeeIdArr != null && employeeIdArr.size() >= 0 && !employeeIdArr.isJsonNull()) {

            JSONArray employeeIdList = new JSONArray();
            for (JsonElement element : employeeIdArr) {
                employeeIdList.put(element.getAsInt());
            }
            map.put(KeyConst.informList, employeeIdList);
        }
        //添加
        final FragmentManager fm = getSupportFragmentManager();
        DialogHelper.showWaiting(fm, "加载中...");

        String url = Constant.WEB_SITE + "/biz/process/draft/WAGE_AUDIT";
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
                            ToastUtil.show(context, "提交失败");
                            return;
                        }
                        ToastUtil.show(context, "提交成功");
                        DialogHelper.hideWaiting(fm);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogHelper.hideWaiting(fm);
                if (error != null && error.networkResponse != null & error.networkResponse.statusCode == 400) {
                    ToastUtil.show(context, "无此工资审核信息");
                } else {
                    ToastUtil.show(context, getString(R.string.server_exception));
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

    public void onEmplyeeMonthDetailClick(View view) {
        Intent intent = new Intent(context, WageEmplyeeListActivity.class);
        intent.putExtra(KeyConst.id, processId + "");
        intent.putExtra(KeyConst.type, 2);
        intent.putExtra(KeyConst.status, status);
        context.startActivity(intent);
    }

    public void onProjsMonthDetailClick(View view) {
        Intent intent = new Intent(context, WageMonthProjectListActivity.class);
        intent.putExtra(KeyConst.wageMonth,   wageYear + "-" + WAGE_MONTH);
        context.startActivity(intent);
    }

    /* private void setInformSeletedStatus() {
        if (employeeIdArr != null && employeeIdArr.size() > 0 && !employeeIdArr.isJsonNull()) {
            int size = parentList.size();
            for (JsonElement element : employeeIdArr) {
                for (int j = 0; j < size; j++) {
                    GroupInfo groupInfo = parentList.get(j);
                    List<GroupInfo.ChildrenBean> children = groupInfo.getChildren();
                    if (children == null) {
                        continue;
                    }
                    for (int i = 0; i < children.size(); i++) {
                        GroupInfo.ChildrenBean childrenBean = children.get(i);
                        String asString = element.getAsString();
                        if (asString.equals(childrenBean.getId() + "")) {
                            parentList.get(j).setAllChecked(true);
                            parentList.get(j).getChildren().get(i).setChildChecked(true);
                            break;//结束这个for
                        }
                    }
                }
            }
        }
    }*/
}
