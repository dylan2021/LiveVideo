package com.android.livevideo.act_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.DeptInfo;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.bean.StatInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.util.Utils;
import com.android.livevideo.view.MyExpandableListView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gool Lee
 * 通讯录
 */

public class AddressListActivity extends BaseFgActivity {

    private int id;
    private List<EmployeeInfo> infoList = new ArrayList<>();
    private List<StatInfo> lvDataList = new ArrayList<>();
    private MyExpandableListView expendList;
    private AddressListAdapter addressListAdapter;
    public static AddressListActivity context;
    private LinearLayout topEmplyeeLayout;
    private String title;
    private TextView closeOpenTv;
    private int employeeCount;
    private List<DeptInfo.ChildrenBeanX.ChildrenBean> parentList;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_address_list);

        Intent intent = getIntent();
        id = intent.getIntExtra(KeyConst.id, 0);
        employeeCount = intent.getIntExtra(KeyConst.numbers, 0);
        type = intent.getIntExtra(KeyConst.type, 0);
        title = intent.getStringExtra(KeyConst.title);
        parentList = (List<DeptInfo.ChildrenBeanX.ChildrenBean>)
                intent.getSerializableExtra(KeyConst.OBJ_INFO);
        context = this;
        initStatusBar();
        initTitleBackBt(title);

        initTopLayout();

        getTopData();

        initListView();

        if (parentList != null && parentList.size() != 0) {
            getListViewData();
        } else {
            //topEmplyeeLayout.setVisibility(View.VISIBLE);
        }

    }

    private void getListViewData() {
        for (DeptInfo.ChildrenBeanX.ChildrenBean childrenBean : parentList) {
            if (childrenBean != null) {
                int groupId = childrenBean.getId();
                String groupName = childrenBean.getTitle();
                getListData(groupId, groupName);
            }
        }

    }

    private void getListData(final int groupId, final String groupName) {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/upms/departments/" + groupId + "/employees/all?included=0";

        Response.Listener<List<EmployeeInfo>> successListener = new Response
                .Listener<List<EmployeeInfo>>() {
            @Override
            public void onResponse(List<EmployeeInfo> result) {
                lvDataList.add(new StatInfo(groupId, groupName, result));
                addressListAdapter.setData(lvDataList, type);
            }
        };

        Request<List<EmployeeInfo>> versionRequest = new
                GsonRequest<List<EmployeeInfo>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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

    private void initLayout() {
        topEmplyeeLayout.removeAllViews();
        if (infoList == null || infoList.size() == 0) {
            return;
        }
        for (final EmployeeInfo employeeInfo : infoList) {
            View itemView = View.inflate(context, R.layout.e_lv_child_item, null);
            TextView nameTv = (TextView) itemView.findViewById(R.id.expand_child);
            TextView numberTv = (TextView) itemView.findViewById(R.id.child_number_tv);
            TextView positionNameTv = (TextView) itemView.findViewById(R.id.position_name_tv);
            if (1 == type) {
                itemView.findViewById(R.id.child_phone_iv).setVisibility(View.GONE);
            }

            nameTv.setHeight(getResources().getDimensionPixelOffset(R.dimen.item_height));
            final String phoneNumber = employeeInfo.getEmployeeMobile();
            nameTv.setText(employeeInfo.getEmployeeName());
            numberTv.setText(phoneNumber);
         /*   String positionName = employeeInfo.getPositionName();
            positionNameTv.setText(null == positionName ? "" : "(" + positionName + ")");
*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == 1) {
                        //返回选择的数据
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) employeeInfo);//序列化,要注意转化(Serializable)
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(3, intent);
                        finish();
                    } else {
                        Utils.callPhone(context, phoneNumber);
                    }
                }
            });
            topEmplyeeLayout.addView(itemView);
        }
    }

    private void initTopLayout() {
        View searchBt = findViewById(R.id.search_bt);
        if (type == 1) {
            searchBt.setVisibility(View.GONE);
        }
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra(KeyConst.type, type);
                context.startActivity(intent);
            }
        });
        topEmplyeeLayout = (LinearLayout) findViewById(R.id.department_emplyee_layout);
        closeOpenTv = (TextView) findViewById(R.id.top_open_close_tv);
        closeOpenTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean selected = closeOpenTv.isSelected();
                closeOpenTv.setSelected(!selected);
                topEmplyeeLayout.setVisibility(!selected ? View.VISIBLE : View.GONE);
            }
        });
        TextView topNameTv = (TextView) findViewById(R.id.top_name_tv);
        topNameTv.setText(title);
        closeOpenTv.setText(employeeCount + "人");

    }

    private void initListView() {
        expendList = (MyExpandableListView) findViewById(R.id.expand_list);
        addressListAdapter = new AddressListAdapter(context);
        expendList.setAdapter(addressListAdapter);

        //关闭其他分组
        expendList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = expendList.getExpandableListAdapter().getGroupCount();
                for (int j = 0; j < count; j++) {
                    if (j != groupPosition) {
                        expendList.collapseGroup(j);
                    }
                }
            }
        });

    }

    private void getTopData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/upms/departments/" + id + "/employees/all";

        final DialogHelper dialogHelper = new DialogHelper(context.getSupportFragmentManager(), context);
        dialogHelper.showAlert("加载中...", true);

        Response.Listener<List<EmployeeInfo>> successListener = new Response
                .Listener<List<EmployeeInfo>>() {
            @Override
            public void onResponse(List<EmployeeInfo> result) {
                if (null != context && !context.isFinishing()) {
                    dialogHelper.hideAlert();
                }
                Log.d(TAG, "数据:" + result.size());
                infoList = result;
                initLayout();
            }
        };

        Request<List<EmployeeInfo>> versionRequest = new
                GsonRequest<List<EmployeeInfo>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (null != context && !context.isFinishing()) {
                            dialogHelper.hideAlert();
                        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
    }
}