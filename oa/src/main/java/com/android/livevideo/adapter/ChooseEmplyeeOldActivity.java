package com.android.livevideo.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.GroupInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.livevideo.view.MyExpandableListView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gool Lee
 * 质量检查类型
 */
public class ChooseEmplyeeOldActivity extends BaseFgActivity {
    private Button title_bar;
    private TextView tv_content;
    private int processorConfigId;
    private MyExpandableListView expandableListView;
    private ChooseEmplyeeOldActivity context;
    private MyExpandableListAdapter expAdapter;
    private Button rightBt;
    private List<GroupInfo> parentList = new ArrayList<>();
    private DialogHelper dialogHelper;
    private String deptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_choose_emplyee_list_old);
        context = this;
        processorConfigId = getIntent().getIntExtra(KeyConst.id, 0);
        parentList = (List<GroupInfo>) getIntent().getSerializableExtra(KeyConst.OBJ_INFO);
        Utils.sortList(parentList);//排序
        initTitleBackBt("选择人员");

        initRightBt();

        initExpandLv();

        expAdapter.setData(parentList, true);
        //todo 选择部门
        getDeptTreeList();

    }

    private void getDeptTreeList() {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        String url = Constant.WEB_SITE + "/upms/departments/tree";
        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result == null || result.isJsonNull() || result.size() == 0) {
                    ToastUtil.show(context, "部门数据为空");
                    return;
                }
                JsonObject companyObj = result.get(0).getAsJsonObject();
                if (companyObj != null && !companyObj.isJsonNull()) {//公司下面
                    JsonArray childrenJsonArr = companyObj.getAsJsonArray(KeyConst.children);
                    showDeptTreeList(childrenJsonArr, false);
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

    List<AlertDialog> dialogList = new ArrayList<>();

    private void showDeptTreeList(JsonArray jsonArray, boolean showBack) {
        if (jsonArray == null || jsonArray.isJsonNull() || jsonArray.size() == 0) {
            ToastUtil.show(context, "部门数据为空");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style
                .dialog_appcompat_theme_fullscreen);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_emplyee_choose, null);
        LinearLayout itemsLayout = (LinearLayout) v.findViewById(R.id.emplyee_seleted_items_layout);
        itemsLayout.removeAllViews();

        final AlertDialog deptDialog = builder.create();
        dialogList.add(deptDialog);
        deptDialog.show();
        deptDialog.getWindow().setContentView(v);
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonObject jsonObj = jsonArray.get(i).getAsJsonObject();
            View itemView = View.inflate(context, R.layout.item_dept_next, null);
            TextView nameTv = (TextView) itemView.findViewById(R.id.dept_next_name_tv);
            nameTv.setPadding(40, 0, 0, 0);
            TextView nextTv = (TextView) itemView.findViewById(R.id.dept_next_bt);

            if (jsonObj != null && !jsonObj.isJsonNull()) {
                final JsonArray childrenJsonArr = jsonObj.getAsJsonArray(KeyConst.children);
                if (childrenJsonArr != null && !childrenJsonArr.isJsonNull() && childrenJsonArr.size() != 0) {
                    nextTv.setVisibility(View.VISIBLE);
                    nextTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDeptTreeList(childrenJsonArr, true);
                        }
                    });
                }

                final String id = Utils.getObjStr(jsonObj, KeyConst.id);
                final String deptName = Utils.getObjStr(jsonObj, KeyConst.title);
                nameTv.setText(deptName);

                nameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo  请求人员列表
                        deptId = id;
                        //http://api.tzy.waylinkage.com/upms/departments/31/include/employees?

                        for (AlertDialog alertDialog : dialogList) {
                            alertDialog.dismiss();
                        }

                    }
                });

                //产值
                itemsLayout.addView(itemView);
            }
        }

        TextView backTv = v.findViewById(R.id.dialog_btn_cancel);
        backTv.setText(showBack ? "返回" : "取消");
        backTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                deptDialog.dismiss();
            }
        });
        v.findViewById(R.id.emplyee_seleted_save_bt).setVisibility(View.GONE);
        deptDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
            }
        });
    }

    private void initExpandLv() {
        expandableListView = (MyExpandableListView) findViewById(R.id.expand_list);
        expAdapter = new MyExpandableListAdapter(context);
        expandableListView.setAdapter(expAdapter);

        //设置分组项的点击监听事件
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView,
                                        View view, int i, long l) {
                AppCompatCheckBox groupCheckBox = (AppCompatCheckBox) view.findViewById(R.id.expand_check_box);

                return false; // 返回 false，否则分组不会展开
            }
        });

        expAdapter.setData(parentList, true);
    }

    private void initRightBt() {
        rightBt = getTitleRightBt("确定");
        rightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == parentList || parentList.size() == 0) {
                    ToastUtil.show(context, "请选择至少一个人员");
                    return;
                }

                //返回选择的数据
                Bundle bundle = new Bundle();
                bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) parentList);//序列化,要注意转化(Serializable)
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(2, intent);

                finish();

            }
        });

    }

}
