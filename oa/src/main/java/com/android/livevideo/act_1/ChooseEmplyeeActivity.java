package com.android.livevideo.act_1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.livevideo.view.MyExpandableListView;
import com.android.livevideo.view.PullScrollView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gool Lee
 * 质量检查类型
 */
public class ChooseEmplyeeActivity extends BaseFgActivity {
    private Button title_bar;
    private TextView tv_content;
    private int processorConfigId;
    private MyExpandableListView expandableListView;
    private ChooseEmplyeeActivity context;
    private TextView bottomsureTv, bottomNumTv;
    private List<EmployeeInfo> selectedList = new ArrayList<>();
    private DialogHelper dialogHelper;
    private String deptId;
    private LinearLayout emplyeeListLayout;
    private AppCompatCheckBox allCheckBt;
    private List<EmployeeInfo> searchData = new ArrayList<>();
    private JSONObject emplyeeIds;
    private WaveSideBar waveSideBar;
    private LinearLayoutManager mManager;
    private PullScrollView scroolView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_choose_emplyee_list);
        context = this;
        processorConfigId = getIntent().getIntExtra(KeyConst.id, 0);
        selectedList = (List<EmployeeInfo>) getIntent().getSerializableExtra(KeyConst.OBJ_INFO);
        initTitleBackBtSetText("", "取消");

        initView();
        setBottomView();
    }

    public class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            EmployeeInfo a = (EmployeeInfo) lhs;
            EmployeeInfo b = (EmployeeInfo) rhs;

            String aindex = TextUtil.getPYIndexStr(a.getEmployeeName());
            String bindex = TextUtil.getPYIndexStr(b.getEmployeeName());
            return aindex.compareTo(bindex);
        }
    }

    private ArrayList<String> pinYinList = new ArrayList<>();

    private void getDeptTree() {
        JsonArray deptTree = Utils.getDeptTree(context);
        if (deptTree != null) {
            JsonObject companyObj = deptTree.get(0).getAsJsonObject();
            if (companyObj != null && !companyObj.isJsonNull()) {//公司下面
                JsonArray childrenJsonArr = companyObj.getAsJsonArray(KeyConst.children);
                showDeptTreeList(childrenJsonArr, false, companyObj.get(KeyConst.title).getAsString());
            }
        }
    }

    List<Dialog> dialogList = new ArrayList<Dialog>();

    private void showDeptTreeList(JsonArray jsonArray, boolean showBack, String deptTitle) {
        if (jsonArray == null || jsonArray.isJsonNull() || jsonArray.size() == 0) {
            ToastUtil.show(context, "部门数据为空");
            return;
        }
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        View v = LayoutInflater.from(context).inflate(R.layout.layout_dialog_emplyee_choose, null);
        LinearLayout itemsLayout = (LinearLayout) v.findViewById(R.id.emplyee_seleted_items_layout);
        itemsLayout.removeAllViews();
        dialog.setContentView(v);

        DialogUtils.setDialogWindowFullScreen(context, dialog, Gravity.BOTTOM);
        dialogList.add(dialog);

        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonObject jsonObj = jsonArray.get(i).getAsJsonObject();
            View itemView = View.inflate(context, R.layout.item_dept_next, null);
            TextView nameTv = (TextView) itemView.findViewById(R.id.dept_next_name_tv);
            nameTv.setPadding(40, 0, 0, 0);
            TextView nextTv = (TextView) itemView.findViewById(R.id.dept_next_bt);

            if (jsonObj != null && !jsonObj.isJsonNull()) {
                final JsonArray childrenJsonArr = jsonObj.getAsJsonArray(KeyConst.children);
                final String id = Utils.getObjStr(jsonObj, KeyConst.id);
                final String deptName = Utils.getObjStr(jsonObj, KeyConst.title);
                if (childrenJsonArr != null && !childrenJsonArr.isJsonNull() && childrenJsonArr.size() != 0) {
                    nextTv.setVisibility(View.VISIBLE);
                    nextTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDeptTreeList(childrenJsonArr, true, deptName);
                        }
                    });
                }

                nameTv.setText(deptName);

                nameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initTitleBackBtSetText(deptName, "取消");
                        deptId = id;
                        search();
                        for (Dialog alertDialog : dialogList) {
                            alertDialog.dismiss();
                        }

                    }
                });

                //产值
                itemsLayout.addView(itemView);
            }
        }

        ((TextView) v.findViewById(R.id.dept_title_tv)).setText(deptTitle);
        TextView backTv = v.findViewById(R.id.dialog_btn_cancel);
        backTv.setText(showBack ? "返回" : "关闭");
        backTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.emplyee_seleted_save_bt).setVisibility(View.GONE);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
            }
        });
    }

    private void search() {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        DialogHelper.showWaiting(fm, getString(R.string.loading));
        final String url = Constant.WEB_SITE + "/upms/departments/" + deptId +
                "/employees/all?included=0";
        Response.Listener<List<EmployeeInfo>> successListener = new Response
                .Listener<List<EmployeeInfo>>() {
            @Override
            public void onResponse(List<EmployeeInfo> result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (context == null || emplyeeListLayout == null) {
                    return;
                }
                emplyeeListLayout.removeAllViews();
                if (result == null || result.size() == 0) {
                    ToastUtil.show(context, R.string.search_no_data);
                    return;
                }
                searchData = result;
                setView(searchData, null);

                pinYinList.clear();
                for (int i = 0; i < searchData.size(); i++) {
                    EmployeeInfo infoObj = searchData.get(i);
                    int id = infoObj.getId();
                    String name = infoObj.getEmployeeName().replace("\"", "");
                    String pinYinIndex = TextUtil.getPYIndexStr(name.substring(0, 1));

                    if (!pinYinList.contains(pinYinIndex)) {
                        pinYinList.add(pinYinIndex);
                    }
                }

                Collections.sort(pinYinList);

                int size = pinYinList.size();
                String[] pinYinArr = new String[size];
                for (int i = 0; i < size; i++) {
                    pinYinArr[i] = pinYinList.get(i);
                }
                //排序
                waveSideBar.setIndexItems(pinYinArr);

                int itemHeight = getResources().getDimensionPixelSize(R.dimen.dm095);
                waveSideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
                    @Override
                    public void onSelectIndexItem(String nameSeleted) {
                        for (int i = 0; i < searchData.size(); i++) {
                            String name = searchData.get(i).getEmployeeName();
                            if (nameSeleted.equals(TextUtil.getPYIndexStr(name.substring(0, 1)))) {
                                scroolView.smoothScrollTo(0, itemHeight * i);
                                return;
                            }
                        }
                    }
                });
            }
        };

        Request<List<EmployeeInfo>> versionRequest = new
                GsonRequest<List<EmployeeInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        DialogHelper.hideWaiting(getSupportFragmentManager());
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

    private void setView(List<EmployeeInfo> result, Boolean isAllCheck) {
        SortComparator comp = new SortComparator();
        Collections.sort(searchData, comp);
        emplyeeListLayout.removeAllViews();
        for (final EmployeeInfo employeeInfo : result) {
            final View itemView = View.inflate(context, R.layout.item_emplyee_choose, null);
            TextView iconTv = (TextView) itemView.findViewById(R.id.emplyee_icon_tv);
            TextView nameTv = (TextView) itemView.findViewById(R.id.emplyee_name_tv);
            TextView descTv = (TextView) itemView.findViewById(R.id.emplyee_phone_tv);
            AppCompatCheckBox checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.emplyee_check_box);

            final int id = employeeInfo.getId();
            String name = employeeInfo.getEmployeeName();
            final String phone = employeeInfo.getEmployeeMobile();

            nameTv.setText(name);
            iconTv.setText(TextUtil.getLast2(name));
            descTv.setText(phone);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (null == selectedList || selectedList.size() == 0) {
                        if (isChecked) {
                            selectedList.add(employeeInfo);
                        }

                    } else {
                        boolean isExist = false;
                        for (EmployeeInfo info : selectedList) {
                            if (id == info.getId()) {
                                isExist = true;
                                break;
                            }
                        }

                        if (isChecked) {
                            if (!isExist) {
                                selectedList.add(employeeInfo);
                            }
                        } else {
                            if (isExist) {
                                int size = selectedList.size();
                                for (int i = 0; i < size; i++) {
                                    if (id == selectedList.get(i).getId()) {
                                        selectedList.remove(i);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                    //底部数据:选中人数
                    setBottomView();
                }
            });

            for (EmployeeInfo info : selectedList) {
                if (id == info.getId()) {
                    checkBox.setChecked(true);
                    break;
                }
            }
            if (isAllCheck != null) {
                checkBox.setChecked(isAllCheck);
            }
            //产值
            emplyeeListLayout.addView(itemView);
        }
    }

    private void setBottomView() {
        int size = selectedList.size();
        bottomNumTv.setText("已选择:" + size + "人");
        bottomsureTv.setText("确定(" + size + ")");
    }

    private void initView() {
        Button bt = getTitleRightBt("部门");
        //bt.setTextColor(ContextCompat.getColor(context,R.color.color212121));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeptTree();
            }
        });
        emplyeeListLayout = (LinearLayout) findViewById(R.id.emplyee_search_layout);
        emplyeeListLayout.post(new Runnable() {
            @Override
            public void run() {
                //弹框
                getDeptTree();
            }
        });
        allCheckBt = (AppCompatCheckBox) findViewById(R.id.emplyee_all_check_bt);
        allCheckBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setView(searchData, isChecked);
            }
        });
        ImageButton clearBt = findViewById(R.id.search_clear_bt);
        EditText searchEt = (EditText) findViewById(R.id.search_et);
        clearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEt.setText("");
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                clearBt.setVisibility(TextUtil.isEmpty(c.toString()) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<EmployeeInfo> list = new ArrayList<>();
                for (EmployeeInfo info : searchData) {
                    if (info.getEmployeeName().contains(editable)) {
                        list.add(info);
                    }
                }
                if (list.size() == 0) {
                    emplyeeListLayout.removeAllViews();
                    ToastUtil.show(context, R.string.search_no_data);
                }
                setView(list, null);
            }
        });

        bottomNumTv = findViewById(R.id.seleted_num_tv);
        bottomNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (selectedList.size() == 0) {
                    ToastUtil.show(context, "无人员");
                    return;
                }
                final Dialog dialog = new Dialog(context, R.style.Dialog_From_Bottom_Style);
                View v = LayoutInflater.from(context).inflate(R.layout.layout_dialog_emplyee_seleted, null);
                LinearLayout seletedLayout = (LinearLayout) v.findViewById(R.id.emplyee_seleted_items_layout);
                TextView sureBt = v.findViewById(R.id.dialog_btn_sure);
                seletedLayout.removeAllViews();
                for (int i = 0; i < selectedList.size(); i++) {
                    EmployeeInfo employeeInfo = selectedList.get(i);
                    View itemView = LayoutInflater.from(context).inflate(R.layout.item_emplyee_seleted
                            , null);
                    TextView iconTv = (TextView) itemView.findViewById(R.id.emplyee_icon_tv);
                    TextView nameTv = (TextView) itemView.findViewById(R.id.emplyee_name_tv);
                    TextView descTv = (TextView) itemView.findViewById(R.id.emplyee_phone_tv);
                    itemView.findViewById(R.id.delete_bt).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seletedLayout.removeView(itemView);
                            selectedList.remove(employeeInfo);

                            setView(searchData, null);
                            setBottomView();
                        }
                    });
                    String name = employeeInfo.getEmployeeName();
                    final String phone = employeeInfo.getEmployeeMobile();

                    nameTv.setText(name);
                    iconTv.setText(TextUtil.getLast2(name));
                    descTv.setText(phone);
                    seletedLayout.addView(itemView);
                }
                dialog.setContentView(v);
                sureBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                DialogUtils.setDialogWindowFullScreen(context, dialog, Gravity.BOTTOM);
            }
        });
        bottomsureTv = findViewById(R.id.seleted_sure_tv);
        bottomsureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedList.size() == 0) {
                    ToastUtil.show(context, "请选择至少一个人员");
                    return;
                }

                //返回选择的数据
                Bundle bundle = new Bundle();
                bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) selectedList);//序列化,要注意转化(Serializable)
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(2, intent);

                finish();

            }
        });

        scroolView = findViewById(R.id.refreshLayout);
        waveSideBar = (WaveSideBar) findViewById(R.id.waveSideBar);
        waveSideBar.setIndexItems("");

    }
}
