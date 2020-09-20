package com.android.livevideo.act_1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.bean.AccountInfo;
import com.android.livevideo.bean.DeptInfo;
import com.android.livevideo.bean.GroupInfo;
import com.android.livevideo.bean.ProjDeptInfo;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.util.DialogUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.TimeUtils;
import com.android.livevideo.util.ToastUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

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
public class WageDailyAddActivity extends BaseFgActivity {
    private WageDailyAddActivity context;
    private TextView timeTv, belongDeptTv, hoursNumTitleTv;
    private EditText totalNumEt, moneyDeductEt, hoursSumEt;
    private TextView remarkTv, deductionTypeTv;
    private TextView everyoneNumTv, formalEmployeeTv, temporaryEmployeeTv;
    private TextView projectNameTv;
    private String workDate, deptId;
    private List<ProjDeptInfo> projInfoList = new ArrayList<>();
    private int pieceWageId, deductionType;
    private int type;
    private double totalNum, totalPeople, formalPeople, tempPeople;
    private List<String> payTypeArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_daily_wage_add);
        context = this;
        type = getIntent().getIntExtra(KeyConst.type, 0);
        initTitleBackBt(type > 0 ? "工时录入" : "小料包计件录入");

        initView();
        //getEmplyeeList(1);//正式
        //getDeptData();
        getEmplyeeList(2);//临时

        getUserDeptData();
    }

    private void getUserDeptData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/biz/process/employeeNameAndDeptName";
        Response.Listener<AccountInfo> successListener = new Response
                .Listener<AccountInfo>() {
            @Override
            public void onResponse(AccountInfo info) {
                if (info == null) {
                    return;
                }
                deptId = info.deptId;
                belongDeptTv.setText(info.deptName);
            }
        };

        Request<AccountInfo> versionRequest = new
                GsonRequest<AccountInfo>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<AccountInfo>() {
                }.getType()) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        params.put(KeyConst.Content_Type, Constant.application_json);
                        return params;
                    }
                };
        App.requestQueue.add(versionRequest);
    }

    private List<EmployeeInfo> employeeList = new ArrayList<>();
    private List<EmployeeInfo> tempEmployeeList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //同行人员
        if (requestCode == 1 && resultCode == 2) {
            JSONArray choosedEmployeeIds = new JSONArray();
            String nameStr = "";
            parentList = (List<EmployeeInfo>) data.
                    getSerializableExtra(KeyConst.OBJ_INFO);

            for (EmployeeInfo employeeInfo : parentList) {

                choosedEmployeeIds.put(employeeInfo.getId());
                String name = employeeInfo.getEmployeeName();
                if (choosedEmployeeIds.length() == 1) {
                    nameStr = name;
                } else if (choosedEmployeeIds.length() < Constant.EMPLYEE_SHOW_NUMBER) {
                    nameStr = name + "、" + nameStr;
                }

            }
            if (choosedEmployeeIds.length() >= Constant.EMPLYEE_SHOW_NUMBER) {
                nameStr = nameStr + getString(R.string.ellipsis_more) + choosedEmployeeIds.length() + "人";
            }
            formalEmployeeTv.setText(nameStr);
            employeeIdList = choosedEmployeeIds;

            formalPeople = employeeIdList == null ? 0 : employeeIdList.length();

            //统计平均件数
            sumAverageNum();
        }
    }

    private void getEmplyeeList(final int type) {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        String url = Constant.WEB_SITE + (type == 1 ? "/upms/employees/all?status=1"
                : "/biz/wage/wageOnDay/tempEmployee");
        Response.Listener<List<EmployeeInfo>> successListener = new Response
                .Listener<List<EmployeeInfo>>() {
            @Override
            public void onResponse(List<EmployeeInfo> result) {
                if (type == 1) {
                    employeeList = result;
                } else {
                    tempEmployeeList = result;
                }
            }
        };

        Request<List<EmployeeInfo>> versionRequest = new
                GsonRequest<List<EmployeeInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, getString(R.string.get_data_faild));
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

    private List<EmployeeInfo> parentList = new ArrayList<>();

    private void initView() {
        projectNameTv = (TextView) findViewById(R.id.hours_in_name_tv);
        hoursNumTitleTv = findViewById(R.id.hours_in_numbers_title_tv);
        if (type > 0) {
            hoursNumTitleTv.setText("日总计件");
            findViewById(R.id.must_inout_iv).setVisibility(View.GONE);
            projectNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getProjsList();
                }
            });
        } else {
            findViewById(R.id.hours_add_layout).setVisibility(View.GONE);
            findViewById(R.id.deduct_layout).setVisibility(View.GONE);
            findViewById(R.id.deduct_layout_type).setVisibility(View.GONE);
            findViewById(R.id.everyone_num_layout).setVisibility(View.GONE);
            projectNameTv.setCompoundDrawables(null, null, null, null);
            projectNameTv.setText(Constant.xiaoliaobao_proj_name);
        }
        timeTv = (TextView) findViewById(R.id.hours_add_time_tv);

        //所属部门
        belongDeptTv = (TextView) findViewById(R.id.belong_department_tv);
    /*    belongDeptTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        //计时,计件
        hoursSumEt = findViewById(R.id.hours_in_hours_sum_tv);
        totalNumEt = findViewById(R.id.hours_in_numbers_sum_tv);
        moneyDeductEt = findViewById(R.id.hours_in_money_deduct_tv);
        TextUtil.setInput2Dot(hoursSumEt);
        TextUtil.setInput2Dot(moneyDeductEt);
        TextUtil.setInput2Dot(totalNumEt);
        totalNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence num, int i, int i1, int i2) {
                totalNum = TextUtil.convertToDouble(num + "", 0);
                sumAverageNum();//统计平均件数
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        everyoneNumTv = findViewById(R.id.everyone_num_tv);
        deductionTypeTv = findViewById(R.id.money_deduct_type_tv);
        getDictDeductionType();
        deductionTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payTypeArr != null) {
                    new MaterialDialog.Builder(context)
                            .items(payTypeArr)// 列表数据
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView,
                                                        int position, CharSequence text) {
                                    deductionType = position + 1;
                                    deductionTypeTv.setText(text);
                                }
                            }).show();
                } else {
                    getDictDeductionType();
                }

            }
        });
        remarkTv = (TextView) findViewById(R.id.remark_tv);
        long todayTime = TimeUtils.getTodayZeroTime();
        workDate = TimeUtils.getTimeYmd(todayTime);
        timeTv.setText(workDate);
        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.Builder timePickerDialog = DialogUtils.getTimePicker(context);
                timePickerDialog.setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        if (millseconds > System.currentTimeMillis()) {
                            ToastUtil.show(context, "时间不可以大于今日");
                            return;
                        }
                        workDate = TimeUtils.getTimeYmd(millseconds);
                        timeTv.setText(workDate);
                    }
                });

                timePickerDialog.build().show(context.getSupportFragmentManager(), "");
            }
        });

        formalEmployeeTv = (TextView) findViewById(R.id.employee_formal_tv);
        temporaryEmployeeTv = (TextView) findViewById(R.id.employee_temporary_tv);


        //正式员工
        formalEmployeeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showEmplyeeListDialog(employeeList, 1);
              /*  if (parentList == null || parentList.size() == 0) {
                    getDeptData();
                } else {*/
                Bundle bundle = new Bundle();
                bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) parentList);//序列化,要注意转化(Serializable)
                Intent intent = new Intent(context, ChooseEmplyeeActivity.class);
                intent.putExtras(bundle);
                context.startActivityForResult(intent, 1);

            }
        });
        //临时
        temporaryEmployeeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEmplyeeListDialog(tempEmployeeList, 2);
            }
        });
    }

    private void getDictDeductionType() {
        String url = Constant.WEB_SITE + "/dict/dicts/cached/" + KeyConst.DEDUCTION_TYPE;

        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result != null || result.size() >= 0) {
                    payTypeArr.clear();
                    for (int i = 0; i < result.size(); i++) {
                        JsonObject object = result.get(i).getAsJsonObject();
                        String status = object.get(KeyConst.status).getAsString();
                        if (Constant.DICT_STATUS_USED.equals(status)) {
                            payTypeArr.add(object.get(KeyConst.name).getAsString());
                        }
                    }
                } else {
                    ToastUtil.show(context, "获取支付方式失败");
                }
            }
        };

        Request<JsonArray> versionRequest = new
                GsonRequest<JsonArray>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "解析:" + volleyError.toString());
                    }
                }, new TypeToken<JsonArray>() {
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
                        getItemData(id, title, lastIndex, orderBy);
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


    //选择项目
    private void getProjsList() {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

        final FragmentManager fm = getSupportFragmentManager();
        DialogHelper.showWaiting(fm, "加载中...");
        String url = Constant.WEB_SITE + "/biz/wage/pieceWage/list";
        Response.Listener<List<ProjDeptInfo>> successListener = new Response
                .Listener<List<ProjDeptInfo>>() {
            @Override
            public void onResponse(List<ProjDeptInfo> result) {
                DialogHelper.hideWaiting(fm);
                projInfoList = result;
                if (result == null || result.size() == 0) {
                    ToastUtil.show(context, "数据为空");
                    return;
                }

                showProjsNameDialog();

            }
        };

        Request<List<ProjDeptInfo>> versionRequest = new
                GsonRequest<List<ProjDeptInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, getString(R.string.get_data_faild));
                        DialogHelper.hideWaiting(fm);
                    }
                }, new TypeToken<List<ProjDeptInfo>>() {
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

    private void showProjsNameDialog() {

        final Dialog dialog = new Dialog(context, R.style.dialog_appcompat_theme);
        View inflate = LayoutInflater.from(context).inflate(R.layout.
                layout_dialog_product_name, null);
        LinearLayout layoutView = inflate.findViewById(R.id.produce_name_layout);
        EditText searchEt = inflate.findViewById(R.id.search_et);
        inflate.findViewById(R.id.produce_name_cancel_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
                layoutView.removeAllViews();
                List<ProjDeptInfo> list = new ArrayList<>();
                for (ProjDeptInfo productInfo : projInfoList) {
                    if (productInfo.getProjectName().contains(editable)) {
                        list.add(productInfo);
                    }
                }
                if (list.size() == 0) {
                    ToastUtil.show(context, R.string.search_no_data);
                }
                initLayoutView(dialog, list, layoutView);
            }
        });

        initLayoutView(dialog, projInfoList, layoutView);

        dialog.setContentView(inflate);

        DialogUtils.setDialogWindowShapeCenter(context, dialog);
    }

    private void initLayoutView(Dialog dialog, List<ProjDeptInfo> list, LinearLayout layout) {
        int textSize = getResources().getDimensionPixelSize(R.dimen.dialog_tv_size);
        for (ProjDeptInfo info : list) {
            String name = info.getProjectName();
            int id = info.getId();
            TextView tv = new TextView(context);
            tv.setText(name);
            tv.setPadding(0, textSize * 2 / 3, 0, textSize * 2 / 3);
            tv.setTextSize(textSize);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    pieceWageId = id;
                    projectNameTv.setText(name);
                }
            });
            layout.addView(tv);
        }
    }

    JSONArray employeeIdList = new JSONArray();
    JSONArray tempEmployeeIdList = new JSONArray();

    private void showEmplyeeListDialog(final List<EmployeeInfo> list, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style
                .dialog_appcompat_theme_fullscreen);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_emplyee_choose, null);
        LinearLayout itemsLayout = (LinearLayout) v.findViewById(R.id.emplyee_seleted_items_layout);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final EmployeeInfo itemInfo = list.get(i);
                View itemView = View.inflate(context, R.layout.layout_dialog_emplyee_item, null);
                final TextView nameTv = (TextView) itemView.findViewById(R.id.emplyee_choosed_name_tv);
                nameTv.setText(itemInfo.getEmployeeName());
                nameTv.setSelected(itemInfo.getSeleted());
                nameTv.setOnClickListener(new View.OnClickListener() {
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
            ToastUtil.show(context, "人员为空");
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
                String nameStr = "";
                JSONArray employeeIds = new JSONArray();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    EmployeeInfo employeeInfo = list.get(i);
                    boolean seleted = employeeInfo.getSeleted();
                    if (type == 1) {
                        employeeList.get(i).setSeleted(seleted);
                    } else {
                        tempEmployeeList.get(i).setSeleted(seleted);
                    }
                    if (seleted) {
                        employeeIds.put(employeeInfo.getId());
                        String name = employeeInfo.getEmployeeName();


                        if (employeeIds.length() == 1) {
                            nameStr = name;
                        } else if (employeeIds.length() < Constant.EMPLYEE_SHOW_NUMBER) {
                            nameStr = name + "、" + nameStr;
                        }

                    }
                }
                if (employeeIds.length() >= Constant.EMPLYEE_SHOW_NUMBER) {
                    nameStr = nameStr + getString(R.string.ellipsis_more) + employeeIds.length() + "人";
                }
                //正式
                if (type == 1) {
                    employeeIdList = employeeIds;
                    formalEmployeeTv.setText(nameStr);
                } else {
                    tempEmployeeIdList = employeeIds;
                    temporaryEmployeeTv.setText(nameStr);
                }
                if (employeeIds.length() == 0) {
                    ToastUtil.show(context, "请至少选择一个人员");
                    return;
                }
                tempPeople = employeeIds.length();
                sumAverageNum();

                dialog.dismiss();
            }
        });
    }

    //计算人平均件数
    private void sumAverageNum() {
        totalPeople = formalPeople + tempPeople;
        everyoneNumTv.setText(TextUtil.div2(totalNum, totalPeople, 2) + "");

    }

    private void addPost() {
        String hoursNum = hoursSumEt.getText().toString();
        String pieceNum = totalNumEt.getText().toString();
        String deduction = moneyDeductEt.getText().toString();//扣款
        String remark = remarkTv.getText().toString();
        if (type == -1) {
            pieceWageId = Constant.xiaoliaobao_proj_id;
        }
        if (pieceWageId == 0) {
            ToastUtil.show(context, "请选择项目");
            return;
        }
        if (TextUtil.isEmpty(deptId)) {
            ToastUtil.show(context, "请选择所属部门");
            return;
        }
        if (TextUtil.isEmpty(workDate)) {
            ToastUtil.show(context, "请选择日期");
            return;
        }

        if ((employeeIdList == null || employeeIdList.length() == 0) &&
                (tempEmployeeIdList == null || tempEmployeeIdList.length() == 0)) {
            ToastUtil.show(context, "至少选择一个参与人员");
            return;
        }

        if (type > 0) {
            if (TextUtil.isEmpty(hoursNum) && TextUtil.isEmpty(pieceNum) && TextUtil.isEmpty(deduction)) {
                ToastUtil.show(context, "(计时/日总计件/扣款)不可都为空");
                return;
            }

            if (TextUtil.isEmpty(hoursNum)) {
                hoursNum = "0";
            }
            if (TextUtil.isEmpty(pieceNum)) {
                pieceNum = "0";
            }
            if (TextUtil.isEmpty(deduction)) {
                deduction = "0";
            } else {
                if (0 == deductionType) {
                    ToastUtil.show(context, "请选择扣款类型");
                    return;
                }
            }

        } else {
            if (ToastUtil.showCannotEmpty(context, pieceNum, "计件数量")) {
                return;
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put(KeyConst.employeeIdList, employeeIdList);
        map.put(KeyConst.tempEmployeeIdList, tempEmployeeIdList);
        map.put(KeyConst.deptId, deptId);
        map.put(KeyConst.workDate, workDate);

        if (type > 0) {
            map.put(KeyConst.pieceWageId, pieceWageId);
            map.put(KeyConst.hourNum, hoursNum);//计时
            map.put(KeyConst.deduction, deduction);//扣款
            map.put(KeyConst.deductionType, deductionType);//扣款类型
        } else {
            map.put(KeyConst.pieceMonthCategoryId, pieceWageId);
        }
        map.put(KeyConst.pieceNum, pieceNum);//计件
        map.put(KeyConst.remark, remark);//扣款


        Log.d(TAG, "提交数据" + map.toString());
        //添加
        final FragmentManager fm = getSupportFragmentManager();
        DialogHelper.showWaiting(fm, "加载中...");
        String url;
        if (type > 0) {
            url = Constant.WEB_SITE + "/biz/wage/wageOnDay";
        } else {
            url = Constant.WEB_SITE + "/biz/pieceMonthRecord/pieceMonthPerDayVO";//小料包
        }
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        if (result == null) {
                            DialogHelper.hideWaiting(fm);
                            ToastUtil.show(context, "工时录入失败,稍后重试");
                            return;
                        }
                        ToastUtil.show(context, "工时录入成功");
                        DialogHelper.hideWaiting(fm);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "数据" + TextUtil.getErrorMsg(error));
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

    public void onReportCommitClick(View view) {
        addPost();
    }
}
