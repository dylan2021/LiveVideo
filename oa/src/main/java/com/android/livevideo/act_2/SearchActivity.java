package com.android.livevideo.act_2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Dylan
 * <p>
 * 搜索页面
 */

public class SearchActivity extends BaseFgActivity {
    private SearchActivity context;
    private LinearLayout emplyeeListLayout;
    private List<EmployeeInfo> employeeList = new ArrayList<>();
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStatusBar();
        setContentView(R.layout.activity_search);
        context = this;
        type = getIntent().getIntExtra(KeyConst.type, 0);

        initView();
    }

    private void search(String searchStr) {
        if (TextUtil.isEmpty(searchStr)) {
            emplyeeListLayout.removeAllViews();
            return;
        }
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

        final String url = Constant.WEB_SITE + "/upms/employees/all?search=" + URLEncoder.encode(
                searchStr);
        Response.Listener<List<EmployeeInfo>> successListener = new Response
                .Listener<List<EmployeeInfo>>() {
            @Override
            public void onResponse(List<EmployeeInfo> result) {
                if (context == null || emplyeeListLayout == null) {
                    return;
                }
                emplyeeListLayout.removeAllViews();
                if (result == null || result.size() == 0) {
                    TextView empty = new TextView(context);
                    empty.setPadding(0, 200, 0, 0);
                    empty.setBackgroundResource(R.color.f5);
                    empty.setGravity(Gravity.CENTER);
                    empty.setText(R.string.search_no_data);
                    emplyeeListLayout.addView(empty);
                    return;
                }
                setView(result);
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

    private void setView(List<EmployeeInfo> result) {
        for (final EmployeeInfo employeeInfo : result) {
            final View itemView = View.inflate(context, R.layout.item_emplyee_search, null);
            TextView iconTv = (TextView) itemView.findViewById(R.id.emplyee_icon_tv);
            TextView nameTv = (TextView) itemView.findViewById(R.id.emplyee_name_tv);
            TextView descTv = (TextView) itemView.findViewById(R.id.emplyee_phone_tv);

            if (type == 1) {
                iconTv.setVisibility(View.GONE);
            }
            String name = employeeInfo.getEmployeeName();
            final String phone = employeeInfo.getEmployeeMobile();

            nameTv.setText(name);
            iconTv.setText(TextUtil.getLast2(name));
            descTv.setText(phone);

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
                        AddressListActivity.context.finish();
                        finish();
                    } else {
                        Utils.callPhone(context, phone);
                    }
                }
            });
            //产值
            emplyeeListLayout.addView(itemView);
        }
    }

    private void initView() {
        emplyeeListLayout = (LinearLayout) findViewById(R.id.emplyee_search_layout);
        ((EditText) findViewById(R.id.search_et)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                search(String.valueOf(editable).trim());
            }
        });
    }


    public void onCancelClick(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        finish();
    }
}
